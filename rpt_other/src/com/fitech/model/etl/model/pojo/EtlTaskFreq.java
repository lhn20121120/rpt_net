package com.fitech.model.etl.model.pojo;

/**
 * EtlTaskFreq entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class EtlTaskFreq implements java.io.Serializable {

	// Fields

	private EtlTaskFreqId id =new EtlTaskFreqId();

	// Constructors

	/** default constructor */
	public EtlTaskFreq() {
	}

	/** full constructor */
	public EtlTaskFreq(EtlTaskFreqId id) {
		this.id = id;
	}

	// Property accessors

	public EtlTaskFreqId getId() {
		return this.id;
	}

	public void setId(EtlTaskFreqId id) {
		this.id = id;
	}

}