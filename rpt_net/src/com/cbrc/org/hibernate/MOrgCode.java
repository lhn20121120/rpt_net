package com.cbrc.org.hibernate;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/** @author Hibernate CodeGenerator */
public class MOrgCode implements Serializable {

    /** identifier field */
    private String orgCode;

    /** persistent field */
    private String regionId;

    /** persistent field */
    private String orgId;

    /** nullable persistent field */
    private String orgName;

    /** full constructor */
    public MOrgCode(String orgCode, String regionId, String orgId, String orgName) {
        this.orgCode = orgCode;
        this.regionId = regionId;
        this.orgId = orgId;
        this.orgName = orgName;
    }

    /** default constructor */
    public MOrgCode() {
    }

    /** minimal constructor */
    public MOrgCode(String orgCode, String regionId, String orgId) {
        this.orgCode = orgCode;
        this.regionId = regionId;
        this.orgId = orgId;
    }

    public String getOrgCode() {
        return this.orgCode;
    }

    public void setOrgCode(String orgCode) {
        this.orgCode = orgCode;
    }

    public String getRegionId() {
        return this.regionId;
    }

    public void setRegionId(String regionId) {
        this.regionId = regionId;
    }

    public String getOrgId() {
        return this.orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public String getOrgName() {
        return this.orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("orgCode", getOrgCode())
            .toString();
    }

    public boolean equals(Object other) {
        if ( !(other instanceof MOrgCode) ) return false;
        MOrgCode castOther = (MOrgCode) other;
        return new EqualsBuilder()
            .append(this.getOrgCode(), castOther.getOrgCode())
            .isEquals();
    }

    public int hashCode() {
        return new HashCodeBuilder()
            .append(getOrgCode())
            .toHashCode();
    }

}
