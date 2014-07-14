package com.fitech.gznx.po;

import com.cbrc.smis.hibernate.ValidateType;

/**
 * AfDatavalidateinfo entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class AfDatavalidateinfo implements java.io.Serializable {

	// Fields

	private AfDatavalidateinfoId id;
	private Long validateFlg;
	private String validateDesc;
	private String sourceValue;
	private String targetValue;
	
    /** nullable persistent field */
    private ValidateType validateType;

    /** nullable persistent field */
    private AfReport afReport;

    /** nullable persistent field */
    private AfValidateformula afValidateformula;
    
	// Constructors

	/** default constructor */
	public AfDatavalidateinfo() {
	}

	/** minimal constructor */
	public AfDatavalidateinfo(AfDatavalidateinfoId id) {
		this.id = id;
	}

	/** full constructor */
	public AfDatavalidateinfo(AfDatavalidateinfoId id, Long validateFlg,
			String validateDesc) {
		this.id = id;
		this.validateFlg = validateFlg;
		this.validateDesc = validateDesc;
	}

	// Property accessors

	public AfDatavalidateinfoId getId() {
		return this.id;
	}

	public void setId(AfDatavalidateinfoId id) {
		this.id = id;
	}

	public Long getValidateFlg() {
		return this.validateFlg;
	}

	public void setValidateFlg(Long validateFlg) {
		this.validateFlg = validateFlg;
	}

	public String getValidateDesc() {
		return this.validateDesc;
	}

	public void setValidateDesc(String validateDesc) {
		this.validateDesc = validateDesc;
	}

	public ValidateType getValidateType() {
		return validateType;
	}

	public void setValidateType(ValidateType validateType) {
		this.validateType = validateType;
	}

	public AfReport getAfReport() {
		return afReport;
	}

	public void setAfReport(AfReport afReport) {
		this.afReport = afReport;
	}

	public AfValidateformula getAfValidateformula() {
		return afValidateformula;
	}

	public void setAfValidateformula(AfValidateformula afValidateformula) {
		this.afValidateformula = afValidateformula;
	}

	public String getSourceValue() {
		return sourceValue;
	}

	public void setSourceValue(String sourceValue) {
		this.sourceValue = sourceValue;
	}

	public String getTargetValue() {
		return targetValue;
	}

	public void setTargetValue(String targetValue) {
		this.targetValue = targetValue;
	}

}