package com.fitech.gznx.po;

/**
 * AfCollectRelationId entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class AfCollectRelationId implements java.io.Serializable {

	// Fields

	private String collectId;
	private String orgId;

	// Constructors

	/** default constructor */
	public AfCollectRelationId() {
	}

	/** full constructor */
	public AfCollectRelationId(String collectId, String orgId) {
		this.collectId = collectId;
		this.orgId = orgId;
	}

	// Property accessors

	public String getCollectId() {
		return this.collectId;
	}

	public void setCollectId(String collectId) {
		this.collectId = collectId;
	}

	public String getOrgId() {
		return this.orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof AfCollectRelationId))
			return false;
		AfCollectRelationId castOther = (AfCollectRelationId) other;

		return ((this.getCollectId() == castOther.getCollectId()) || (this
				.getCollectId() != null
				&& castOther.getCollectId() != null && this.getCollectId()
				.equals(castOther.getCollectId())))
				&& ((this.getOrgId() == castOther.getOrgId()) || (this
						.getOrgId() != null
						&& castOther.getOrgId() != null && this.getOrgId()
						.equals(castOther.getOrgId())));
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result
				+ (getCollectId() == null ? 0 : this.getCollectId().hashCode());
		result = 37 * result
				+ (getOrgId() == null ? 0 : this.getOrgId().hashCode());
		return result;
	}

}