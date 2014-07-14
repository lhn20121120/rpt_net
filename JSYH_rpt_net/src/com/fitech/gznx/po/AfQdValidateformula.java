package com.fitech.gznx.po;

import java.math.BigDecimal;

/**
 * AfQdValidateformula entity. @author MyEclipse Persistence Tools
 */

public class AfQdValidateformula implements java.io.Serializable {

	// Fields

	private Integer formulaId;
	private String templateId;
	private String versionId;
	private String colNum;
	private String colName;
	private String formuValue;
	private Integer formuType;
	private String formuName;
	private String formuDes;
	private String validateMessage;

	// Constructors

	/** default constructor */
	public AfQdValidateformula() {
	}

	/** full constructor */
	public AfQdValidateformula(String templateId, String versionId,
			String colNum, String colName, String formuValue,
			Integer formuType, String formuName, String formuDes,
			String validateMessage) {
		this.templateId = templateId;
		this.versionId = versionId;
		this.colNum = colNum;
		this.colName = colName;
		this.formuValue = formuValue;
		this.formuType = formuType;
		this.formuName = formuName;
		this.formuDes = formuDes;
		this.validateMessage = validateMessage;
	}

	// Property accessors

	public Integer getFormulaId() {
		return this.formulaId;
	}

	public void setFormulaId(Integer formulaId) {
		this.formulaId = formulaId;
	}

	public String getTemplateId() {
		return this.templateId;
	}

	public void setTemplateId(String templateId) {
		this.templateId = templateId;
	}

	public String getVersionId() {
		return this.versionId;
	}

	public void setVersionId(String versionId) {
		this.versionId = versionId;
	}

	public String getColNum() {
		return this.colNum;
	}

	public void setColNum(String colNum) {
		this.colNum = colNum;
	}

	public String getColName() {
		return this.colName;
	}

	public void setColName(String colName) {
		this.colName = colName;
	}

	public String getFormuValue() {
		return this.formuValue;
	}

	public void setFormuValue(String formuValue) {
		this.formuValue = formuValue;
	}

	public Integer getFormuType() {
		return this.formuType;
	}

	public void setFormuType(Integer formuType) {
		this.formuType = formuType;
	}

	public String getFormuName() {
		return this.formuName;
	}

	public void setFormuName(String formuName) {
		this.formuName = formuName;
	}

	public String getFormuDes() {
		return this.formuDes;
	}

	public void setFormuDes(String formuDes) {
		this.formuDes = formuDes;
	}

	public String getValidateMessage() {
		return this.validateMessage;
	}

	public void setValidateMessage(String validateMessage) {
		this.validateMessage = validateMessage;
	}

}