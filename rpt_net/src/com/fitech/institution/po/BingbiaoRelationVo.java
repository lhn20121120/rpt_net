package com.fitech.institution.po;

public class BingbiaoRelationVo {
	private String templateId;
	private String reportName;
	private String colName;
	private String colNameStandard;
	private String currId;
	public String getTemplateId() {
		return templateId;
	}
	public void setTemplateId(String templateId) {
		this.templateId = templateId;
	}
	public String getReportName() {
		return reportName;
	}
	public void setReportName(String reportName) {
		this.reportName = reportName;
	}
	public String getColName() {
		return colName;
	}
	public void setColName(String colName) {
		this.colName = colName;
	}
	public String getColNameStandard() {
		return colNameStandard;
	}
	public void setColNameStandard(String colNameStandard) {
		this.colNameStandard = colNameStandard;
	}
	public String getCurrId() {
		return currId;
	}
	public void setCurrId(String currId) {
		this.currId = currId;
	}
	@Override
	public String toString() {
		return "BingbiaoRelattionVo [templateId=" + templateId
				+ ", reportName=" + reportName + ", colName=" + colName
				+ ", colNameStandard=" + colNameStandard + ", currId=" + currId
				+ "]";
	}
	
	
}
