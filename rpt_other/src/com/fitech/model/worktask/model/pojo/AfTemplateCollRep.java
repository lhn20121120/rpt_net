package com.fitech.model.worktask.model.pojo;

/**
 * AfTemplateCollRep entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class AfTemplateCollRep implements java.io.Serializable {

	// Fields

	private AfTemplateCollRepId id;

	// Constructors

	/** default constructor */
	public AfTemplateCollRep() {
	}

	/** full constructor */
	public AfTemplateCollRep(AfTemplateCollRepId id) {
		this.id = id;
	}

	// Property accessors

	public AfTemplateCollRepId getId() {
		return this.id;
	}

	public void setId(AfTemplateCollRepId id) {
		this.id = id;
	}

}