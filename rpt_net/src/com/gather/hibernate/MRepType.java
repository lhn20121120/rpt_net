package com.gather.hibernate;

import java.io.Serializable;
import java.util.Set;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/** @author Hibernate CodeGenerator */
public class MRepType implements Serializable {

    /** identifier field */
    private Integer repTypeId;

    /** nullable persistent field */
    private String repTypeName;

    /** persistent field */
    private Set MMainReps;

    /** full constructor */
    public MRepType(Integer repTypeId, String repTypeName, Set MMainReps) {
        this.repTypeId = repTypeId;
        this.repTypeName = repTypeName;
        this.MMainReps = MMainReps;
    }

    /** default constructor */
    public MRepType() {
    }

    /** minimal constructor */
    public MRepType(Integer repTypeId, Set MMainReps) {
        this.repTypeId = repTypeId;
        this.MMainReps = MMainReps;
    }

    public Integer getRepTypeId() {
        return this.repTypeId;
    }

    public void setRepTypeId(Integer repTypeId) {
        this.repTypeId = repTypeId;
    }

    public String getRepTypeName() {
        return this.repTypeName;
    }

    public void setRepTypeName(String repTypeName) {
        this.repTypeName = repTypeName;
    }

    public Set getMMainReps() {
        return this.MMainReps;
    }

    public void setMMainReps(Set MMainReps) {
        this.MMainReps = MMainReps;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("repTypeId", getRepTypeId())
            .toString();
    }

    public boolean equals(Object other) {
        if ( !(other instanceof MRepType) ) return false;
        MRepType castOther = (MRepType) other;
        return new EqualsBuilder()
            .append(this.getRepTypeId(), castOther.getRepTypeId())
            .isEquals();
    }

    public int hashCode() {
        return new HashCodeBuilder()
            .append(getRepTypeId())
            .toHashCode();
    }

}
