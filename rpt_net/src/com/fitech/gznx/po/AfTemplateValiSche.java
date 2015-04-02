package com.fitech.gznx.po;

/**
 * AfTemplateValiSche entity. @author MyEclipse Persistence Tools
 */

public class AfTemplateValiSche implements java.io.Serializable {

	// Fields

	private AfTemplateValiScheId id;
	private Integer validateSchemeId;

	// Constructors

	/** default constructor */
	public AfTemplateValiSche() {
	}

	/** minimal constructor */
	public AfTemplateValiSche(AfTemplateValiScheId id) {
		this.id = id;
	}

	/** full constructor */
	public AfTemplateValiSche(AfTemplateValiScheId id, Integer validateSchemeId) {
		this.id = id;
		this.validateSchemeId = validateSchemeId;
	}

	// Property accessors

	public AfTemplateValiScheId getId() {
		return this.id;
	}

	public void setId(AfTemplateValiScheId id) {
		this.id = id;
	}

	public Integer getValidateSchemeId() {
		return this.validateSchemeId;
	}

	public void setValidateSchemeId(Integer validateSchemeId) {
		this.validateSchemeId = validateSchemeId;
	}

}