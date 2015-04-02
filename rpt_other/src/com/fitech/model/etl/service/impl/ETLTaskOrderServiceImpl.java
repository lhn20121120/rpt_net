package com.fitech.model.etl.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;

import com.fitech.framework.core.service.impl.DefaultBaseService;
import com.fitech.model.etl.model.pojo.EtlTaskOrder;
import com.fitech.model.etl.model.vo.EtlTaskOrderVo;
import com.fitech.model.etl.service.IETLTaskOrderService;

/***
 * 前置任务服务类
 * @author admin
 *
 */
public class ETLTaskOrderServiceImpl extends DefaultBaseService<EtlTaskOrder, Integer> implements IETLTaskOrderService{
	
	/***
	 * 根据任务id查询所有任务
	 */
	@Override
	public List<EtlTaskOrderVo> findETLTaskOrderByTaskId(Integer taskId)
			throws Exception {
		List<EtlTaskOrderVo> voList=null;
		List<EtlTaskOrder> orderList=null;
		
		if(taskId==null || taskId.equals(""))
			return null;
		
		//hql
		String hql="from EtlTaskOrder e where e.id.taskId="+taskId;
		orderList=this.findListByHsql(hql, null);//查处对象集合
		if(orderList==null || orderList.size()==0)
			return null;
		//实例化任务集合
		voList=new ArrayList<EtlTaskOrderVo>();
		for(EtlTaskOrder o:orderList){//遍历
			EtlTaskOrderVo ov=new EtlTaskOrderVo();
			BeanUtils.copyProperties(ov, o);
			voList.add(ov);//添加进集合
		}
		return voList;
	}
	
	/**
	 * 检索所有任务的前置任务
	 */
	@Override
	public List<EtlTaskOrderVo> findETLTaskOrderList() throws Exception {
		List<EtlTaskOrder> orderList=null;
		List<EtlTaskOrderVo> voList=null;
		String hql="from EtlTaskOrder";
		orderList=this.findListByHsql(hql, null);
		if(orderList==null || orderList.size()==0)
			return null;
		voList=new ArrayList<EtlTaskOrderVo>();
		for(EtlTaskOrder o:orderList){//遍历实例对象集合
			EtlTaskOrderVo vo=new EtlTaskOrderVo();
			BeanUtils.copyProperties(vo, o);
			voList.add(vo);//添加进业务对象集合
		}
		return voList;//返回
	}
	
	/***
	 * 根据任务ID检索与之关联的前置任务,先删除改任务关联的前置任务，再执行插入操作
	 */
	@Override
	public void insertETLTaskOrderByTaskId(Integer taskId, List<Integer> preTaskIds)
			throws Exception {
		if(taskId==null || taskId.equals(""))//非空判断
			return;
		if(preTaskIds==null || preTaskIds.size()==0)//非空判断
			return;
		
		/**根据任务id检索与之关联的前置任务*/
		String searchHql="from EtlTaskOrder e where e.id.taskId="+taskId;
		List<EtlTaskOrder> orderList=null;
		orderList=this.findListByHsql(searchHql, null);
		if(orderList==null || orderList.size()==0)//非空判断
			return;
		this.delete(orderList);//删除
		
		//循环插入
		for(Integer preTaskId:preTaskIds){
			if(preTaskId==null || preTaskId.equals(""))
				continue;
			EtlTaskOrder order=new EtlTaskOrder();
			order.getId().setTaskId(taskId);
			order.getId().setPreTaskId(preTaskId);
			this.save(order);
		}
		
	}
	
	/***
	 * 根据TaskId 删除任务
	 */
	@Override
	public void deleteETLTaskOrderByTaskId(Integer taskId) throws Exception {
		String hql="from EtlTaskOrder e where e.id.taskId="+taskId+" or e.id.preTaskId="+taskId;
		//查出该任务的前置任务数据 和 其他任务的前置任务是该任务的数据
		List<EtlTaskOrder> orderList=this.findListByHsql(hql, null);
		this.delete(orderList);//删除
	}
	
	
}
