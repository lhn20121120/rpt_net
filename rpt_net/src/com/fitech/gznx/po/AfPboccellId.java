package com.fitech.gznx.po;

/**
 * AfPboccellId entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class AfPboccellId implements java.io.Serializable {

	// Fields

	private String templateId;
	private String versionId;
	private String colId;

	// Constructors

	/** default constructor */
	public AfPboccellId() {
	}

	/** full constructor */
	public AfPboccellId(String templateId, String versionId, String colId) {
		this.templateId = templateId;
		this.versionId = versionId;
		this.colId = colId;
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

	public String getColId() {
		return this.colId;
	}

	public void setColId(String colId) {
		this.colId = colId;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof AfPboccellId))
			return false;
		AfPboccellId castOther = (AfPboccellId) other;

		return ((this.getTemplateId() == castOther.getTemplateId()) || (this
				.getTemplateId() != null
				&& castOther.getTemplateId() != null && this.getTemplateId()
				.equals(castOther.getTemplateId())))
				&& ((this.getVersionId() == castOther.getVersionId()) || (this
						.getVersionId() != null
						&& castOther.getVersionId() != null && this
						.getVersionId().equals(castOther.getVersionId())))
				&& ((this.getColId() == castOther.getColId()) || (this
						.getColId() != null
						&& castOther.getColId() != null && this.getColId()
						.equals(castOther.getColId())));
	}

	public int hashCode() {
		int result = 17;

		result = 37
				* result
				+ (getTemplateId() == null ? 0 : this.getTemplateId()
						.hashCode());
		result = 37 * result
				+ (getVersionId() == null ? 0 : this.getVersionId().hashCode());
		result = 37 * result
				+ (getColId() == null ? 0 : this.getColId().hashCode());
		return result;
	}

}