package com.fitech.gznx.po;

/**
 * AfTemplateShapeId entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class AfTemplateShapeId implements java.io.Serializable {

	// Fields

	private String templateId;
	private String versionId;
	private String cellName;

	// Constructors

	/** default constructor */
	public AfTemplateShapeId() {
	}

	/** full constructor */
	public AfTemplateShapeId(String templateId, String versionId, String cellName) {
		this.templateId = templateId;
		this.versionId = versionId;
		this.cellName = cellName;
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

	public String getCellName() {
		return this.cellName;
	}

	public void setCellName(String cellName) {
		this.cellName = cellName;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof AfTemplateShapeId))
			return false;
		AfTemplateShapeId castOther = (AfTemplateShapeId) other;

		return ((this.getTemplateId() == castOther.getTemplateId()) || (this.getTemplateId() != null && castOther.getTemplateId() != null && this
				.getTemplateId().equals(castOther.getTemplateId())))
				&& ((this.getVersionId() == castOther.getVersionId()) || (this.getVersionId() != null && castOther.getVersionId() != null && this
						.getVersionId().equals(castOther.getVersionId())))
				&& ((this.getCellName() == castOther.getCellName()) || (this.getCellName() != null && castOther.getCellName() != null && this
						.getCellName().equals(castOther.getCellName())));
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result + (getTemplateId() == null ? 0 : this.getTemplateId().hashCode());
		result = 37 * result + (getVersionId() == null ? 0 : this.getVersionId().hashCode());
		result = 37 * result + (getCellName() == null ? 0 : this.getCellName().hashCode());
		return result;
	}

}