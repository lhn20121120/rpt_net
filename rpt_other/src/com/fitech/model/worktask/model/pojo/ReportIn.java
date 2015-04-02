package com.fitech.model.worktask.model.pojo;

import java.math.BigDecimal;
import java.util.Date;

/**
 * ReportIn entity. @author MyEclipse Persistence Tools
 */

public class ReportIn implements java.io.Serializable {

	// Fields

	private Long repInId;
	private String childRepId;
	private Long dataRangeId;
	private Long term;
	private Long times;
	private String orgId;
	private Long year;
	private Integer tblOuterValidateFlag;
	private Integer reportDataWarehouseFlag;
	private Integer tblInnerValidateFlag;
	private String repName;
	private Integer checkFlag;
	private Long package_;
	private Long curId;
	private String versionId;
	private Date reportDate;
	private Integer abmormityChangeFlag;
	private Integer repRangeFlag;
	private Integer forseReportAgainFlag;
	private Integer laterReportDay;
	private Integer notReportFlag;
	private String writer;
	private String checker;
	private String principal;
	private String tblOuterInvalidateCause;
	private Long repOutId;
	private Date checkDate;
	private Date verifyDate;
	private BigDecimal recheckFlag;
	private String repDesc;

	// Constructors

	/** default constructor */
	public ReportIn() {
	}

	/** minimal constructor */
	public ReportIn(Long repInId) {
		this.repInId = repInId;
	}

	/** full constructor */
	public ReportIn(Long repInId, String childRepId, Long dataRangeId,
			Long term, Long times, String orgId, Long year,
			Integer tblOuterValidateFlag, Integer reportDataWarehouseFlag,
			Integer tblInnerValidateFlag, String repName, Integer checkFlag,
			Long package_, Long curId, String versionId, Date reportDate,
			Integer abmormityChangeFlag, Integer repRangeFlag,
			Integer forseReportAgainFlag, Integer laterReportDay,
			Integer notReportFlag, String writer, String checker,
			String principal, String tblOuterInvalidateCause, Long repOutId,
			Date checkDate, Date verifyDate, BigDecimal recheckFlag,
			String repDesc) {
		this.repInId = repInId;
		this.childRepId = childRepId;
		this.dataRangeId = dataRangeId;
		this.term = term;
		this.times = times;
		this.orgId = orgId;
		this.year = year;
		this.tblOuterValidateFlag = tblOuterValidateFlag;
		this.reportDataWarehouseFlag = reportDataWarehouseFlag;
		this.tblInnerValidateFlag = tblInnerValidateFlag;
		this.repName = repName;
		this.checkFlag = checkFlag;
		this.package_ = package_;
		this.curId = curId;
		this.versionId = versionId;
		this.reportDate = reportDate;
		this.abmormityChangeFlag = abmormityChangeFlag;
		this.repRangeFlag = repRangeFlag;
		this.forseReportAgainFlag = forseReportAgainFlag;
		this.laterReportDay = laterReportDay;
		this.notReportFlag = notReportFlag;
		this.writer = writer;
		this.checker = checker;
		this.principal = principal;
		this.tblOuterInvalidateCause = tblOuterInvalidateCause;
		this.repOutId = repOutId;
		this.checkDate = checkDate;
		this.verifyDate = verifyDate;
		this.recheckFlag = recheckFlag;
		this.repDesc = repDesc;
	}

	// Property accessors

	public Long getRepInId() {
		return this.repInId;
	}

	public void setRepInId(Long repInId) {
		this.repInId = repInId;
	}

	public String getChildRepId() {
		return this.childRepId;
	}

	public void setChildRepId(String childRepId) {
		this.childRepId = childRepId;
	}

	public Long getDataRangeId() {
		return this.dataRangeId;
	}

	public void setDataRangeId(Long dataRangeId) {
		this.dataRangeId = dataRangeId;
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

	public String getOrgId() {
		return this.orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

	public Long getYear() {
		return this.year;
	}

	public void setYear(Long year) {
		this.year = year;
	}

	public Integer getTblOuterValidateFlag() {
		return this.tblOuterValidateFlag;
	}

	public void setTblOuterValidateFlag(Integer tblOuterValidateFlag) {
		this.tblOuterValidateFlag = tblOuterValidateFlag;
	}

	public Integer getReportDataWarehouseFlag() {
		return this.reportDataWarehouseFlag;
	}

	public void setReportDataWarehouseFlag(Integer reportDataWarehouseFlag) {
		this.reportDataWarehouseFlag = reportDataWarehouseFlag;
	}

	public Integer getTblInnerValidateFlag() {
		return this.tblInnerValidateFlag;
	}

	public void setTblInnerValidateFlag(Integer tblInnerValidateFlag) {
		this.tblInnerValidateFlag = tblInnerValidateFlag;
	}

	public String getRepName() {
		return this.repName;
	}

	public void setRepName(String repName) {
		this.repName = repName;
	}

	public Integer getCheckFlag() {
		return this.checkFlag;
	}

	public void setCheckFlag(Integer checkFlag) {
		this.checkFlag = checkFlag;
	}

	public Long getPackage_() {
		return this.package_;
	}

	public void setPackage_(Long package_) {
		this.package_ = package_;
	}

	public Long getCurId() {
		return this.curId;
	}

	public void setCurId(Long curId) {
		this.curId = curId;
	}

	public String getVersionId() {
		return this.versionId;
	}

	public void setVersionId(String versionId) {
		this.versionId = versionId;
	}

	public Date getReportDate() {
		return this.reportDate;
	}

	public void setReportDate(Date reportDate) {
		this.reportDate = reportDate;
	}

	public Integer getAbmormityChangeFlag() {
		return this.abmormityChangeFlag;
	}

	public void setAbmormityChangeFlag(Integer abmormityChangeFlag) {
		this.abmormityChangeFlag = abmormityChangeFlag;
	}

	public Integer getRepRangeFlag() {
		return this.repRangeFlag;
	}

	public void setRepRangeFlag(Integer repRangeFlag) {
		this.repRangeFlag = repRangeFlag;
	}

	public Integer getForseReportAgainFlag() {
		return this.forseReportAgainFlag;
	}

	public void setForseReportAgainFlag(Integer forseReportAgainFlag) {
		this.forseReportAgainFlag = forseReportAgainFlag;
	}

	public Integer getLaterReportDay() {
		return this.laterReportDay;
	}

	public void setLaterReportDay(Integer laterReportDay) {
		this.laterReportDay = laterReportDay;
	}

	public Integer getNotReportFlag() {
		return this.notReportFlag;
	}

	public void setNotReportFlag(Integer notReportFlag) {
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

	public String getTblOuterInvalidateCause() {
		return this.tblOuterInvalidateCause;
	}

	public void setTblOuterInvalidateCause(String tblOuterInvalidateCause) {
		this.tblOuterInvalidateCause = tblOuterInvalidateCause;
	}

	public Long getRepOutId() {
		return this.repOutId;
	}

	public void setRepOutId(Long repOutId) {
		this.repOutId = repOutId;
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

	public BigDecimal getRecheckFlag() {
		return this.recheckFlag;
	}

	public void setRecheckFlag(BigDecimal recheckFlag) {
		this.recheckFlag = recheckFlag;
	}

	public String getRepDesc() {
		return this.repDesc;
	}

	public void setRepDesc(String repDesc) {
		this.repDesc = repDesc;
	}

}