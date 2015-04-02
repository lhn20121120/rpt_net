package com.cbrc.smis.entity;

/**
 * AFForceRep entity. @author MyEclipse Persistence Tools
 */

public class AFForceRep implements java.io.Serializable {

	// Fields

	private Integer repInId;
	private Integer forceType;

	// Constructors

	/** default constructor */
	public AFForceRep() {
	}

	/** full constructor */
	public AFForceRep(Integer forceType) {
		this.forceType = forceType;
	}

	// Property accessors

	public Integer getRepInId() {
		return this.repInId;
	}

	public void setRepInId(Integer repInId) {
		this.repInId = repInId;
	}

	public Integer getForceType() {
		return this.forceType;
	}

	public void setForceType(Integer forceType) {
		this.forceType = forceType;
	}

}