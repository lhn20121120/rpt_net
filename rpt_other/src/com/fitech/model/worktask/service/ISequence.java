package com.fitech.model.worktask.service;




import java.sql.Statement;

import com.fitech.framework.core.service.IBaseService;
import com.fitech.model.worktask.model.pojo.WorkTaskInfo;

public interface ISequence extends IBaseService<WorkTaskInfo, String> {

	public Integer getSequenceNextval(Statement st,String id,String name);
	public void createOrDropSequence(Statement st,String name,Integer minvalue);
	public void batchInitSequense(String str);
}
