package com.fitech.net.form;

import org.apache.struts.action.ActionForm;

public class CollectTypeForm extends ActionForm {
	/**
	 * ���ܷ�ʽID
	 */
	private Integer collectId = null;
	/**
	 * ���ܷ�ʽ����
	 */
	private String collectName = null;
	/**
	 * ���ܷ�ʽ��������
	 */
	private String orgId = null;
	/**
	 * ���ܻ���ID
	 */
	private String collectOrgId = null;
	/**
	 * ����ID
	 */
	private String childRepId = null;
	/**
	 * �汾��
	 */
	private String versionId = null;
	/**
	 * ���ܷ�ʽ������������
	 */
	private String orgName = null;
	/**
	 * ���ܻ�������
	 */
	private String collectOrgName = null;
	/**
	 * ��ѡ�����Ҫ���ܵĻ���
	 */
	private String selectedCollectOrgs = null;
	/**
	 * ��ѡ�����Ҫ�øû��ܷ�ʽ���ܵı���
	 */
	private String selectedCollectReport = null;
	/**
	 * ����ѡ�����Ҫ���ܵĻ���
	 */
	private String collectOrgs = null;
	/**
	 * ����ѡ�����Ҫ�øû��ܷ�ʽ���ܵı���
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
