package com.fitech.papp.webservice.pojo;

public class WebSysOrg implements java.io.Serializable {
	
	private String orgId;
	private String viewOrgId;
	private String orgName;
	private String orgShortName;
	private String higherOrgId;
	private String orgAreaId;
	private String orgType;
	private String orgLevel;
	private String isEnable;
	private String startDate;
	private String endDate;
	private String updateDate;
	private String viewType;
	private String isAllo;
	private String licenseNum;
	private String director;
	private String postcode;
	private String endFlag;
	private String directorRank;
	private String operatingStatus;
	private String networkNum;
	private String directorPhone;
	private String address;
	
	// Property accessors

	public String getOrgId() {
		return this.orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

	public String getViewOrgId() {
		return this.viewOrgId;
	}

	public void setViewOrgId(String viewOrgId) {
		this.viewOrgId = viewOrgId;
	}

	public String getOrgName() {
		return this.orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

	public String getOrgShortName() {
		return this.orgShortName;
	}

	public void setOrgShortName(String orgShortName) {
		this.orgShortName = orgShortName;
	}

	public String getHigherOrgId() {
		return this.higherOrgId;
	}

	public void setHigherOrgId(String higherOrgId) {
		this.higherOrgId = higherOrgId;
	}

	public String getOrgAreaId() {
		return this.orgAreaId;
	}

	public void setOrgAreaId(String orgAreaId) {
		this.orgAreaId = orgAreaId;
	}

	public String getOrgType() {
		return this.orgType;
	}

	public void setOrgType(String orgType) {
		this.orgType = orgType;
	}

	public String getOrgLevel() {
		return this.orgLevel;
	}

	public void setOrgLevel(String orgLevel) {
		this.orgLevel = orgLevel;
	}

	public String getIsEnable() {
		return this.isEnable;
	}

	public void setIsEnable(String isEnable) {
		this.isEnable = isEnable;
	}

	public String getStartDate() {
		return this.startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return this.endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	

	public String getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(String updateDate) {
		this.updateDate = updateDate;
	}

	public String getViewType() {
		return this.viewType;
	}

	public void setViewType(String viewType) {
		this.viewType = viewType;
	}

	public String getIsAllo() {
		return this.isAllo;
	}

	public void setIsAllo(String isAllo) {
		this.isAllo = isAllo;
	}

	public String getLicenseNum() {
		return this.licenseNum;
	}

	public void setLicenseNum(String licenseNum) {
		this.licenseNum = licenseNum;
	}

	public String getDirector() {
		return this.director;
	}

	public void setDirector(String director) {
		this.director = director;
	}

	public String getPostcode() {
		return this.postcode;
	}

	public void setPostcode(String postcode) {
		this.postcode = postcode;
	}

	public String getEndFlag() {
		return this.endFlag;
	}

	public void setEndFlag(String endFlag) {
		this.endFlag = endFlag;
	}

	public String getDirectorRank() {
		return this.directorRank;
	}

	public void setDirectorRank(String directorRank) {
		this.directorRank = directorRank;
	}

	public String getOperatingStatus() {
		return this.operatingStatus;
	}

	public void setOperatingStatus(String operatingStatus) {
		this.operatingStatus = operatingStatus;
	}

	public String getNetworkNum() {
		return this.networkNum;
	}

	public void setNetworkNum(String networkNum) {
		this.networkNum = networkNum;
	}

	@Override
	public String toString() {
		return "WebSysOrg [orgId=" + orgId + ", viewOrgId=" + viewOrgId
				+ ", orgName=" + orgName + ", orgShortName=" + orgShortName
				+ ", higherOrgId=" + higherOrgId + ", orgAreaId=" + orgAreaId
				+ ", orgType=" + orgType + ", orgLevel=" + orgLevel
				+ ", isEnable=" + isEnable + ", startDate=" + startDate
				+ ", endDate=" + endDate + ", updateDate=" + updateDate
				+ ", viewType=" + viewType + ", isAllo=" + isAllo
				+ ", licenseNum=" + licenseNum + ", director=" + director
				+ ", postcode=" + postcode + ", endFlag=" + endFlag
				+ ", directorRank=" + directorRank + ", operatingStatus="
				+ operatingStatus + ", networkNum=" + networkNum
				+ ", directorPhone=" + directorPhone + ", address=" + address
				+ "]";
	}

	public String getDirectorPhone() {
		return this.directorPhone;
	}

	public void setDirectorPhone(String directorPhone) {
		this.directorPhone = directorPhone;
	}

	public String getAddress() {
		return this.address;
	}

	public void setAddress(String address) {
		this.address = address;
	}
}
