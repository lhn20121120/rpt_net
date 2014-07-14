package com.fitech.gznx.po;

/**
 * AfTemplateCollRepId entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class AfTemplateCollRepId implements java.io.Serializable {

	// Fields

	private String orgId;
	private String templateId;
	private String versionId;

	// Constructors

	/** default constructor */
	public AfTemplateCollRepId() {
	}

	/** full constructor */
	public AfTemplateCollRepId(String orgId, String templateId, String versionId) {
		this.orgId = orgId;
		this.templateId = templateId;
		this.versionId = versionId;
	}

	// Property accessors

	public String getOrgId() {
		return this.orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

	public String getTemplateId() {
		return this.templateId;
	}

	public void setTemplateId(String templateId) {
		this.templateId = templateId;
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
		if (!(other instanceof AfTemplateCollRepId))
			return false;
		AfTemplateCollRepId castOther = (AfTemplateCollRepId) other;

		return ((this.getOrgId() == castOther.getOrgId()) || (this.getOrgId() != null && castOther.getOrgId() != null && this.getOrgId().equals(
				castOther.getOrgId())))
				&& ((this.getTemplateId() == castOther.getTemplateId()) || (this.getTemplateId() != null && castOther.getTemplateId() != null && this
						.getTemplateId().equals(castOther.getTemplateId())))
				&& ((this.getVersionId() == castOther.getVersionId()) || (this.getVersionId() != null && castOther.getVersionId() != null && this
						.getVersionId().equals(castOther.getVersionId())));
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result + (getOrgId() == null ? 0 : this.getOrgId().hashCode());
		result = 37 * result + (getTemplateId() == null ? 0 : this.getTemplateId().hashCode());
		result = 37 * result + (getVersionId() == null ? 0 : this.getVersionId().hashCode());
		return result;
	}

}