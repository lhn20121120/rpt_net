package com.fitech.gznx.po;

/**
 * vOrgRelId entity. @author MyEclipse Persistence Tools
 */

public class vOrgRelId implements java.io.Serializable {

	// Fields

	private String orgId;
	private String sysFlag;

	// Constructors

	/** default constructor */
	public vOrgRelId() {
	}

	/** full constructor */
	public vOrgRelId(String orgId, String sysFlag) {
		this.orgId = orgId;
		this.sysFlag = sysFlag;
	}

	// Property accessors

	public String getOrgId() {
		return this.orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

	public String getSysFlag() {
		return this.sysFlag;
	}

	public void setSysFlag(String sysFlag) {
		this.sysFlag = sysFlag;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof vOrgRelId))
			return false;
		vOrgRelId castOther = (vOrgRelId) other;

		return ((this.getOrgId() == castOther.getOrgId()) || (this.getOrgId() != null
				&& castOther.getOrgId() != null && this.getOrgId().equals(
				castOther.getOrgId())))
				&& ((this.getSysFlag() == castOther.getSysFlag()) || (this
						.getSysFlag() != null
						&& castOther.getSysFlag() != null && this.getSysFlag()
						.equals(castOther.getSysFlag())));
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result
				+ (getOrgId() == null ? 0 : this.getOrgId().hashCode());
		result = 37 * result
				+ (getSysFlag() == null ? 0 : this.getSysFlag().hashCode());
		return result;
	}

}