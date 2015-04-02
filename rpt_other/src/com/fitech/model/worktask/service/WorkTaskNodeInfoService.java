package com.fitech.model.worktask.service;

import java.lang.reflect.InvocationTargetException;

import com.fitech.framework.core.service.BaseServiceException;
import com.fitech.framework.core.service.IBaseService;
import com.fitech.model.worktask.model.pojo.WorkTaskNodeInfo;
import com.fitech.model.worktask.security.Operator;
import com.fitech.model.worktask.vo.WorkTaskNodeDefineVo;

public interface WorkTaskNodeInfoService extends IBaseService<WorkTaskNodeInfo, String> {
	/*
	 * 增加节点
	 */
	public void save(WorkTaskNodeDefineVo vo,String orgIds,String templateIds)throws BaseServiceException ,IllegalAccessException, InvocationTargetException;
	
	
	/**
	 * 生成orgJson
	 */
	public String createAFOrgDataJSON(Operator opr);
	/**
	 * 生成orgJson
	 */
	public String createEditAFOrgDataJSON(Operator opr,String taskId,String nodeId);
	/**
	 * 生成templateJson
	 */
	public String createAFTemplateDataJSON(Operator opr);
	/**
	 * 生成templateJson
	 */
	public String createEditAFTemplateDataJSON(Operator opr,String taskId,String nodeId);
	/**
	 * 更新
	 */
	public void update(WorkTaskNodeDefineVo vo,String orgIds,String templateIds) throws BaseServiceException ,IllegalAccessException, InvocationTargetException;
}
