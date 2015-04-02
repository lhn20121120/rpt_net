package com.fitech.model.worktask.model.pojo;

import java.math.BigDecimal;

/**
 * ViewWorkTaskPurOrgId entity. @author MyEclipse Persistence Tools
 */

public class ViewWorkTaskPurOrgId implements java.io.Serializable {

	// Fields

	private Long userId;
	private String orgId;
	private Long powType;

	// Constructors

	/** default constructor */
	public ViewWorkTaskPurOrgId() {
	}

	/** full constructor */
	public ViewWorkTaskPurOrgId(Long userId, String orgId, Long powType) {
		this.userId = userId;
		this.orgId = orgId;
		this.powType = powType;
	}

	// Property accessors

	public Long getUserId() {
		return this.userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
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
		if (!(other instanceof ViewWorkTaskPurOrgId))
			return false;
		ViewWorkTaskPurOrgId castOther = (ViewWorkTaskPurOrgId) other;

		return ((this.getUserId() == castOther.getUserId()) || (this
				.getUserId() != null
				&& castOther.getUserId() != null && this.getUserId().equals(
				castOther.getUserId())))
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

		result = 37 * result
				+ (getUserId() == null ? 0 : this.getUserId().hashCode());
		result = 37 * result
				+ (getOrgId() == null ? 0 : this.getOrgId().hashCode());
		result = 37 * result
				+ (getPowType() == null ? 0 : this.getPowType().hashCode());
		return result;
	}

}