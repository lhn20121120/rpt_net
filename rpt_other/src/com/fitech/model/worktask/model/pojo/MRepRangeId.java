package com.fitech.model.worktask.model.pojo;

/**
 * MRepRangeId entity. @author MyEclipse Persistence Tools
 */

public class MRepRangeId implements java.io.Serializable {

	// Fields

	private String orgId;
	private String childRepId;
	private String versionId;

	// Constructors

	/** default constructor */
	public MRepRangeId() {
	}

	/** full constructor */
	public MRepRangeId(String orgId, String childRepId, String versionId) {
		this.orgId = orgId;
		this.childRepId = childRepId;
		this.versionId = versionId;
	}

	// Property accessors

	public String getOrgId() {
		return this.orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

	public String getChildRepId() {
		return this.childRepId;
	}

	public void setChildRepId(String childRepId) {
		this.childRepId = childRepId;
	}

	public String getVersionId() {
		return this.versionId;
	}

	public void setVersionId(String versionId) {
		this.versionId = versionId;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof MRepRangeId))
			return false;
		MRepRangeId castOther = (MRepRangeId) other;

		return ((this.getOrgId() == castOther.getOrgId()) || (this.getOrgId() != null
				&& castOther.getOrgId() != null && this.getOrgId().equals(
				castOther.getOrgId())))
				&& ((this.getChildRepId() == castOther.getChildRepId()) || (this
						.getChildRepId() != null
						&& castOther.getChildRepId() != null && this
						.getChildRepId().equals(castOther.getChildRepId())))
				&& ((this.getVersionId() == castOther.getVersionId()) || (this
						.getVersionId() != null
						&& castOther.getVersionId() != null && this
						.getVersionId().equals(castOther.getVersionId())));
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result
				+ (getOrgId() == null ? 0 : this.getOrgId().hashCode());
		result = 37
				* result
				+ (getChildRepId() == null ? 0 : this.getChildRepId()
						.hashCode());
		result = 37 * result
				+ (getVersionId() == null ? 0 : this.getVersionId().hashCode());
		return result;
	}

}