package com.fitech.gznx.po;

public class AfValiRelation implements java.io.Serializable{
	// Fields

	private AfValiRelationId id;
	private String sysFlag;
	// Constructors

	/** default constructor */
	public AfValiRelation() {
	}

	/** full constructor */
	public AfValiRelation(AfValiRelationId id) {
		this.id = id;
	}

	// Property accessors

	public AfValiRelationId getId() {
		return this.id;
	}

	public void setId(AfValiRelationId id) {
		this.id = id;
	}

	public String getSysFlag() {
		return sysFlag;
	}

	public void setSysFlag(String sysFlag) {
		this.sysFlag = sysFlag;
	}

}
