package com.fitech.net.form;

import org.apache.struts.action.ActionForm;

public class FromCompareForm  extends ActionForm {

	
	private String reportStr = null;
	private String cellName = null;
	private String reportName=null;
	private Integer year=null;
	private Integer term=null;
	private Integer cellId=null;
	private String reportValue=null;
	private String preValue=null;
	private String preTowValue=null;
	private String preYearValue=null;
	private String versionId=null;
	public String getPreTowValue()
	{
		return preTowValue;
	}
	public void setPreTowValue(String preTowValue)
	{
		this.preTowValue = preTowValue;
	}
	public String getPreValue()
	{
		return preValue;
	}
	public void setPreValue(String preValue)
	{
		this.preValue = preValue;
	}
	public String getPreYearValue()
	{
		return preYearValue;
	}
	public void setPreYearValue(String preYearValue)
	{
		this.preYearValue = preYearValue;
	}
	public String getReportName()
	{
		return reportName;
	}
	public void setReportName(String reportName)
	{
		this.reportName = reportName;
	}
	public String getReportValue()
	{
		return reportValue;
	}
	public void setReportValue(String reportValue)
	{
		this.reportValue = reportValue;
	}
	public Integer getTerm()
	{
		return term;
	}
	public void setTerm(Integer term)
	{
		this.term = term;
	}
	public Integer getYear()
	{
		return year;
	}
	public void setYear(Integer year)
	{
		this.year = year;
	}
	public Integer getCellId()
	{
		return cellId;
	}
	public void setCellId(Integer cellId)
	{
		this.cellId = cellId;
	}
	public String getCellName()
	{
		return cellName;
	}
	public void setCellName(String cellName)
	{
		this.cellName = cellName;
	}
	public String getReportStr()
	{
		return reportStr;
	}
	public void setReportStr(String reportStr)
	{
		this.reportStr = reportStr;
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
