package com.gather.hibernate;

import java.io.Serializable;
import java.util.Set;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/** @author Hibernate CodeGenerator */
public class MCurUnit implements Serializable {

    /** identifier field */
    private Integer curUnit;

    /** nullable persistent field */
    private String curUnitName;

    /** persistent field */
    private Set MChildReports;

    /** persistent field */
    private Set MMainReps;

    /** full constructor */
    public MCurUnit(Integer curUnit, String curUnitName, Set MChildReports, Set MMainReps) {
        this.curUnit = curUnit;
        this.curUnitName = curUnitName;
        this.MChildReports = MChildReports;
        this.MMainReps = MMainReps;
    }

    /** default constructor */
    public MCurUnit() {
    }

    /** minimal constructor */
    public MCurUnit(Integer curUnit, Set MChildReports, Set MMainReps) {
        this.curUnit = curUnit;
        this.MChildReports = MChildReports;
        this.MMainReps = MMainReps;
    }

    public Integer getCurUnit() {
        return this.curUnit;
    }

    public void setCurUnit(Integer curUnit) {
        this.curUnit = curUnit;
    }

    public String getCurUnitName() {
        return this.curUnitName;
    }

    public void setCurUnitName(String curUnitName) {
        this.curUnitName = curUnitName;
    }

    public Set getMChildReports() {
        return this.MChildReports;
    }

    public void setMChildReports(Set MChildReports) {
        this.MChildReports = MChildReports;
    }

    public Set getMMainReps() {
        return this.MMainReps;
    }

    public void setMMainReps(Set MMainReps) {
        this.MMainReps = MMainReps;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("curUnit", getCurUnit())
            .toString();
    }

    public boolean equals(Object other) {
        if ( !(other instanceof MCurUnit) ) return false;
        MCurUnit castOther = (MCurUnit) other;
        return new EqualsBuilder()
            .append(this.getCurUnit(), castOther.getCurUnit())
            .isEquals();
    }

    public int hashCode() {
        return new HashCodeBuilder()
            .append(getCurUnit())
            .toHashCode();
    }

}
