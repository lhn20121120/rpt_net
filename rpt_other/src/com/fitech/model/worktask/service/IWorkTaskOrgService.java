package com.fitech.model.worktask.service;

import java.util.List;

import com.fitech.framework.core.service.IBaseService;
import com.fitech.model.worktask.model.pojo.ViewWorktaskOrg;
import com.fitech.model.worktask.model.pojo.ViewWorktaskOrgId;
import com.fitech.model.worktask.model.pojo.WorkTaskInfo;

public interface IWorkTaskOrgService extends IBaseService<ViewWorktaskOrg, String>{

	public List getAllFirstOrg();

	public List getFirstOrgById(String orgId,Long userId);

	public List getChildListByOrgId(String id ,String userId);

	public void getChildListByOrgId(List list,String id,String userId);
}
