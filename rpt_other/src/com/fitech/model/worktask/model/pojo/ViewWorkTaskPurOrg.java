package com.fitech.model.worktask.model.pojo;

/**
 * ViewWorkTaskPurOrg entity. @author MyEclipse Persistence Tools
 */

public class ViewWorkTaskPurOrg implements java.io.Serializable {

	// Fields

	private ViewWorkTaskPurOrgId id;

	// Constructors

	/** default constructor */
	public ViewWorkTaskPurOrg() {
	}

	/** full constructor */
	public ViewWorkTaskPurOrg(ViewWorkTaskPurOrgId id) {
		this.id = id;
	}

	// Property accessors

	public ViewWorkTaskPurOrgId getId() {
		return this.id;
	}

	public void setId(ViewWorkTaskPurOrgId id) {
		this.id = id;
	}

}