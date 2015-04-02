package com.fitech.model.worktask.service.impl;

import org.hibernate.Session;

import com.fitech.framework.core.service.impl.DefaultBaseService;
import com.fitech.model.worktask.model.pojo.WorkTaskInfo;
import com.fitech.model.worktask.model.pojo.WorkTaskType;
import com.fitech.model.worktask.service.TestService;

public class TestServiceImpl extends DefaultBaseService<WorkTaskInfo, String> implements TestService{

	@Override
	public void saveWorkTaskType() throws Exception {
		// TODO Auto-generated method stub
		WorkTaskType type = (WorkTaskType)this.objectDao.findObject("from WorkTaskType where taskTypeId='pjhz'");
		Session session = this.objectDao.getHibernateTemplate().getSessionFactory().getCurrentSession();
		type.setTaskTypeName("拼接汇总流程");
		this.objectDao.saveOrUpdate(type);
		System.out.println();
	}
}
