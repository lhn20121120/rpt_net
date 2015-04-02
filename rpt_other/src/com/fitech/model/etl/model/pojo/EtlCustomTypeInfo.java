package com.fitech.model.etl.model.pojo;

/**
 * EtlCustomTypeInfo entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class EtlCustomTypeInfo implements java.io.Serializable {

	// Fields

	private String customTypeCode;
	private String customTypeName;
	private Integer orderId;

	// Constructors

	/** default constructor */
	public EtlCustomTypeInfo() {
	}

	/** full constructor */
	public EtlCustomTypeInfo(String customTypeName, Integer orderId) {
		this.customTypeName = customTypeName;
		this.orderId = orderId;
	}

	// Property accessors

	public String getCustomTypeCode() {
		return this.customTypeCode;
	}

	public void setCustomTypeCode(String customTypeCode) {
		this.customTypeCode = customTypeCode;
	}

	public String getCustomTypeName() {
		return this.customTypeName;
	}

	public void setCustomTypeName(String customTypeName) {
		this.customTypeName = customTypeName;
	}

	public Integer getOrderId() {
		return this.orderId;
	}

	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}

}