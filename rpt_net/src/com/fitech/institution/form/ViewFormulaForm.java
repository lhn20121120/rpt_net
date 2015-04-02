package com.fitech.institution.form;

import org.apache.struts.action.ActionForm;

public class ViewFormulaForm extends ActionForm {
	private Long formulaId;
	private String formulaValue;
	private String formulaName;
	private Long validateTypeId;
	private String templateId;
	private String versionId;
	private Long cellId;
	private String reportName;
	public String getReportName() {
		return reportName;
	}
	public void setReportName(String reportName) {
		this.reportName = reportName;
	}
	public ViewFormulaForm() {
		super();
	}
	
	@Override
	public String toString() {
		return "ViewFormulaForm [formulaId=" + formulaId + ", formulaValue="
				+ formulaValue + ", formulaName=" + formulaName
				+ ", validateTypeId=" + validateTypeId + ", templateId="
				+ templateId + ", versionId=" + versionId + ", cellId="
				+ cellId + ", reportName=" + reportName + "]";
	}
	public Long getFormulaId() {
		return formulaId;
	}
	public void setFormulaId(Long formulaId) {
		this.formulaId = formulaId;
	}
	public String getFormulaValue() {
		return formulaValue;
	}
	public void setFormulaValue(String formulaValue) {
		this.formulaValue = formulaValue;
	}
	public String getFormulaName() {
		return formulaName;
	}
	public void setFormulaName(String formulaName) {
		this.formulaName = formulaName;
	}
	public Long getValidateTypeId() {
		return validateTypeId;
	}
	public void setValidateTypeId(Long validateTypeId) {
		this.validateTypeId = validateTypeId;
	}
	public String getTemplateId() {
		return templateId;
	}
	public void setTemplateId(String templateId) {
		this.templateId = templateId;
	}
	public String getVersionId() {
		return versionId;
	}
	public void setVersionId(String versionId) {
		this.versionId = versionId;
	}
	public Long getCellId() {
		return cellId;
	}
	public void setCellId(Long cellId) {
		this.cellId = cellId;
	}
}
