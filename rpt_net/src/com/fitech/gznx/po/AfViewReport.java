package com.fitech.gznx.po;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

public class AfViewReport implements Serializable {

	private AfViewReportPK comp_id;
	private String curName;
	private String repFreqName;
	private String templateName;
	private Integer laterTime;
	private Integer normalTime;
	private String startDate;
	private String endDate;
	private String templateType;
	private Integer isReport;
	private Integer isLeader;
	private Integer priorityFlag;
	private Integer supplementFlag;
	private String bak1; // 报表类型
	private Integer reportStyle; //报表模板类型

	public Integer getReportStyle() {
		return reportStyle;
	}

	public void setReportStyle(Integer reportStyle) {
		this.reportStyle = reportStyle;
	}

	/**
	 * 返回 endDate
	 */
	public String getEndDate() {
		return endDate;
	}

	/**
	 * 参数：endDate 设置 endDate
	 */
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	/**
	 * 返回 startDate
	 */
	public String getStartDate() {
		return startDate;
	}

	/**
	 * 参数：startDate 设置 startDate
	 */
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public AfViewReport() {

	}

	/**
	 * 返回 comp_id
	 */
	public AfViewReportPK getComp_id() {
		return comp_id;
	}

	/**
	 * 参数：comp_id 设置 comp_id
	 */
	public void setComp_id(AfViewReportPK comp_id) {
		this.comp_id = comp_id;
	}

	/**
	 * 返回 curName
	 */
	public String getCurName() {
		return curName;
	}

	/**
	 * 参数：curName 设置 curName
	 */
	public void setCurName(String curName) {
		this.curName = curName;
	}

	/**
	 * 返回 normalTime
	 */
	public Integer getNormalTime() {
		return normalTime;
	}

	/**
	 * 参数：normalTime 设置 normalTime
	 */
	public void setNormalTime(Integer normalTime) {
		this.normalTime = normalTime;
	}

	/**
	 * 返回 repFreqName
	 */
	public String getRepFreqName() {
		return repFreqName;
	}

	/**
	 * 参数：repFreqName 设置 repFreqName
	 */
	public void setRepFreqName(String repFreqName) {
		this.repFreqName = repFreqName;
	}

	public String toString() {
		return new ToStringBuilder(this).append("comp_id", getComp_id())
				.toString();
	}

	public boolean equals(Object other) {
		if (!(other instanceof AfViewReport))
			return false;
		AfViewReport castOther = (AfViewReport) other;
		return new EqualsBuilder().append(this.getComp_id(),
				castOther.getComp_id()).isEquals();
	}

	public int hashCode() {
		return new HashCodeBuilder().append(getComp_id()).toHashCode();
	}

	public String getTemplateName() {
		return templateName;
	}

	public void setTemplateName(String templateName) {
		this.templateName = templateName;
	}

	public Integer getLaterTime() {
		return laterTime;
	}

	public void setLaterTime(Integer laterTime) {
		this.laterTime = laterTime;
	}

	public String getTemplateType() {
		return templateType;
	}

	public void setTemplateType(String templateType) {
		this.templateType = templateType;
	}

	public Integer getIsReport() {
		return isReport;
	}

	public void setIsReport(Integer isReport) {
		this.isReport = isReport;
	}

	public Integer getPriorityFlag() {
		return priorityFlag;
	}

	public void setPriorityFlag(Integer priorityFlag) {
		this.priorityFlag = priorityFlag;
	}

	public String getBak1() {
		return bak1;
	}

	public void setBak1(String bak1) {
		this.bak1 = bak1;
	}

	public Integer getIsLeader() {
		return isLeader;
	}

	public void setIsLeader(Integer isLeader) {
		this.isLeader = isLeader;
	}

	public Integer getSupplementFlag() {
		return supplementFlag;
	}

	public void setSupplementFlag(Integer supplementFlag) {
		this.supplementFlag = supplementFlag;
	}

}
