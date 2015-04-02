package com.fitech.model.worktask.vo;

import java.io.Serializable;
import java.util.Date;

public class WorkTaskIndexVo extends WorkTaskBaseVo implements Serializable{
	private String taskName; //任务名称
	private String taskTerm; //任务期数
	private Long taskNum; //任务数量
	private Long taskMoniId; //工作任务id
	private String busiLine;  //任务条线
	private Integer nodeFlag; //任务状态
	private Date taskDate;
	public Date getTaskDate() {
		return taskDate;
	}
	public void setTaskDate(Date taskDate) {
		this.taskDate = taskDate;
	}
	private String orgId;
	public String getTaskTerm() {
		return taskTerm;
	}
	public void setTaskTerm(String taskTerm) {
		this.taskTerm = taskTerm;
	}
	public String getOrgId() {
		return orgId;
	}
	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}
	public String getTaskName() {
		return taskName;
	}
	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}
	public Long getTaskNum() {
		return taskNum;
	}
	public void setTaskNum(Long taskNum) {
		this.taskNum = taskNum;
	}
	public Long getTaskMoniId() {
		return taskMoniId;
	}
	public void setTaskMoniId(Long taskMoniId) {
		this.taskMoniId = taskMoniId;
	}
	public String getBusiLine() {
		return busiLine;
	}
	public void setBusiLine(String busiLine) {
		this.busiLine = busiLine;
	}
	public Integer getNodeFlag() {
		return nodeFlag;
	}
	public void setNodeFlag(Integer nodeFlag) {
		this.nodeFlag = nodeFlag;
	}
}
