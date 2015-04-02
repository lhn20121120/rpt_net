package com.fitech.model.worktask.model.pojo;

/**
 * ViewWorktaskRole entity. @author MyEclipse Persistence Tools
 */

public class ViewWorktaskRole implements java.io.Serializable {

	// Fields

	private ViewWorktaskRoleId id;

	// Constructors

	/** default constructor */
	public ViewWorktaskRole() {
	}

	/** full constructor */
	public ViewWorktaskRole(ViewWorktaskRoleId id) {
		this.id = id;
	}

	// Property accessors

	public ViewWorktaskRoleId getId() {
		return this.id;
	}

	public void setId(ViewWorktaskRoleId id) {
		this.id = id;
	}

}