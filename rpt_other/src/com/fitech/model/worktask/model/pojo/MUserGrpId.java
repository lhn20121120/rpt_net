package com.fitech.model.worktask.model.pojo;

/**
 * MUserGrpId entity. @author MyEclipse Persistence Tools
 */

public class MUserGrpId implements java.io.Serializable {

	// Fields

	private Long userGrpId;
	private String userGrpNm;
	private String setOrgId;

	// Constructors

	/** default constructor */
	public MUserGrpId() {
	}

	/** minimal constructor */
	public MUserGrpId(Long userGrpId) {
		this.userGrpId = userGrpId;
	}

	/** full constructor */
	public MUserGrpId(Long userGrpId, String userGrpNm, String setOrgId) {
		this.userGrpId = userGrpId;
		this.userGrpNm = userGrpNm;
		this.setOrgId = setOrgId;
	}

	// Property accessors

	public Long getUserGrpId() {
		return this.userGrpId;
	}

	public void setUserGrpId(Long userGrpId) {
		this.userGrpId = userGrpId;
	}

	public String getUserGrpNm() {
		return this.userGrpNm;
	}

	public void setUserGrpNm(String userGrpNm) {
		this.userGrpNm = userGrpNm;
	}

	public String getSetOrgId() {
		return this.setOrgId;
	}

	public void setSetOrgId(String setOrgId) {
		this.setOrgId = setOrgId;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof MUserGrpId))
			return false;
		MUserGrpId castOther = (MUserGrpId) other;

		return ((this.getUserGrpId() == castOther.getUserGrpId()) || (this
				.getUserGrpId() != null
				&& castOther.getUserGrpId() != null && this.getUserGrpId()
				.equals(castOther.getUserGrpId())))
				&& ((this.getUserGrpNm() == castOther.getUserGrpNm()) || (this
						.getUserGrpNm() != null
						&& castOther.getUserGrpNm() != null && this
						.getUserGrpNm().equals(castOther.getUserGrpNm())))
				&& ((this.getSetOrgId() == castOther.getSetOrgId()) || (this
						.getSetOrgId() != null
						&& castOther.getSetOrgId() != null && this
						.getSetOrgId().equals(castOther.getSetOrgId())));
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result
				+ (getUserGrpId() == null ? 0 : this.getUserGrpId().hashCode());
		result = 37 * result
				+ (getUserGrpNm() == null ? 0 : this.getUserGrpNm().hashCode());
		result = 37 * result
				+ (getSetOrgId() == null ? 0 : this.getSetOrgId().hashCode());
		return result;
	}

}