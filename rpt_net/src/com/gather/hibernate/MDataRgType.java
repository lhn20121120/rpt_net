package com.gather.hibernate;

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
    private Set reports;

    /** persistent field */
    private Set MRepdescs;

    /** full constructor */
    public MDataRgType(Integer dataRangeId, String dataRgDesc, Set MActuReps, Set reports, Set MRepdescs) {
        this.dataRangeId = dataRangeId;
        this.dataRgDesc = dataRgDesc;
        this.MActuReps = MActuReps;
        this.reports = reports;
        this.MRepdescs = MRepdescs;
    }

    /** default constructor */
    public MDataRgType() {
    }

    /** minimal constructor */
    public MDataRgType(Integer dataRangeId, Set MActuReps, Set reports, Set MRepdescs) {
        this.dataRangeId = dataRangeId;
        this.MActuReps = MActuReps;
        this.reports = reports;
        this.MRepdescs = MRepdescs;
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

    public Set getReports() {
        return this.reports;
    }

    public void setReports(Set reports) {
        this.reports = reports;
    }

    public Set getMRepdescs() {
        return this.MRepdescs;
    }

    public void setMRepdescs(Set MRepdescs) {
        this.MRepdescs = MRepdescs;
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
