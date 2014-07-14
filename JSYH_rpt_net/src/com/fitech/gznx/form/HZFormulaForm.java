package com.fitech.gznx.form;

import java.io.File;
import java.util.List;
import java.util.Map;

import org.apache.struts.action.ActionForm;
import org.apache.struts.upload.FormFile;

import com.fitech.gznx.service.XmlTreeUtil;

public class HZFormulaForm extends ActionForm {
	private String orgName;
	private String collSchema;
	private String orgId;
	private String templateId;
	private String versionId;
	private String reportName;
	private String collFormula;
	private String templateName;
	
	
	public String getTemplateName() {
		return templateName;
	}
	public void setTemplateName(String templateName) {
		this.templateName = templateName;
	}
	public String getCollFormula() {
		return collFormula;
	}
	public void setCollFormula(String collFormula) {
		this.collFormula = collFormula;
	}
	public String getOrgId() {
		return orgId;
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
	public String getReportName() {
		return reportName;
	}
	public void setReportName(String reportName) {
		this.reportName = reportName;
	}
	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}
	public String getOrgName() {
		return orgName;
	}
	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}
	public String getCollSchema() {
		return collSchema;
	}
	public void setCollSchema(String collSchema) {
		this.collSchema = collSchema;
	}
	
}
