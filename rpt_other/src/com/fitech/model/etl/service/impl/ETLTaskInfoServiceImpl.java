package com.fitech.model.etl.service.impl;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;
import com.fitech.framework.core.service.BaseServiceException;
import com.fitech.framework.core.service.impl.DefaultBaseService;
import com.fitech.model.etl.model.pojo.EtlTaskInfo;
import com.fitech.model.etl.model.pojo.EtlTaskOrder;
import com.fitech.model.etl.model.pojo.TFreqInfo;
import com.fitech.model.etl.model.vo.ETLLoadFileDetailVo;
import com.fitech.model.etl.model.vo.ETLLoadFileInfoVo;
import com.fitech.model.etl.model.vo.ETLOrderDataVo;
import com.fitech.model.etl.model.vo.ETLTaskInfoVo;
import com.fitech.model.etl.model.vo.EtlTaskOrderVo;
import com.fitech.model.etl.service.IETLLoadFileDetailService;
import com.fitech.model.etl.service.IETLLoadFileInfoService;
import com.fitech.model.etl.service.IETLTaskInfoService;
import com.fitech.model.etl.service.IETLTaskOrderService;

/**
 * 频度信息服务类
 * @author admin
 *
 */
public class ETLTaskInfoServiceImpl extends DefaultBaseService<EtlTaskInfo, Integer>  implements IETLTaskInfoService {
	
	private IETLTaskOrderService orderService;
	private IETLLoadFileInfoService loadFileInfoService;
	private IETLLoadFileDetailService detailService;
	

	
	/***
	 * 保存任务配置
	 * @author 卞以刚 2012-2-21
	 * @param vo
	 * @throws BaseServiceException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public void save(ETLTaskInfoVo vo) 
		throws BaseServiceException ,IllegalAccessException, InvocationTargetException{
		EtlTaskInfo etlTaskInfo=null;
		if(vo!=null){//非空判断
			etlTaskInfo=new EtlTaskInfo();
			BeanUtils.copyProperties(etlTaskInfo, vo);
			this.save(etlTaskInfo);
			
			//默认保存他为-1
			EtlTaskOrder order=new EtlTaskOrder();
			order.getId().setTaskId(etlTaskInfo.getTaskId());
			order.getId().setPreTaskId(-1);
			orderService.save(order);
			
		}
		
	}
	
	@Override
	/***
	 * 查询所有任务配置
	 * @author 卞以刚  2012-02-17
	 */
	public List<ETLTaskInfoVo> findTaskList() 
		throws  BaseServiceException,InvocationTargetException ,IllegalAccessException{
		List<EtlTaskInfo> list=null;
		List<ETLTaskInfoVo> voList=null;
		//查询所有任务配置实体对象
		String hql="from EtlTaskInfo e order by e.taskId,e.orderId";
		list=this.findListByHsql(hql, null);
		if(list!=null){
			//实例任务配置业务对象集合
			voList=new ArrayList<ETLTaskInfoVo>();
			//讲实体对象拷贝到业务对象
			for (EtlTaskInfo e:list) {
				ETLTaskInfoVo vo=new ETLTaskInfoVo();
				BeanUtils.copyProperties(vo, e);
				voList.add(vo);//添加进业务对象集合
			}
			return voList;//返回业务对象集合
		}
		return null;
	}
	
	/***
	 * 删除任务配置
	 * @author 卞以刚 2012-2-21
	 */
	public void deleteETLTaskInfoByTaskId(ETLTaskInfoVo vo)
		throws BaseServiceException,InvocationTargetException ,IllegalAccessException{
		if(vo!=null && vo.getTaskId()!=null && !vo.getTaskId().equals("")){//判断
			List<String> delList = new ArrayList();
			delList.add("delete from EtlTaskInfo where taskId=" + vo.getTaskId());
			delList.add("delete from EtlTaskOrder where id.taskId=" + vo.getTaskId());
			delList.add("delete from EtlLoadFileDetail where id.fileId in(select fileId from EtlLoadFileInfo where taskId=" + vo.getTaskId() + ")");
			delList.add("delete from EtlLoadFileInfo where taskId=" + vo.getTaskId());
			delList.add("delete from EtlTaskFreq where id.taskId=" + vo.getTaskId());
			delList.add("delete from EtlTaskProcStatus where id.taskMoniId in(select taskMoniId from EtlTaskMoni where taskId=" + vo.getTaskId() + ")");
			delList.add("delete from EtlTaskMoni where taskId=" + vo.getTaskId());
			for(int i=0;i<delList.size();i++)
				this.objectDao.deleteObjects(delList.get(i));
		}
	}
	
