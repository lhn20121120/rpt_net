package com.fitech.model.worktask.service;

import java.util.List;

import com.fitech.framework.core.service.IBaseService;
import com.fitech.model.worktask.model.pojo.WorkTaskInfo;
import com.fitech.model.worktask.model.pojo.WorkTaskNodeInfo;
import com.fitech.model.worktask.model.pojo.WorkTaskNodeObject;
import com.fitech.model.worktask.model.pojo.WorkTaskNodeRole;

public interface IWorkTaskNodeInfoService extends
		IBaseService<WorkTaskNodeInfo, String> {
	public WorkTaskInfo saveTask(WorkTaskInfo task) throws Exception;

	public Integer saveNodeAndObject(WorkTaskInfo task, WorkTaskNodeInfo node,
			WorkTaskNodeRole role, WorkTaskNodeObject object, String orgIds,
			String templateIds) throws Exception;

	public void save(WorkTaskInfo task, WorkTaskNodeInfo tbNode,
			WorkTaskNodeInfo fhNode, WorkTaskNodeRole tbRole,
			WorkTaskNodeRole fhRole, WorkTaskNodeObject object, String orgIds,
			String templateIds, String zTime, String fTime, String cTime)
			throws Exception;

	public void saveRalation(WorkTaskInfo task, String orgIds,
			String templateIds) throws Exception;

	public void saveRepTime(Integer nodeId, String zTime, String fTime,
			String cTime) throws Exception;

	public void edit(WorkTaskInfo task, WorkTaskNodeInfo tbNode,
			WorkTaskNodeInfo fhNode, WorkTaskNodeRole tbRole,
			WorkTaskNodeRole fhRole, WorkTaskNodeObject object, String orgIds,
			String templateIds, String zTime, String fTime, String cTime)
			throws Exception;

	public List<Integer> findNodeIdListByTaskId(Integer taskId)
			throws Exception;
	
	public void deleteRalation(WorkTaskInfo task) throws Exception  ;

	public void deleteInfo(WorkTaskInfo task)throws Exception ;
}
