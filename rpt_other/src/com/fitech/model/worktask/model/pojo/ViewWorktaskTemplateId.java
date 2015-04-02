package com.fitech.model.worktask.model.pojo;

import java.math.BigDecimal;

/**
 * ViewWorktaskTemplateId entity. @author MyEclipse Persistence Tools
 */

public class ViewWorktaskTemplateId implements java.io.Serializable {

	// Fields

	private String templateId;
	private String versionId;
	private String templateName;
	private String startDate;
	private String endDate;
	private BigDecimal isLeader;
	private BigDecimal supplementFlag;
	private String filePath;
	private BigDecimal usingFlag;
	private String templateType;
	private BigDecimal priorityFlag;
	private BigDecimal isReport;
	private BigDecimal isCollect;
	private String bak1;
	private String bak2;
	private BigDecimal reportStyle;
	private String busiLineId;
	private String templateGroupId;
	private String repFreqId;

	// Constructors

	/** default constructor */
	public ViewWorktaskTemplateId() {
	}

	/** minimal constructor */
	public ViewWorktaskTemplateId(String templateId) {
		this.templateId = templateId;
	}

	/** full constructor */
	public ViewWorktaskTemplateId(String templateId, String versionId,
			String templateName, String startDate, String endDate,
			BigDecimal isLeader, BigDecimal supplementFlag, String filePath,
			BigDecimal usingFlag, String templateType, BigDecimal priorityFlag,
			BigDecimal isReport, BigDecimal isCollect, String bak1,
			String bak2, BigDecimal reportStyle, String busiLineId,
			String templateGroupId, String repFreqId) {
		this.templateId = templateId;
		this.versionId = versionId;
		this.templateName = templateName;
		this.startDate = startDate;
		this.endDate = endDate;
		this.isLeader = isLeader;
		this.supplementFlag = supplementFlag;
		this.filePath = filePath;
		this.usingFlag = usingFlag;
		this.templateType = templateType;
		this.priorityFlag = priorityFlag;
		this.isReport = isReport;
		this.isCollect = isCollect;
		this.bak1 = bak1;
		this.bak2 = bak2;
		this.reportStyle = reportStyle;
		this.busiLineId = busiLineId;
		this.templateGroupId = templateGroupId;
		this.repFreqId = repFreqId;
	}

	// Property accessors

	public String getTemplateId() {
		return this.templateId;
	}

	public void setTemplateId(String templateId) {
		this.templateId = templateId;
	}

	public String getVersionId() {
		return this.versionId;
	}

	public void setVersionId(String versionId) {
		this.versionId = versionId;
	}

	public String getTemplateName() {
		return this.templateName;
	}

	public void setTemplateName(String templateName) {
		this.templateName = templateName;
	}

