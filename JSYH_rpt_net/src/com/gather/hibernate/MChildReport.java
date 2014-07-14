package com.gather.hibernate;

import java.io.Serializable;
import java.util.Set;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/** @author Hibernate CodeGenerator */
public class MChildReport implements Serializable {

    /** identifier field */
    private com.gather.hibernate.MChildReportPK comp_id;

    /** nullable persistent field */
    private String reportName;

    /** nullable persistent field */
    private java.util.Date startDate;

    /** nullable persistent field */
    private java.util.Date endDate;

    /** nullable persistent field */
    private String formatTempId;

    /** nullable persistent field */
    private com.gather.hibernate.ReportData reportData;

    /** persistent field */
    private com.gather.hibernate.MCurUnit MCurUnit;

    /** persistent field */
    private com.gather.hibernate.MMainRep MMainRep;

    /** persistent field */
    private Set MActuReps;

    /** persistent field */
    private Set MRepRanges;

    /** persistent field */
    private Set reports;

    /** persistent field */
    private Set repRangeDownLogs;

    /** full constructor */
    public MChildReport(com.gather.hibernate.MChildReportPK comp_id, String reportName, java.util.Date startDate, java.util.Date endDate, String formatTempId, com.gather.hibernate.ReportData reportData, com.gather.hibernate.MCurUnit MCurUnit, com.gather.hibernate.MMainRep MMainRep, Set MActuReps, Set MRepRanges, Set reports, Set repRangeDownLogs) {
        this.comp_id = comp_id;
        this.reportName = reportName;
        this.startDate = startDate;
        this.endDate = endDate;
        this.formatTempId = formatTempId;
        this.reportData = reportData;
        this.MCurUnit = MCurUnit;
        this.MMainRep = MMainRep;
        this.MActuReps = MActuReps;
        this.MRepRanges = MRepRanges;
        this.reports = reports;
        this.repRangeDownLogs = repRangeDownLogs;
    }

    /** default constructor */
    public MChildReport() {
    }

    /** minimal constructor */
    public MChildReport(com.gather.hibernate.MChildReportPK comp_id, com.gather.hibernate.MCurUnit MCurUnit, com.gather.hibernate.MMainRep MMainRep, Set MActuReps, Set MRepRanges, Set reports, Set repRangeDownLogs) {
        this.comp_id = comp_id;
        this.MCurUnit = MCurUnit;
        this.MMainRep = MMainRep;
        this.MActuReps = MActuReps;
        this.MRepRanges = MRepRanges;
        this.reports = reports;
        this.repRangeDownLogs = repRangeDownLogs;
    }

    public com.gather.hibernate.MChildReportPK getComp_id() {
        return this.comp_id;
    }

    public void setComp_id(com.gather.hibernate.MChildReportPK comp_id) {
        this.comp_id = comp_id;
    }

    public String getReportName() {
        return this.reportName;
    }

    public void setReportName(String reportName) {
        this.reportName = reportName;
    }

    public java.util.Date getStartDate() {
        return this.startDate;
    }

    public void setStartDate(java.util.Date startDate) {
        this.startDate = startDate;
    }

    public java.util.Date getEndDate() {
        return this.endDate;
    }

    public void setEndDate(java.util.Date endDate) {
        this.endDate = endDate;
    }

    public String getFormatTempId() {
        return this.formatTempId;
    }

    public void setFormatTempId(String formatTempId) {
        this.formatTempId = formatTempId;
    }

    public com.gather.hibernate.ReportData getReportData() {
        return this.reportData;
    }

    public void setReportData(com.gather.hibernate.ReportData reportData) {
        this.reportData = reportData;
    }

    public com.gather.hibernate.MCurUnit getMCurUnit() {
        return this.MCurUnit;
    }

    public void setMCurUnit(com.gather.hibernate.MCurUnit MCurUnit) {
        this.MCurUnit = MCurUnit;
    }

    public com.gather.hibernate.MMainRep getMMainRep() {
        return this.MMainRep;
    }

    public void setMMainRep(com.gather.hibernate.MMainRep MMainRep) {
        this.MMainRep = MMainRep;
    }

    public Set getMActuReps() {
        return this.MActuReps;
    }

    public void setMActuReps(Set MActuReps) {
        this.MActuReps = MActuReps;
    }

    public Set getMRepRanges() {
        return this.MRepRanges;
    }

    public void setMRepRanges(Set MRepRanges) {
        this.MRepRanges = MRepRanges;
    }

    public Set getReports() {
        return this.reports;
    }

    public void setReports(Set reports) {
        this.reports = reports;
    }

    public Set getRepRangeDownLogs() {
        return this.repRangeDownLogs;
    }

    public void setRepRangeDownLogs(Set repRangeDownLogs) {
        this.repRangeDownLogs = repRangeDownLogs;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("comp_id", getComp_id())
            .toString();
    }

    public boolean equals(Object other) {
        if ( !(other instanceof MChildReport) ) return false;
        MChildReport castOther = (MChildReport) other;
        return new EqualsBuilder()
            .append(this.getComp_id(), castOther.getComp_id())
            .isEquals();
    }

    public int hashCode() {
        return new HashCodeBuilder()
            .append(getComp_id())
            .toHashCode();
    }

}
