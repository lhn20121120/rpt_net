package com.cbrc.smis.form;

import org.apache.struts.action.ActionForm;

import com.fitech.gznx.po.vOrgRelId;

public class VorgRelForm  extends ActionForm{
	
	private vOrgRelId id =new vOrgRelId();
	private String orgNm;
	private String orgJb;
	private String preOrgid;
	private String bak1;
	private String orgName;
	
	
	public vOrgRelId getId() {
		return id;
	}
	public void setId(vOrgRelId id) {
		this.id = id;
	}
	public String getOrgNm() {
		return orgNm;
	}
	public void setOrgNm(String orgNm) {
		this.orgNm = orgNm;
	}
	public String getOrgJb() {
		return orgJb;
	}
	public void setOrgJb(String orgJb) {
		this.orgJb = orgJb;
	}
	public String getPreOrgid() {
		return preOrgid;
	}
	public void setPreOrgid(String preOrgid) {
		this.preOrgid = preOrgid;
	}
	public String getBak1() {
		return bak1;
	}
	public void setBak1(String bak1) {
		this.bak1 = bak1;
	}
	public String getOrgName() {
		return orgName;
	}
	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}
	
	
	
}
