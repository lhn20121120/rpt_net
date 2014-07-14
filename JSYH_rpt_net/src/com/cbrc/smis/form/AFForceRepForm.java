package com.cbrc.smis.form;

public class AFForceRepForm {
	private Integer repInId;
	private Integer forceType;
	private String repInIds;//多个机构ID拼接的字符串

	// Constructors

	/** default constructor */
	public AFForceRepForm() {
	}

	/** full constructor */
	public AFForceRepForm(Integer forceType) {
		this.forceType = forceType;
	}

	// Property accessors

	public Integer getRepInId() {
		return this.repInId;
	}

	public void setRepInId(Integer repInId) {
		this.repInId = repInId;
	}

	public Integer getForceType() {
		return this.forceType;
	}

	public void setForceType(Integer forceType) {
		this.forceType = forceType;
	}

	public String getRepInIds() {
		return repInIds;
	}

	public void setRepInIds(String repInIds) {
		this.repInIds = repInIds;
	}
	
	
}
