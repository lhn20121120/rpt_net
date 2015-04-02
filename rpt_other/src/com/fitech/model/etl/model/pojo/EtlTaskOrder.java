package com.fitech.model.etl.model.pojo;

/**
 * EtlTaskOrder entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class EtlTaskOrder implements java.io.Serializable {

	// Fields

	private EtlTaskOrderId id=new EtlTaskOrderId();

	// Constructors

	/** default constructor */
	public EtlTaskOrder() {
	}

	/** full constructor */
	public EtlTaskOrder(EtlTaskOrderId id) {
		this.id = id;
	}

	// Property accessors

	public EtlTaskOrderId getId() {
		return this.id;
	}

	public void setId(EtlTaskOrderId id) {
		this.id = id;
	}

}