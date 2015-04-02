package com.fitech.model.worktask.model.pojo;

/**
 * WorkTaskCondType entity. @author MyEclipse Persistence Tools
 */

public class WorkTaskCondType implements java.io.Serializable {

	// Fields

	private String condTypeId;
	private String condTypeName;

	// Constructors

	/** default constructor */
	public WorkTaskCondType() {
	}

	/** minimal constructor */
	public WorkTaskCondType(String condTypeId) {
		this.condTypeId = condTypeId;
	}

	/** full constructor */
	public WorkTaskCondType(String condTypeId, String condTypeName) {
		this.condTypeId = condTypeId;
		this.condTypeName = condTypeName;
	}

	// Property accessors

	public String getCondTypeId() {
		return this.condTypeId;
	}

	public void setCondTypeId(String condTypeId) {
		this.condTypeId = condTypeId;
	}

	public String getCondTypeName() {
		return this.condTypeName;
	}

	public void setCondTypeName(String condTypeName) {
		this.condTypeName = condTypeName;
	}

}