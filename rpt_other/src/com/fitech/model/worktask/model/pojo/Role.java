package com.fitech.model.worktask.model.pojo;

/**
 * Role entity. @author MyEclipse Persistence Tools
 */

public class Role implements java.io.Serializable {

	// Fields

	private RoleId id;

	// Constructors

	/** default constructor */
	public Role() {
	}

	/** full constructor */
	public Role(RoleId id) {
		this.id = id;
	}

	// Property accessors

	public RoleId getId() {
		return this.id;
	}

	public void setId(RoleId id) {
		this.id = id;
	}

}