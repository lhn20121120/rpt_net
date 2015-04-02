package com.fitech.gznx.po;

/**
 * ViewWorkTaskInfoId entity. @author MyEclipse Persistence Tools
 */

public class ViewWorkTaskInfoId implements java.io.Serializable {

	// Fields

	private Integer taskId;
	private String taskName;

	// Constructors

	/** default constructor */
	public ViewWorkTaskInfoId() {
	}

	/** minimal constructor */
	public ViewWorkTaskInfoId(Integer taskId) {
		this.taskId = taskId;
	}

	/** full constructor */
	public ViewWorkTaskInfoId(Integer taskId, String taskName) {
		this.taskId = taskId;
		this.taskName = taskName;
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

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof ViewWorkTaskInfoId))
			return false;
		ViewWorkTaskInfoId castOther = (ViewWorkTaskInfoId) other;

		return ((this.getTaskId() == castOther.getTaskId()) || (this
				.getTaskId() != null
				&& castOther.getTaskId() != null && this.getTaskId().equals(
				castOther.getTaskId())))
				&& ((this.getTaskName() == castOther.getTaskName()) || (this
						.getTaskName() != null
						&& castOther.getTaskName() != null && this
						.getTaskName().equals(castOther.getTaskName())));
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result
				+ (getTaskId() == null ? 0 : this.getTaskId().hashCode());
		result = 37 * result
				+ (getTaskName() == null ? 0 : this.getTaskName().hashCode());
		return result;
	}

}