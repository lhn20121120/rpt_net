package com.fitech.model.etl.model.pojo;

/**
 * EtlProcess entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class EtlProcess implements java.io.Serializable {

	// Fields

	private Integer procId;
	private String procName;

	// Constructors

	/** default constructor */
	public EtlProcess() {
	}

	/** full constructor */
	public EtlProcess(String procName) {
		this.procName = procName;
	}

	// Property accessors

	public Integer getProcId() {
		return this.procId;
	}

	public void setProcId(Integer procId) {
		this.procId = procId;
	}

	public String getProcName() {
		return this.procName;
	}

	public void setProcName(String procName) {
		this.procName = procName;
	}

}