package com.fitech.gznx.po;

/**
 * AfTemplateValiScheId entity. @author MyEclipse Persistence Tools
 */

public class AfTemplateValiScheId implements java.io.Serializable {

	// Fields

	private String templateId;
	private String versionId;

	// Constructors

	/** default constructor */
	public AfTemplateValiScheId() {
	}

	/** full constructor */
	public AfTemplateValiScheId(String templateId, String versionId) {
		this.templateId = templateId;
		this.versionId = versionId;
	}

	// Property accessors

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
		if (!(other instanceof AfTemplateValiScheId))
			return false;
		AfTemplateValiScheId castOther = (AfTemplateValiScheId) other;

		return ((this.getTemplateId() == castOther.getTemplateId()) || (this
				.getTemplateId() != null
				&& castOther.getTemplateId() != null && this.getTemplateId()
				.equals(castOther.getTemplateId())))
				&& ((this.getVersionId() == castOther.getVersionId()) || (this
						.getVersionId() != null
						&& castOther.getVersionId() != null && this
						.getVersionId().equals(castOther.getVersionId())));
	}

	public int hashCode() {
		int result = 17;

		result = 37
				* result
				+ (getTemplateId() == null ? 0 : this.getTemplateId()
						.hashCode());
		result = 37 * result
				+ (getVersionId() == null ? 0 : this.getVersionId().hashCode());
		return result;
	}

}