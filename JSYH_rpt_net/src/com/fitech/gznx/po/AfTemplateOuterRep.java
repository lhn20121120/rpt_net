package com.fitech.gznx.po;

/**
 * AfTemplateOuterRep entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class AfTemplateOuterRep implements java.io.Serializable {

	// Fields

	private AfTemplateOuterRepId id;

	// Constructors

	/** default constructor */
	public AfTemplateOuterRep() {
	}

	/** full constructor */
	public AfTemplateOuterRep(AfTemplateOuterRepId id) {
		this.id = id;
	}

	// Property accessors

	public AfTemplateOuterRepId getId() {
		return this.id;
	}

	public void setId(AfTemplateOuterRepId id) {
		this.id = id;
	}

}