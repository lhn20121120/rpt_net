package com.fitech.gznx.po;

/**
 * AfCollectRelationId entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class AfValiRelationId implements java.io.Serializable {

	// Fields

	private String valiId;
	private String orgId;

	// Constructors

	/** default constructor */
	public AfValiRelationId() {
	}

	/** full constructor */
	public AfValiRelationId(String valiId, String orgId) {
		this.valiId = valiId;
		this.orgId = orgId;
	}

	// Property accessors

	public String getvaliId() {
		return this.valiId;
	}

	public void setvaliId(String valiId) {
		this.valiId = valiId;
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
		if (!(other instanceof AfValiRelationId))
			return false;
		AfValiRelationId castOther = (AfValiRelationId) other;

		return ((this.getvaliId() == castOther.getvaliId()) || (this
				.getvaliId() != null
				&& castOther.getvaliId() != null && this.getvaliId()
				.equals(castOther.getvaliId())))
				&& ((this.getOrgId() == castOther.getOrgId()) || (this
						.getOrgId() != null
						&& castOther.getOrgId() != null && this.getOrgId()
						.equals(castOther.getOrgId())));
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result
				+ (getvaliId() == null ? 0 : this.getvaliId().hashCode());
		result = 37 * result
				+ (getOrgId() == null ? 0 : this.getOrgId().hashCode());
		return result;
	}

}