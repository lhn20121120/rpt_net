package com.fitech.model.etl.service;

import java.util.List;
import java.util.Map;

import com.fitech.framework.core.dao.BaseDaoException;
import com.fitech.framework.core.service.BaseServiceException;
import com.fitech.framework.core.service.IBaseService;
import com.fitech.model.etl.model.pojo.EtlTaskMoni;
import com.fitech.model.etl.model.vo.ETLMoniDataVo;
import com.fitech.model.etl.model.vo.ETLTaskMoniVo;

/***
 * 任务监控服务接口
 * @author 卞以刚
 *
 */
public interface IETLTaskMoniService extends IBaseService<EtlTaskMoni, Integer>{
	/**查询所有监控信息*/
	public List<ETLTaskMoniVo> findEtlTaskList() throws Exception;
	
	/**带分页多条件查询查询当前页的监控信息*/
	public List<ETLTaskMoniVo> findEtlTaskListByKeyWords(ETLTaskMoniVo vo,Integer pageSize,Integer pageNo) throws Exception;
	
	/**不带分页多条件查询*/
	public List<ETLTaskMoniVo>  findEtlTaskListByKeyWords(ETLTaskMoniVo vo) throws Exception;
	
	/**根据页面的显示查询监控信息*/
	public List<ETLMoniDataVo>  findEtlTaskLikeKeyWords(ETLTaskMoniVo vo) throws Exception;
	/**
	 * 获取上一个频度中未完成的任务
	  * @Title: getNotOverLastRreqETLTaskMoniVo 
	  * @author: xfc100
	  * @date: Mar 8, 2012  1:09:30 AM
	  * @param vo
	  * @return
	  * @throws Exception ETLTaskMoniVo
	 */
	public ETLTaskMoniVo getNotOverLastRreqETLTaskMoniVo(ETLTaskMoniVo vo) throws Exception;
	/**
	 * 获取上一个未完成的任务
	  * @Title: getNotOverLastTaskETLTaskMoniVo 
	  * @author: xfc100
	  * @date: Mar 8, 2012  1:10:06 AM
	  * @param vo
	  * @return
	  * @throws Exception ETLTaskMoniVo
	 */
	public ETLTaskMoniVo getNotOverLastTaskETLTaskMoniVo(ETLTaskMoniVo vo,List<Integer> parTaskList) throws Exception;
	/**
	 * 	重新执行任务
	  * @Title: reStartTaskMoni 
	  * @author: xfc100
	  * @date: Mar 8, 2012  10:16:46 AM
	  * @param taskMoniId
	  * @throws Exception void
	 */
	public void reRunningTaskMoni(Integer taskMoniId)throws Exception ;
	/**
	 * 启动任务
	  * @Title: startTaskMoni 
	  * @author: xfc100
	  * @date: Mar 8, 2012  10:18:52 AM
	  * @param taskMoniId
	  * @throws Exception void
	 */
	public void startTaskMoni(Integer taskMoniId)throws Exception;
	/**
	 * 停止任务
	  * @Title: stopTaskMoni 
	  * @author: xfc100
	  * @date: Mar 8, 2012  10:19:05 AM
	  * @param taskMoniId
	  * @throws Exception void
	 */
	public void stopTaskMoni(Integer taskMoniId)throws Exception;
	
