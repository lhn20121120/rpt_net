package com.fitech.model.worktask.model.pojo;

import java.util.Date;

/**
 * WorkTaskMoni entity. @author MyEclipse Persistence Tools
 */

public class WorkTaskMoni implements java.io.Serializable {

	// Fields

	private Long taskMoniId;
	private Integer taskId;
	private Date taskTerm;
	private Integer overFlag;
	private Date startDate;
	private Date endDate;
	private Long priWorkTaskId;
	private Integer execFlag;
	private Integer year;
	private Integer term;
	private Integer day;
	private String taskName;

	// Constructors

	/** default constructor */
	public WorkTaskMoni() {
	}

	/** full constructor */
	public WorkTaskMoni(Integer taskId, Date taskTerm, Integer overFlag,
			Date startDate, Date endDate, Long priWorkTaskId, Integer execFlag,
			Integer year, Integer term, Integer day, String taskName) {
		this.taskId = taskId;
		this.taskTerm = taskTerm;
		this.overFlag = overFlag;
		this.startDate = startDate;
		this.endDate = endDate;
		this.priWorkTaskId = priWorkTaskId;
		this.execFlag = execFlag;
		this.year = year;
		this.term = term;
		this.day = day;
		this.taskName = taskName;
	}

	// Property accessors

	public Long getTaskMoniId() {
		return this.taskMoniId;
	}

	public void setTaskMoniId(Long taskMoniId) {
		this.taskMoniId = taskMoniId;
	}

	public Integer getTaskId() {
		return this.taskId;
	}

	public void setTaskId(Integer taskId) {
		this.taskId = taskId;
	}

	public Date getTaskTerm() {
		return this.taskTerm;
	}

	public void setTaskTerm(Date taskTerm) {
		this.taskTerm = taskTerm;
	}

	public Integer getOverFlag() {
		return this.overFlag;
	}

	public void setOverFlag(Integer overFlag) {
		this.overFlag = overFlag;
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

	public Long getPriWorkTaskId() {
		return this.priWorkTaskId;
	}

	public void setPriWorkTaskId(Long priWorkTaskId) {
		this.priWorkTaskId = priWorkTaskId;
	}

	public Integer getExecFlag() {
		return this.execFlag;
	}

	public void setExecFlag(Integer execFlag) {
		this.execFlag = execFlag;
	}

	public Integer getYear() {
		return this.year;
	}

	public void setYear(Integer year) {
		this.year = year;
	}

	public Integer getTerm() {
		return this.term;
	}

	public void setTerm(Integer term) {
		this.term = term;
	}

	public Integer getDay() {
		return this.day;
	}

	public void setDay(Integer day) {
		this.day = day;
	}

	public String getTaskName() {
		return this.taskName;
	}

	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}

}