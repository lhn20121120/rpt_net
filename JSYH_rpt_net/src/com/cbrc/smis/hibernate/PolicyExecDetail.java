package com.cbrc.smis.hibernate;

import java.io.Serializable;
import java.util.Date;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/** @author Hibernate CodeGenerator */
public class PolicyExecDetail implements Serializable {

    /** identifier field */
    private com.cbrc.smis.hibernate.PolicyExecDetailPK comp_id;

    /** nullable persistent field */
    private Date execTime;

    /** nullable persistent field */
    private com.cbrc.smis.hibernate.Policy policy;

    /** nullable persistent field */
    private com.cbrc.smis.hibernate.ReportIn reportIn;

    /** full constructor */
    public PolicyExecDetail(com.cbrc.smis.hibernate.PolicyExecDetailPK comp_id, Date execTime, com.cbrc.smis.hibernate.Policy policy, com.cbrc.smis.hibernate.ReportIn reportIn) {
        this.comp_id = comp_id;
        this.execTime = execTime;
        this.policy = policy;
        this.reportIn = reportIn;
    }

    /** default constructor */
    public PolicyExecDetail() {
    }

    /** minimal constructor */
    public PolicyExecDetail(com.cbrc.smis.hibernate.PolicyExecDetailPK comp_id) {
        this.comp_id = comp_id;
    }

    public com.cbrc.smis.hibernate.PolicyExecDetailPK getComp_id() {
        return this.comp_id;
    }

    public void setComp_id(com.cbrc.smis.hibernate.PolicyExecDetailPK comp_id) {
        this.comp_id = comp_id;
    }

    public Date getExecTime() {
        return this.execTime;
    }

    public void setExecTime(Date execTime) {
        this.execTime = execTime;
    }

    public com.cbrc.smis.hibernate.Policy getPolicy() {
        return this.policy;
    }

    public void setPolicy(com.cbrc.smis.hibernate.Policy policy) {
        this.policy = policy;
    }

    public com.cbrc.smis.hibernate.ReportIn getReportIn() {
        return this.reportIn;
    }

    public void setReportIn(com.cbrc.smis.hibernate.ReportIn reportIn) {
        this.reportIn = reportIn;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("comp_id", getComp_id())
            .toString();
    }

    public boolean equals(Object other) {
        if ( !(other instanceof PolicyExecDetail) ) return false;
        PolicyExecDetail castOther = (PolicyExecDetail) other;
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
