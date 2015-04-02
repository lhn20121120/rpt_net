package com.fitech.model.worktask.model.pojo;

/**
 * WorkTaskNodeParam entity. @author MyEclipse Persistence Tools
 */

public class WorkTaskNodeParam implements java.io.Serializable {

	// Fields

	private WorkTaskNodeParamId id;

	// Constructors

	/** default constructor */
	public WorkTaskNodeParam() {
	}

	/** full constructor */
	public WorkTaskNodeParam(WorkTaskNodeParamId id) {
		this.id = id;
	}

	// Property accessors

	public WorkTaskNodeParamId getId() {
		return this.id;
	}

	public void setId(WorkTaskNodeParamId id) {
		this.id = id;
	}

}