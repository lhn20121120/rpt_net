package com.fitech.model.etl.service;

import java.util.List;

import com.fitech.framework.core.service.BaseServiceException;
import com.fitech.framework.core.service.IBaseService;
import com.fitech.model.etl.model.pojo.EtlTaskMoni;

public interface IETLJobService extends IBaseService<EtlTaskMoni, Integer>{
	/**
	 * 
	  * @Title: execMainTask 
	  * @author: xfc100
	  * @date: Apr 9, 2012  8:25:48 PM
	  * @param parTaskList
	  * @param startDate
	  * @param endDate
	  * @throws BaseServiceException void
	 */
	public void execMainTask(List<Integer> parTaskList,String startDate,String endDate)throws BaseServiceException;
	/**
	 * 删除job列表
	  * @Title: deleteJobList 
	  * @author: xfc100
	  * @date: Apr 9, 2012  8:25:53 PM
	  * @param taskMoniList
	  * @throws Exception void
	 */
	public void deleteJobList(List<Integer> taskMoniList)throws Exception;
	/**
	 * 删除job
	  * @Title: deleteJob 
	  * @author: xfc100
	  * @date: Apr 9, 2012  8:40:49 PM
	  * @param taskMoniId
	  * @throws Exception void
	 */
	public void deleteJob(Integer taskMoniId)throws Exception;
	
}