	/***
	 * 修改任务配置
	 * @author 卞以刚 2012-02-22
	 */
	@Override
	public void margeETLTaskInfo(ETLTaskInfoVo vo) throws Exception {
		EtlTaskInfo task=null;
		if(vo!=null){//非空判断
			task=new EtlTaskInfo();//实例话对象
			BeanUtils.copyProperties(task, vo);//复制对象
			this.update(task);//更新
		}
	}
	
	
	
	/**
	 * 多条件查询任务配置
	 */
	@Override
	public List<ETLTaskInfoVo> findETLTaskInfoLikeTaskName(ETLTaskInfoVo vo) throws Exception{
		StringBuffer hql=new StringBuffer("from EtlTaskInfo e where 1=1 ");
		if(vo.getTaskName()!=null && !vo.getTaskName().equals(""))//拼接hql
			hql.append(" and e.taskName like '"+vo.getTaskName()+"%'");
		List<ETLTaskInfoVo> voList=null;
		List<EtlTaskInfo> infoList=null;
		infoList=this.findListByHsql(hql.toString(), null);//查找所有
		if(infoList!=null && infoList.size()>0){
			voList=new ArrayList<ETLTaskInfoVo>();//实例化业务对象集合
			for(EtlTaskInfo e:infoList){//遍历
				ETLTaskInfoVo infoVo=new ETLTaskInfoVo();
				BeanUtils.copyProperties(infoVo, e);
				voList.add(infoVo);//添加进集合
			}
		}
		return voList;
	}
	
	/**根据id查找任务配置*/
	public ETLTaskInfoVo findETLTaskInfoById(ETLTaskInfoVo vo) throws Exception{
		ETLTaskInfoVo etlVo=null;
		if(vo==null)
			return etlVo;
		if(vo.getTaskId()==null || vo.getTaskId().equals(""))
			return etlVo;
		String hql="from EtlTaskInfo e where e.taskId="+vo.getTaskId();
		EtlTaskInfo info=(EtlTaskInfo)this.findObject(hql);//查找对象
		if(info!=null){
			etlVo=new ETLTaskInfoVo();//实例化业务对象
			BeanUtils.copyProperties(etlVo, info);//覆盖
		}
		return etlVo;//返回
	}
	
	/***
	 * 根据任务ID检索此任务可以设置的前置任务
	 * 且设置的前置任务不会造成死锁
	 */
	public List<ETLOrderDataVo> findETLTaskInfoCanInsertETLTaskOrderByTaskId(Integer taskId)throws Exception{
		String hql="from EtlTaskInfo e where e.taskId!="+taskId;
		List<EtlTaskInfo> infoList=null;
		List<EtlTaskOrderVo> orderVoList=null;
		List<ETLOrderDataVo> orderDataList=null;
		//先查出其他的任务
		infoList=this.findListByHsql(hql, null);
		if(infoList==null || infoList.size()==0)
			return null;
		orderDataList =new ArrayList<ETLOrderDataVo>();
		//遍历其他任务，查处每个任务对应的前置任务
		for(EtlTaskInfo in:infoList){
			int id=in.getTaskId();//获取每个任务的id
			//此任务是否能够被设置为前置条件
			boolean isAdd=isAddTaskOrder(id,taskId);
			if(isAdd){//能就添加进集合
				ETLOrderDataVo data=new ETLOrderDataVo();
				data.setTaskId(in.getTaskId());
				data.setTaskName(in.getTaskName());
				orderDataList.add(data);//添加进集合
			}
		}
		//根据该任务Id检索该任务包含的前置任务
		orderVoList=orderService.findETLTaskOrderByTaskId(taskId);
		if(orderVoList==null || orderVoList.size()==0)
			return orderDataList;
		for(EtlTaskOrderVo o:orderVoList){//遍历集合
			for(int i=0;i<orderDataList.size(); i++){
				if(orderDataList.get(i).getTaskId().equals(o.getId().getPreTaskId()))
					orderDataList.get(i).setIsOrder(1);
			}
		}
		return orderDataList;
	}
	
