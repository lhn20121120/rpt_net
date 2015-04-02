package com.fitech.model.etl.model.vo;

public class ETLCustomTypeInfoVo extends ETLBaseVo{
	// Fields

	private String customTypeCode;
	private String customTypeName;
	private Integer orderId;

	// Constructors

	/** default constructor */
	public ETLCustomTypeInfoVo() {
	}

	/** full constructor */
	public ETLCustomTypeInfoVo(String customTypeName, Integer orderId) {
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
