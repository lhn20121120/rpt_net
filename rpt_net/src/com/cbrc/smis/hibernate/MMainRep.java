package com.cbrc.smis.hibernate;

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
    private com.cbrc.smis.hibernate.MCurUnit MCurUnit;

    /** persistent field */
    private com.cbrc.smis.hibernate.MRepType MRepType;

    /** persistent field */
    private Set MChildReports;

    /** full constructor */
    public MMainRep(Integer repId, String repCnName, String repEnName, com.cbrc.smis.hibernate.MCurUnit MCurUnit, com.cbrc.smis.hibernate.MRepType MRepType, Set MChildReports) {
        this.repId = repId;
        this.repCnName = repCnName;
        this.repEnName = repEnName;
        this.MCurUnit = MCurUnit;
        this.MRepType = MRepType;
        this.MChildReports = MChildReports;
    }

    /** default constructor */
    public MMainRep() {
    }

    /** minimal constructor */
    public MMainRep(Integer repId, com.cbrc.smis.hibernate.MCurUnit MCurUnit, com.cbrc.smis.hibernate.MRepType MRepType, Set MChildReports) {
        this.repId = repId;
        this.MCurUnit = MCurUnit;
        this.MRepType = MRepType;
        this.MChildReports = MChildReports;
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

    public com.cbrc.smis.hibernate.MCurUnit getMCurUnit() {
        return this.MCurUnit;
    }

    public void setMCurUnit(com.cbrc.smis.hibernate.MCurUnit MCurUnit) {
        this.MCurUnit = MCurUnit;
    }

    public com.cbrc.smis.hibernate.MRepType getMRepType() {
        return this.MRepType;
    }

    public void setMRepType(com.cbrc.smis.hibernate.MRepType MRepType) {
        this.MRepType = MRepType;
    }

    public Set getMChildReports() {
        return this.MChildReports;
    }

    public void setMChildReports(Set MChildReports) {
        this.MChildReports = MChildReports;
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
