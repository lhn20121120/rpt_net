package com.cbrc.smis.hibernate;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/** @author Hibernate CodeGenerator */
public class ReportIn implements Serializable {

    /** identifier field */
    private Integer repInId;

    private String orgId;
    
    public String orgName;
    
    /** nullable persistent field */
    private Integer term;

    /** nullable persistent field */
    private Integer times;

    /** nullable persistent field */
    private Integer year;

    /** nullable persistent field */
    private Short tblOuterValidateFlag;

    /** nullable persistent field */
    private Short reportDataWarehouseFlag;

    /** nullable persistent field */
    private Short tblInnerValidateFlag;

    /** nullable persistent field */
    private String repName;

    /** nullable persistent field */
    private Short checkFlag;

    /** nullable persistent field */
    private Integer _package;//借用字段（汇总方式ID）

    /** nullable persistent field */
    private Date reportDate;

    /** nullable persistent field */
    private Short abmormityChangeFlag;

    /** nullable persistent field */
    private Short repRangeFlag;

    /** nullable persistent field */
    private Short forseReportAgainFlag;

    /** nullable persistent field */
    private Short laterReportDay;

    /** nullable persistent field */
    private Short notReportFlag;

    /** nullable persistent field */
    private String writer;

    /** nullable persistent field */
    private String checker;

    /** nullable persistent field */
    private String principal;

    /** nullable persistent field */
    private String tblOuterInvalidateCause;
    
    private Integer  repOutId;

	private Date checkDate;
	private Date verifyDate;
	private Integer recheckFlag;
	private String repDesc;
    public ReportIn(Integer repInId, String orgId, String orgName,
			Integer term, Integer times, Integer year,
			Short tblOuterValidateFlag, Short reportDataWarehouseFlag,
			Short tblInnerValidateFlag, String repName, Short checkFlag,
			Integer _package, Date reportDate, Short abmormityChangeFlag,
			Short repRangeFlag, Short forseReportAgainFlag,
			Short laterReportDay, Short notReportFlag, String writer,
			String checker, String principal, String tblOuterInvalidateCause,
			Integer repOutId, Date checkDate, Date verifyDate,
			Integer recheckFlag, String repDesc, MDataRgType dataRgType,
			MCurr curr, MRepRange repRange, MChildReport childReport,
			Set reportInInfos, Set policyExecDetails, Set reportAgainSets,
			Set dataValidateInfos) {
		super();
		this.repInId = repInId;
		this.orgId = orgId;
		this.orgName = orgName;
		this.term = term;
		this.times = times;
		this.year = year;
		this.tblOuterValidateFlag = tblOuterValidateFlag;
		this.reportDataWarehouseFlag = reportDataWarehouseFlag;
		this.tblInnerValidateFlag = tblInnerValidateFlag;
		this.repName = repName;
		this.checkFlag = checkFlag;
		this._package = _package;
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
		MDataRgType = dataRgType;
		MCurr = curr;
		MRepRange = repRange;
		MChildReport = childReport;
		this.reportInInfos = reportInInfos;
		this.policyExecDetails = policyExecDetails;
		this.reportAgainSets = reportAgainSets;
		this.dataValidateInfos = dataValidateInfos;
	}

	public String getRepDesc() {
		return repDesc;
	}

	public void setRepDesc(String repDesc) {
		this.repDesc = repDesc;
	}

