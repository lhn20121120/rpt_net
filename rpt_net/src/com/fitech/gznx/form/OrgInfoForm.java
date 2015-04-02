package com.fitech.gznx.form;

import java.util.List;

import org.apache.struts.action.ActionForm;

/**
 * ������ϢForm
 * 
 * @author XY
 * 
 */
public class OrgInfoForm extends ActionForm {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * ����ID
	 */
	private String orgId;
	/*
	 * ģ��id
	 */
	private String templateId;
	/**
	 * ��������
	 */
	private String orgName;

	/**
	 * �������
	 */
	private Integer orgLevel;

	/**
	 * ��������
	 */
	private String orgType;

	/**
	 * ������������
	 */
	private String orgRegion;

	/**
	 * �ϼ�����ID
	 */
	private String parentOrgId;

	/**
	 * ��������
	 */
	private String beginDate;

	/**
	 * ��������
	 */
	private String orgAttr;

	/**
	 * �ⲿ����ID
	 */
	private String orgOuterId;

	/**
	 * �Ƿ�Ϊ���ܻ��� 1�����ܻ��� 0�����ǻ��ܻ���
	 */
	private Integer isCollect;

	private String orgTreeContent;
	
	private String setOrgId;
	
	/**
	 * �������ͱ������
	 */
	private Integer year = null;
	/**
	 * �������ͱ����·�
	 */
	private Integer term = null;
	/**
	 * ����Ӧ����������
	 */
	private Integer ybReportNum = null;
	/**
	 * �����ѱ���������
	 */
	private Integer bsReportNum = null;
	/**
	 * ��������
	 */
	private Integer fhReportNum = null;
	/**
	 * ��ǩ����
	 */
	private Integer sqReportNum = null;
	/**
	 * �ر�������
	 */
	private Integer cbReportNum = null;
	/**
	 * ��֧�����б���Ϣ
	 */
	private List subOrgList = null;
	
	private String date = null;
	
	/**
	 * �»���ID
	 */
	private String newOrgId = null;
	
	
	public String getTemplateId() {
		return templateId;
	}

	public void setTemplateId(String templateId) {
		this.templateId = templateId;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getBeginDate() {
		return beginDate;
	}

	public void setBeginDate(String beginDate) {
		this.beginDate = beginDate;
	}

	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

	public Integer getOrgLevel() {
		return orgLevel;
	}

	public void setOrgLevel(Integer orgLevel) {
		this.orgLevel = orgLevel;
	}

	public String getOrgName() {
		return orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

	public String getOrgRegion() {
		return orgRegion;
	}

	public void setOrgRegion(String orgRegion) {
		this.orgRegion = orgRegion;
	}

	public String getOrgType() {
		return orgType;
	}

	public void setOrgType(String orgType) {
		this.orgType = orgType;
	}

	public String getParentOrgId() {
		return parentOrgId;
	}

	public void setParentOrgId(String parentOrgId) {
		this.parentOrgId = parentOrgId;
	}

	public String getOrgAttr() {
		return orgAttr;
	}

	public void setOrgAttr(String orgAttr) {
		this.orgAttr = orgAttr;
	}

	public String getOrgOuterId() {
		return orgOuterId;
	}

	public void setOrgOuterId(String orgOuterId) {
		this.orgOuterId = orgOuterId;
	}

	public Integer getIsCollect() {
		return isCollect;
	}

	public void setIsCollect(Integer isCollect) {
		this.isCollect = isCollect;
	}

	public String getOrgTreeContent() {
		return orgTreeContent;
	}

	public void setOrgTreeContent(String orgTreeContent) {
		this.orgTreeContent = orgTreeContent;
	}

	public Integer getYear() {
		return year;
	}

	public void setYear(Integer year) {
		this.year = year;
	}

	public Integer getTerm() {
		return term;
	}

	public void setTerm(Integer term) {
		this.term = term;
	}

	public Integer getYbReportNum() {
		return ybReportNum;
	}

	public void setYbReportNum(Integer ybReportNum) {
		this.ybReportNum = ybReportNum;
	}

	public Integer getBsReportNum() {
		return bsReportNum;
	}

	public void setBsReportNum(Integer bsReportNum) {
		this.bsReportNum = bsReportNum;
	}

	public Integer getFhReportNum() {
		return fhReportNum;
	}

	public void setFhReportNum(Integer fhReportNum) {
		this.fhReportNum = fhReportNum;
	}

	public Integer getSqReportNum() {
		return sqReportNum;
	}

	public void setSqReportNum(Integer sqReportNum) {
		this.sqReportNum = sqReportNum;
	}

	public Integer getCbReportNum() {
		return cbReportNum;
	}

	public void setCbReportNum(Integer cbReportNum) {
		this.cbReportNum = cbReportNum;
	}

	public List getSubOrgList() {
		return subOrgList;
	}

	public void setSubOrgList(List subOrgList) {
		this.subOrgList = subOrgList;
	}

	public String getSetOrgId() {
		return setOrgId;
	}

	public void setSetOrgId(String setOrgId) {
		this.setOrgId = setOrgId;
	}

	public String getNewOrgId() {
		return newOrgId;
	}

	public void setNewOrgId(String newOrgId) {
		this.newOrgId = newOrgId;
	}

}