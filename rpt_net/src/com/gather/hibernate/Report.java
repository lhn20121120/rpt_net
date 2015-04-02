package com.gather.hibernate;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/** @author Hibernate CodeGenerator */
public class Report implements Serializable {

    /** identifier field */
    private Integer repId;
    

    /** nullable persistent field */
    private Integer term;

    /** persistent field */
    private Integer times;

    /** nullable persistent field */
    private Integer tblOuterValidateFlag;

    /** nullable persistent field */
    private Integer reportDataWarehouseFlag;

    /** nullable persistent field */
    private Integer repRangeFlag;

    /** nullable persistent field */
    private Integer abmormityChangeFlag;

    /** nullable persistent field */
    private Integer tblInnerValidateFlag;

    /** persistent field */
    private String repName;

    /** nullable persistent field */
    private Integer checkFlag;

    /** nullable persistent field */
    private Integer pNumber;

    /** nullable persistent field */
    private String reportFlag;

    /** nullable persistent field */
    private Integer fileFlag;

    /** nullable persistent field */
    private String laterReportDay;

    /** nullable persistent field */
    private String writer;

    /** nullable persistent field */
    private Date reportDate;

    /** nullable persistent field */
    private Integer notReportFlag;

    /** nullable persistent field */
    private String checker;

    /** nullable persistent field */
    private String principal;

    /** nullable persistent field */
    private Integer year;
    
    private Integer frequency;

    /** persistent field */
    private com.gather.hibernate.MDataRgType MDataRgType;

    /** persistent field */
    private com.gather.hibernate.MOrg MOrg;

    /** persistent field */
    private com.gather.hibernate.MCurr MCurr;

    /** persistent field */
    private com.gather.hibernate.MChildReport MChildReport;

    /** persistent field */
    private Set dataValidateInfos;
    
    private String orgclsid;

    /** full constructor */
    public Report(Integer repId, Integer term, Integer times, Integer tblOuterValidateFlag, Integer reportDataWarehouseFlag, Integer repRangeFlag, Integer abmormityChangeFlag, Integer tblInnerValidateFlag, String repName, Integer checkFlag, Integer pNumber, String reportFlag, Integer fileFlag, String laterReportDay, String writer, Date reportDate, Integer notReportFlag, String checker, String principal, Integer year, com.gather.hibernate.MDataRgType MDataRgType, com.gather.hibernate.MOrg MOrg, com.gather.hibernate.MCurr MCurr, com.gather.hibernate.MChildReport MChildReport, Set dataValidateInfos) {
        this.repId = repId;
        this.term = term;
        this.times = times;
        this.tblOuterValidateFlag = tblOuterValidateFlag;
        this.reportDataWarehouseFlag = reportDataWarehouseFlag;
        this.repRangeFlag = repRangeFlag;
        this.abmormityChangeFlag = abmormityChangeFlag;
        this.tblInnerValidateFlag = tblInnerValidateFlag;
        this.repName = repName;
        this.checkFlag = checkFlag;
        this.pNumber = pNumber;
        this.reportFlag = reportFlag;
        this.fileFlag = fileFlag;
        this.laterReportDay = laterReportDay;
        this.writer = writer;
        this.reportDate = reportDate;
        this.notReportFlag = notReportFlag;
        this.checker = checker;
        this.principal = principal;
        this.year = year;
        this.MDataRgType = MDataRgType;
        this.MOrg = MOrg;
        this.MCurr = MCurr;
        this.MChildReport = MChildReport;
        this.dataValidateInfos = dataValidateInfos;
    }

    /** default constructor */
    public Report() {
    }

    /** minimal constructor */
    public Report(Integer repId, Integer times, String repName, com.gather.hibernate.MDataRgType MDataRgType, com.gather.hibernate.MOrg MOrg, com.gather.hibernate.MCurr MCurr, com.gather.hibernate.MChildReport MChildReport, Set dataValidateInfos) {
        this.repId = repId;
        this.times = times;
        this.repName = repName;
        this.MDataRgType = MDataRgType;
        this.MOrg = MOrg;
        this.MCurr = MCurr;
        this.MChildReport = MChildReport;
        this.dataValidateInfos = dataValidateInfos;
    }

