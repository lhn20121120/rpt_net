package com.fitech.gznx.po;

/**
 * AfTemplatetypeId entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class AfTemplatetypeId implements java.io.Serializable {

	// Fields

	private Long typeId;
	private Long templateType;

	// Constructors

	/** default constructor */
	public AfTemplatetypeId() {
	}

	/** full constructor */
	public AfTemplatetypeId(Long typeId, Long templateType) {
		this.typeId = typeId;
		this.templateType = templateType;
	}

	// Property accessors

	public Long getTypeId() {
		return this.typeId;
	}

	public void setTypeId(Long typeId) {
		this.typeId = typeId;
	}

	public Long getTemplateType() {
		return this.templateType;
	}

	public void setTemplateType(Long templateType) {
		this.templateType = templateType;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof AfTemplatetypeId))
			return false;
		AfTemplatetypeId castOther = (AfTemplatetypeId) other;

		return ((this.getTypeId() == castOther.getTypeId()) || (this
				.getTypeId() != null
				&& castOther.getTypeId() != null && this.getTypeId().equals(
				castOther.getTypeId())))
				&& ((this.getTemplateType() == castOther.getTemplateType()) || (this
						.getTemplateType() != null
						&& castOther.getTemplateType() != null && this
						.getTemplateType().equals(castOther.getTemplateType())));
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result
				+ (getTypeId() == null ? 0 : this.getTypeId().hashCode());
		result = 37
				* result
				+ (getTemplateType() == null ? 0 : this.getTemplateType()
						.hashCode());
		return result;
	}

}