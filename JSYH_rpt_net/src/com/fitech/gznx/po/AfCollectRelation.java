package com.fitech.gznx.po;

/**
 * AfCollectRelation entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class AfCollectRelation implements java.io.Serializable {

	// Fields

	private AfCollectRelationId id;

	// Constructors

	/** default constructor */
	public AfCollectRelation() {
	}

	/** full constructor */
	public AfCollectRelation(AfCollectRelationId id) {
		this.id = id;
	}

	// Property accessors

	public AfCollectRelationId getId() {
		return this.id;
	}

	public void setId(AfCollectRelationId id) {
		this.id = id;
	}

}