package com.fitech.model.etl.model.pojo;

import java.util.Date;

/**
 * EtlTaskMoni entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class EtlTaskMoni implements java.io.Serializable {

	// Fields

	private Integer taskMoniId;
	private Integer taskId;
	private String freqId;
	private String taskDate;
	private String taskTerm;
	private Date startDate;
	private Date endDate;
	private Integer overFlag;
	private String objectName;
	private Integer mainTaskFlag;
	private Integer execFlag;

	// Constructors

	public Integer getExecFlag() {
		return execFlag;
	}

	public void setExecFlag(Integer execFlag) {
		this.execFlag = execFlag;
	}

	/** default constructor */
	public EtlTaskMoni() {
	}

	/** full constructor */
	public EtlTaskMoni(Integer taskId, String freqId, String taskDate, String taskTerm, Date startDate, Date endDate, Integer overFlag,
			String objectName, Integer mainTaskFlag) {
		this.taskId = taskId;
		this.freqId = freqId;
		this.taskDate = taskDate;
		this.taskTerm = taskTerm;
		this.startDate = startDate;
		this.endDate = endDate;
		this.overFlag = overFlag;
		this.objectName = objectName;
		this.mainTaskFlag = mainTaskFlag;
	}

	// Property accessors

	public Integer getTaskMoniId() {
		return this.taskMoniId;
	}

	public void setTaskMoniId(Integer taskMoniId) {
		this.taskMoniId = taskMoniId;
	}

	public Integer getTaskId() {
		return this.taskId;
	}

	public void setTaskId(Integer taskId) {
		this.taskId = taskId;
	}

	public String getFreqId() {
		return this.freqId;
	}

	public void setFreqId(String freqId) {
		this.freqId = freqId;
	}

	public String getTaskDate() {
		return this.taskDate;
	}

	public void setTaskDate(String taskDate) {
		this.taskDate = taskDate;
	}

	public String getTaskTerm() {
		return this.taskTerm;
	}

	public void setTaskTerm(String taskTerm) {
		this.taskTerm = taskTerm;
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

	public Integer getOverFlag() {
		return this.overFlag;
	}

	public void setOverFlag(Integer overFlag) {
		this.overFlag = overFlag;
	}

	public String getObjectName() {
		return this.objectName;
	}

	public void setObjectName(String objectName) {
		this.objectName = objectName;
	}

	public Integer getMainTaskFlag() {
		return this.mainTaskFlag;
	}

	public void setMainTaskFlag(Integer mainTaskFlag) {
		this.mainTaskFlag = mainTaskFlag;
	}

}