	/**
     * @param repInId
     * @param orgId
     * @param term
     * @param times
     * @param year
     * @param tblOuterValidateFlag
     * @param reportDataWarehouseFlag
     * @param tblInnerValidateFlag
     * @param repName
     * @param checkFlag
     * @param _package
     * @param reportDate
     * @param abmormityChangeFlag
     * @param repRangeFlag
     * @param forseReportAgainFlag
     * @param laterReportDay
     * @param notReportFlag
     * @param writer
     * @param checker
     * @param principal
     * @param tblOuterInvalidateCause
     * @param repOutId
     * @param dataRgType
     * @param curr
     * @param repRange
     * @param childReport
     * @param reportInInfos
     * @param policyExecDetails
     * @param reportAgainSets
     * @param dataValidateInfos
     */
    public ReportIn(Integer repInId, String orgId, Integer term, Integer times,
            Integer year, Short tblOuterValidateFlag,
            Short reportDataWarehouseFlag, Short tblInnerValidateFlag,
            String repName, Short checkFlag, Integer _package, Date reportDate,
            Short abmormityChangeFlag, Short repRangeFlag,
            Short forseReportAgainFlag, Short laterReportDay,
            Short notReportFlag, String writer, String checker,
            String principal, String tblOuterInvalidateCause, Integer repOutId,
            Date checkDate,Date verifyDate,Integer recheckFlag,
            com.cbrc.smis.hibernate.MDataRgType dataRgType,
            com.cbrc.smis.hibernate.MCurr curr,
            com.cbrc.smis.hibernate.MRepRange repRange,
            com.cbrc.smis.hibernate.MChildReport childReport,
            Set reportInInfos, Set policyExecDetails, Set reportAgainSets,
            Set dataValidateInfos) {
        super();
        this.repInId = repInId;
        this.orgId = orgId;
        this.term = term;
        this.times = times;
        this.year = year;
        this.tblOuterValidateFlag = tblOuterValidateFlag;
        this.reportDataWarehouseFlag = reportDataWarehouseFlag;
        this.tblInnerValidateFlag = tblInnerValidateFlag;
        this.repName = repName;
        this.checkFlag = checkFlag;
        this._package = _package;
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
        MDataRgType = dataRgType;
        MCurr = curr;
        MRepRange = repRange;
        MChildReport = childReport;
        this.reportInInfos = reportInInfos;
        this.policyExecDetails = policyExecDetails;
        this.reportAgainSets = reportAgainSets;
        this.dataValidateInfos = dataValidateInfos;
        this.checkDate = checkDate;
        this.verifyDate = verifyDate;
        this.recheckFlag = recheckFlag;
    }
    /** persistent field */
    private com.cbrc.smis.hibernate.MDataRgType MDataRgType = new MDataRgType();

    /** persistent field */
    private com.cbrc.smis.hibernate.MCurr MCurr = new MCurr();

    /** persistent field */
    private com.cbrc.smis.hibernate.MRepRange MRepRange = new MRepRange();

    /** persistent field */
    private com.cbrc.smis.hibernate.MChildReport MChildReport =new MChildReport();

    /** persistent field */
    private Set reportInInfos = new HashSet();

    /** persistent field */
    private Set policyExecDetails;

    /** persistent field */
    private Set reportAgainSets;

    /** persistent field */
    private Set dataValidateInfos;

    /** full constructor */
    public ReportIn(Integer repInId, String orgId,Integer term, Integer times, Integer year, Short tblOuterValidateFlag, Short reportDataWarehouseFlag, Short tblInnerValidateFlag, String repName, Short checkFlag, Integer paramPackage, Date reportDate, Short abmormityChangeFlag, Short repRangeFlag, Short forseReportAgainFlag, Short laterReportDay, Short notReportFlag, String writer, String checker, String principal, String tblOuterInvalidateCause, com.cbrc.smis.hibernate.MDataRgType MDataRgType, com.cbrc.smis.hibernate.MCurr MCurr, com.cbrc.smis.hibernate.MRepRange MRepRange, com.cbrc.smis.hibernate.MChildReport MChildReport, Set reportInInfos, Set policyExecDetails, Set reportAgainSets, Set dataValidateInfos,
    		Date checkDate,Date verifyDate,Integer recheckFlag) {
        this.repInId = repInId;
        this.orgId=orgId;
        this.term = term;
        this.times = times;
        this.year = year;
        this.tblOuterValidateFlag = tblOuterValidateFlag;
        this.reportDataWarehouseFlag = reportDataWarehouseFlag;
        this.tblInnerValidateFlag = tblInnerValidateFlag;
        this.repName = repName;
        this.checkFlag = checkFlag;
        this._package = paramPackage;
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
        this.MDataRgType = MDataRgType;
        this.MCurr = MCurr;
        this.MRepRange = MRepRange;
        this.MChildReport = MChildReport;
        this.reportInInfos = reportInInfos;
        this.policyExecDetails = policyExecDetails;
        this.reportAgainSets = reportAgainSets;
        this.dataValidateInfos = dataValidateInfos;
        this.checkDate = checkDate;
        this.verifyDate = verifyDate;
        this.recheckFlag = recheckFlag;
    }

