package com.fitech.net.hibernate;

/**
 *描述：
 *日期：2008-1-27
 *作者：吴昊
 */
public class OrgDeptMapping   implements java.io.Serializable
{

	// Fields

	private OrgDeptMappingKey key;

	private String orgName;

	// Constructors

	/** default constructor */
	public OrgDeptMapping()
	{
	}

	/** minimal constructor */
	public OrgDeptMapping(OrgDeptMappingKey key)
	{
		this.key = key;
	}

	/** full constructor */
	public OrgDeptMapping(OrgDeptMappingKey key, String orgName)
	{
		this.key = key;
		this.orgName = orgName;
	}

	// Property accessors

	public OrgDeptMappingKey getKey()
	{
		return this.key;
	}

	public void setKey(OrgDeptMappingKey key)
	{
		this.key = key;
	}

	public String getOrgName()
	{
		return this.orgName;
	}

	public void setOrgName(String orgName)
	{
		this.orgName = orgName;
	}

}