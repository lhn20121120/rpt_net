package com.fitech.model.worktask.service.impl;

import java.util.ArrayList;
import java.util.List;

import com.fitech.framework.core.service.BaseServiceException;
import com.fitech.framework.core.service.impl.DefaultBaseService;
import com.fitech.model.worktask.model.pojo.ViewWorktaskRole;
import com.fitech.model.worktask.model.pojo.WorkTaskNodeRole;
import com.fitech.model.worktask.service.IWorkTaskRoleService;

public class WorkTaskRoleServiceImpl extends
		DefaultBaseService<WorkTaskNodeRole, String> implements
		IWorkTaskRoleService {
	@Override
	public void deleteByTaskId(Integer taskId) throws Exception {
		String hsql = "delete from WorkTaskNodeRole t where t.id.nodeId in (select i.nodeId from WorkTaskNodeInfo i where i.taskId = "+taskId+" )";
		this.objectDao.deleteObjects(hsql);
		
	}
	@Override
	public List<ViewWorktaskRole> findAllRole() throws Exception{
		List<ViewWorktaskRole> list = new ArrayList<ViewWorktaskRole>();
		String hsql = "from ViewWorktaskRole vr ";
		list = this.findListByHsql(hsql, null);
		return list;
	}

	@Override
	public List findRoleByTaskId(Integer taskId) throws Exception {
		List <Integer >roleList = new ArrayList<Integer > ();

		String hsql = "select t.id.roleId from WorkTaskNodeRole t where t.id.nodeId in (select i.nodeId from WorkTaskNodeInfo i where i.taskId = "+taskId+" )";
		roleList = this.findListByHsql(hsql, null);
		if (roleList.size() == 0)
			roleList = null;
		return roleList;
	}


}
