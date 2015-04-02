package com.fitech.model.worktask.model.pojo;

/**
 * MUserToGrpId entity. @author MyEclipse Persistence Tools
 */

public class MUserToGrpId implements java.io.Serializable {

	// Fields

	private Long userToGrpId;
	private Long userId;
	private Long userGrpId;

	// Constructors

	/** default constructor */
	public MUserToGrpId() {
	}

	/** minimal constructor */
	public MUserToGrpId(Long userToGrpId) {
		this.userToGrpId = userToGrpId;
	}

	/** full constructor */
	public MUserToGrpId(Long userToGrpId, Long userId, Long userGrpId) {
		this.userToGrpId = userToGrpId;
		this.userId = userId;
		this.userGrpId = userGrpId;
	}

	// Property accessors

	public Long getUserToGrpId() {
		return this.userToGrpId;
	}

	public void setUserToGrpId(Long userToGrpId) {
		this.userToGrpId = userToGrpId;
	}

	public Long getUserId() {
		return this.userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getUserGrpId() {
		return this.userGrpId;
	}

	public void setUserGrpId(Long userGrpId) {
		this.userGrpId = userGrpId;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof MUserToGrpId))
			return false;
		MUserToGrpId castOther = (MUserToGrpId) other;

		return ((this.getUserToGrpId() == castOther.getUserToGrpId()) || (this
				.getUserToGrpId() != null
				&& castOther.getUserToGrpId() != null && this.getUserToGrpId()
				.equals(castOther.getUserToGrpId())))
				&& ((this.getUserId() == castOther.getUserId()) || (this
						.getUserId() != null
						&& castOther.getUserId() != null && this.getUserId()
						.equals(castOther.getUserId())))
				&& ((this.getUserGrpId() == castOther.getUserGrpId()) || (this
						.getUserGrpId() != null
						&& castOther.getUserGrpId() != null && this
						.getUserGrpId().equals(castOther.getUserGrpId())));
	}

	public int hashCode() {
		int result = 17;

		result = 37
				* result
				+ (getUserToGrpId() == null ? 0 : this.getUserToGrpId()
						.hashCode());
		result = 37 * result
				+ (getUserId() == null ? 0 : this.getUserId().hashCode());
		result = 37 * result
				+ (getUserGrpId() == null ? 0 : this.getUserGrpId().hashCode());
		return result;
	}

}