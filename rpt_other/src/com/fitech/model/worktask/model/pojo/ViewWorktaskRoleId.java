package com.fitech.model.worktask.model.pojo;

/**
 * ViewWorktaskRoleId entity. @author MyEclipse Persistence Tools
 */

public class ViewWorktaskRoleId implements java.io.Serializable {

	// Fields

	private Long roleId;
	private String roleName;
	private String setOrgId;

	// Constructors

	/** default constructor */
	public ViewWorktaskRoleId() {
	}

	/** minimal constructor */
	public ViewWorktaskRoleId(Long roleId) {
		this.roleId = roleId;
	}

	/** full constructor */
	public ViewWorktaskRoleId(Long roleId, String roleName, String setOrgId) {
		this.roleId = roleId;
		this.roleName = roleName;
		this.setOrgId = setOrgId;
	}

	// Property accessors

	public Long getRoleId() {
		return this.roleId;
	}

	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}

	public String getRoleName() {
		return this.roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
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
		if (!(other instanceof ViewWorktaskRoleId))
			return false;
		ViewWorktaskRoleId castOther = (ViewWorktaskRoleId) other;

		return ((this.getRoleId() == castOther.getRoleId()) || (this
				.getRoleId() != null
				&& castOther.getRoleId() != null && this.getRoleId().equals(
				castOther.getRoleId())))
				&& ((this.getRoleName() == castOther.getRoleName()) || (this
						.getRoleName() != null
						&& castOther.getRoleName() != null && this
						.getRoleName().equals(castOther.getRoleName())))
				&& ((this.getSetOrgId() == castOther.getSetOrgId()) || (this
						.getSetOrgId() != null
						&& castOther.getSetOrgId() != null && this
						.getSetOrgId().equals(castOther.getSetOrgId())));
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result
				+ (getRoleId() == null ? 0 : this.getRoleId().hashCode());
		result = 37 * result
				+ (getRoleName() == null ? 0 : this.getRoleName().hashCode());
		result = 37 * result
				+ (getSetOrgId() == null ? 0 : this.getSetOrgId().hashCode());
		return result;
	}

}