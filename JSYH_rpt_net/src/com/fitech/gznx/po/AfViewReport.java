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
	private String bak1; // ��������
	private Integer reportStyle; //����ģ������

	public Integer getReportStyle() {
		return reportStyle;
	}

	public void setReportStyle(Integer reportStyle) {
		this.reportStyle = reportStyle;
	}

	/**
	 * ���� endDate
	 */
	public String getEndDate() {
		return endDate;
	}

	/**
	 * ������endDate ���� endDate
	 */
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	/**
	 * ���� startDate
	 */
	public String getStartDate() {
		return startDate;
	}

	/**
	 * ������startDate ���� startDate
	 */
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public AfViewReport() {

	}

	/**
	 * ���� comp_id
	 */
	public AfViewReportPK getComp_id() {
		return comp_id;
	}

	/**
	 * ������comp_id ���� comp_id
	 */
	public void setComp_id(AfViewReportPK comp_id) {
		this.comp_id = comp_id;
	}

	/**
	 * ���� curName
	 */
	public String getCurName() {
		return curName;
	}

	/**
	 * ������curName ���� curName
	 */
	public void setCurName(String curName) {
		this.curName = curName;
	}

	/**
	 * ���� normalTime
	 */
	public Integer getNormalTime() {
		return normalTime;
	}

	/**
	 * ������normalTime ���� normalTime
	 */
	public void setNormalTime(Integer normalTime) {
		this.normalTime = normalTime;
	}

	/**
	 * ���� repFreqName
	 */
	public String getRepFreqName() {
		return repFreqName;
	}

	/**
	 * ������repFreqName ���� repFreqName
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
