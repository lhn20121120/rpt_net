package com.fitech.model.etl.model.vo;

public class ETLFileTypeInfoVo {
	// Fields

	private String fileTypeCode;
	private String fileTypeName;
	private Integer orderId;

	// Constructors

	/** default constructor */
	public ETLFileTypeInfoVo() {
	}

	/** full constructor */
	public ETLFileTypeInfoVo(String fileTypeName, Integer orderId) {
		this.fileTypeName = fileTypeName;
		this.orderId = orderId;
	}

	// Property accessors

	public String getFileTypeCode() {
		return this.fileTypeCode;
	}

	public void setFileTypeCode(String fileTypeCode) {
		this.fileTypeCode = fileTypeCode;
	}

	public String getFileTypeName() {
		return this.fileTypeName;
	}

	public void setFileTypeName(String fileTypeName) {
		this.fileTypeName = fileTypeName;
	}

	public Integer getOrderId() {
		return this.orderId;
	}

	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}
}
