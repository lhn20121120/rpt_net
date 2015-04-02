package com.fitech.gznx.po;

/**
 * AfTemplateCurrRelationId entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class AfTemplateCurrRelationId implements java.io.Serializable {

	// Fields

	private String templateId;
	private Long curId;
	private String versionId;

	// Constructors

	/** default constructor */
	public AfTemplateCurrRelationId() {
	}

	/** full constructor */
	public AfTemplateCurrRelationId(String templateId, Long curId,
			String versionId) {
		this.templateId = templateId;
		this.curId = curId;
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
		if (!(other instanceof AfTemplateCurrRelationId))
			return false;
		AfTemplateCurrRelationId castOther = (AfTemplateCurrRelationId) other;

		return ((this.getTemplateId() == castOther.getTemplateId()) || (this
				.getTemplateId() != null
				&& castOther.getTemplateId() != null && this.getTemplateId()
				.equals(castOther.getTemplateId())))
				&& ((this.getCurId() == castOther.getCurId()) || (this
						.getCurId() != null
						&& castOther.getCurId() != null && this.getCurId()
						.equals(castOther.getCurId())))
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
				+ (getCurId() == null ? 0 : this.getCurId().hashCode());
		result = 37 * result
				+ (getVersionId() == null ? 0 : this.getVersionId().hashCode());
		return result;
	}

	public Long getCurId() {
		return curId;
	}

	public void setCurId(Long curId) {
		this.curId = curId;
	}

}