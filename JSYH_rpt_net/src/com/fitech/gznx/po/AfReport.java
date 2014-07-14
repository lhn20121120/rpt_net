package com.fitech.gznx.po;

import java.util.Date;
import java.util.Set;

/**
 * AfReport entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class AfReport implements java.io.Serializable {

	// Fields

	private Long repId;
	private String versionId;
	private String preStandard;
	private Long repFreqId;
	private Long curId;
	private Long year;
	private Long term;
	private Long day;
	private Long times;
	private Long tblInnerValidateFlag;
	private Long tblOuterValidateFlag;
	private Long reportDataWarehouseFlag;
	private String repName;
	private Long checkFlag;
	private Long package_;
	private Date reportDate;
	private Long abmormityChangeFlag;
	private Long repRangeFlag;
	private Long forseReportAgainFlag;
	private Long laterReportDay;
	private Long notReportFlag;
	private String writer;
	private String checker;
	private String principal;
	private String orgId;
	private Date checkDate;
	private Date verifyDate;
	private String bak1;
	private String bak2;
	private Long recheckFlag;
	private String templateId;
	private Long reReportTimes;

	private Integer templateStyle;
	private String repDesc;

	/** default constructor */
	public AfReport() {
	}

	public Integer getTemplateStyle() {
		return templateStyle;
	}

	public void setTemplateStyle(Integer templateStyle) {
		this.templateStyle = templateStyle;
	}

	/** minimal constructor */
	public AfReport(Long repId) {
		this.repId = repId;
	}

	/** full constructor */
	public AfReport(Long repId, String versionId, String preStandard,
			Long repFreqId, Long curId, Long year, Long term, Long times,
			Long tblInnerValidateFlag, Long tblOuterValidateFlag,
			Long reportDataWarehouseFlag, String repName, Long checkFlag,
			Long package_, Date reportDate, Long abmormityChangeFlag,
			Long repRangeFlag, Long forseReportAgainFlag, Long laterReportDay,
			Long notReportFlag, String writer, String checker,
			String principal, String orgId, Date checkDate, Date verifyDate,
			String bak1, String bak2, Long recheckFlag, String templateId,
			Long reReportTimes) {
		this.repId = repId;
		this.versionId = versionId;
		this.preStandard = preStandard;
		this.repFreqId = repFreqId;
		this.curId = curId;
		this.year = year;
		this.term = term;
		this.times = times;
		this.tblInnerValidateFlag = tblInnerValidateFlag;
		this.tblOuterValidateFlag = tblOuterValidateFlag;
		this.reportDataWarehouseFlag = reportDataWarehouseFlag;
		this.repName = repName;
		this.checkFlag = checkFlag;
		this.package_ = package_;
		this.reportDate = reportDate;
		this.abmormityChangeFlag = abmormityChangeFlag;
		this.repRangeFlag = repRangeFlag;
		this.forseReportAgainFlag = forseReportAgainFlag;
		this.laterReportDay = laterReportDay;
		this.notReportFlag = notReportFlag;
		this.writer = writer;
		this.checker = checker;
		this.principal = principal;
		this.orgId = orgId;
		this.checkDate = checkDate;
		this.verifyDate = verifyDate;
		this.bak1 = bak1;
		this.bak2 = bak2;
		this.recheckFlag = recheckFlag;
		this.templateId = templateId;
		this.reReportTimes = reReportTimes;
	}

	// Property accessors

	public Long getRepId() {
		return this.repId;
	}

	public void setRepId(Long repId) {
		this.repId = repId;
	}

	public String getVersionId() {
		return this.versionId;
	}

	public void setVersionId(String versionId) {
		this.versionId = versionId;
	}

	public String getPreStandard() {
		return this.preStandard;
	}

	public void setPreStandard(String preStandard) {
		this.preStandard = preStandard;
	}

	public Long getCurId() {
		return this.curId;
	}

	public void setCurId(Long curId) {
		this.curId = curId;
	}

	public Long getYear() {
		return this.year;
	}

	public void setYear(Long year) {
		this.year = year;
	}

	public Long getTerm() {
		return this.term;
	}

	public void setTerm(Long term) {
		this.term = term;
	}

	public Long getTimes() {
		return this.times;
	}

	public void setTimes(Long times) {
		this.times = times;
	}

	public Long getTblInnerValidateFlag() {
		return this.tblInnerValidateFlag;
	}

	public void setTblInnerValidateFlag(Long tblInnerValidateFlag) {
		this.tblInnerValidateFlag = tblInnerValidateFlag;
	}

	public Long getTblOuterValidateFlag() {
		return this.tblOuterValidateFlag;
	}

	public void setTblOuterValidateFlag(Long tblOuterValidateFlag) {
		this.tblOuterValidateFlag = tblOuterValidateFlag;
	}

	public Long getReportDataWarehouseFlag() {
		return this.reportDataWarehouseFlag;
	}

	public void setReportDataWarehouseFlag(Long reportDataWarehouseFlag) {
		this.reportDataWarehouseFlag = reportDataWarehouseFlag;
	}

	public String getRepName() {
		return this.repName;
	}

	public void setRepName(String repName) {
		this.repName = repName;
	}

	public Long getCheckFlag() {
		return this.checkFlag;
	}

	public void setCheckFlag(Long checkFlag) {
		this.checkFlag = checkFlag;
	}

	public Long getPackage_() {
		return this.package_;
	}

	public void setPackage_(Long package_) {
		this.package_ = package_;
	}

	public Date getReportDate() {
		return this.reportDate;
	}

	public void setReportDate(Date reportDate) {
		this.reportDate = reportDate;
	}

	public Long getAbmormityChangeFlag() {
		return this.abmormityChangeFlag;
	}

	public void setAbmormityChangeFlag(Long abmormityChangeFlag) {
		this.abmormityChangeFlag = abmormityChangeFlag;
	}

	public Long getRepRangeFlag() {
		return this.repRangeFlag;
	}

	public void setRepRangeFlag(Long repRangeFlag) {
		this.repRangeFlag = repRangeFlag;
	}

	public Long getForseReportAgainFlag() {
		return this.forseReportAgainFlag;
	}

	public void setForseReportAgainFlag(Long forseReportAgainFlag) {
		this.forseReportAgainFlag = forseReportAgainFlag;
	}

	public Long getLaterReportDay() {
		return this.laterReportDay;
	}

	public void setLaterReportDay(Long laterReportDay) {
		this.laterReportDay = laterReportDay;
	}

	public Long getNotReportFlag() {
		return this.notReportFlag;
	}

	public void setNotReportFlag(Long notReportFlag) {
		this.notReportFlag = notReportFlag;
	}

	public String getWriter() {
		return this.writer;
	}

	public void setWriter(String writer) {
		this.writer = writer;
	}

	public String getChecker() {
		return this.checker;
	}

	public void setChecker(String checker) {
		this.checker = checker;
	}

	public String getPrincipal() {
		return this.principal;
	}

	public void setPrincipal(String principal) {
		this.principal = principal;
	}

	public String getOrgId() {
		return this.orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

	public Date getCheckDate() {
		return this.checkDate;
	}

	public void setCheckDate(Date checkDate) {
		this.checkDate = checkDate;
	}

	public Date getVerifyDate() {
		return this.verifyDate;
	}

	public void setVerifyDate(Date verifyDate) {
		this.verifyDate = verifyDate;
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

	public Long getRecheckFlag() {
		return this.recheckFlag;
	}

	public void setRecheckFlag(Long recheckFlag) {
		this.recheckFlag = recheckFlag;
	}

	public String getTemplateId() {
		return this.templateId;
	}

	public void setTemplateId(String templateId) {
		this.templateId = templateId;
	}

	public Long getReReportTimes() {
		return this.reReportTimes;
	}

	public void setReReportTimes(Long reReportTimes) {
		this.reReportTimes = reReportTimes;
	}

	public Long getDay() {
		return day;
	}

	public void setDay(Long day) {
		this.day = day;
	}

	public Long getRepFreqId() {
		return repFreqId;
	}

	public void setRepFreqId(Long repFreqId) {
		this.repFreqId = repFreqId;
	}

	private Set reportAgainSets;

	public Set getReportAgainSets() {
		return reportAgainSets;
	}

	public void setReportAgainSets(Set reportAgainSets) {
		this.reportAgainSets = reportAgainSets;
	}

	public String getRepDesc() {
		return repDesc;
	}

	public void setRepDesc(String repDesc) {
		this.repDesc = repDesc;
	}
	
	
}