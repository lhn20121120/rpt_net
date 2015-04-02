package com.fitech.model.worktask.service;

import com.fitech.framework.core.service.IBaseService;
import com.fitech.model.worktask.model.pojo.WorkTaskInfo;

public interface TestService extends IBaseService<WorkTaskInfo, String>{
	/**
	 * 保存工作任务类型
	 * @throws Exception
	 */
	public void saveWorkTaskType()throws Exception;
}
