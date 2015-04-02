package com.fitech.model.commoninfo.model.pojo;

/**
 * UserRoleId entity. @author MyEclipse Persistence Tools
 */

public class UserRoleId implements java.io.Serializable {

	// Fields

	private Long userRoleId;
	private Long roleId;
	private Long userId;

	// Constructors

	/** default constructor */
	public UserRoleId() {
	}

	/** minimal constructor */
	public UserRoleId(Long userRoleId) {
		this.userRoleId = userRoleId;
	}

	/** full constructor */
	public UserRoleId(Long userRoleId, Long roleId, Long userId) {
		this.userRoleId = userRoleId;
		this.roleId = roleId;
		this.userId = userId;
	}

	// Property accessors

	public Long getUserRoleId() {
		return this.userRoleId;
	}

	public void setUserRoleId(Long userRoleId) {
		this.userRoleId = userRoleId;
	}

	public Long getRoleId() {
		return this.roleId;
	}

	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}

	public Long getUserId() {
		return this.userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof UserRoleId))
			return false;
		UserRoleId castOther = (UserRoleId) other;

		return ((this.getUserRoleId() == castOther.getUserRoleId()) || (this
				.getUserRoleId() != null
				&& castOther.getUserRoleId() != null && this.getUserRoleId()
				.equals(castOther.getUserRoleId())))
				&& ((this.getRoleId() == castOther.getRoleId()) || (this
						.getRoleId() != null
						&& castOther.getRoleId() != null && this.getRoleId()
						.equals(castOther.getRoleId())))
				&& ((this.getUserId() == castOther.getUserId()) || (this
						.getUserId() != null
						&& castOther.getUserId() != null && this.getUserId()
						.equals(castOther.getUserId())));
	}

	public int hashCode() {
		int result = 17;

		result = 37
				* result
				+ (getUserRoleId() == null ? 0 : this.getUserRoleId()
						.hashCode());
		result = 37 * result
				+ (getRoleId() == null ? 0 : this.getRoleId().hashCode());
		result = 37 * result
				+ (getUserId() == null ? 0 : this.getUserId().hashCode());
		return result;
	}

}