    /** default constructor */
    public ReportIn() {
    }

    /** minimal constructor */
    public ReportIn(Integer repInId, String orgId,com.cbrc.smis.hibernate.MDataRgType MDataRgType, com.cbrc.smis.hibernate.MCurr MCurr, com.cbrc.smis.hibernate.MRepRange MRepRange, com.cbrc.smis.hibernate.MChildReport MChildReport, Set reportInInfos, Set policyExecDetails, Set reportAgainSets, Set dataValidateInfos) {
        this.repInId = repInId;
        this.orgId=orgId;
        this.MDataRgType = MDataRgType;
        this.MCurr = MCurr;
        this.MRepRange = MRepRange;
        this.MChildReport = MChildReport;
        this.reportInInfos = reportInInfos;
        this.policyExecDetails = policyExecDetails;
        this.reportAgainSets = reportAgainSets;
        this.dataValidateInfos = dataValidateInfos;
    }

    public Integer getRepInId() {
        return this.repInId;
    }

    public void setRepInId(Integer repInId) {
        this.repInId = repInId;
    }

    public Integer getTerm() {
        return this.term;
    }

    public void setTerm(Integer term) {
        this.term = term;
    }

    public Integer getTimes() {
        return this.times;
    }

    public void setTimes(Integer times) {
        this.times = times;
    }

