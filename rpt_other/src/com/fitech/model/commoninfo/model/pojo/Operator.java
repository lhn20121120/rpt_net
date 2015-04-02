package com.fitech.model.commoninfo.model.pojo;

/**
 * Operator entity. @author MyEclipse Persistence Tools
 */

public class Operator implements java.io.Serializable {

	// Fields

	private OperatorId id;

	// Constructors

	/** default constructor */
	public Operator() {
	}

	/** full constructor */
	public Operator(OperatorId id) {
		this.id = id;
	}

	// Property accessors

	public OperatorId getId() {
		return this.id;
	}

	public void setId(OperatorId id) {
		this.id = id;
	}

}