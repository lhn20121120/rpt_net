package com.cbrc.org.hibernate;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/** @author Hibernate CodeGenerator */
public class MSupvOrg implements Serializable {

    /** identifier field */
    private String orgId;

    /** nullable persistent field */
    private String supvOrgName;

    /** nullable persistent field */
    private String priorOrgId;

    /** nullable persistent field */
    private String orgCode;

    /** full constructor */
    public MSupvOrg(String orgId, String supvOrgName, String priorOrgId, String orgCode) {
        this.orgId = orgId;
        this.supvOrgName = supvOrgName;
        this.priorOrgId = priorOrgId;
        this.orgCode = orgCode;
    }

    /** default constructor */
    public MSupvOrg() {
    }

    /** minimal constructor */
    public MSupvOrg(String orgId) {
        this.orgId = orgId;
    }

    public String getOrgId() {
        return this.orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public String getSupvOrgName() {
        return this.supvOrgName;
    }

    public void setSupvOrgName(String supvOrgName) {
        this.supvOrgName = supvOrgName;
    }

    public String getPriorOrgId() {
        return this.priorOrgId;
    }

    public void setPriorOrgId(String priorOrgId) {
        this.priorOrgId = priorOrgId;
    }

    public String getOrgCode() {
        return this.orgCode;
    }

    public void setOrgCode(String orgCode) {
        this.orgCode = orgCode;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("orgId", getOrgId())
            .toString();
    }

    public boolean equals(Object other) {
        if ( !(other instanceof MSupvOrg) ) return false;
        MSupvOrg castOther = (MSupvOrg) other;
        return new EqualsBuilder()
            .append(this.getOrgId(), castOther.getOrgId())
            .isEquals();
    }

    public int hashCode() {
        return new HashCodeBuilder()
            .append(getOrgId())
            .toHashCode();
    }

}
