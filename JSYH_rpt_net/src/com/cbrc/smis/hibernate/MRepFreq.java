package com.cbrc.smis.hibernate;

import java.io.Serializable;
import java.util.Set;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/** @author Hibernate CodeGenerator */
public class MRepFreq implements Serializable {

    /** identifier field */
    private Integer repFreqId;

    /** nullable persistent field */
    private String repFreqName;

    /** persistent field */
    private Set MActuReps;

    /** full constructor */
    public MRepFreq(Integer repFreqId, String repFreqName, Set MActuReps) {
        this.repFreqId = repFreqId;
        this.repFreqName = repFreqName;
        this.MActuReps = MActuReps;
    }

    /** default constructor */
    public MRepFreq() {
    }

    /** minimal constructor */
    public MRepFreq(Integer repFreqId, Set MActuReps) {
        this.repFreqId = repFreqId;
        this.MActuReps = MActuReps;
    }

    public Integer getRepFreqId() {
        return this.repFreqId;
    }

    public void setRepFreqId(Integer repFreqId) {
        this.repFreqId = repFreqId;
    }

    public String getRepFreqName() {
        return this.repFreqName;
    }

    public void setRepFreqName(String repFreqName) {
        this.repFreqName = repFreqName;
    }

    public Set getMActuReps() {
        return this.MActuReps;
    }

    public void setMActuReps(Set MActuReps) {
        this.MActuReps = MActuReps;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("repFreqId", getRepFreqId())
            .toString();
    }

    public boolean equals(Object other) {
        if ( !(other instanceof MRepFreq) ) return false;
        MRepFreq castOther = (MRepFreq) other;
        return new EqualsBuilder()
            .append(this.getRepFreqId(), castOther.getRepFreqId())
            .isEquals();
    }

    public int hashCode() {
        return new HashCodeBuilder()
            .append(getRepFreqId())
            .toHashCode();
    }

}
