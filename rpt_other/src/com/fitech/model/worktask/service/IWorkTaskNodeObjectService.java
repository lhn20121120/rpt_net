package com.fitech.model.worktask.service;

import java.util.List;
import java.util.Map;

import com.fitech.framework.core.service.IBaseService;
import com.fitech.model.worktask.model.pojo.WorkTaskNodeObject;

public interface IWorkTaskNodeObjectService extends IBaseService<WorkTaskNodeObject, String> {

	public List findCheckOrgListByTaskId(String taskId) throws Exception ;
	public List findCheckTemplateListByTaskId(String taskId) throws Exception;
	public List findCheckOrgListByTaskIdFlow(String taskId,String nodeId) throws Exception ;
	public List findCheckTemplateListByTaskIdFlow(String taskId,String nodeId) throws Exception;
	public void deleteByTaskId(Integer taskId)throws Exception;
	
	public Map<String,List> findNodetIsNotNormalEdi(String taskId,String nodeId)throws Exception;
}
