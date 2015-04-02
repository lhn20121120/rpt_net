package com.fitech.model.worktask.service;

import java.util.List;

import com.fitech.framework.core.service.IBaseService;
import com.fitech.model.worktask.model.pojo.ViewWorktaskRole;
import com.fitech.model.worktask.model.pojo.WorkTaskNodeRole;

public interface IWorkTaskRoleService extends IBaseService<WorkTaskNodeRole, String>{


	public List <ViewWorktaskRole>findAllRole()throws Exception;

	public List findRoleByTaskId(Integer taskId) throws Exception;

	public void deleteByTaskId(Integer taskId)throws Exception;
	
	

}
