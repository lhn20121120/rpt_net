package com.fitech.model.etl.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;

import com.fitech.framework.core.service.impl.DefaultBaseService;
import com.fitech.model.etl.model.pojo.EtlTaskFreq;
import com.fitech.model.etl.model.vo.ETLTaskFreqVo;
import com.fitech.model.etl.service.IETLTaskFreqService;

/***
 * 任务关联的频度信息服务接口实现类
 * @author admin
 *
 */
public class ETLTaskFreqServiceImpl extends DefaultBaseService<EtlTaskFreq, Integer> implements IETLTaskFreqService{
	
	/***
	 * 根据任务id查询所有该任务关联的频率
	 * @author 卞以刚 2012-02-24
	 */
	@Override
	public List<ETLTaskFreqVo> findEtlTaskFreqByTaskId(Integer taskId)
			throws Exception {
		String hql="from EtlTaskFreq e where e.id.taskId="+taskId;
		List<EtlTaskFreq> freqList=null;
		List<ETLTaskFreqVo> freqVoList=null;
		//查询
		freqList=this.findListByHsql(hql, null);
		if(freqList!=null && freqList.size()>0)
		{
			//实例化集合
			freqVoList=new ArrayList<ETLTaskFreqVo>();
			for(EtlTaskFreq f:freqList){//遍历
				ETLTaskFreqVo vo=new ETLTaskFreqVo();
				BeanUtils.copyProperties(vo, f);
				freqVoList.add(vo);//添加进集合
			}
		}
		return freqVoList;
	}
	
	/***
	 * 查询所有任务关联的所有频率
	 * @author 卞以刚 2012-02-24
	 */
	@Override
	public List<ETLTaskFreqVo> findEtlTaskFreqList() throws Exception {
		List<EtlTaskFreq> freqList=null;
		List<ETLTaskFreqVo> freqVoList=null;
		freqList=this.findAll();//查询所有
		if(freqList!=null && freqList.size()>0)
		{
			freqVoList=new ArrayList<ETLTaskFreqVo>();
			for(EtlTaskFreq f:freqList){
				ETLTaskFreqVo vo=new ETLTaskFreqVo();
				BeanUtils.copyProperties(vo, f);
				freqVoList.add(vo);//添加进集合
			}
		}
		return freqVoList;
	}
	
	/***
	 * 增加或更新任务的频率信息
	 * @author 卞以刚 2012-02-24
	 */
	public void saveOrUpdateEtlTaskFreq(Integer taskId,List<String> freqList) throws Exception{
		List<ETLTaskFreqVo> freqVoList=null;
		ETLTaskFreqVo efVo=null;
		if(freqList==null || freqList.size()==0)
			return;
		//根据taskId查询数据库 检索该任务是否已经关联频度
		freqVoList=this.findEtlTaskFreqByTaskId(taskId);
		//若检索出的集合大于0，则说明已关联频度，则删除关联的频度
		if(freqVoList!=null && freqVoList.size()>0){
			for(ETLTaskFreqVo vo:freqVoList)
			{
//				EtlTaskFreq ef=new EtlTaskFreq();
//				BeanUtils.copyProperties(ef, vo);
				if(vo!=null && vo.getId()!=null){
					String hql = "delete from EtlTaskFreq et where et.id.taskId="+vo.getId().getTaskId();
					this.objectDao.deleteObjects(hql);
				}
//				this.delete(ef);
			}
		}
		for(String freq:freqList){//遍历频度集合
			if(freq!=null && !freq.equals("")){
				EtlTaskFreq ef=new EtlTaskFreq();
				efVo=new ETLTaskFreqVo();//实例化业务对象
				efVo.getId().setTaskId(taskId);//放入业务id
				efVo.getId().setFreqId(freq);
				BeanUtils.copyProperties(ef, efVo);
				this.save(ef);//执行保存操作
			}
		}
	}
	
}
