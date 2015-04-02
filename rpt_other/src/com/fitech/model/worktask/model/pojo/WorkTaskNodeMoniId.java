package com.fitech.model.worktask.model.pojo;

/**
 * WorkTaskNodeMoniId entity. @author MyEclipse Persistence Tools
 */

public class WorkTaskNodeMoniId implements java.io.Serializable {

	// Fields

	private Long taskMoniId;
	private Integer nodeId;
	private String orgId;
	private Integer performNumber;

	// Constructors

	/** default constructor */
	public WorkTaskNodeMoniId() {
	}

	/** full constructor */
	public WorkTaskNodeMoniId(Long taskMoniId, Integer nodeId, String orgId,
			Integer performNumber) {
		this.taskMoniId = taskMoniId;
		this.nodeId = nodeId;
		this.orgId = orgId;
		this.performNumber = performNumber;
	}

	// Property accessors

	public Long getTaskMoniId() {
		return this.taskMoniId;
	}

	public void setTaskMoniId(Long taskMoniId) {
		this.taskMoniId = taskMoniId;
	}

	public Integer getNodeId() {
		return this.nodeId;
	}

	public void setNodeId(Integer nodeId) {
		this.nodeId = nodeId;
	}

	public String getOrgId() {
		return this.orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

	public Integer getPerformNumber() {
		return this.performNumber;
	}

	public void setPerformNumber(Integer performNumber) {
		this.performNumber = performNumber;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof WorkTaskNodeMoniId))
			return false;
		WorkTaskNodeMoniId castOther = (WorkTaskNodeMoniId) other;

		return ((this.getTaskMoniId() == castOther.getTaskMoniId()) || (this
				.getTaskMoniId() != null
				&& castOther.getTaskMoniId() != null && this.getTaskMoniId()
				.equals(castOther.getTaskMoniId())))
				&& ((this.getNodeId() == castOther.getNodeId()) || (this
						.getNodeId() != null
						&& castOther.getNodeId() != null && this.getNodeId()
						.equals(castOther.getNodeId())))
				&& ((this.getOrgId() == castOther.getOrgId()) || (this
						.getOrgId() != null
						&& castOther.getOrgId() != null && this.getOrgId()
						.equals(castOther.getOrgId())))
				&& ((this.getPerformNumber() == castOther.getPerformNumber()) || (this
						.getPerformNumber() != null
						&& castOther.getPerformNumber() != null && this
						.getPerformNumber()
						.equals(castOther.getPerformNumber())));
	}

	public int hashCode() {
		int result = 17;

		result = 37
				* result
				+ (getTaskMoniId() == null ? 0 : this.getTaskMoniId()
						.hashCode());
		result = 37 * result
				+ (getNodeId() == null ? 0 : this.getNodeId().hashCode());
		result = 37 * result
				+ (getOrgId() == null ? 0 : this.getOrgId().hashCode());
		result = 37
				* result
				+ (getPerformNumber() == null ? 0 : this.getPerformNumber()
						.hashCode());
		return result;
	}

}