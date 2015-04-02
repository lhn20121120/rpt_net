package com.fitech.model.worktask.service;

import java.util.List;
import java.util.Map;

import com.fitech.framework.core.service.IBaseService;
import com.fitech.framework.core.web.PageResults;
import com.fitech.model.worktask.model.pojo.ViewWorktaskOrg;
import com.fitech.model.worktask.model.pojo.WorkTaskInfo;
import com.fitech.model.worktask.model.pojo.WorkTaskNodeInfo;
import com.fitech.model.worktask.model.pojo.WorkTaskNodeMoni;
import com.fitech.model.worktask.model.pojo.WorkTaskNodeRole;
import com.fitech.model.worktask.vo.PendingTaskQueryConditions;
import com.fitech.model.worktask.vo.WorkTaskIndexVo;
import com.fitech.model.worktask.vo.WorkTaskNodeInfoVo;
import com.fitech.model.worktask.vo.WorkTaskPendingTaskVo;

public interface WorkTaskNodeMoniService extends IBaseService<WorkTaskNodeMoni, String>{
	
	/**
	 * 保存节点执行任务
	 * @param workTaskNodeModi
	 * @throws Exception
	 */
	public WorkTaskNodeMoni saveWorkTaskNodeModi(String[] strs)throws Exception;
	
	/**
	 * 查找待办任务
	 * @throws Exception
	 */
	public PageResults findPendingTask(Map parameterMap,PendingTaskQueryConditions pendingTaskQueryConditions,
			PageResults pageResults,List<WorkTaskNodeRole> nodes
			,Long userId,String orgId,Integer repFlag,String ids)throws Exception;
	
	/**
	 * 更新待办任务
	 * @param nodeMoni
	 * updateType 1更新节点状态      2
	 * @throws Exception
	 */
	public boolean updateWorkTaskNodeMoni(String[] strs,int updateType)throws Exception;
	
	/**
	 * 退回待办任务
	 * @throws Exception
	 */
	public void backWorkTaskNodeMoni(String[] strs,String returnDesc
							,String taskNodeIds,Integer nodeIds,Integer repDay)throws Exception;
	/**
	 * 查找单个任务
	 * @param moni
	 * @return
	 * @throws Exception
	 */
	public WorkTaskNodeMoni findOneWorkTaskNodeMoni(WorkTaskNodeMoni moni)
	throws Exception ;
	/**
	 * 查找任务匹配的所有的模板id
	 * @param moni
	 * @return
	 * @throws Exception
	 */
	public String[] findByParamsTemplateIds(WorkTaskNodeMoni moni)throws Exception;
	
	public List<WorkTaskIndexVo> findAllWorkTask(PendingTaskQueryConditions pc,
			List<WorkTaskNodeRole> nodes,Long userId,String orgId)throws Exception;
	
	public List<WorkTaskNodeMoni> createWorkTaskNodeMoni(String[] strs)throws Exception;
	
	public String getWorkTaskNodes(List<WorkTaskNodeRole> nodes)throws Exception;
	
	public List<WorkTaskPendingTaskVo> findPendingTaskVos(String[] strs)throws Exception;
	
	public String findAllViewWtPurOrgsByRoleIds(Long userId)throws Exception;
	
	public List<WorkTaskNodeInfoVo> findAllNodeMonisByTaskMoniId(Integer taskId)throws Exception;
	
	/**
	 * 查询所有任务列表
	 * @param parameterMap
	 * @param pendingTaskQueryConditions
	 * @param pageResults
	 * @param nodes
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	public List<WorkTaskInfo> findAllWorkTaskInfosByParams(Map parameterMap,PendingTaskQueryConditions pendingTaskQueryConditions,
			PageResults pageResults,List<WorkTaskNodeRole> nodes,Long userId)throws Exception;
	
	/**
	 * 根据机构id查询当前机构是否是总行
	 * @param orgId
	 * @return
	 * @throws Exception
	 */
	public ViewWorktaskOrg findOneWorkTaskorg(String orgId)throws Exception;
	
	/**
	 * 查询所有已经提交成功的任务
	 * @param nMonis
	 * @return
	 * @throws Exception
	 */
	public String findRepTaskInfoNodeIds()throws Exception;
	/**
	 * 保存重报任务
	 * @return
	 * @throws Exception
	 */
	public boolean saveRepTaskInfo(List<WorkTaskPendingTaskVo> pvos,
			PendingTaskQueryConditions pendingTaskQueryConditions,Integer repDay)throws Exception;
	
	
	public boolean findPowTypeByOrgId(String orgId,Integer nodeId,Long userId)throws Exception;
	
	public WorkTaskNodeInfo findOneWorkTaskNodeInfo(Integer nodeId)throws Exception;
	
	public WorkTaskNodeMoni findNextOneWorkNodeMoni(WorkTaskNodeMoni nodeMoni)throws Exception;
	
	public void resetReport(List<WorkTaskPendingTaskVo> pvos)throws Exception;
	/**
	 * 测试是否需要检查任务所
	 * @param taskMoniId
	 * @param nodeId
	 * @param orgId
	 * @return
	 */
	public boolean isNeedCheckLockTask(Integer taskMoniId,Integer nodeId,String orgId);
}
