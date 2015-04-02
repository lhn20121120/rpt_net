package com.fitech.model.worktask.service;

import java.util.List;

import com.fitech.framework.core.service.IBaseService;
import com.fitech.model.worktask.model.pojo.WorkTaskNodeRepTime;

public interface IWorkTaskNodeRepTimeService extends IBaseService<WorkTaskNodeRepTime, String> {

	public List findRepTimeByTaskId(Integer taskId) throws Exception;

	public void deleteByTaskId(Integer taskId)throws Exception;

}
