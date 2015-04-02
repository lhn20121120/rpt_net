package com.fitech.model.worktask.service;

import com.fitech.framework.core.service.IBaseService;
import com.fitech.model.worktask.model.pojo.WorkTaskLock;

public interface IWorkTaskLockService extends IBaseService<WorkTaskLock, String>{
	public void save(WorkTaskLock w);
	public void delete(String userId);
}
