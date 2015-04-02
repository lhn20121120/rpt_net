package com.fitech.gznx.po;

/**
 * AfTemplateCurrRelation entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class AfTemplateCurrRelation implements java.io.Serializable {

	// Fields

	private AfTemplateCurrRelationId id;

	// Constructors

	/** default constructor */
	public AfTemplateCurrRelation() {
	}

	/** full constructor */
	public AfTemplateCurrRelation(AfTemplateCurrRelationId id) {
		this.id = id;
	}

	// Property accessors

	public AfTemplateCurrRelationId getId() {
		return this.id;
	}

	public void setId(AfTemplateCurrRelationId id) {
		this.id = id;
	}

}