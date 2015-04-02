package com.fitech.net.hibernate;

/**
 *描述：
 *日期：2008-1-27
 *作者：吴昊
 */

public class ReprotExcelMapping implements java.io.Serializable
{

	// Fields

	private ReprotExcelMappingKey key;

	private String reportName;

	private Integer currId;

	// Constructors

	/** default constructor */
	public ReprotExcelMapping()
	{
	}

	/** minimal constructor */
	public ReprotExcelMapping(ReprotExcelMappingKey key)
	{
		this.key = key;
	}

	/** full constructor */
	public ReprotExcelMapping(ReprotExcelMappingKey key, String reportName, Integer currId)
	{
		this.key = key;
		this.reportName = reportName;
		this.currId = currId;
	}

	// Property accessors

	public ReprotExcelMappingKey getKey()
	{
		return this.key;
	}

	public void setKey(ReprotExcelMappingKey key)
	{
		this.key = key;
	}

	public String getReportName()
	{
		return this.reportName;
	}

	public void setReportName(String reportName)
	{
		this.reportName = reportName;
	}

	public Integer getCurrId()
	{
		return this.currId;
	}

	public void setCurrId(Integer currId)
	{
		this.currId = currId;
	}

}