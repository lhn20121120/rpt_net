package com.fitech.model.etl.service;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import com.fitech.framework.core.service.BaseServiceException;
import com.fitech.framework.core.service.IBaseService;
import com.fitech.model.etl.model.pojo.EtlTaskInfo;
import com.fitech.model.etl.model.vo.ETLOrderDataVo;
import com.fitech.model.etl.model.vo.ETLTaskInfoVo;

/**
 *  任务配置服务接口
 *  此接口提供了对任务配置的增加，检索，删除方法
  * @Title: IETLTaskInfoService.java 
  * @Package com.fitech.model.etl.service 
  * @Description: TODO
  * @author xfc100
  * @date Feb 17, 2012 12:11:08 AM 
  * @version V1.2
 */
public interface IETLTaskInfoService extends IBaseService<EtlTaskInfo, Integer>{
	/**增加任务配置*/
	public void save(ETLTaskInfoVo vo)throws BaseServiceException ,IllegalAccessException, InvocationTargetException;
	/**检索全部任务配置*/
	public List<ETLTaskInfoVo> findTaskList()throws  BaseServiceException,InvocationTargetException ,IllegalAccessException;
	/**删除单个任务*/
	public void deleteETLTaskInfoByTaskId(ETLTaskInfoVo vo)throws BaseServiceException,InvocationTargetException ,IllegalAccessException;
	
	/**修改任务配置*/
	public void margeETLTaskInfo(ETLTaskInfoVo vo) throws Exception;
	
	/**多条件查询任务配置*/
	public List<ETLTaskInfoVo> findETLTaskInfoLikeTaskName(ETLTaskInfoVo vo) throws Exception;
	
	/**根据id查找任务配置*/
	public ETLTaskInfoVo findETLTaskInfoById(ETLTaskInfoVo vo) throws Exception;
	
	/***
	 * 根据任务ID检索此任务可以设置的前置任务
	 * 且设置的前置任务不会造成死锁
	 */
	public List<ETLOrderDataVo> findETLTaskInfoCanInsertETLTaskOrderByTaskId(Integer taskId)throws Exception;
	
	/***
	 * 根据任务id检索任务信息 确认任务配置信息是否完整
	 * 是否可以设置任务生效
	 * @param taskId
	 * @return
	 * @throws Exception
	 */
	public boolean isCanSetUsingFlagByTaskId(Integer taskId) throws Exception;
}
