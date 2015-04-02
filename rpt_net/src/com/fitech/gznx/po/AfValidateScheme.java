package com.fitech.gznx.po;

/**
 * AfValidateScheme entity. @author MyEclipse Persistence Tools
 */

public class AfValidateScheme implements java.io.Serializable {

	// Fields

	private Integer validateSchemeId;
	private String validateSchemeName;

	// Constructors

	/** default constructor */
	public AfValidateScheme() {
	}

	/** minimal constructor */
	public AfValidateScheme(Integer validateSchemeId) {
		this.validateSchemeId = validateSchemeId;
	}

	/** full constructor */
	public AfValidateScheme(Integer validateSchemeId, String validateSchemeName) {
		this.validateSchemeId = validateSchemeId;
		this.validateSchemeName = validateSchemeName;
	}

	// Property accessors

	public Integer getValidateSchemeId() {
		return this.validateSchemeId;
	}

	public void setValidateSchemeId(Integer validateSchemeId) {
		this.validateSchemeId = validateSchemeId;
	}

	public String getValidateSchemeName() {
		return this.validateSchemeName;
	}

	public void setValidateSchemeName(String validateSchemeName) {
		this.validateSchemeName = validateSchemeName;
	}

}