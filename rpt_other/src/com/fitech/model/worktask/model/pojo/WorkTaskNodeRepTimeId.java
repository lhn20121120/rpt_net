package com.fitech.model.worktask.model.pojo;

/**
 * WorkTaskNodeRepTimeId entity. @author MyEclipse Persistence Tools
 */

public class WorkTaskNodeRepTimeId implements java.io.Serializable {

	// Fields

	private Integer nodeId;
	private String orgLevel;

	// Constructors

	@Override
	public String toString() {
		return "WorkTaskNodeRepTimeId [nodeId=" + nodeId + ", orgLevel="
				+ orgLevel + "]";
	}

	/** default constructor */
	public WorkTaskNodeRepTimeId() {
	}

	/** full constructor */
	public WorkTaskNodeRepTimeId(Integer nodeId, String orgLevel) {
		this.nodeId = nodeId;
		this.orgLevel = orgLevel;
	}

	// Property accessors

	public Integer getNodeId() {
		return this.nodeId;
	}

	public void setNodeId(Integer nodeId) {
		this.nodeId = nodeId;
	}

	public String getOrgLevel() {
		return this.orgLevel;
	}

	public void setOrgLevel(String orgLevel) {
		this.orgLevel = orgLevel;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof WorkTaskNodeRepTimeId))
			return false;
		WorkTaskNodeRepTimeId castOther = (WorkTaskNodeRepTimeId) other;

		return ((this.getNodeId() == castOther.getNodeId()) || (this
				.getNodeId() != null
				&& castOther.getNodeId() != null && this.getNodeId().equals(
				castOther.getNodeId())))
				&& ((this.getOrgLevel() == castOther.getOrgLevel()) || (this
						.getOrgLevel() != null
						&& castOther.getOrgLevel() != null && this
						.getOrgLevel().equals(castOther.getOrgLevel())));
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result
				+ (getNodeId() == null ? 0 : this.getNodeId().hashCode());
		result = 37 * result
				+ (getOrgLevel() == null ? 0 : this.getOrgLevel().hashCode());
		return result;
	}

}