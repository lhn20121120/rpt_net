package com.fitech.gznx.po;

/**
 * AfCollectRelation entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class AfProductRelation implements java.io.Serializable {

	// Fields

	private AfProductRelationId id;
	private String sysFlag;
	// Constructors

	/** default constructor */
	public AfProductRelation() {
	}

	/** full constructor */
	public AfProductRelation(AfProductRelationId id) {
		this.id = id;
	}

	// Property accessors

	public AfProductRelationId getId() {
		return this.id;
	}

	public void setId(AfProductRelationId id) {
		this.id = id;
	}

	public String getSysFlag() {
		return sysFlag;
	}

	public void setSysFlag(String sysFlag) {
		this.sysFlag = sysFlag;
	}
	
	
}