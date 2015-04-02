package com.fitech.model.worktask.model.pojo;

/**
 * WorkTaskType entity. @author MyEclipse Persistence Tools
 */

public class WorkTaskType implements java.io.Serializable {

	// Fields

	private String taskTypeId;
	private String taskTypeName;
	private Integer useingFlag;

	// Constructors

	/** default constructor */
	public WorkTaskType() {
	}

	/** minimal constructor */
	public WorkTaskType(String taskTypeId) {
		this.taskTypeId = taskTypeId;
	}

	/** full constructor */
	public WorkTaskType(String taskTypeId, String taskTypeName,
			Integer useingFlag) {
		this.taskTypeId = taskTypeId;
		this.taskTypeName = taskTypeName;
		this.useingFlag = useingFlag;
	}

	// Property accessors

	public String getTaskTypeId() {
		return this.taskTypeId;
	}

	public void setTaskTypeId(String taskTypeId) {
		this.taskTypeId = taskTypeId;
	}

	public String getTaskTypeName() {
		return this.taskTypeName;
	}

	public void setTaskTypeName(String taskTypeName) {
		this.taskTypeName = taskTypeName;
	}

	public Integer getUseingFlag() {
		return this.useingFlag;
	}

	public void setUseingFlag(Integer useingFlag) {
		this.useingFlag = useingFlag;
	}

}