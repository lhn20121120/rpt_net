package com.gather.hibernate;

import java.io.Serializable;
import java.util.Set;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/** @author Hibernate CodeGenerator */
public class MMainRep implements Serializable {

    /** identifier field */
    private Integer repId;

    /** nullable persistent field */
    private String repCnName;

    /** nullable persistent field */
    private String repEnName;

    /** persistent field */
    private com.gather.hibernate.MCurUnit MCurUnit;

    /** persistent field */
    private com.gather.hibernate.MRepType MRepType;

    /** persistent field */
    private Set MChildReports;

    /** persistent field */
    private Set MRepdescs;

    /** full constructor */
    public MMainRep(Integer repId, String repCnName, String repEnName, com.gather.hibernate.MCurUnit MCurUnit, com.gather.hibernate.MRepType MRepType, Set MChildReports, Set MRepdescs) {
        this.repId = repId;
        this.repCnName = repCnName;
        this.repEnName = repEnName;
        this.MCurUnit = MCurUnit;
        this.MRepType = MRepType;
        this.MChildReports = MChildReports;
        this.MRepdescs = MRepdescs;
    }

    /** default constructor */
    public MMainRep() {
    }

    /** minimal constructor */
    public MMainRep(Integer repId, com.gather.hibernate.MCurUnit MCurUnit, com.gather.hibernate.MRepType MRepType, Set MChildReports, Set MRepdescs) {
        this.repId = repId;
        this.MCurUnit = MCurUnit;
        this.MRepType = MRepType;
        this.MChildReports = MChildReports;
        this.MRepdescs = MRepdescs;
    }

    public Integer getRepId() {
        return this.repId;
    }

    public void setRepId(Integer repId) {
        this.repId = repId;
    }

    public String getRepCnName() {
        return this.repCnName;
    }

    public void setRepCnName(String repCnName) {
        this.repCnName = repCnName;
    }

    public String getRepEnName() {
        return this.repEnName;
    }

    public void setRepEnName(String repEnName) {
        this.repEnName = repEnName;
    }

    public com.gather.hibernate.MCurUnit getMCurUnit() {
        return this.MCurUnit;
    }

    public void setMCurUnit(com.gather.hibernate.MCurUnit MCurUnit) {
        this.MCurUnit = MCurUnit;
    }

    public com.gather.hibernate.MRepType getMRepType() {
        return this.MRepType;
    }

    public void setMRepType(com.gather.hibernate.MRepType MRepType) {
        this.MRepType = MRepType;
    }

    public Set getMChildReports() {
        return this.MChildReports;
    }

    public void setMChildReports(Set MChildReports) {
        this.MChildReports = MChildReports;
    }

    public Set getMRepdescs() {
        return this.MRepdescs;
    }

    public void setMRepdescs(Set MRepdescs) {
        this.MRepdescs = MRepdescs;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("repId", getRepId())
            .toString();
    }

    public boolean equals(Object other) {
        if ( !(other instanceof MMainRep) ) return false;
        MMainRep castOther = (MMainRep) other;
        return new EqualsBuilder()
            .append(this.getRepId(), castOther.getRepId())
            .isEquals();
    }

    public int hashCode() {
        return new HashCodeBuilder()
            .append(getRepId())
            .toHashCode();
    }

}
