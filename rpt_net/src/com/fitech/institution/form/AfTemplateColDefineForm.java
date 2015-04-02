package com.fitech.institution.form;

import java.util.List;

import org.apache.struts.action.ActionForm;

import com.cbrc.smis.hibernate.SysParameter;



@SuppressWarnings("serial")
public class AfTemplateColDefineForm extends ActionForm
{
	public String templateId;
	
	public String col;

	public String colName;
	
	public String reportName;
	public String versionId;
	
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

	List <SysParameter>parameters;
	public String getTemplateId() {
		return templateId;
	}

	public List<SysParameter> getParameters() {
		return parameters;
	}

	public void setParameters(List<SysParameter> parameters) {
		this.parameters = parameters;
	}

	public void setTemplateId(String templateId) {
		this.templateId = templateId;
	}

	public String getCol() {
		return col;
	}

	public void setCol(String col) {
		this.col = col;
	}

	public String getColName() {
		return colName;
	}

	public void setColName(String colName) {
		this.colName = colName;
	}

	@Override
	public String toString() {
		return "AfTemplateColDefineForm [templateId=" + templateId + ", col="
				+ col + ", colName=" + colName + ", reportName=" + reportName
				+ ", parameters=" + parameters + "]";
	}

	

	
}


