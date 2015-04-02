package com.fitech.model.worktask.service.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.fitech.framework.core.service.impl.DefaultBaseService;
import com.fitech.framework.core.util.DateUtil;
import com.fitech.model.worktask.common.WorkTaskConfig;
import com.fitech.model.worktask.model.pojo.WorkTaskInfo;
import com.fitech.model.worktask.service.IWorkTaskMoniService;

public class WorkTaskJobFreqBaseServiceImpl extends DefaultBaseService<WorkTaskInfo, Integer>{
	
	private String objectName;

	private String freqId;

	private Integer mainTaskFlag;
	
	public Log log = LogFactory.getLog(WorkTaskJobFreqBaseServiceImpl.class);
	
	private IWorkTaskMoniService workTaskMoniService;
	
	public void insertTaskMoni() throws Exception {
		this.workTaskMoniService.insertWorkTaskAll(this.freqId,false);
	}
	
	public IWorkTaskMoniService getWorkTaskMoniService() {
		return workTaskMoniService;
	}

	public void setWorkTaskMoniService(IWorkTaskMoniService workTaskMoniService) {
		this.workTaskMoniService = workTaskMoniService;
	}
	
	public Integer getMainTaskFlag() {
		return mainTaskFlag;
	}

	public void setMainTaskFlag(Integer mainTaskFlag) {
		this.mainTaskFlag = mainTaskFlag;
	}

	public String getObjectName() {
		return objectName;
	}

	public void setObjectName(String objectName) {
		this.objectName = objectName;
	}

	public String getFreqId() {
		return freqId;
	}

	public void setFreqId(String freqId) {
		this.freqId = freqId;
	}

	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		// TODO Auto-generated method stub
		
	}

	
}
