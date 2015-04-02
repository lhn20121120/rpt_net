package com.fitech.model.worktask.model.pojo;

import java.math.BigDecimal;

/**
 * ViewWorktaskOrgId entity. @author MyEclipse Persistence Tools
 */

public class ViewWorktaskOrgId implements java.io.Serializable {

	// Fields

	private String orgId;
	private String orgName;
	private String preOrgId;
	private Long regionId;
	private String setOrgId;
	private String orgLevel;
	private String orgType;
	private String beginDate;
	private String orgAttr;
	private String orgOuterId;
	private Integer isCollect;

	// Constructors

	/** default constructor */
	public ViewWorktaskOrgId() {
	}

	/** minimal constructor */
	public ViewWorktaskOrgId(String orgId) {
		this.orgId = orgId;
	}

	/** full constructor */
	public ViewWorktaskOrgId(String orgId, String orgName, String preOrgId,
			Long regionId, String setOrgId, String orgLevel, String orgType,
			String beginDate, String orgAttr, String orgOuterId,
			Integer isCollect) {
		this.orgId = orgId;
		this.orgName = orgName;
		this.preOrgId = preOrgId;
		this.regionId = regionId;
		this.setOrgId = setOrgId;
		this.orgLevel = orgLevel;
		this.orgType = orgType;
		this.beginDate = beginDate;
		this.orgAttr = orgAttr;
		this.orgOuterId = orgOuterId;
		this.isCollect = isCollect;
	}

	// Property accessors

	public String getOrgId() {
		return this.orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

	public String getOrgName() {
		return this.orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

	public String getPreOrgId() {
		return this.preOrgId;
	}

	public void setPreOrgId(String preOrgId) {
		this.preOrgId = preOrgId;
	}

	public Long getRegionId() {
		return this.regionId;
	}

	public void setRegionId(Long regionId) {
		this.regionId = regionId;
	}

	public String getSetOrgId() {
		return this.setOrgId;
	}

	public void setSetOrgId(String setOrgId) {
		this.setOrgId = setOrgId;
	}

	public String getOrgLevel() {
		return this.orgLevel;
	}

	public void setOrgLevel(String orgLevel) {
		this.orgLevel = orgLevel;
	}

	public String getOrgType() {
		return this.orgType;
	}

	public void setOrgType(String orgType) {
		this.orgType = orgType;
	}

	public String getBeginDate() {
		return this.beginDate;
	}

	public void setBeginDate(String beginDate) {
		this.beginDate = beginDate;
	}

	public String getOrgAttr() {
		return this.orgAttr;
	}

	public void setOrgAttr(String orgAttr) {
		this.orgAttr = orgAttr;
	}

	public String getOrgOuterId() {
		return this.orgOuterId;
	}

	public void setOrgOuterId(String orgOuterId) {
		this.orgOuterId = orgOuterId;
	}

	public Integer getIsCollect() {
		return this.isCollect;
	}

	public void setIsCollect(Integer isCollect) {
		this.isCollect = isCollect;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof ViewWorktaskOrgId))
			return false;
		ViewWorktaskOrgId castOther = (ViewWorktaskOrgId) other;

		return ((this.getOrgId() == castOther.getOrgId()) || (this.getOrgId() != null
				&& castOther.getOrgId() != null && this.getOrgId().equals(
				castOther.getOrgId())))
				&& ((this.getOrgName() == castOther.getOrgName()) || (this
						.getOrgName() != null
						&& castOther.getOrgName() != null && this.getOrgName()
						.equals(castOther.getOrgName())))
				&& ((this.getPreOrgId() == castOther.getPreOrgId()) || (this
						.getPreOrgId() != null
						&& castOther.getPreOrgId() != null && this
						.getPreOrgId().equals(castOther.getPreOrgId())))
				&& ((this.getRegionId() == castOther.getRegionId()) || (this
						.getRegionId() != null
						&& castOther.getRegionId() != null && this
						.getRegionId().equals(castOther.getRegionId())))
				&& ((this.getSetOrgId() == castOther.getSetOrgId()) || (this
						.getSetOrgId() != null
						&& castOther.getSetOrgId() != null && this
						.getSetOrgId().equals(castOther.getSetOrgId())))
				&& ((this.getOrgLevel() == castOther.getOrgLevel()) || (this
						.getOrgLevel() != null
						&& castOther.getOrgLevel() != null && this
						.getOrgLevel().equals(castOther.getOrgLevel())))
				&& ((this.getOrgType() == castOther.getOrgType()) || (this
						.getOrgType() != null
						&& castOther.getOrgType() != null && this.getOrgType()
						.equals(castOther.getOrgType())))
				&& ((this.getBeginDate() == castOther.getBeginDate()) || (this
						.getBeginDate() != null
						&& castOther.getBeginDate() != null && this
						.getBeginDate().equals(castOther.getBeginDate())))
				&& ((this.getOrgAttr() == castOther.getOrgAttr()) || (this
						.getOrgAttr() != null
						&& castOther.getOrgAttr() != null && this.getOrgAttr()
						.equals(castOther.getOrgAttr())))
				&& ((this.getOrgOuterId() == castOther.getOrgOuterId()) || (this
						.getOrgOuterId() != null
						&& castOther.getOrgOuterId() != null && this
						.getOrgOuterId().equals(castOther.getOrgOuterId())))
				&& ((this.getIsCollect() == castOther.getIsCollect()) || (this
						.getIsCollect() != null
						&& castOther.getIsCollect() != null && this
						.getIsCollect().equals(castOther.getIsCollect())));
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result
				+ (getOrgId() == null ? 0 : this.getOrgId().hashCode());
		result = 37 * result
				+ (getOrgName() == null ? 0 : this.getOrgName().hashCode());
		result = 37 * result
				+ (getPreOrgId() == null ? 0 : this.getPreOrgId().hashCode());
		result = 37 * result
				+ (getRegionId() == null ? 0 : this.getRegionId().hashCode());
		result = 37 * result
				+ (getSetOrgId() == null ? 0 : this.getSetOrgId().hashCode());
		result = 37 * result
				+ (getOrgLevel() == null ? 0 : this.getOrgLevel().hashCode());
		result = 37 * result
				+ (getOrgType() == null ? 0 : this.getOrgType().hashCode());
		result = 37 * result
				+ (getBeginDate() == null ? 0 : this.getBeginDate().hashCode());
		result = 37 * result
				+ (getOrgAttr() == null ? 0 : this.getOrgAttr().hashCode());
		result = 37
				* result
				+ (getOrgOuterId() == null ? 0 : this.getOrgOuterId()
						.hashCode());
		result = 37 * result
				+ (getIsCollect() == null ? 0 : this.getIsCollect().hashCode());
		return result;
	}

}