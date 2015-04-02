package com.fitech.model.worktask.model.pojo;

/**
 * MUserGrp entity. @author MyEclipse Persistence Tools
 */

public class MUserGrp implements java.io.Serializable {

	// Fields

	private Long userGrpId;
	private String userGrpNm;
	private String setOrgId;

	// Constructors

	/** default constructor */
	public MUserGrp() {
	}

	/** full constructor */
	public MUserGrp(String userGrpNm, String setOrgId) {
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

}