package com.fitech.model.worktask.service;

import java.util.List;

import com.fitech.framework.core.service.IBaseService;
import com.fitech.model.commoninfo.security.Operator;
import com.fitech.model.worktask.model.pojo.WorkTaskNodeInfo;
import com.fitech.model.worktask.model.pojo.WorkTaskNodeMoni;
import com.fitech.model.worktask.model.pojo.WorkTaskType;
import com.fitech.model.worktask.vo.WorkTaskPendingTaskVo;

public interface WorkTaskPendingTaskService extends IBaseService<WorkTaskNodeMoni, String>{
	/**
	 * 根据选中待办任务判断是否都是同一任务流程
	 * @param pvos
	 * @return
	 * @throws Exception
	 */
	public boolean findTaskIsHjOrDJ(List<WorkTaskPendingTaskVo> pvos)throws Exception;
	/**
	 * 查找任务类型
	 * @param taskTypeId
	 * @return
	 * @throws Exception
	 */
	public WorkTaskType findOneWorkTaskType(String taskTypeId)throws Exception;
	
	/**
	 * 查找指定节点的所有前置节点
	 * @param nodeId
	 * @param nodeInfos
	 */
	public void findBeforeNodesByNodeId(Integer nodeId,List<WorkTaskNodeInfo> nodeInfos);
	/**
	 * 根据节点Id查找参与该节点任务的用户列表执行具体操作
	 * @param nodeId
	 * @throws Exception
	 */
	public void performByNodeId(Integer nodeId,String term,String orgId)throws Exception;
	/**
	 * 根据当前提交的任务查找下一个节点的待办任务，并执行相关的操作(发送邮件,短信提醒)
	 * @param pvos
	 * @throws Exception
	 */
	public void performByPendingTask(WorkTaskPendingTaskVo pvos)throws Exception;
	
}
