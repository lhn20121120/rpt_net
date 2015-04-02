package com.fitech.model.worktask.model.pojo;

import java.util.Date;

/**
 * WorkTaskInfo entity. @author MyEclipse Persistence Tools
 */

public class WorkTaskInfo implements java.io.Serializable {

	// Fields

	private Integer taskId;
	private String taskName;
	private String freqId;
	private String taskTypeId;
	private Integer triggerShifting;
	private Date startDate;
	private Date endDate;
	private Integer taskTime;
	private Integer preTaskId;
	private String busiLineId;
	private String trrigerId;
	private Integer publicFlag;
	private Integer splitComitFlag;
	// Constructors

	public Integer getSplitComitFlag() {
		return splitComitFlag;
	}

	public void setSplitComitFlag(Integer splitComitFlag) {
		this.splitComitFlag = splitComitFlag;
	}

	public Integer getPublicFlag() {
		return publicFlag;
	}

	public void setPublicFlag(Integer publicFlag) {
		this.publicFlag = publicFlag;
	}

	/** default constructor */
	public WorkTaskInfo() {
	}

	/** minimal constructor */
	public WorkTaskInfo(Integer taskId) {
		this.taskId = taskId;
	}

	@Override
	public String toString() {
		return "WorkTaskInfo [taskId=" + taskId + ", taskName=" + taskName
				+ ", freqId=" + freqId + ", taskTypeId=" + taskTypeId
				+ ", triggerShifting=" + triggerShifting + ", startDate="
				+ startDate + ", endDate=" + endDate + ", taskTime=" + taskTime
				+ ", preTaskId=" + preTaskId + ", busiLineId=" + busiLineId
				+ ", trrigerId=" + trrigerId + "]";
	}

	/** full constructor */
	public WorkTaskInfo(Integer taskId, String taskName, String freqId,
			String taskTypeId, Integer triggerShifting, Date startDate,
			Date endDate, Integer taskTime, Integer preTaskId,
			String busiLineId, String trrigerId) {
		this.taskId = taskId;
		this.taskName = taskName;
		this.freqId = freqId;
		this.taskTypeId = taskTypeId;
		this.triggerShifting = triggerShifting;
		this.startDate = startDate;
		this.endDate = endDate;
		this.taskTime = taskTime;
		this.preTaskId = preTaskId;
		this.busiLineId = busiLineId;
		this.trrigerId = trrigerId;
	}

	// Property accessors

	public Integer getTaskId() {
		return this.taskId;
	}

	public void setTaskId(Integer taskId) {
		this.taskId = taskId;
	}

	public String getTaskName() {
		return this.taskName;
	}

	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}

	public String getFreqId() {
		return this.freqId;
	}

	public void setFreqId(String freqId) {
		this.freqId = freqId;
	}

	public String getTaskTypeId() {
		return this.taskTypeId;
	}

	public void setTaskTypeId(String taskTypeId) {
		this.taskTypeId = taskTypeId;
	}

	public Integer getTriggerShifting() {
		return this.triggerShifting;
	}

	public void setTriggerShifting(Integer triggerShifting) {
		this.triggerShifting = triggerShifting;
	}

	public Date getStartDate() {
		return this.startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return this.endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public Integer getTaskTime() {
		return this.taskTime;
	}

	public void setTaskTime(Integer taskTime) {
		this.taskTime = taskTime;
	}

	public Integer getPreTaskId() {
		return this.preTaskId;
	}

	public void setPreTaskId(Integer preTaskId) {
		this.preTaskId = preTaskId;
	}

	public String getBusiLineId() {
		return this.busiLineId;
	}

	public void setBusiLineId(String busiLineId) {
		this.busiLineId = busiLineId;
	}

	public String getTrrigerId() {
		return this.trrigerId;
	}

	public void setTrrigerId(String trrigerId) {
		this.trrigerId = trrigerId;
	}

}