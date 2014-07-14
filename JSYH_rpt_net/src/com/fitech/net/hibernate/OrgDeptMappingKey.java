package com.fitech.net.hibernate;

/**
 *描述：
 *日期：2008-1-27
 *作者：吴昊
 */
public class OrgDeptMappingKey  implements java.io.Serializable
{

	// Fields

	private String deptId;

	private String orgId;

	// Constructors

	/** default constructor */
	public OrgDeptMappingKey()
	{
	}

	/** full constructor */
	public OrgDeptMappingKey(String deptId, String orgId)
	{
		this.deptId = deptId;
		this.orgId = orgId;
	}

	// Property accessors

	public String getDeptId()
	{
		return this.deptId;
	}

	public void setDeptId(String deptId)
	{
		this.deptId = deptId;
	}

	public String getOrgId()
	{
		return this.orgId;
	}

	public void setOrgId(String orgId)
	{
		this.orgId = orgId;
	}

	public boolean equals(Object other)
	{
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof OrgDeptMappingKey))
			return false;
		OrgDeptMappingKey castOther = (OrgDeptMappingKey) other;

		return ((this.getDeptId() == castOther.getDeptId()) || (this.getDeptId() != null && castOther.getDeptId() != null && this.getDeptId().equals(
				castOther.getDeptId())))
				&& ((this.getOrgId() == castOther.getOrgId()) || (this.getOrgId() != null && castOther.getOrgId() != null && this.getOrgId().equals(
						castOther.getOrgId())));
	}

	public int hashCode()
	{
		int result = 17;

		result = 37 * result + (getDeptId() == null ? 0 : this.getDeptId().hashCode());
		result = 37 * result + (getOrgId() == null ? 0 : this.getOrgId().hashCode());
		return result;
	}

}