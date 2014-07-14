package com.cbrc.smis.hibernate;

import java.io.Serializable;
import java.util.Date;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/** @author Hibernate CodeGenerator */
public class ReportAgainSet implements Serializable {

	 /** identifier field */
    private Integer rasId;

    /** identifier field */
    private Integer repInId;

    /** nullable persistent field */
    private String cause;

    /** nullable persistent field */
    private Date setDate;

    /** nullable persistent field */
    private com.cbrc.smis.hibernate.ReportIn reportIn;

    /** full constructor */
    public ReportAgainSet(Integer rasId,Integer repInId,String cause, Date setDate, com.cbrc.smis.hibernate.ReportIn reportIn) {
    	this.rasId=rasId;
    	this.repInId=repInId;
        this.cause = cause;
        this.setDate = setDate;
        this.reportIn = reportIn;
    }

    /** default constructor */
    public ReportAgainSet() {
    }

    /** minimal constructor */
    public ReportAgainSet(Integer rasId) {
    	this.rasId=rasId;
    }

    public String getCause() {
        return this.cause;
    }

    public void setCause(String cause) {
        this.cause = cause;
    }

    public Date getSetDate() {
        return this.setDate;
    }

    public void setSetDate(Date setDate) {
        this.setDate = setDate;
    }

    public com.cbrc.smis.hibernate.ReportIn getReportIn() {
        return this.reportIn;
    }

    public void setReportIn(com.cbrc.smis.hibernate.ReportIn reportIn) {
        this.reportIn = reportIn;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("rasId", getRasId())
            .append("repInId",getRepInId())
            .toString();
    }

    public boolean equals(Object other) {
        if ( !(other instanceof ReportAgainSet) ) return false;
        ReportAgainSet castOther = (ReportAgainSet) other;
        return new EqualsBuilder()
            .append(this.getRasId(), castOther.getRasId())
            .append(this.getRepInId(),castOther.getRepInId())
            .isEquals();
    }

    public int hashCode() {
        return new HashCodeBuilder()
            .append(this.getRasId())
            .toHashCode();
    }

	public Integer getRasId() {
		return rasId;
	}

	public void setRasId(Integer rasId) {
		this.rasId = rasId;
	}

	public Integer getRepInId() {
		return repInId;
	}

	public void setRepInId(Integer repInId) {
		this.repInId = repInId;
	}

}