	public String getStartDate() {
		return this.startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return this.endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public BigDecimal getIsLeader() {
		return this.isLeader;
	}

	public void setIsLeader(BigDecimal isLeader) {
		this.isLeader = isLeader;
	}

	public BigDecimal getSupplementFlag() {
		return this.supplementFlag;
	}

	public void setSupplementFlag(BigDecimal supplementFlag) {
		this.supplementFlag = supplementFlag;
	}

	public String getFilePath() {
		return this.filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public BigDecimal getUsingFlag() {
		return this.usingFlag;
	}

	public void setUsingFlag(BigDecimal usingFlag) {
		this.usingFlag = usingFlag;
	}

	public String getTemplateType() {
		return this.templateType;
	}

	public void setTemplateType(String templateType) {
		this.templateType = templateType;
	}

	public BigDecimal getPriorityFlag() {
		return this.priorityFlag;
	}

	public void setPriorityFlag(BigDecimal priorityFlag) {
		this.priorityFlag = priorityFlag;
	}

	public BigDecimal getIsReport() {
		return this.isReport;
	}

	public void setIsReport(BigDecimal isReport) {
		this.isReport = isReport;
	}

	public BigDecimal getIsCollect() {
		return this.isCollect;
	}

	public void setIsCollect(BigDecimal isCollect) {
		this.isCollect = isCollect;
	}

	public String getBak1() {
		return this.bak1;
	}

	public void setBak1(String bak1) {
		this.bak1 = bak1;
	}

	public String getBak2() {
		return this.bak2;
	}

	public void setBak2(String bak2) {
		this.bak2 = bak2;
	}

	public BigDecimal getReportStyle() {
		return this.reportStyle;
	}

	public void setReportStyle(BigDecimal reportStyle) {
		this.reportStyle = reportStyle;
	}

	public String getBusiLineId() {
		return this.busiLineId;
	}

	public void setBusiLineId(String busiLineId) {
		this.busiLineId = busiLineId;
	}

	public String getTemplateGroupId() {
		return this.templateGroupId;
	}

	public void setTemplateGroupId(String templateGroupId) {
		this.templateGroupId = templateGroupId;
	}

	public String getRepFreqId() {
		return this.repFreqId;
	}

	public void setRepFreqId(String repFreqId) {
		this.repFreqId = repFreqId;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof ViewWorktaskTemplateId))
			return false;
		ViewWorktaskTemplateId castOther = (ViewWorktaskTemplateId) other;

		return ((this.getTemplateId() == castOther.getTemplateId()) || (this
				.getTemplateId() != null
				&& castOther.getTemplateId() != null && this.getTemplateId()
				.equals(castOther.getTemplateId())))
				&& ((this.getVersionId() == castOther.getVersionId()) || (this
						.getVersionId() != null
						&& castOther.getVersionId() != null && this
						.getVersionId().equals(castOther.getVersionId())))
				&& ((this.getTemplateName() == castOther.getTemplateName()) || (this
						.getTemplateName() != null
						&& castOther.getTemplateName() != null && this
						.getTemplateName().equals(castOther.getTemplateName())))
				&& ((this.getStartDate() == castOther.getStartDate()) || (this
						.getStartDate() != null
						&& castOther.getStartDate() != null && this
						.getStartDate().equals(castOther.getStartDate())))
				&& ((this.getEndDate() == castOther.getEndDate()) || (this
						.getEndDate() != null
						&& castOther.getEndDate() != null && this.getEndDate()
						.equals(castOther.getEndDate())))
				&& ((this.getIsLeader() == castOther.getIsLeader()) || (this
						.getIsLeader() != null
						&& castOther.getIsLeader() != null && this
						.getIsLeader().equals(castOther.getIsLeader())))
				&& ((this.getSupplementFlag() == castOther.getSupplementFlag()) || (this
						.getSupplementFlag() != null
						&& castOther.getSupplementFlag() != null && this
						.getSupplementFlag().equals(
								castOther.getSupplementFlag())))
				&& ((this.getFilePath() == castOther.getFilePath()) || (this
						.getFilePath() != null
						&& castOther.getFilePath() != null && this
						.getFilePath().equals(castOther.getFilePath())))
				&& ((this.getUsingFlag() == castOther.getUsingFlag()) || (this
						.getUsingFlag() != null
						&& castOther.getUsingFlag() != null && this
						.getUsingFlag().equals(castOther.getUsingFlag())))
				&& ((this.getTemplateType() == castOther.getTemplateType()) || (this
						.getTemplateType() != null
						&& castOther.getTemplateType() != null && this
						.getTemplateType().equals(castOther.getTemplateType())))
				&& ((this.getPriorityFlag() == castOther.getPriorityFlag()) || (this
						.getPriorityFlag() != null
						&& castOther.getPriorityFlag() != null && this
						.getPriorityFlag().equals(castOther.getPriorityFlag())))
				&& ((this.getIsReport() == castOther.getIsReport()) || (this
						.getIsReport() != null
						&& castOther.getIsReport() != null && this
						.getIsReport().equals(castOther.getIsReport())))
				&& ((this.getIsCollect() == castOther.getIsCollect()) || (this
						.getIsCollect() != null
						&& castOther.getIsCollect() != null && this
						.getIsCollect().equals(castOther.getIsCollect())))
				&& ((this.getBak1() == castOther.getBak1()) || (this.getBak1() != null
						&& castOther.getBak1() != null && this.getBak1()
						.equals(castOther.getBak1())))
				&& ((this.getBak2() == castOther.getBak2()) || (this.getBak2() != null
						&& castOther.getBak2() != null && this.getBak2()
						.equals(castOther.getBak2())))
				&& ((this.getReportStyle() == castOther.getReportStyle()) || (this
						.getReportStyle() != null
						&& castOther.getReportStyle() != null && this
						.getReportStyle().equals(castOther.getReportStyle())))
				&& ((this.getBusiLineId() == castOther.getBusiLineId()) || (this
						.getBusiLineId() != null
						&& castOther.getBusiLineId() != null && this
						.getBusiLineId().equals(castOther.getBusiLineId())))
				&& ((this.getTemplateGroupId() == castOther
						.getTemplateGroupId()) || (this.getTemplateGroupId() != null
						&& castOther.getTemplateGroupId() != null && this
						.getTemplateGroupId().equals(
								castOther.getTemplateGroupId())))
				&& ((this.getRepFreqId() == castOther.getRepFreqId()) || (this
						.getRepFreqId() != null
						&& castOther.getRepFreqId() != null && this
						.getRepFreqId().equals(castOther.getRepFreqId())));
	}

	public int hashCode() {
		int result = 17;

		result = 37
				* result
				+ (getTemplateId() == null ? 0 : this.getTemplateId()
						.hashCode());
		result = 37 * result
				+ (getVersionId() == null ? 0 : this.getVersionId().hashCode());
		result = 37
				* result
				+ (getTemplateName() == null ? 0 : this.getTemplateName()
						.hashCode());
		result = 37 * result
				+ (getStartDate() == null ? 0 : this.getStartDate().hashCode());
		result = 37 * result
				+ (getEndDate() == null ? 0 : this.getEndDate().hashCode());
		result = 37 * result
				+ (getIsLeader() == null ? 0 : this.getIsLeader().hashCode());
		result = 37
				* result
				+ (getSupplementFlag() == null ? 0 : this.getSupplementFlag()
						.hashCode());
		result = 37 * result
				+ (getFilePath() == null ? 0 : this.getFilePath().hashCode());
		result = 37 * result
				+ (getUsingFlag() == null ? 0 : this.getUsingFlag().hashCode());
		result = 37
				* result
				+ (getTemplateType() == null ? 0 : this.getTemplateType()
						.hashCode());
		result = 37
				* result
				+ (getPriorityFlag() == null ? 0 : this.getPriorityFlag()
						.hashCode());
		result = 37 * result
				+ (getIsReport() == null ? 0 : this.getIsReport().hashCode());
		result = 37 * result
				+ (getIsCollect() == null ? 0 : this.getIsCollect().hashCode());
		result = 37 * result
				+ (getBak1() == null ? 0 : this.getBak1().hashCode());
		result = 37 * result
				+ (getBak2() == null ? 0 : this.getBak2().hashCode());
		result = 37
				* result
				+ (getReportStyle() == null ? 0 : this.getReportStyle()
						.hashCode());
		result = 37
				* result
				+ (getBusiLineId() == null ? 0 : this.getBusiLineId()
						.hashCode());
		result = 37
				* result
				+ (getTemplateGroupId() == null ? 0 : this.getTemplateGroupId()
						.hashCode());
		result = 37 * result
				+ (getRepFreqId() == null ? 0 : this.getRepFreqId().hashCode());
		return result;
	}

}