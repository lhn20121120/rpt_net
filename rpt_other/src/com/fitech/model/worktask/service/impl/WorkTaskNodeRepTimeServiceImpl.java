package com.fitech.model.worktask.service.impl;

import java.util.ArrayList;
import java.util.List;

import com.fitech.framework.core.service.impl.DefaultBaseService;
import com.fitech.model.worktask.common.WorkTaskConfig;
import com.fitech.model.worktask.model.pojo.WorkTaskNodeRepTime;
import com.fitech.model.worktask.service.IWorkTaskNodeRepTimeService;

public class WorkTaskNodeRepTimeServiceImpl extends
		DefaultBaseService<WorkTaskNodeRepTime, String> implements
		IWorkTaskNodeRepTimeService {

	@Override
	public List findRepTimeByTaskId(Integer taskId) throws Exception {
		List<Object[]> timeList = new ArrayList<Object[]>();
		String hsql = "select   distinct(t.id.orgLevel ) ,t.repTimeLimit from WorkTaskNodeRepTime t where t.id.nodeId in (select i.nodeId from WorkTaskNodeInfo i where i.taskId = "
				+ taskId + " )";
		List list = this.findListByHsql(hsql, null);
		for (int i = 0; i < list.size(); i++) {

			Object[] os = (Object[]) list.get(i);

			Object[] strs = new Object[2];

			strs[0] = ((String) os[0]).trim();

			strs[1] = ((Integer) os[1]);

			timeList.add(strs);

			if (timeList.size() == 0)
				timeList = null;
		}
		return timeList;
	}

	
	public void deleteByTaskId(Integer taskId) throws Exception {
		String hsql = "delete from WorkTaskNodeRepTime t where t.id.nodeId in (select i.nodeId from WorkTaskNodeInfo i where i.taskId = "+taskId+" )";
		this.objectDao.deleteObjects(hsql);
		
	}
}
