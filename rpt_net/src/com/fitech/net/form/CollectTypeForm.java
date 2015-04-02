package com.fitech.net.form;

import org.apache.struts.action.ActionForm;

public class CollectTypeForm extends ActionForm {
	/**
	 * 汇总方式ID
	 */
	private Integer collectId = null;
	/**
	 * 汇总方式名称
	 */
	private String collectName = null;
	/**
	 * 汇总方式所属机构
	 */
	private String orgId = null;
	/**
	 * 汇总机构ID
	 */
	private String collectOrgId = null;
	/**
	 * 报表ID
	 */
	private String childRepId = null;
	/**
	 * 版本号
	 */
	private String versionId = null;
	/**
	 * 汇总方式所属机构名称
	 */
	private String orgName = null;
	/**
	 * 汇总机构名称
	 */
	private String collectOrgName = null;
	/**
	 * 已选择的需要汇总的机构
	 */
	private String selectedCollectOrgs = null;
	/**
	 * 已选择的需要用该汇总方式汇总的报表
	 */
	private String selectedCollectReport = null;
	/**
	 * 可以选择的需要汇总的机构
	 */
	private String collectOrgs = null;
	/**
	 * 可以选择的需要用该汇总方式汇总的报表
	 */
	private String collectReports = null;
	
	public String getChildRepId() {
		return childRepId;
	}
	public void setChildRepId(String childRepId) {
		this.childRepId = childRepId;
	}
	public Integer getCollectId() {
		return collectId;
	}
	public void setCollectId(Integer collectId) {
		this.collectId = collectId;
	}
	public String getCollectName() {
		return collectName;
	}
	public void setCollectName(String collectName) {
		this.collectName = collectName;
	}
	public String getCollectOrgId() {
		return collectOrgId;
	}
	public void setCollectOrgId(String collectOrgId) {
		this.collectOrgId = collectOrgId;
	}
	public String getOrgId() {
		return orgId;
	}
	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}
	public String getVersionId() {
		return versionId;
	}
	public void setVersionId(String versionId) {
		this.versionId = versionId;
	}
	public String getCollectOrgName() {
		return collectOrgName;
	}
	public void setCollectOrgName(String collectOrgName) {
		this.collectOrgName = collectOrgName;
	}
	public String getOrgName() {
		return orgName;
	}
	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}
	public String getCollectOrgs() {
		return collectOrgs;
	}
	public void setCollectOrgs(String collectOrgs) {
		this.collectOrgs = collectOrgs;
	}
	public String getCollectReports() {
		return collectReports;
	}
	public void setCollectReports(String collectReports) {
		this.collectReports = collectReports;
	}
	public String getSelectedCollectOrgs() {
		return selectedCollectOrgs;
	}
	public void setSelectedCollectOrgs(String selectedCollectOrgs) {
		this.selectedCollectOrgs = selectedCollectOrgs;
	}
	public String getSelectedCollectReport() {
		return selectedCollectReport;
	}
	public void setSelectedCollectReport(String selectedCollectReport) {
		this.selectedCollectReport = selectedCollectReport;
	}
}
