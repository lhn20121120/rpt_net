package com.fitech.net.form;

import org.apache.struts.action.ActionForm;

public class ETLReportForm extends ActionForm {
	public String repName;
	public String year;
	public String month;
	public String page;
	private String reportName;

	private Integer currId;
	
	private String excelId;

	private String reportId;

	private String versionId;
	/**
	 * ��������
	 */
	private String orgName;
	/**
	 * ����ID
	 */
	private String deptId;
	/**
	 * ����ID
	 */
	private String orgId;
	/** ��Ԫ��ֵ */
	private String result;
	
	private  String dataRangeId;
	/** X�� */
	private     String colId;
	/** Y�� */
	private    String rowId;
	private String cellName;
	private String cellID;

	
	/**
	 * ӳ�䱨��ID
	 */

	
	public String getPage() {
		return page;
	}
	public void setPage(String page) {
		this.page = page;
	}
	public String getMonth() {
		return month;
	}
	public void setMonth(String month) {
		this.month = month;
	}
	public String getOrgName() {
		return orgName;
	}
	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}
	public String getRepName() {
		return repName;
	}
	public void setRepName(String repName) {
		this.repName = repName;
	}
	public String getYear() {
		return year;
	}
	public void setYear(String year) {
		this.year = year;
	}
	public String getCellID()
	{
		return cellID;
	}
	public void setCellID(String cellID)
	{
		this.cellID = cellID;
	}
	public String getCellName()
	{
		return cellName;
	}
	public void setCellName(String cellName)
	{
		this.cellName = cellName;
	}
	public String getColId()
	{
		return colId;
	}
	public void setColId(String colId)
	{
		this.colId = colId;
	}
	public Integer getCurrId()
	{
		return currId;
	}
	public void setCurrId(Integer currId)
	{
		this.currId = currId;
	}
	public String getDataRangeId()
	{
		return dataRangeId;
	}
	public void setDataRangeId(String dataRangeId)
	{
		this.dataRangeId = dataRangeId;
	}
	public String getDeptId()
	{
		return deptId;
	}
	public void setDeptId(String deptId)
	{
		this.deptId = deptId;
	}
	public String getExcelId()
	{
		return excelId;
	}
	public void setExcelId(String excelId)
	{
		this.excelId = excelId;
	}
	public String getOrgId()
	{
		return orgId;
	}
	public void setOrgId(String orgId)
	{
		this.orgId = orgId;
	}
	public String getReportId()
	{
		return reportId;
	}
	public void setReportId(String reportId)
	{
		this.reportId = reportId;
	}
	public String getReportName()
	{
		return reportName;
	}
	public void setReportName(String reportName)
	{
		this.reportName = reportName;
	}
	public String getResult()
	{
		return result;
	}
	public void setResult(String result)
	{
		this.result = result;
	}
	public String getRowId()
	{
		return rowId;
	}
	public void setRowId(String rowId)
	{
		this.rowId = rowId;
	}
	public String getVersionId()
	{
		return versionId;
	}
	public void setVersionId(String versionId)
	{
		this.versionId = versionId;
	}

}
