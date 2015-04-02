package com.fitech.model.worktask.service;

import java.util.Date;
import java.util.List;

import com.fitech.framework.core.service.IBaseService;
import com.fitech.model.worktask.model.pojo.WorkTaskInfo;
import com.fitech.model.worktask.model.pojo.WorkTaskMoni;

public interface IWorkTaskMoniService extends IBaseService<WorkTaskMoni, Long>{
	
	public WorkTaskMoni findOneWorkTaskMoni(Long taskMoniId)throws Exception;
	/**
	 * 根据频度增加任务执行表
	 * @param freqId 频度编号
	 * @throws Exception
	 */
	public void insertWorkTaskAll(String freqId,boolean isDelExist) throws Exception;
	/**
	 * 按期数增加工作任务执行表
	 * @param taskTerm 期数 yyyy-MM-dd
	 * @param execFlag 可执行标志  1:可执行  0:禁用
	 * @throws Exception
	 */
	public void insertWorkTaskAll(String taskTerm,Integer execFlag,String busiLineId,boolean isDelExist) throws Exception;
	/**
	 * 根据机构添加节点执行表
	 * @param taskId	任务标号
	 * @param orgId		机构编号
	 * @param taskTerm	期数 yyyy-MM-dd
	 * @param execFlag	可执行标志  1:可执行  0:禁用
	 * @throws Exception
	 */
	public void insertWorkTaskByOrg(WorkTaskInfo info,WorkTaskMoni moni,List<String> tempIdList,String orgId,Date taskTerm,boolean isDelExist)throws Exception;
	/**
	 * 拆分任务
	 * @param notPassTempateIdList 未通过报表列表
	 * @param taskMoniId 任务监控ID
	 * @param nodeId	节点ID
	 * @param orgId	机构ID
	 * @param taskTerm 期数
	 * @throws Exception
	 */
	public WorkTaskMoni insertWorkTaskSplit(List<String> notPassTempateIdList,Integer taskMoniId,Integer nodeId,String orgId,Date taskTerm)throws Exception;
	/**
	 * 手动拆出任务
	 * @param tempateIdList 需要拆除的报表列表
	 * @param taskMoniId 任务监控ID
	 * @param nodeId 节点ID
	 * @param orgId 机构ID
	 * @param taskTerm 期数
	 * @param inputTaskName 输入任务名称
	 * @throws Exception
	 */
	public void insertWorkTaskSplitByManual(List<String> tempateIdList,Integer taskMoniId,Integer nodeId,String orgId,Date taskTerm,String inputTaskName)throws Exception;
}
