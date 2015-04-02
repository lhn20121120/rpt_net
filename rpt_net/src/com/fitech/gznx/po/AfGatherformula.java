package com.fitech.gznx.po;

/**
 * AfGatherformula entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class AfGatherformula implements java.io.Serializable {

	// Fields

	private Long formulaId;
	private String formulaValue;
	private String formulaName;
	private String templateId;
	private String versionId;
	private Long cellId;

	// Constructors

	/** default constructor */
	public AfGatherformula() {
	}

	/** minimal constructor */
	public AfGatherformula(Long formulaId, String templateId, String versionId,
			Long cellId) {
		this.formulaId = formulaId;
		this.templateId = templateId;
		this.versionId = versionId;
		this.cellId = cellId;
	}

	/** full constructor */
	public AfGatherformula(Long formulaId, String formulaValue,
			String formulaName, String templateId, String versionId, Long cellId) {
		this.formulaId = formulaId;
		this.formulaValue = formulaValue;
		this.formulaName = formulaName;
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