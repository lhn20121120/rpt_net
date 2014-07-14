package com.cbrc.smis.form;

import org.apache.struts.action.ActionForm;

public class MExcelChildReportForm extends ActionForm{
	private String reportName;
	private String childRepId;
	private String TmpFileName;
	private Integer repTypeId=null;
	private String repStyleId;
	private String versionId;
	private String reportCurUnit;
	private String startRow;
	private String startCol;
	private String endRow;
	private String endCol;
	
	
	public MExcelChildReportForm(){}

	public String getChildRepId() {
		return childRepId;
	}

	public void setChildRepId(String childRepId) {
		this.childRepId = childRepId;
	}

	public String getReportCurUnit() {
		return reportCurUnit;
	}

	public void setReportCurUnit(String reportCurUnit) {
		this.reportCurUnit = reportCurUnit;
	}

	

	public String getTmpFileName() {
		return TmpFileName;
	}

	public void setTmpFileName(String tmpFileName) {
		TmpFileName = tmpFileName;
	}

	public String getVersionId() {
		return versionId;
	}

	public void setVersionId(String versionId) {
		this.versionId = versionId;
	}

	public String getEndCol() {
		return endCol;
	}

	public void setEndCol(String endCol) {
		this.endCol = endCol;
	}

	public String getEndRow() {
		return endRow;
	}

	public void setEndRow(String endRow) {
		this.endRow = endRow;
	}

	public String getStartCol() {
		return startCol;
	}

	public void setStartCol(String startCol) {
		this.startCol = startCol;
	}

	public String getStartRow() {
		return startRow;
	}

	public void setStartRow(String startRow) {
		this.startRow = startRow;
	}

	public Integer getRepTypeId() {
		return repTypeId;
	}

	public void setRepTypeId(Integer repTypeId) {
		this.repTypeId = repTypeId;
	}

	public String getRepStyleId() {
		return repStyleId;
	}

	public void setRepStyleId(String repStyleId) {
		this.repStyleId = repStyleId;
	}

	public String getReportName() {
		return reportName;
	}

	public void setReportName(String reportName) {
		this.reportName = reportName;
	}
}
