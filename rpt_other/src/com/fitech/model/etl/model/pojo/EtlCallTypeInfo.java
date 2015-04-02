package com.fitech.model.etl.model.pojo;

/**
 * EtlCallTypeInfo entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class EtlCallTypeInfo implements java.io.Serializable {

	// Fields

	private Integer callTypeId;
	private String callTypeName;

	// Constructors

	/** default constructor */
	public EtlCallTypeInfo() {
	}

	/** full constructor */
	public EtlCallTypeInfo(String callTypeName) {
		this.callTypeName = callTypeName;
	}

	// Property accessors

	public Integer getCallTypeId() {
		return this.callTypeId;
	}

	public void setCallTypeId(Integer callTypeId) {
		this.callTypeId = callTypeId;
	}

	public String getCallTypeName() {
		return this.callTypeName;
	}

	public void setCallTypeName(String callTypeName) {
		this.callTypeName = callTypeName;
	}

}