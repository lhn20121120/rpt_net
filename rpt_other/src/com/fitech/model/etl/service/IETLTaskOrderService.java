package com.fitech.model.etl.service;

import java.util.List;

import com.fitech.framework.core.service.IBaseService;
import com.fitech.model.etl.model.pojo.EtlTaskOrder;
import com.fitech.model.etl.model.vo.EtlTaskOrderVo;

/***
 * 前置任务服务接口
 * @author admin
 *
 */
public interface IETLTaskOrderService extends IBaseService<EtlTaskOrder, Integer>{
	/**
	 * 根据任务id检索指定任务的前置任务
	 * @param taskId
	 * @return
	 * @throws Exception
	 */
	public List<EtlTaskOrderVo> findETLTaskOrderByTaskId(Integer taskId) throws Exception;
	
	/***
	 * 检索所有任务的前置任务
	 * @return
	 * @throws Exception
	 */
	public List<EtlTaskOrderVo> findETLTaskOrderList()throws Exception;
	
	/**
	 * 根据任务id增加前置任务
	 * @param taskId
	 * @param procIds
	 * @throws Exception
	 */
	public void insertETLTaskOrderByTaskId(Integer taskId,List<Integer> preTaskIds)throws Exception;
	
	/***
	 * 根据TaskId 删除前置任务
	 * @param taskId
	 * @throws Exception
	 */
	public void deleteETLTaskOrderByTaskId(Integer taskId) throws Exception;
}
