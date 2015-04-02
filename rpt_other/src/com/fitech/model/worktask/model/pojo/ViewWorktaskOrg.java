package com.fitech.model.worktask.model.pojo;

/**
 * ViewWorktaskOrg entity. @author MyEclipse Persistence Tools
 */

public class ViewWorktaskOrg implements java.io.Serializable {

	// Fields

	private ViewWorktaskOrgId id;

	// Constructors

	/** default constructor */
	public ViewWorktaskOrg() {
	}

	/** full constructor */
	public ViewWorktaskOrg(ViewWorktaskOrgId id) {
		this.id = id;
	}

	// Property accessors

	public ViewWorktaskOrgId getId() {
		return this.id;
	}

	public void setId(ViewWorktaskOrgId id) {
		this.id = id;
	}

}