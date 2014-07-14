package com.fitech.gznx.form;

import org.apache.struts.action.ActionForm;

public class CheckForm extends ActionForm{
	//报表上传操作
	private Long validateTypeId;
	private String validateTypeName;
	private Long formulaId;
	private String formulaValue;
	private String formulaName;
	private String templateId;
	private String versionId;
	private Long cellId;
	private String templateName;
	private String templateType;
	public String getTemplateType() {
		return templateType;
	}
	public void setTemplateType(String templateType) {
		this.templateType = templateType;
	}
	public Long getValidateTypeId() {
		return validateTypeId;
	}
	public void setValidateTypeId(Long validateTypeId) {
		this.validateTypeId = validateTypeId;
	}
	public String getValidateTypeName() {
		return validateTypeName;
	}
	public void setValidateTypeName(String validateTypeName) {
		this.validateTypeName = validateTypeName;
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
	public String getTemplateName() {
		return templateName;
	}
	public void setTemplateName(String templateName) {
		this.templateName = templateName;
	}
}
