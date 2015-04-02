package com.fitech.model.worktask.model.pojo;

import java.math.BigDecimal;
import java.util.Date;

/**
 * AfReport entity. @author MyEclipse Persistence Tools
 */

public class AfReport implements java.io.Serializable {

	// Fields

	private BigDecimal repId;
	private String versionId;
	private String preStandard;
	private BigDecimal repFreqId;
	private BigDecimal curId;
	private BigDecimal year;
	private BigDecimal term;
	private BigDecimal times;
	private BigDecimal tblInnerValidateFlag;
	private BigDecimal tblOuterValidateFlag;
	private BigDecimal reportDataWarehouseFlag;
	private String repName;
	private BigDecimal checkFlag;
	private BigDecimal package_;
	private Date reportDate;
	private BigDecimal abmormityChangeFlag;
	private BigDecimal repRangeFlag;
	private BigDecimal forseReportAgainFlag;
	private BigDecimal laterReportDay;
	private BigDecimal notReportFlag;
	private String writer;
	private String checker;
	private String principal;
	private String orgId;
	private Date checkDate;
	private Date verifyDate;
	private String bak1;
	private String bak2;
	private BigDecimal recheckFlag;
	private String templateId;
	private BigDecimal reReportTimes;
	private BigDecimal day;
	private String repDesc;

	// Constructors

	/** default constructor */
	public AfReport() {
	}

	/** minimal constructor */
	public AfReport(BigDecimal repId) {
		this.repId = repId;
	}

	/** full constructor */
	public AfReport(BigDecimal repId, String versionId, String preStandard,
			BigDecimal repFreqId, BigDecimal curId, BigDecimal year,
			BigDecimal term, BigDecimal times, BigDecimal tblInnerValidateFlag,
			BigDecimal tblOuterValidateFlag,
			BigDecimal reportDataWarehouseFlag, String repName,
			BigDecimal checkFlag, BigDecimal package_, Date reportDate,
			BigDecimal abmormityChangeFlag, BigDecimal repRangeFlag,
			BigDecimal forseReportAgainFlag, BigDecimal laterReportDay,
			BigDecimal notReportFlag, String writer, String checker,
			String principal, String orgId, Date checkDate, Date verifyDate,
			String bak1, String bak2, BigDecimal recheckFlag,
			String templateId, BigDecimal reReportTimes, BigDecimal day,
			String repDesc) {
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
		this.day = day;
		this.repDesc = repDesc;
	}

	// Property accessors

	public BigDecimal getRepId() {
		return this.repId;
	}

	public void setRepId(BigDecimal repId) {
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

	public BigDecimal getRepFreqId() {
		return this.repFreqId;
	}

	public void setRepFreqId(BigDecimal repFreqId) {
		this.repFreqId = repFreqId;
	}

	public BigDecimal getCurId() {
		return this.curId;
	}

	public void setCurId(BigDecimal curId) {
		this.curId = curId;
	}

	public BigDecimal getYear() {
		return this.year;
	}

	public void setYear(BigDecimal year) {
		this.year = year;
	}

	public BigDecimal getTerm() {
		return this.term;
	}

	public void setTerm(BigDecimal term) {
		this.term = term;
	}

	public BigDecimal getTimes() {
		return this.times;
	}

	public void setTimes(BigDecimal times) {
		this.times = times;
	}

	public BigDecimal getTblInnerValidateFlag() {
		return this.tblInnerValidateFlag;
	}

	public void setTblInnerValidateFlag(BigDecimal tblInnerValidateFlag) {
		this.tblInnerValidateFlag = tblInnerValidateFlag;
	}

	public BigDecimal getTblOuterValidateFlag() {
		return this.tblOuterValidateFlag;
	}

	public void setTblOuterValidateFlag(BigDecimal tblOuterValidateFlag) {
		this.tblOuterValidateFlag = tblOuterValidateFlag;
	}

	public BigDecimal getReportDataWarehouseFlag() {
		return this.reportDataWarehouseFlag;
	}

	public void setReportDataWarehouseFlag(BigDecimal reportDataWarehouseFlag) {
		this.reportDataWarehouseFlag = reportDataWarehouseFlag;
	}

	public String getRepName() {
		return this.repName;
	}

	public void setRepName(String repName) {
		this.repName = repName;
	}

	public BigDecimal getCheckFlag() {
		return this.checkFlag;
	}

	public void setCheckFlag(BigDecimal checkFlag) {
		this.checkFlag = checkFlag;
	}

	public BigDecimal getPackage_() {
		return this.package_;
	}

	public void setPackage_(BigDecimal package_) {
		this.package_ = package_;
	}

	public Date getReportDate() {
		return this.reportDate;
	}

	public void setReportDate(Date reportDate) {
		this.reportDate = reportDate;
	}

	public BigDecimal getAbmormityChangeFlag() {
		return this.abmormityChangeFlag;
	}

	public void setAbmormityChangeFlag(BigDecimal abmormityChangeFlag) {
		this.abmormityChangeFlag = abmormityChangeFlag;
	}

	public BigDecimal getRepRangeFlag() {
		return this.repRangeFlag;
	}

	public void setRepRangeFlag(BigDecimal repRangeFlag) {
		this.repRangeFlag = repRangeFlag;
	}

	public BigDecimal getForseReportAgainFlag() {
		return this.forseReportAgainFlag;
	}

	public void setForseReportAgainFlag(BigDecimal forseReportAgainFlag) {
		this.forseReportAgainFlag = forseReportAgainFlag;
	}

	public BigDecimal getLaterReportDay() {
		return this.laterReportDay;
	}

	public void setLaterReportDay(BigDecimal laterReportDay) {
		this.laterReportDay = laterReportDay;
	}

	public BigDecimal getNotReportFlag() {
		return this.notReportFlag;
	}

	public void setNotReportFlag(BigDecimal notReportFlag) {
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

	public BigDecimal getRecheckFlag() {
		return this.recheckFlag;
	}

	public void setRecheckFlag(BigDecimal recheckFlag) {
		this.recheckFlag = recheckFlag;
	}

	public String getTemplateId() {
		return this.templateId;
	}

	public void setTemplateId(String templateId) {
		this.templateId = templateId;
	}

	public BigDecimal getReReportTimes() {
		return this.reReportTimes;
	}

	public void setReReportTimes(BigDecimal reReportTimes) {
		this.reReportTimes = reReportTimes;
	}

	public BigDecimal getDay() {
		return this.day;
	}

	public void setDay(BigDecimal day) {
		this.day = day;
	}

	public String getRepDesc() {
		return this.repDesc;
	}

	public void setRepDesc(String repDesc) {
		this.repDesc = repDesc;
	}

}