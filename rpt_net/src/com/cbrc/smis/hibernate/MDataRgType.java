package com.cbrc.smis.hibernate;

import java.io.Serializable;
import java.util.Set;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/** @author Hibernate CodeGenerator */
public class MDataRgType implements Serializable {

    /** identifier field */
    private Integer dataRangeId;

    /** nullable persistent field */
    private String dataRgDesc;

    /** persistent field */
    private Set MActuReps;

    /** persistent field */
    private Set reportIns;

    /** full constructor */
    public MDataRgType(Integer dataRangeId, String dataRgDesc, Set MActuReps, Set reportIns) {
        this.dataRangeId = dataRangeId;
        this.dataRgDesc = dataRgDesc;
        this.MActuReps = MActuReps;
        this.reportIns = reportIns;
    }

    /** default constructor */
    public MDataRgType() {
    }

    /** default constructor */
    public MDataRgType(Integer dataRangeId) {
    	this.dataRangeId = dataRangeId;
    }

    
    /** minimal constructor */
    public MDataRgType(Integer dataRangeId, Set MActuReps, Set reportIns) {
        this.dataRangeId = dataRangeId;
        this.MActuReps = MActuReps;
        this.reportIns = reportIns;
    }

    public Integer getDataRangeId() {
        return this.dataRangeId;
    }

    public void setDataRangeId(Integer dataRangeId) {
        this.dataRangeId = dataRangeId;
    }

    public String getDataRgDesc() {
        return this.dataRgDesc;
    }

    public void setDataRgDesc(String dataRgDesc) {
        this.dataRgDesc = dataRgDesc;
    }

    public Set getMActuReps() {
        return this.MActuReps;
    }

    public void setMActuReps(Set MActuReps) {
        this.MActuReps = MActuReps;
    }

    public Set getReportIns() {
        return this.reportIns;
    }

    public void setReportIns(Set reportIns) {
        this.reportIns = reportIns;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("dataRangeId", getDataRangeId())
            .toString();
    }

    public boolean equals(Object other) {
        if ( !(other instanceof MDataRgType) ) return false;
        MDataRgType castOther = (MDataRgType) other;
        return new EqualsBuilder()
            .append(this.getDataRangeId(), castOther.getDataRangeId())
            .isEquals();
    }

    public int hashCode() {
        return new HashCodeBuilder()
            .append(getDataRangeId())
            .toHashCode();
    }

}
