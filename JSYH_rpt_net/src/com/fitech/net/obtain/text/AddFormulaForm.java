package com.fitech.net.obtain.text;

import org.apache.struts.action.ActionForm;

public class AddFormulaForm extends ActionForm {
	/** 主键*/
	private String id=null;
	/**子报表id*/
	private String childReportId=null;
	/** 版本号*/
	private String versionId=null;
	/**模板名字*/
	private String reportname=null;
	/** text*/
	private String dataSourceEname=null;
	/****/
	private String dataSourceCname=null;
	/** 行列号**/
	private String rowColumn=null;
	/** 分隔符**/
	private String 	splitChar=null;
	/** 公式**/
	private String formula=null;
	/** 状态**/
	private String flag=null;
	/** 描述*/
	private String des=null;
	/**结构列**/
	private String orgId=null;
	
	private String formulaValue=null;
	private String column=null;
	
	public AddFormulaForm(){}
	public String getChildReportId() {
		return childReportId;
	}
	public void setChildReportId(String childReportId) {
		this.childReportId = childReportId;
	}
	public String getColumn() {
		return column;
	}
	public void setColumn(String column) {
		this.column = column;
	}
	public String getDataSourceCname() {
		return dataSourceCname;
	}
	public void setDataSourceCname(String dataSourceCname) {
		this.dataSourceCname = dataSourceCname;
	}
	public String getDataSourceEname() {
		return dataSourceEname;
	}
	public void setDataSourceEname(String dataSourceEname) {
		this.dataSourceEname = dataSourceEname;
	}
	public String getDes() {
		return des;
	}
	public void setDes(String des) {
		this.des = des;
	}
	public String getFlag() {
		return flag;
	}
	public void setFlag(String flag) {
		this.flag = flag;
	}
	public String getFormula() {
		return formula;
	}
	public void setFormula(String formula) {
		this.formula = formula;
	}
	public String getFormulaValue() {
		return formulaValue;
	}
	public void setFormulaValue(String formulaValue) {
		this.formulaValue = formulaValue;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getReportname() {
		return reportname;
	}
	public void setReportname(String reportname) {
		this.reportname = reportname;
	}
	public String getRowColumn() {
		return rowColumn;
	}
	public void setRowColumn(String rowColumn) {
		this.rowColumn = rowColumn;
	}
	public String getSplitChar() {
		return splitChar;
	}
	public void setSplitChar(String splitChar) {
		this.splitChar = splitChar;
	}
	public String getVersionId() {
		return versionId;
	}
	public void setVersionId(String versionId) {
		this.versionId = versionId;
	}
	public String getOrgId() {
		return orgId;
	}
	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}
	
	

}
