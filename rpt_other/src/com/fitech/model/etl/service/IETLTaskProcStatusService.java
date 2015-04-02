package com.fitech.model.etl.service;

import java.util.List;

import com.fitech.framework.core.service.BaseServiceException;
import com.fitech.framework.core.service.IBaseService;
import com.fitech.model.etl.model.pojo.EtlTaskMoni;
import com.fitech.model.etl.model.pojo.EtlTaskProcStatus;
import com.fitech.model.etl.model.vo.ETLTaskMoniVo;
import com.fitech.model.etl.model.vo.EtlTaskProcStatusVo;

public interface IETLTaskProcStatusService extends IBaseService<EtlTaskProcStatus, Integer>{
	/**查询所有任务流程状态*/
	public List<EtlTaskProcStatusVo> findEtlTaskProcStatusList() throws Exception;
	
	/**根据监控id查询所有流程状态*/
	public List<EtlTaskProcStatusVo> findEtlTaskProcStatusByMoniId(Integer moniId)throws Exception;
	/**
	 * 执行任务流程
	  * @Title: executeTaskProc 
	  * @author: xfc100
	  * @date: Mar 8, 2012  11:00:29 AM
	  * @param vo
	  * @throws BaseServiceException void
	 */
	public void executeTaskProc(ETLTaskMoniVo vo) throws BaseServiceException;
	
	/***
	 * 根据ID检索唯一的流程
	 * @param vo
	 * @return
	 * @throws Exception
	 */
	public EtlTaskProcStatusVo findEtlTaskProcStatusById(Integer taskMoniId, Integer procId)throws Exception;
	
	/**
	 * 更新警告信息
	  * @Title: updateWarning 
	  * @author: xfc100
	  * @date: Apr 13, 2012  11:22:05 AM
	  * @param em
	  * @throws Exception void
	 */
	public void updateWarning(EtlTaskMoni em)throws Exception ;
	
	/***
	 * 检索有问题的任务流程
	 * @param taskMoniId
	 * @param problemFlag
	 * @return
	 * @throws Exception
	 */
	public List<EtlTaskProcStatusVo> findErrorTaskProc(Integer taskMoniId)throws Exception;
}
