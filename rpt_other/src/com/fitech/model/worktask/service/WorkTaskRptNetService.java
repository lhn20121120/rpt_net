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
	public String updateReport(String userName ,List<WorkTaskPendingTaskVo> pvos,String cuse) throws Exception;

	void writLog(String userName, String cuse, List<String> noPass)
			throws Exception;
	
	
	
}
