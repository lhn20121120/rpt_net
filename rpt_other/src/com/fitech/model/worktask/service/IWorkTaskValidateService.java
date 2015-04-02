package com.fitech.model.worktask.service;

import java.util.List;

import com.fitech.framework.core.service.IBaseService;
import com.fitech.model.worktask.model.pojo.WorkTaskNodeMoni;
import com.fitech.model.worktask.vo.WorkTaskPendingTaskVo;

public interface IWorkTaskValidateService extends IBaseService<WorkTaskNodeMoni, String>{
	/**
	 * 任务校验
	 * @return
	 * 参数templateIds,taskid,taskMoniId,taskTerm
	 * @throws Exception
	 */
	public String validate(List<WorkTaskPendingTaskVo> pvos)throws Exception;
	/**
	 * 得到未通过的templateIds
	 */
	public List<String>  getNoPassTemplateIds(WorkTaskPendingTaskVo pvos,String operatorType) throws Exception;
	/**
	 * 任务重置
	 * 参数templateIds,taskid,taskMoniId,taskTerm
	 * @throws Exception
	 */
	public void resetByFill(WorkTaskPendingTaskVo vo,List passTemplateIdList)throws Exception;
	/**
	 * 任务提交时将任务的提交时间与报表的报送时间同步
	 * @param vo
	 * @param passTemplateIdList
	 * @throws Exception
	 */
	public void updateReportDate(WorkTaskPendingTaskVo vo,List passTemplateIdList)throws Exception;
	/**
	 * 任务重置
	 * 参数templateIds,taskid,taskMoniId,taskTerm
	 * @throws Exception
	 */
	public void resetByCheck(WorkTaskPendingTaskVo vo,List passTemplateIdList)throws Exception;
	
	/**
	 * 退回校验检查
	 * @param pvos
	 * @return
	 * @throws Exception
	 */
	public String validateReturn(List<WorkTaskPendingTaskVo> pvos)throws Exception;
}
