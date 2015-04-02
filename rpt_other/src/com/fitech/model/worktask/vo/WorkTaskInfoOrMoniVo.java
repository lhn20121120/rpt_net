package com.fitech.model.worktask.vo;

import java.io.Serializable;
import java.util.Date;

public class WorkTaskInfoOrMoniVo implements Serializable {

	private String taskName;  //任务名称
	
	private String freqId;   //任务频度
	
	private String trrigerName;   //触发方式
	
	private String busiLineName;   //业务条线
	
	private Date taskTerm;   //期数
	
	private Integer execFlag;  //执行状态

	public String getTaskName() {
		return taskName;
	}

	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}

	public String getFreqId() {
		return freqId;
	}

	public void setFreqId(String freqId) {
		this.freqId = freqId;
	}

	public String getTrrigerName() {
		return trrigerName;
	}

	public void setTrrigerName(String trrigerName) {
		this.trrigerName = trrigerName;
	}

	public String getBusiLineName() {
		return busiLineName;
	}

	public void setBusiLineName(String busiLineName) {
		this.busiLineName = busiLineName;
	}

	public Date getTaskTerm() {
		return taskTerm;
	}

	public void setTaskTerm(Date taskTerm) {
		this.taskTerm = taskTerm;
	}

	public Integer getExecFlag() {
		return execFlag;
	}

	public void setExecFlag(Integer execFlag) {
		this.execFlag = execFlag;
	}
}
