package com.fitech.model.worktask.service.impl;

import com.fitech.framework.core.dao.BaseDaoException;
import com.fitech.framework.core.service.impl.DefaultBaseService;
import com.fitech.model.worktask.model.pojo.WorkTaskLock;
import com.fitech.model.worktask.service.IWorkTaskLockService;

public class WorkTaskLockServiceImpl extends
		DefaultBaseService<WorkTaskLock, String> implements
		IWorkTaskLockService {

	public void save(WorkTaskLock w){
		try {
			this.objectDao.save(w);
		} catch (BaseDaoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void delete(WorkTaskLock w){
		try {
			this.objectDao.delete(w);
		} catch (BaseDaoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void delete(String userId) {
		String hql = "from WorkTaskLock where userId='" + userId + "'";
		this.objectDao.deleteObjects(hql);
	}
}
