package com.fitech.model.worktask.model.pojo;

/**
 * AfTemplateOrgRelation entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class AfTemplateOrgRelation implements java.io.Serializable {

	// Fields

	private AfTemplateOrgRelationId id;

	// Constructors

	/** default constructor */
	public AfTemplateOrgRelation() {
	}

	/** full constructor */
	public AfTemplateOrgRelation(AfTemplateOrgRelationId id) {
		this.id = id;
	}

	// Property accessors

	public AfTemplateOrgRelationId getId() {
		return this.id;
	}

	public void setId(AfTemplateOrgRelationId id) {
		this.id = id;
	}

}