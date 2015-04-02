package com.fitech.model.etl.service;

import java.util.List;

import com.fitech.framework.core.service.IBaseService;
import com.fitech.model.etl.model.pojo.EtlTaskFreq;
import com.fitech.model.etl.model.vo.ETLTaskFreqVo;

/***
 * 任务频率信息服务接口
 * @author admin
 *
 */
public interface IETLTaskFreqService extends IBaseService<EtlTaskFreq, Integer>{
	/**根据任务id查询所有该任务关联的频率*/
	public List<ETLTaskFreqVo> findEtlTaskFreqByTaskId(Integer taskId) throws Exception;
	
	/**查询所有任务关联的所有频率*/
	public List<ETLTaskFreqVo> findEtlTaskFreqList() throws Exception;

	/**增加或更新任务关联的频率*/
	public void saveOrUpdateEtlTaskFreq(Integer taskId,List<String> freqList) throws Exception;
}
