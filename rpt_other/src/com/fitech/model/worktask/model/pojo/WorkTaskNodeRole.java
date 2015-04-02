package com.fitech.model.worktask.model.pojo;

/**
 * WorkTaskNodeRole entity. @author MyEclipse Persistence Tools
 */

public class WorkTaskNodeRole implements java.io.Serializable {

	// Fields

	private WorkTaskNodeRoleId id;

	// Constructors

	@Override
	public String toString() {
		return "WorkTaskNodeRole [id=" + id + "]";
	}

	/** default constructor */
	public WorkTaskNodeRole() {
	}

	/** full constructor */
	public WorkTaskNodeRole(WorkTaskNodeRoleId id) {
		this.id = id;
	}

	// Property accessors

	public WorkTaskNodeRoleId getId() {
		return this.id;
	}

	public void setId(WorkTaskNodeRoleId id) {
		this.id = id;
	}

}