	/***
	 * 是否需要添加前置条件
	 * 此方法是为了防止用户设置的前置条件与原先存在的前提条件形成死锁
	 * 故 首先过滤掉能够形成死锁的前置条件
	 * @param id
	 * @param taskId
	 * @return
	 * @throws Exception
	 */
	public boolean isAddTaskOrder(int id,int taskId)throws Exception{	
		//是否需要添加：真为需要 假为不需要
		boolean isAdd=true;
		//根据任务id查询此任务所有的前置任务
		List<EtlTaskOrderVo> orderVoList=orderService.findETLTaskOrderByTaskId(id);
		if(orderVoList==null || orderVoList.size()==0)//此任务没有前置任务
			return isAdd;
		for(EtlTaskOrderVo ovo:orderVoList){//遍历所有前置任务
			/**若前置任务的id等于需要添加的任务的id，则表明不需要添加*/
			if(ovo.getId().getPreTaskId()==taskId)
				isAdd=false;//设置变量
			/**若前置任务的id为-1，则表明是初始任务,则继续遍历*/
			if(ovo.getId().getPreTaskId()==-1){
				
			}else{
				if(isAdd)//需要添加，则以前置任务的id为taskId 检索该前置任务具备的前置任务
					isAdd=isAddTaskOrder(ovo.getId().getPreTaskId(),taskId);
			}
		}
		return isAdd;//返回
	}
	
	
	
	/**
	 * 根据任务id检索任务信息 确认任务配置信息是否完整
	 * 是否可以设置任务生效
	 */
	@Override
	public boolean isCanSetUsingFlagByTaskId(Integer taskId) throws Exception {
		String hql="from EtlTaskInfo e where e.taskId="+taskId;
		Object obj=this.findObject(hql);
		if(obj==null)
			return false;
		EtlTaskInfo info=(EtlTaskInfo)obj;
		if(info.getLoadFlag()==null && info.getTransFlag()==null)//是否需要数据装载和转换未配置
			return false;
		if(info.getLoadFlag()!=null && !info.getLoadFlag().equals("") && !info.getLoadFlag().toString().equals("0")){//需要数据装载
			return isSetLoadFlag(info);
		}
		
		if(info.getTransFlag()!=null && !info.getTransFlag().equals("")){//需要数据转换
			if(info.getTransWay()==null || info.getTransWay().equals("") //数据转换模式
					|| info.getCustomTypeCode()==null || info.getCustomTypeCode().equals(""))//调用程序
				return false;
		}
		return true;
		
	}
	
	/***
	 * 检索源数据配置是否已配置完整
	 * @param info 任务对象
	 * @return
	 * @throws Exception
	 */
	private boolean isSetLoadFlag(EtlTaskInfo info) throws Exception{
		if(info.getEtlCallTypeId()==0){//通用方式
			//检索是否有数据源抽取配置
			ETLLoadFileInfoVo fileInfoVo=new ETLLoadFileInfoVo();
			fileInfoVo.setTaskId(info.getTaskId());
			List<ETLLoadFileInfoVo> fileInfoVoList=loadFileInfoService.findETLLoadFileInfoByTaskId(fileInfoVo);
			if(fileInfoVoList==null || fileInfoVoList.size()==0)//无数据源抽取配置，则返回false
				return false;
			for(ETLLoadFileInfoVo v : fileInfoVoList){//遍历
				if(v.getCallTypeId()==0){//通用方式
					//遍历检索数据源抽取配置信息是否完整
					if(v.getServerId()==null || v.getServerId().equals(""))//服务器信息
						return false;
					if(v.getFileTypeCode()==null || v.getFileTypeCode().equals(""))//文本类型
						return false;
					if(v.getStartRow()==null  || v.getStartRow().equals(""))//起始行
						return false;
					if(v.getFileSeper()==null || v.getFileSeper().equals(""))//列分隔符
						return false;
					//是否具有列和文件信息的配置
					List<ETLLoadFileDetailVo> detailVoList=detailService.findETLLoadFileDetailByFileId(v.getFileId());
					if(detailVoList==null || detailVoList.size()==0)
						return false;
				}
				if(v.getCallTypeId()==1){//自定义方式
					if(v.getCustomWay()==null || v.getCustomWay().equals(""))//数据转换模式
						return false;
				}
				
			}
		}
		if(info.getEtlCallTypeId()==1){//自定义方式
			//装载模式或执行过程为null，返回false
			if(info.getLoadTypeCode()==null || info.getLoadTypeCode().equals("")
					|| info.getLoadWay()==null || info.getLoadWay().equals(""))
				return false;
		}
		return true;
	}

	public IETLTaskOrderService getOrderService() {
		return orderService;
	}

	public void setOrderService(IETLTaskOrderService orderService) {
		this.orderService = orderService;
	}
	public IETLLoadFileInfoService getLoadFileInfoService() {
		return loadFileInfoService;
	}

	public void setLoadFileInfoService(IETLLoadFileInfoService loadFileInfoService) {
		this.loadFileInfoService = loadFileInfoService;
	}
	public IETLLoadFileDetailService getDetailService() {
		return detailService;
	}

	public void setDetailService(IETLLoadFileDetailService detailService) {
		this.detailService = detailService;
	}

}
