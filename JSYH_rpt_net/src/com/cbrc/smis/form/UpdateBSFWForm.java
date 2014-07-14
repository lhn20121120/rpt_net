package com.cbrc.smis.form;

import org.apache.struts.action.ActionForm;

public class UpdateBSFWForm extends ActionForm{
//	private String[] orgIds=new String[0];
	private String curOrgId=null;
	private String[] orgIds=null;
	private String childRepId=null;
	private String versionId=null;
	private String reportName=null;
	private String reportStyle=null;
	private String orgId=null;
	private String gotoOrg=null;
	public String getGotoOrg() {
		return gotoOrg;
	}

	public void setGotoOrg(String gotoOrg) {
		this.gotoOrg = gotoOrg;
	}

	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

	public String getReportName() {
		return reportName;
	}

	public void setReportName(String reportName) {
		this.reportName = reportName;
	}

	public String getReportStyle() {
		return reportStyle;
	}

	public void setReportStyle(String reportStyle) {
		this.reportStyle = reportStyle;
	}

	public UpdateBSFWForm(){}

//	public String[] getOrgIds() {
//		return orgIds;
//	}
//
//	public void setOrgIds(String[] orgIds) {
//		this.orgIds = orgIds;
//	}

	public String getChildRepId() {
		return childRepId;
	}

	public void setChildRepId(String childRepId) {
		this.childRepId = childRepId;
	}

	public String getVersionId() {
		return versionId;
	}

	public void setVersionId(String versionId) {
		this.versionId = versionId;
	}

	public String[] getOrgIds() {
		return orgIds;
	}

	public void setOrgIds(String[] orgIds) {
		this.orgIds = orgIds;
	}

	public String getCurOrgId() {
		return curOrgId;
	}

	public void setCurOrgId(String curOrgId) {
		this.curOrgId = curOrgId;
	}

}
