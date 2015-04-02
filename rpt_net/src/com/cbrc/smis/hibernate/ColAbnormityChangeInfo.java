package com.cbrc.smis.hibernate;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/** @author Hibernate CodeGenerator */
public class ColAbnormityChangeInfo implements Serializable {

    /** identifier field */
    private com.cbrc.smis.hibernate.ColAbnormityChangeInfoPK comp_id;

    /** nullable persistent field */
    private String reportValue;

    /** nullable persistent field */
    private Float thanPrevRise;

    /** nullable persistent field */
    private Float thanSameRise;

    /** nullable persistent field */
    private Float thanSameFall;

    /** nullable persistent field */
    private Float thanPrevFall;

    /** nullable persistent field */
    private com.cbrc.smis.hibernate.ReportIn reportIn;

    /** full constructor */
    public ColAbnormityChangeInfo(com.cbrc.smis.hibernate.ColAbnormityChangeInfoPK comp_id, String reportValue, Float thanPrevRise, Float thanSameRise, Float thanSameFall, Float thanPrevFall, com.cbrc.smis.hibernate.ReportIn reportIn) {
        this.comp_id = comp_id;
        this.reportValue = reportValue;
        this.thanPrevRise = thanPrevRise;
        this.thanSameRise = thanSameRise;
        this.thanSameFall = thanSameFall;
        this.thanPrevFall = thanPrevFall;
        this.reportIn = reportIn;
    }

    /** default constructor */
    public ColAbnormityChangeInfo() {
    }

    /** minimal constructor */
    public ColAbnormityChangeInfo(com.cbrc.smis.hibernate.ColAbnormityChangeInfoPK comp_id) {
        this.comp_id = comp_id;
    }

    public com.cbrc.smis.hibernate.ColAbnormityChangeInfoPK getComp_id() {
        return this.comp_id;
    }

    public void setComp_id(com.cbrc.smis.hibernate.ColAbnormityChangeInfoPK comp_id) {
        this.comp_id = comp_id;
    }

    public String getReportValue() {
        return this.reportValue;
    }

    public void setReportValue(String reportValue) {
        this.reportValue = reportValue;
    }

    public Float getThanPrevRise() {
        return this.thanPrevRise;
    }

    public void setThanPrevRise(Float thanPrevRise) {
        this.thanPrevRise = thanPrevRise;
    }

    public Float getThanSameRise() {
        return this.thanSameRise;
    }

    public void setThanSameRise(Float thanSameRise) {
        this.thanSameRise = thanSameRise;
    }

    public Float getThanSameFall() {
        return this.thanSameFall;
    }

    public void setThanSameFall(Float thanSameFall) {
        this.thanSameFall = thanSameFall;
    }

    public Float getThanPrevFall() {
        return this.thanPrevFall;
    }

    public void setThanPrevFall(Float thanPrevFall) {
        this.thanPrevFall = thanPrevFall;
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
        if ( !(other instanceof ColAbnormityChangeInfo) ) return false;
        ColAbnormityChangeInfo castOther = (ColAbnormityChangeInfo) other;
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
