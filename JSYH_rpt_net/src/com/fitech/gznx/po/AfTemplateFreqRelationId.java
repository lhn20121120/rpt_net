package com.fitech.gznx.po;

/**
 * AfTemplateFreqRelationId entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class AfTemplateFreqRelationId implements java.io.Serializable {

	// Fields

	private String templateId;
    private Integer repFreqId;
	private String versionId;

	// Constructors

	/** default constructor */
	public AfTemplateFreqRelationId() {
	}

	/** full constructor */
	public AfTemplateFreqRelationId(String templateId, Integer repFreqId,
			String versionId) {
		this.templateId = templateId;
		this.repFreqId = repFreqId;
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
		if (!(other instanceof AfTemplateFreqRelationId))
			return false;
		AfTemplateFreqRelationId castOther = (AfTemplateFreqRelationId) other;

		return ((this.getTemplateId() == castOther.getTemplateId()) || (this
				.getTemplateId() != null
				&& castOther.getTemplateId() != null && this.getTemplateId()
				.equals(castOther.getTemplateId())))
				&& ((this.getRepFreqId() == castOther.getRepFreqId()) || (this
						.getRepFreqId() != null
						&& castOther.getRepFreqId() != null && this.getRepFreqId()
						.equals(castOther.getRepFreqId())))
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
				+ (getRepFreqId() == null ? 0 : this.getRepFreqId().hashCode());
		result = 37 * result
				+ (getVersionId() == null ? 0 : this.getVersionId().hashCode());
		return result;
	}

	public Integer getRepFreqId() {
		return repFreqId;
	}

	public void setRepFreqId(Integer repFreqId) {
		this.repFreqId = repFreqId;
	}

}