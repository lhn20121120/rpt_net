package com.fitech.model.worktask.service;

import com.fitech.framework.core.service.IBaseService;
import com.fitech.model.worktask.model.pojo.WorkTaskInfo;


public interface IWorkTaskFreqService extends IBaseService<WorkTaskInfo, Integer>{
	
	public void insertTaskMoni()throws Exception ;
	
}
