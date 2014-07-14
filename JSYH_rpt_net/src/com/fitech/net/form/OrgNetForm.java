package com.fitech.net.form;

import org.apache.struts.action.ActionForm;

public class OrgNetForm extends ActionForm {
	private static final long serialVersionUID = 1L;
	/**
	 * 机构部门ID
	 */
	private String org_dept_id = null;
	/**
	 * 机构ID
	 */
	private String org_id = null;
	/**
	 * 机构名称
	 */
	private String org_name = null;
	/**
	 * 机构类别ID
	 */
	private Integer org_type_id = null;
	/**
	 * 机构类别名称
	 */
	private String org_type_name = null;
	/**
	 * 上级机构ID
	 */
	private String pre_org_id = null;
	/**
	 * 上级机构名称
	 */
	private String pre_org_name = null;
	/**
	 * 机构所属地区ID
	 */
	private String region_id = null;
	/**
	 * 机构所属地区名称
	 */
	private String region_name = null;
	/**
	 * 被建机构ID
	 */
	private java.lang.String setOrgId;
	/**
	 * 机构应报报表数量
	 */
	private java.lang.Integer ybReportNum = null;
	/**
	 * 机构已报报表数量
	 */
	private java.lang.Integer bsReportNum = null;
	/**
	 * 机构报送报表年份
	 */
	private java.lang.Integer year = null;
	/**
	 * 机构报送报表月份
	 */
	private java.lang.Integer term = null;
	/**
	 * 分支机构列表信息
	 */
	private java.util.List subOrgList = null;
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
	 * 迟报的数量
	 */
	private Integer zbReportNum = null;
	/**
	 * 漏报的数量
	 */
	private Integer lbReportNum = null;
	/**
	 * 报表报送日期
	 */
	private String date = null;
	/**
	 * 开始时间
	 */
	private String startDate = null;
	/**
	 * 结束时间
	 */
	private String endDate = null;
	/**
	 * 条件
	 */
	private Integer condition = null;

	public Integer getCondition() {
		return condition;
	}
	public void setCondition(Integer condition) {
		this.condition = condition;
	}
	public String getOrg_id() {
		return org_id;
	}
	public void setOrg_id(String org_id) {
		this.org_id = org_id;
	}
	public String getOrg_name() {
		return org_name;
	}
	public void setOrg_name(String org_name) {
		this.org_name = org_name;
        //-------------------------
        // gongming 2008-07-25
        if(null != this.org_name)
            this.org_name = this.org_name.trim();
	}
	public Integer getOrg_type_id() {
		return org_type_id;
	}
	public void setOrg_type_id(Integer org_type_id) {
		this.org_type_id = org_type_id;
	}
	public String getOrg_type_name() {
		return org_type_name;
	}
	public void setOrg_type_name(String org_type_name) {
		this.org_type_name = org_type_name;
	}
	public String getPre_org_id() {
		return pre_org_id;
	}
	public void setPre_org_id(String pre_org_id) {
		this.pre_org_id = pre_org_id;
	}
	public String getPre_org_name() {
		return pre_org_name;
	}
	public void setPre_org_name(String pre_org_name) {
		this.pre_org_name = pre_org_name;
	}
	public String getRegion_id() {
		return region_id;
	}
	public void setRegion_id(String region_id) {
		this.region_id = region_id;
	}
	public String getRegion_name() {
		return region_name;
	}
	public void setRegion_name(String region_name) {
		this.region_name = region_name;
	}
	public java.lang.String getSetOrgId() {
		return setOrgId;
	}
	public void setSetOrgId(java.lang.String setOrgId) {
		this.setOrgId = setOrgId;
	}
	/**
	 * @return Returns the bsReportNum.
	 */
	public java.lang.Integer getBsReportNum() {
		return bsReportNum;
	}
	/**
	 * @param bsReportNum The bsReportNum to set.
	 */
	public void setBsReportNum(java.lang.Integer bsReportNum) {
		this.bsReportNum = bsReportNum;
	}
	/**
	 * @return Returns the ybReportNum.
	 */
	public java.lang.Integer getYbReportNum() {
		return ybReportNum;
	}
	/**
	 * @param ybReportNum The ybReportNum to set.
	 */
	public void setYbReportNum(java.lang.Integer ybReportNum) {
		this.ybReportNum = ybReportNum;
	}
	/**
	 * @return Returns the term.
	 */
	public java.lang.Integer getTerm() {
		return term;
	}
	/**
	 * @param term The term to set.
	 */
	public void setTerm(java.lang.Integer term) {
		this.term = term;
	}
	/**
	 * @return Returns the year.
	 */
	public java.lang.Integer getYear() {
		return year;
	}
	/**
	 * @param year The year to set.
	 */
	public void setYear(java.lang.Integer year) {
		this.year = year;
	}
	/**
	 * @return Returns the subOrgList.
	 */
	public java.util.List getSubOrgList() {
		return subOrgList;
	}
	/**
	 * @param subOrgList The subOrgList to set.
	 */
	public void setSubOrgList(java.util.List subOrgList) {
		this.subOrgList = subOrgList;
	}
	public String getOrg_dept_id()
	{
		return org_dept_id;
	}
	public void setOrg_dept_id(String org_dept_id)
	{
		this.org_dept_id = org_dept_id;
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
	public Integer getZbReportNum() {
		return zbReportNum;
	}
	public void setZbReportNum(Integer zbReportNum) {
		this.zbReportNum = zbReportNum;
	}
	public Integer getLbReportNum() {
		return lbReportNum;
	}
	public void setLbReportNum(Integer lbReportNum) {
		this.lbReportNum = lbReportNum;
	}
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
}
