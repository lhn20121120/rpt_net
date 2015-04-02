package com.fitech.gznx.po;

/**
 * AfTemplate entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class AfTemplate implements java.io.Serializable {

	// Fields

	private AfTemplateId id;
	private String templateName;
	private String startDate;
	private String endDate;
	private Long isLeader;
	private Long supplementFlag;
	private String filePath;
	private int usingFlag;
	private String templateType;
	private Long priorityFlag;
	private Long isReport;
	private Long isCollect;
	private String bak1;
	private String bak2;
	private Long reportStyle;
	
	// Constructors

	/** default constructor */
	public AfTemplate() {
	}

	/** minimal constructor */
	public AfTemplate(AfTemplateId id) {
		this.id = id;
	}

	/** full constructor */
	public AfTemplate(AfTemplateId id, String templateName, String startDate,
			String endDate, Long isLeader, Long supplementFlag,
			String filePath, int usingFlag, String templateType,
			Long priorityFlag, Long isReport, Long isCollect, String bak1,
			String bak2,Long reportStyle) {
		this.id = id;
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
	}

	// Property accessors

	public AfTemplateId getId() {
		return this.id;
	}

	public void setId(AfTemplateId id) {
		this.id = id;
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

	public Long getIsLeader() {
		return this.isLeader;
	}

	public void setIsLeader(Long isLeader) {
		this.isLeader = isLeader;
	}

	public Long getSupplementFlag() {
		return this.supplementFlag;
	}

	public void setSupplementFlag(Long supplementFlag) {
		this.supplementFlag = supplementFlag;
	}

	public String getFilePath() {
		return this.filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public int getUsingFlag() {
		return this.usingFlag;
	}

	public void setUsingFlag(int usingFlag) {
		this.usingFlag = usingFlag;
	}

	public String getTemplateType() {
		return this.templateType;
	}

	public void setTemplateType(String templateType) {
		this.templateType = templateType;
	}

	public Long getPriorityFlag() {
		return this.priorityFlag;
	}

	public void setPriorityFlag(Long priorityFlag) {
		this.priorityFlag = priorityFlag;
	}

	public Long getIsReport() {
		return this.isReport;
	}

	public void setIsReport(Long isReport) {
		this.isReport = isReport;
	}

	public Long getIsCollect() {
		return this.isCollect;
	}

	public void setIsCollect(Long isCollect) {
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

	public Long getReportStyle() {
		return reportStyle;
	}

	public void setReportStyle(Long reportStyle) {
		this.reportStyle = reportStyle;
	}

}