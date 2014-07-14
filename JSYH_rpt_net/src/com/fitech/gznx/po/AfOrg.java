package com.fitech.gznx.po;

/**
 * AfOrg entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class AfOrg implements java.io.Serializable {

	// Fields

	private String orgId;
	private String orgName;
	private String preOrgId;
	private String regionId;
	private String setOrgId;
	private Long orgLevel;
	private String orgType;
	private String beginDate;
	private String orgAttr;
	private String orgOuterId;
	private Long isCollect;

	// Constructors

	/** default constructor */
	public AfOrg() {
	}

	/** minimal constructor */
	public AfOrg(String orgId) {
		this.orgId = orgId;
	}

	/** full constructor */
	public AfOrg(String orgId, String orgName, String preOrgId, String regionId,
			String setOrgId, Long orgLevel, String orgType, String beginDate,
			String orgAttr, String orgOuterId, Long isCollect) {
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

	public String getRegionId() {
		return this.regionId;
	}

	public void setRegionId(String regionId) {
		this.regionId = regionId;
	}

	public String getSetOrgId() {
		return this.setOrgId;
	}

	public void setSetOrgId(String setOrgId) {
		this.setOrgId = setOrgId;
	}

	public Long getOrgLevel() {
		return this.orgLevel;
	}

	public void setOrgLevel(Long orgLevel) {
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

	public Long getIsCollect() {
		return this.isCollect;
	}

	public void setIsCollect(Long isCollect) {
		this.isCollect = isCollect;
	}

}