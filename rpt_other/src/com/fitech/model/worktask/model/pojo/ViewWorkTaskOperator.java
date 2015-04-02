package com.fitech.model.worktask.model.pojo;

/**
 * ViewWorkTaskOperator entity. @author MyEclipse Persistence Tools
 */

public class ViewWorkTaskOperator implements java.io.Serializable {

	// Fields

	private ViewWorkTaskOperatorId id;

	// Constructors

	/** default constructor */
	public ViewWorkTaskOperator() {
	}

	/** full constructor */
	public ViewWorkTaskOperator(ViewWorkTaskOperatorId id) {
		this.id = id;
	}

	// Property accessors

	public ViewWorkTaskOperatorId getId() {
		return this.id;
	}

	public void setId(ViewWorkTaskOperatorId id) {
		this.id = id;
	}

}