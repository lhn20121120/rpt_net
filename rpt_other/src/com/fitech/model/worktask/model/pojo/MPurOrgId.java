package com.fitech.model.worktask.model.pojo;

/**
 * MPurOrgId entity. @author MyEclipse Persistence Tools
 */

public class MPurOrgId implements java.io.Serializable {

	// Fields

	private String childRepId;
	private Long userGrpId;
	private String orgId;
	private Long powType;

	// Constructors

	/** default constructor */
	public MPurOrgId() {
	}

	/** full constructor */
	public MPurOrgId(String childRepId, Long userGrpId, String orgId,
			Long powType) {
		this.childRepId = childRepId;
		this.userGrpId = userGrpId;
		this.orgId = orgId;
		this.powType = powType;
	}

	// Property accessors

	public String getChildRepId() {
		return this.childRepId;
	}

	public void setChildRepId(String childRepId) {
		this.childRepId = childRepId;
	}

	public Long getUserGrpId() {
		return this.userGrpId;
	}

	public void setUserGrpId(Long userGrpId) {
		this.userGrpId = userGrpId;
	}

	public String getOrgId() {
		return this.orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

	public Long getPowType() {
		return this.powType;
	}

	public void setPowType(Long powType) {
		this.powType = powType;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof MPurOrgId))
			return false;
		MPurOrgId castOther = (MPurOrgId) other;

		return ((this.getChildRepId() == castOther.getChildRepId()) || (this
				.getChildRepId() != null
				&& castOther.getChildRepId() != null && this.getChildRepId()
				.equals(castOther.getChildRepId())))
				&& ((this.getUserGrpId() == castOther.getUserGrpId()) || (this
						.getUserGrpId() != null
						&& castOther.getUserGrpId() != null && this
						.getUserGrpId().equals(castOther.getUserGrpId())))
				&& ((this.getOrgId() == castOther.getOrgId()) || (this
						.getOrgId() != null
						&& castOther.getOrgId() != null && this.getOrgId()
						.equals(castOther.getOrgId())))
				&& ((this.getPowType() == castOther.getPowType()) || (this
						.getPowType() != null
						&& castOther.getPowType() != null && this.getPowType()
						.equals(castOther.getPowType())));
	}

	public int hashCode() {
		int result = 17;

		result = 37
				* result
				+ (getChildRepId() == null ? 0 : this.getChildRepId()
						.hashCode());
		result = 37 * result
				+ (getUserGrpId() == null ? 0 : this.getUserGrpId().hashCode());
		result = 37 * result
				+ (getOrgId() == null ? 0 : this.getOrgId().hashCode());
		result = 37 * result
				+ (getPowType() == null ? 0 : this.getPowType().hashCode());
		return result;
	}

}