    public Integer getRepId() {
        return this.repId;
    }

    public void setRepId(Integer repId) {
        this.repId = repId;
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

    public Integer getRepRangeFlag() {
        return this.repRangeFlag;
    }

    public void setRepRangeFlag(Integer repRangeFlag) {
        this.repRangeFlag = repRangeFlag;
    }

    public Integer getAbmormityChangeFlag() {
        return this.abmormityChangeFlag;
    }

    public void setAbmormityChangeFlag(Integer abmormityChangeFlag) {
        this.abmormityChangeFlag = abmormityChangeFlag;
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
    
    public Integer getPNumber() {
        return this.pNumber;
    }
    
    public void setPNumber(Integer pNumber) {
        this.pNumber = pNumber;
    }

    public String getReportFlag() {
        return this.reportFlag;
    }

    public void setReportFlag(String reportFlag) {
        this.reportFlag = reportFlag;
    }

    public Integer getFileFlag() {
        return this.fileFlag;
    }

    public void setFileFlag(Integer fileFlag) {
        this.fileFlag = fileFlag;
    }

    public String getLaterReportDay() {
        return this.laterReportDay;
    }

    public void setLaterReportDay(String laterReportDay) {
        this.laterReportDay = laterReportDay;
    }

    public String getWriter() {
        return this.writer;
    }

    public void setWriter(String writer) {
        this.writer = writer;
    }

    public Date getReportDate() {
        return this.reportDate;
    }

    public void setReportDate(Date reportDate) {
        this.reportDate = reportDate;
    }

    public Integer getNotReportFlag() {
        return this.notReportFlag;
    }

    public void setNotReportFlag(Integer notReportFlag) {
        this.notReportFlag = notReportFlag;
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

    public Integer getYear() {
        return this.year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }
    
    public Integer getFrequency() {
        return this.frequency;
    }

    public void setFrequency(Integer frequency) {
        this.frequency = frequency;
    }

    public com.gather.hibernate.MDataRgType getMDataRgType() {
        return this.MDataRgType;
    }

    public void setMDataRgType(com.gather.hibernate.MDataRgType MDataRgType) {
        this.MDataRgType = MDataRgType;
    }

    public com.gather.hibernate.MOrg getMOrg() {
        return this.MOrg;
    }

    public void setMOrg(com.gather.hibernate.MOrg MOrg) {
        this.MOrg = MOrg;
    }

    public com.gather.hibernate.MCurr getMCurr() {
        return this.MCurr;
    }

    public void setMCurr(com.gather.hibernate.MCurr MCurr) {
        this.MCurr = MCurr;
    }

    public com.gather.hibernate.MChildReport getMChildReport() {
        return this.MChildReport;
    }

    public void setMChildReport(com.gather.hibernate.MChildReport MChildReport) {
        this.MChildReport = MChildReport;
    }

    public Set getDataValidateInfos() {
        return this.dataValidateInfos;
    }

    public void setDataValidateInfos(Set dataValidateInfos) {
        this.dataValidateInfos = dataValidateInfos;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("repId", getRepId())
            .toString();
    }

    public boolean equals(Object other) {
        if ( !(other instanceof Report) ) return false;
        Report castOther = (Report) other;
        return new EqualsBuilder()
            .append(this.getRepId(), castOther.getRepId())
            .isEquals();
    }

    public int hashCode() {
        return new HashCodeBuilder()
            .append(getRepId())
            .toHashCode();
    }

	public String getOrgclsid() {
		return orgclsid;
	}

	public void setOrgclsid(String orgclsid) {
		this.orgclsid = orgclsid;
	}
    
    

}
