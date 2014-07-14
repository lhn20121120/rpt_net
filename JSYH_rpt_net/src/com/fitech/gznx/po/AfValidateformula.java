package com.fitech.gznx.po;

/**
 * AfValidateformula entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class AfValidateformula implements java.io.Serializable {

	// Fields

	private Long formulaId;
	private String formulaValue;
	private String formulaName;
	private Long validateTypeId;
	private String templateId;
	private String versionId;
	private Long cellId;

	private String source;
	private String target;
	
	// Constructors

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getTarget() {
		return target;
	}

	public void setTarget(String target) {
		this.target = target;
	}

	/** default constructor */
	public AfValidateformula() {
	}

	/** minimal constructor */
	public AfValidateformula(Long formulaId, Long validateTypeId,
			String templateId, String versionId, Long cellId) {
		this.formulaId = formulaId;
		this.validateTypeId = validateTypeId;
		this.templateId = templateId;
		this.versionId = versionId;
		this.cellId = cellId;
	}

	/** full constructor */
	public AfValidateformula(Long formulaId, String formulaValue,
			String formulaName, Long validateTypeId, String templateId,
			String versionId, Long cellId) {
		this.formulaId = formulaId;
		this.formulaValue = formulaValue;
		this.formulaName = formulaName;
		this.validateTypeId = validateTypeId;
		this.templateId = templateId;
		this.versionId = versionId;
		this.cellId = cellId;
	}

	// Property accessors

	public Long getFormulaId() {
		return this.formulaId;
	}

	public void setFormulaId(Long formulaId) {
		this.formulaId = formulaId;
	}

	public String getFormulaValue() {
		return this.formulaValue;
	}

	public void setFormulaValue(String formulaValue) {
		this.formulaValue = formulaValue;
	}

	public String getFormulaName() {
		return this.formulaName;
	}

	public void setFormulaName(String formulaName) {
		this.formulaName = formulaName;
	}

	public Long getValidateTypeId() {
		return this.validateTypeId;
	}

	public void setValidateTypeId(Long validateTypeId) {
		this.validateTypeId = validateTypeId;
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

	public Long getCellId() {
		return this.cellId;
	}

	public void setCellId(Long cellId) {
		this.cellId = cellId;
	}

}