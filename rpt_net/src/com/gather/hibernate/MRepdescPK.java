package com.gather.hibernate;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/** @author Hibernate CodeGenerator */
public class MRepdescPK implements Serializable {

    /** identifier field */
    private String reportdate;

    /** identifier field */
    private Integer datarangeid;

    /** identifier field */
    private Integer repid;

    /** identifier field */
    private Integer orgid;

    /** full constructor */
    public MRepdescPK(String reportdate, Integer datarangeid, Integer repid, Integer orgid) {
        this.reportdate = reportdate;
        this.datarangeid = datarangeid;
        this.repid = repid;
        this.orgid = orgid;
    }

    /** default constructor */
    public MRepdescPK() {
    }

    public String getReportdate() {
        return this.reportdate;
    }

    public void setReportdate(String reportdate) {
        this.reportdate = reportdate;
    }

    public Integer getDatarangeid() {
        return this.datarangeid;
    }

    public void setDatarangeid(Integer datarangeid) {
        this.datarangeid = datarangeid;
    }

    public Integer getRepid() {
        return this.repid;
    }

    public void setRepid(Integer repid) {
        this.repid = repid;
    }

    public Integer getOrgid() {
        return this.orgid;
    }

    public void setOrgid(Integer orgid) {
        this.orgid = orgid;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("reportdate", getReportdate())
            .append("datarangeid", getDatarangeid())
            .append("repid", getRepid())
            .append("orgid", getOrgid())
            .toString();
    }

    public boolean equals(Object other) {
        if ( !(other instanceof MRepdescPK) ) return false;
        MRepdescPK castOther = (MRepdescPK) other;
        return new EqualsBuilder()
            .append(this.getReportdate(), castOther.getReportdate())
            .append(this.getDatarangeid(), castOther.getDatarangeid())
            .append(this.getRepid(), castOther.getRepid())
            .append(this.getOrgid(), castOther.getOrgid())
            .isEquals();
    }

    public int hashCode() {
        return new HashCodeBuilder()
            .append(getReportdate())
            .append(getDatarangeid())
            .append(getRepid())
            .append(getOrgid())
            .toHashCode();
    }

}
