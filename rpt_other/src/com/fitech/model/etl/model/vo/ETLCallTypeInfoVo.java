package com.fitech.model.etl.model.vo;

public class ETLCallTypeInfoVo {
	// Fields

	private Integer callTypeId;
	private String callTypeName;

	// Constructors

	/** default constructor */
	public ETLCallTypeInfoVo() {
	}

	/** full constructor */
	public ETLCallTypeInfoVo(String callTypeName) {
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
