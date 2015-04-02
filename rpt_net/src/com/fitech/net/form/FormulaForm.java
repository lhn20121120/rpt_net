package com.fitech.net.form;
public class FormulaForm
{
	private String reportId=null;
	private Integer reportInId=null;
	private String cell=null;
	private int index;
	private String cellData=null;
	private String repInId = null;
	private String versionId = null;
	
	public String getCellData() {
		return cellData;
	}
	public void setCellData(String cellData) {
		this.cellData = cellData;
	}
	public String getCell() {
		return cell;
	}
	public void setCell(String cell) {
		this.cell = cell;
	}
	public String getReportId() {
		return reportId;
	}
	public void setReportId(String reportId) {
		this.reportId = reportId;
	}
	public Integer getReportInId() {
		return reportInId;
	}
	public void setReportInId(Integer reportInId) {
		this.reportInId = reportInId;
	}
	public FormulaForm(String report,String cell,int in)
	{
		this.reportId=report;
		this.cell=cell;
		this.index=in;
	}
	public FormulaForm(String report,String cell,int in,Integer reportInId)
	{
		this.reportId=report;
		this.reportInId=reportInId;
		this.cell=cell;
		this.index=in;
	}
	public int getIndex() {
		return index;
	}
	public void setIndex(int index) {
		this.index = index;
	}
	public String getRepInId() {
		return repInId;
	}
	public void setRepInId(String repInId) {
		this.repInId = repInId;
	}
	public String getVersionId() {
		return versionId;
	}
	public void setVersionId(String versionId) {
		this.versionId = versionId;
	}
}