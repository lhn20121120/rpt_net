package com.fitech.gznx.form;

import org.apache.struts.action.ActionForm;

public class QDValidateForm extends ActionForm {
	private String templateId = null;
	private String versionId = null;
	private String reportName = null;
	private Integer reportStyle = null;
	private String formulaId = null;
	private String colNum = null;
	private String colName = null;
	private String formuValue = null;
	private String formuType = null;
	private String formuName = null;
	private String formuDes = null;
	private String validateMessage = null;
	private String vFlg = null;
	
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
	public String getColNum() {
		return colNum;
	}
	public void setColNum(String colNum) {
		this.colNum = colNum;
	}
	public String getColName() {
		return colName;
	}
	public void setColName(String colName) {
		this.colName = colName;
	}
	public String getFormuValue() {
		return formuValue;
	}
	public void setFormuValue(String formuValue) {
		this.formuValue = formuValue;
	}
	public String getFormuType() {
		return formuType;
	}
	public void setFormuType(String formuType) {
		this.formuType = formuType;
	}
	public String getFormuName() {
		return formuName;
	}
	public void setFormuName(String formuName) {
		this.formuName = formuName;
	}
	public String getFormuDes() {
		return formuDes;
	}
	public void setFormuDes(String formuDes) {
		this.formuDes = formuDes;
	}
	public String getValidateMessage() {
		return validateMessage;
	}
	public void setValidateMessage(String validateMessage) {
		this.validateMessage = validateMessage;
	}
	public String getFormulaId() {
		return formulaId;
	}
	public void setFormulaId(String formulaId) {
		this.formulaId = formulaId;
	}
	public String getReportName() {
		return reportName;
	}
	public void setReportName(String reportName) {
		this.reportName = reportName;
	}
	public Integer getReportStyle() {
		return reportStyle;
	}
	public void setReportStyle(Integer reportStyle) {
		this.reportStyle = reportStyle;
	}
	public String getVFlg() {
		return vFlg;
	}
	public void setVFlg(String flg) {
		vFlg = flg;
	}

}
