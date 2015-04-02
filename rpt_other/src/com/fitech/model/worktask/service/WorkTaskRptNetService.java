package com.fitech.model.worktask.service;

import java.util.List;

import com.fitech.framework.core.service.IBaseService;
import com.fitech.model.worktask.model.pojo.WorkTaskNodeMoni;
import com.fitech.model.worktask.vo.WorkTaskPendingTaskVo;

public interface WorkTaskRptNetService extends IBaseService<WorkTaskNodeMoni, String>{
	/**
	 * 更新rpt_net中的报表状态
	 * @param pvos
	 * @return
	 * @throws Exception
	 */
	public String updateReport(List<WorkTaskPendingTaskVo> pvos) throws Exception;
}