	/***
	 * 根据ID检索任务监控
	 * @param moni
	 * @return
	 * @throws Exception
	 */
	public ETLTaskMoniVo findETLTaskMoniByMoniId(EtlTaskMoni moni)throws Exception;
	/**
	 * 手动执行任务
	  * @Title: manualExceuteTask 
	  * @author: xfc100
	  * @date: Mar 23, 2012  5:20:23 PM
	  * @param parTaskList 监控任务ID列表
	  * @param startDate
	  * @param endDate
	  * @throws BaseServiceException void
	 */
	public void manualExceuteTask(List<Integer> parTaskList,String startDate,String endDate)throws Exception;
	/**
	 * 返回任务时间MAP
	  * @Title: getTaskTimeMap 
	  * @author: xfc100
	  * @date: Mar 29, 2012  3:16:45 PM
	  * @return Map
	 */
	public Map getTaskTimeMap();
	/**
	 * 更新重置timeMap
	  * @Title: setTaskTimeMap 
	  * @author: xfc100
	  * @date: Mar 29, 2012  3:17:11 PM
	  * @param taskTimeMap void
	 */
	public void setTaskTimeMap(Map taskTimeMap);
	/**
	 * 初始化警告信息
	  * @Title: initETLWarning 
	  * @author: xfc100
	  * @date: Mar 29, 2012  4:35:54 PM
	  * @param taskMoniId
	  * @throws Exception void
	 */
	public void initETLWarning(Integer taskMoniId)throws Exception;
	/**
	 *  如未发生则移除ETL警告信息
	  * @Title: removeETLWarning 
	  * @author: xfc100
	  * @date: Mar 29, 2012  4:30:19 PM
	  * @param taskMoniId
	  * @param etlTaskMoniService
	  * @throws Exception void
	 */
	public void removeETLWarning(Integer taskMoniId)throws Exception;
	
	/***
	 * 获取可执行，但是并未执行结束任务的最大的期数
	 * @param execFalg 可执行标志 0:不允许执行 1:可执行
	 * @param overFlag 任务结束标志 0:未结束 1:已结束
	 * @return
	 * @throws Exception
	 */
	public String getMaxTaskTerm(Integer execFalg,Integer overFlag)throws Exception;
	
	/***
	 *  获取可执行，但是并未执行结束任务的最小的期数
	 * @param execFalg 可执行标志 0:不允许执行 1:可执行
	 * @param overFlag 任务结束标志 0:未结束 1:已结束
	 * @return
	 * @throws Exception
	 */
	public String getMinTaskTerm(Integer execFalg,Integer overFlag)throws Exception;
	/**
	 * 
	  * @Title: insertTaskMoniBatch 
	  * @author: xfc100
	  * @date: Apr 11, 2012  7:10:29 PM
	  * @param dateList
	  * @param execFlag
	  * @param freqId
	  * @param objectName
	  * @throws BaseDaoException
	  * @throws BaseServiceException void
	 */
	public void insertTaskMoniBatch(List<String> dateList,Integer execFlag,String freqId,String objectName) throws BaseDaoException, BaseServiceException ;
	/**
	 * 
	  * @Title: insertTaskMoni 
	  * @author: xfc100
	  * @date: Apr 11, 2012  7:10:33 PM
	  * @param taskDate
	  * @param execFlag
	  * @param freqId
	  * @param objectName
	  * @throws BaseDaoException
	  * @throws BaseServiceException void
	 */
	public void insertTaskMoni(String taskDate,Integer execFlag,String freqId,String objectName) throws BaseDaoException, BaseServiceException ;
	/**
	 * 按任务段重新执行
	  * @Title: reRunningPorcessProc 
	  * @author: xfc100
	  * @date: Apr 11, 2012  7:20:57 PM
	  * @param taskMoniId
	  * @param procId
	  * @throws Exception void
	 */
	public void reRunningPorcessProc(Integer taskMoniId,Integer procId,List removeJobList)throws Exception;
	/**
	 *  取消指定列表的任务，取消的任务，将不会再被执行，同时后继的任务可以忽略该任务
	  * @Title: cancelTaskMoniList 
	  * @author: xfc100
	  * @date: Apr 13, 2012  2:13:15 PM
	  * @param taskMoniList
	  * @throws Exception void
	 */
	public void cancelTaskMoniList(List<Integer> taskMoniList)throws Exception;
	/**
	 * 将任务列表进行排序
	 * @param dataList
	 */
	public List<ETLMoniDataVo> orderTaskMoniList(List<ETLMoniDataVo> dataList);
}
