package com.fitech.model.worktask.model.pojo;

/**
 * MUserToGrp entity. @author MyEclipse Persistence Tools
 */

public class MUserToGrp implements java.io.Serializable {

	// Fields

	private MUserToGrpId id;

	// Constructors

	/** default constructor */
	public MUserToGrp() {
	}

	/** full constructor */
	public MUserToGrp(MUserToGrpId id) {
		this.id = id;
	}

	// Property accessors

	public MUserToGrpId getId() {
		return this.id;
	}

	public void setId(MUserToGrpId id) {
		this.id = id;
	}

}