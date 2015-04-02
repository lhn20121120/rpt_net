package com.gather.hibernate;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/** @author Hibernate CodeGenerator */
public class MActuRep implements Serializable {

    /** identifier field */
    private com.gather.hibernate.MActuRepPK comp_id;

    /** nullable persistent field */
    private Integer delayTime;

    /** nullable persistent field */
    private Integer normalTime;

    /** nullable persistent field */
    private com.gather.hibernate.MDataRgType MDataRgType;

    /** nullable persistent field */
    private com.gather.hibernate.MRepFreq MRepFreq;

    /** nullable persistent field */
    private com.gather.hibernate.MChildReport MChildReport;

    /** full constructor */
    public MActuRep(com.gather.hibernate.MActuRepPK comp_id, Integer delayTime, Integer normalTime, com.gather.hibernate.MDataRgType MDataRgType, com.gather.hibernate.MRepFreq MRepFreq, com.gather.hibernate.MChildReport MChildReport) {
        this.comp_id = comp_id;
        this.delayTime = delayTime;
        this.normalTime = normalTime;
        this.MDataRgType = MDataRgType;
        this.MRepFreq = MRepFreq;
        this.MChildReport = MChildReport;
    }

    /** default constructor */
    public MActuRep() {
    }

    /** minimal constructor */
    public MActuRep(com.gather.hibernate.MActuRepPK comp_id) {
        this.comp_id = comp_id;
    }

    public com.gather.hibernate.MActuRepPK getComp_id() {
        return this.comp_id;
    }

    public void setComp_id(com.gather.hibernate.MActuRepPK comp_id) {
        this.comp_id = comp_id;
    }

    public Integer getDelayTime() {
        return this.delayTime;
    }

    public void setDelayTime(Integer delayTime) {
        this.delayTime = delayTime;
    }

    public Integer getNormalTime() {
        return this.normalTime;
    }

    public void setNormalTime(Integer normalTime) {
        this.normalTime = normalTime;
    }

    public com.gather.hibernate.MDataRgType getMDataRgType() {
        return this.MDataRgType;
    }

    public void setMDataRgType(com.gather.hibernate.MDataRgType MDataRgType) {
        this.MDataRgType = MDataRgType;
    }

    public com.gather.hibernate.MRepFreq getMRepFreq() {
        return this.MRepFreq;
    }

    public void setMRepFreq(com.gather.hibernate.MRepFreq MRepFreq) {
        this.MRepFreq = MRepFreq;
    }

    public com.gather.hibernate.MChildReport getMChildReport() {
        return this.MChildReport;
    }

    public void setMChildReport(com.gather.hibernate.MChildReport MChildReport) {
        this.MChildReport = MChildReport;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("comp_id", getComp_id())
            .toString();
    }

    public boolean equals(Object other) {
        if ( !(other instanceof MActuRep) ) return false;
        MActuRep castOther = (MActuRep) other;
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