    public Integer getYear() {
        return this.year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Short getTblOuterValidateFlag() {
        return this.tblOuterValidateFlag;
    }

    public void setTblOuterValidateFlag(Short tblOuterValidateFlag) {
        this.tblOuterValidateFlag = tblOuterValidateFlag;
    }

    public Short getReportDataWarehouseFlag() {
        return this.reportDataWarehouseFlag;
    }

    public void setReportDataWarehouseFlag(Short reportDataWarehouseFlag) {
        this.reportDataWarehouseFlag = reportDataWarehouseFlag;
    }

    public Short getTblInnerValidateFlag() {
        return this.tblInnerValidateFlag;
    }

    public void setTblInnerValidateFlag(Short tblInnerValidateFlag) {
        this.tblInnerValidateFlag = tblInnerValidateFlag;
    }

    public String getRepName() {
        return this.repName;
    }

    public void setRepName(String repName) {
        this.repName = repName;
    }

    public Short getCheckFlag() {
        return this.checkFlag;
    }

    public void setCheckFlag(Short checkFlag) {
    			this.checkFlag = checkFlag;
    }

    public Integer getPackage() {
        return this._package;
    }

    public void setPackage(Integer paramPackage) {
        this._package = paramPackage;
    }

    public Date getReportDate() {
        return this.reportDate;
    }

    public void setReportDate(Date reportDate) {
        this.reportDate = reportDate;
    }

    public Short getAbmormityChangeFlag() {
        return this.abmormityChangeFlag;
    }

    public void setAbmormityChangeFlag(Short abmormityChangeFlag) {
        this.abmormityChangeFlag = abmormityChangeFlag;
    }

    public Short getRepRangeFlag() {
        return this.repRangeFlag;
    }

    public void setRepRangeFlag(Short repRangeFlag) {
        this.repRangeFlag = repRangeFlag;
    }

    public Short getForseReportAgainFlag() {
        return this.forseReportAgainFlag;
    }

    public void setForseReportAgainFlag(Short forseReportAgainFlag) {
        this.forseReportAgainFlag = forseReportAgainFlag;
    }

    public Short getLaterReportDay() {
        return this.laterReportDay;
    }

    public void setLaterReportDay(Short laterReportDay) {
        this.laterReportDay = laterReportDay;
    }

    public Short getNotReportFlag() {
        return this.notReportFlag;
    }

    public void setNotReportFlag(Short notReportFlag) {
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

    public com.cbrc.smis.hibernate.MDataRgType getMDataRgType() {
        return this.MDataRgType;
    }

    public void setMDataRgType(com.cbrc.smis.hibernate.MDataRgType MDataRgType) {
        this.MDataRgType = MDataRgType;
    }

    public com.cbrc.smis.hibernate.MCurr getMCurr() {
        return this.MCurr;
    }

    public void setMCurr(com.cbrc.smis.hibernate.MCurr MCurr) {
        this.MCurr = MCurr;
    }

    public com.cbrc.smis.hibernate.MRepRange getMRepRange() {
        return this.MRepRange;
    }

    public void setMRepRange(com.cbrc.smis.hibernate.MRepRange MRepRange) {
        this.MRepRange = MRepRange;
    }

    public com.cbrc.smis.hibernate.MChildReport getMChildReport() {
        return this.MChildReport;
    }

    public void setMChildReport(com.cbrc.smis.hibernate.MChildReport MChildReport) {
        this.MChildReport = MChildReport;
    }

    public Set getReportInInfos() {
        return this.reportInInfos;
    }

    public void setReportInInfos(Set reportInInfos) {
        this.reportInInfos = reportInInfos;
    }

    public Set getPolicyExecDetails() {
        return this.policyExecDetails;
    }

    public void setPolicyExecDetails(Set policyExecDetails) {
        this.policyExecDetails = policyExecDetails;
    }

    public Set getReportAgainSets() {
        return this.reportAgainSets;
    }

    public void setReportAgainSets(Set reportAgainSets) {
        this.reportAgainSets = reportAgainSets;
    }

    public Set getDataValidateInfos() {
        return this.dataValidateInfos;
    }

    public void setDataValidateInfos(Set dataValidateInfos) {
        this.dataValidateInfos = dataValidateInfos;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("repInId", getRepInId())
            .toString();
    }

    public boolean equals(Object other) {
        if ( !(other instanceof ReportIn) ) return false;
        ReportIn castOther = (ReportIn) other;
        return new EqualsBuilder()
            .append(this.getRepInId(), castOther.getRepInId())
            .isEquals();
    }

    public int hashCode() {
        return new HashCodeBuilder()
            .append(getRepInId())
            .toHashCode();
    }

	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

    /**
     * @return Returns the _package.
     */
    public Integer get_package() {
        return _package;
    }
    /**
     * @param _package The _package to set.
     */
    public void set_package(Integer _package) {
        this._package = _package;
    }
    /**
     * @return Returns the repOutId.
     */
    public Integer getRepOutId() {
        return repOutId;
    }
    /**
     * @param repOutId The repOutId to set.
     */
    public void setRepOutId(Integer repOutId) {
        this.repOutId = repOutId;
    }

	public Date getCheckDate() {
		return checkDate;
	}

	public void setCheckDate(Date checkDate) {
		this.checkDate = checkDate;
	}

	public Date getVerifyDate() {
		return verifyDate;
	}

	public void setVerifyDate(Date verifyDate) {
		this.verifyDate = verifyDate;
	}

	public Integer getRecheckFlag() {
		return recheckFlag;
	}

	public void setRecheckFlag(Integer recheckFlag) {
		this.recheckFlag = recheckFlag;
	}

}
