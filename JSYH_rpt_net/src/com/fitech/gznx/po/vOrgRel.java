package com.fitech.gznx.po;

/**
 * vOrgRel entity. @author MyEclipse Persistence Tools
 */

public class vOrgRel implements java.io.Serializable {

	// Fields

	private vOrgRelId id =new vOrgRelId();
	private String orgNm;
	private String orgJb;
	private String preOrgid;
	private String bak1;

	// Constructors

	/** default constructor */
	public vOrgRel() {
	}

	/** minimal constructor */
	public vOrgRel(vOrgRelId id, String orgNm) {
		this.id = id;
		this.orgNm = orgNm;
	}

	/** full constructor */
	public vOrgRel(vOrgRelId id, String orgNm, String orgJb, String preOrgid,
			String bak1) {
		this.id = id;
		this.orgNm = orgNm;
		this.orgJb = orgJb;
		this.preOrgid = preOrgid;
		this.bak1 = bak1;
	}

	// Property accessors

	public vOrgRelId getId() {
		return this.id;
	}

	public void setId(vOrgRelId id) {
		this.id = id;
	}

	public String getOrgNm() {
		return this.orgNm;
	}

	public void setOrgNm(String orgNm) {
		this.orgNm = orgNm;
	}

	public String getOrgJb() {
		return this.orgJb;
	}

	public void setOrgJb(String orgJb) {
		this.orgJb = orgJb;
	}

	public String getPreOrgid() {
		return this.preOrgid;
	}

	public void setPreOrgid(String preOrgid) {
		this.preOrgid = preOrgid;
	}

	public String getBak1() {
		return this.bak1;
	}

	public void setBak1(String bak1) {
		this.bak1 = bak1;
	}

}