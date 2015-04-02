package com.fitech.model.worktask.model.pojo;

/**
 * WorkTaskNodeRoleId entity. @author MyEclipse Persistence Tools
 */

public class WorkTaskNodeRoleId implements java.io.Serializable {

	// Fields

	private Integer nodeId;
	private Integer roleId;

	// Constructors

	/** default constructor */
	public WorkTaskNodeRoleId() {
	}

	@Override
	public String toString() {
		return "WorkTaskNodeRoleId [nodeId=" + nodeId + ", roleId=" + roleId
				+ "]";
	}

	/** full constructor */
	public WorkTaskNodeRoleId(Integer nodeId, Integer roleId) {
		this.nodeId = nodeId;
		this.roleId = roleId;
	}

	// Property accessors

	public Integer getNodeId() {
		return this.nodeId;
	}

	public void setNodeId(Integer nodeId) {
		this.nodeId = nodeId;
	}

	public Integer getRoleId() {
		return this.roleId;
	}

	public void setRoleId(Integer roleId) {
		this.roleId = roleId;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof WorkTaskNodeRoleId))
			return false;
		WorkTaskNodeRoleId castOther = (WorkTaskNodeRoleId) other;

		return ((this.getNodeId() == castOther.getNodeId()) || (this
				.getNodeId() != null
				&& castOther.getNodeId() != null && this.getNodeId().equals(
				castOther.getNodeId())))
				&& ((this.getRoleId() == castOther.getRoleId()) || (this
						.getRoleId() != null
						&& castOther.getRoleId() != null && this.getRoleId()
						.equals(castOther.getRoleId())));
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result
				+ (getNodeId() == null ? 0 : this.getNodeId().hashCode());
		result = 37 * result
				+ (getRoleId() == null ? 0 : this.getRoleId().hashCode());
		return result;
	}

}