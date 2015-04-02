package com.fitech.model.worktask.model.pojo;

/**
 * WorkTaskNodeObjectMoni entity. @author MyEclipse Persistence Tools
 */

public class WorkTaskNodeObjectMoni implements java.io.Serializable {

	// Fields

	private WorkTaskNodeObjectMoniId id;

	// Constructors

	/** default constructor */
	public WorkTaskNodeObjectMoni() {
	}

	/** full constructor */
	public WorkTaskNodeObjectMoni(WorkTaskNodeObjectMoniId id) {
		this.id = id;
	}

	// Property accessors

	public WorkTaskNodeObjectMoniId getId() {
		return this.id;
	}

	public void setId(WorkTaskNodeObjectMoniId id) {
		this.id = id;
	}

}