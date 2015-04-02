package com.fitech.net.form;

public class ReprotExcelMappingForm
{
	private String reportName;

	private Integer currId;
	
	private String excelId;

	private String reportId;

	private String versionId;

	public Integer getCurrId()
	{
		return currId;
	}

	public void setCurrId(Integer currId)
	{
		this.currId = currId;
	}

	public String getExcelId()
	{
		return excelId;
	}

	public void setExcelId(String excelId)
	{
		this.excelId = excelId;
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

	public String getVersionId()
	{
		return versionId;
	}

	public void setVersionId(String versionId)
	{
		this.versionId = versionId;
	}
}
