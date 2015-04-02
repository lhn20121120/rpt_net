package com.fitech.model.worktask.model.pojo;

/**
 * MPurOrg entity. @author MyEclipse Persistence Tools
 */

public class MPurOrg implements java.io.Serializable {

	// Fields

	private MPurOrgId id;

	// Constructors

	/** default constructor */
	public MPurOrg() {
	}

	/** full constructor */
	public MPurOrg(MPurOrgId id) {
		this.id = id;
	}

	// Property accessors

	public MPurOrgId getId() {
		return this.id;
	}

	public void setId(MPurOrgId id) {
		this.id = id;
	}

}