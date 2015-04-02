package com.fitech.model.worktask.service;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import com.fitech.framework.core.service.BaseServiceException;
import com.fitech.framework.core.service.IBaseService;
import com.fitech.model.worktask.model.pojo.WorkTaskInfo;
import com.fitech.model.worktask.model.pojo.WorkTaskNodeInfo;
import com.fitech.model.worktask.security.Operator;
import com.fitech.model.worktask.vo.WorkTaskNodeDefineVo;

public interface IWorkTaskNodeInfoServiceFlow extends IBaseService<WorkTaskNodeInfo, String> {
	/*
	 * 增加节点
	 */
	public void save(WorkTaskNodeDefineVo vo,String orgIds,String templateIds,WorkTaskInfo task)throws BaseServiceException ,IllegalAccessException, InvocationTargetException;
	public void save(HttpSession session,int countNode)throws BaseServiceException ,IllegalAccessException, InvocationTargetException;
	
	/**
	 * 定制总分报送流程，
	 * @param nodeVos  多个节点
	 * @param type   保存类型
	 * @param task   任务基础信息
	 * @throws Exception
	 */
	public void save(List<WorkTaskNodeDefineVo> nodeVos,String type,WorkTaskInfo task)throws Exception;
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
	public void update(WorkTaskNodeDefineVo vo,String orgIds,String templateIds,WorkTaskInfo task,Map<String,List> org_templates) throws BaseServiceException ,IllegalAccessException, InvocationTargetException;
	
	/**
	 * 更新
	 */
	public void update(WorkTaskNodeDefineVo vo,String orgIds,String templateIds,WorkTaskInfo task) throws BaseServiceException ,IllegalAccessException, InvocationTargetException;
	
	public void updateReport(WorkTaskNodeDefineVo vo,String orgIds,String templateIds,WorkTaskInfo task,Integer limitTime) throws BaseServiceException ,IllegalAccessException, InvocationTargetException,Exception;
	/**
	 * 向前插入
	 */
	public void insertPreNode(WorkTaskNodeDefineVo vo,String orgIds,String templateIds,WorkTaskInfo task) throws BaseServiceException ,IllegalAccessException, InvocationTargetException;
	/**
	 *向后 插入
	 */
	public void insertBackNode(WorkTaskNodeDefineVo vo,String orgIds,String templateIds,WorkTaskInfo task) throws BaseServiceException ,IllegalAccessException, InvocationTargetException;
}
