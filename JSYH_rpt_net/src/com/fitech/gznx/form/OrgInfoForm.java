package com.fitech.gznx.form;

import java.util.List;

import org.apache.struts.action.ActionForm;

/**
 * 机构信息Form
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
	 * 机构ID
	 */
	private String orgId;
	/*
	 * 模板id
	 */
	private String templateId;
	/**
	 * 机构名称
	 */
	private String orgName;

	/**
	 * 机构层次
	 */
	private Integer orgLevel;

	/**
	 * 机构类型
	 */
	private String orgType;

	/**
	 * 机构所属地区
	 */
	private String orgRegion;

	/**
	 * 上级机构ID
	 */
	private String parentOrgId;

	/**
	 * 启用日期
	 */
	private String beginDate;

	/**
	 * 机构属性
	 */
	private String orgAttr;

	/**
	 * 外部机构ID
	 */
	private String orgOuterId;

	/**
	 * 是否为汇总机构 1：汇总机构 0：不是汇总机构
	 */
	private Integer isCollect;

	private String orgTreeContent;
	
	private String setOrgId;
	
	/**
	 * 机构报送报表年份
	 */
	private Integer year = null;
	/**
	 * 机构报送报表月份
	 */
	private Integer term = null;
	/**
	 * 机构应报报表数量
	 */
	private Integer ybReportNum = null;
	/**
	 * 机构已报报表数量
	 */
	private Integer bsReportNum = null;
	/**
	 * 复核数量
	 */
	private Integer fhReportNum = null;
	/**
	 * 审签数量
	 */
	private Integer sqReportNum = null;
	/**
	 * 重报的数量
	 */
	private Integer cbReportNum = null;
	/**
	 * 分支机构列表信息
	 */
	private List subOrgList = null;
	
	private String date = null;
	
	/**
	 * 新机构ID
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