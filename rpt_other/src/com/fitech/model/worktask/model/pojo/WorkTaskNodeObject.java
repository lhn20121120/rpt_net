package com.fitech.model.worktask.model.pojo;

/**
 * WorkTaskNodeObject entity. @author MyEclipse Persistence Tools
 */

public class WorkTaskNodeObject implements java.io.Serializable {

	// Fields

	@Override
	public String toString() {
		return "WorkTaskNodeObject [id=" + id + "]";
	}

	private WorkTaskNodeObjectId id;

	// Constructors

	/** default constructor */
	public WorkTaskNodeObject() {
	}

	/** full constructor */
	public WorkTaskNodeObject(WorkTaskNodeObjectId id) {
		this.id = id;
	}

	// Property accessors

	public WorkTaskNodeObjectId getId() {
		return this.id;
	}

	public void setId(WorkTaskNodeObjectId id) {
		this.id = id;
	}

}