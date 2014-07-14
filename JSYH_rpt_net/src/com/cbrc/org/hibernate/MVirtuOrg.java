package com.cbrc.org.hibernate;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/** @author Hibernate CodeGenerator */
public class MVirtuOrg implements Serializable {

    /** identifier field */
    private String orgId;

    /** nullable persistent field */
    private String virtualOrgName;

    /** nullable persistent field */
    private String startDate;

    /** nullable persistent field */
    private String endDate;

    /** nullable persistent field */
    private String orgCode;

    /** nullable persistent field */
    private String virtuTypeId;

    /** full constructor */
    public MVirtuOrg(String orgId, String virtualOrgName, String startDate, String endDate, String orgCode, String virtuTypeId) {
        this.orgId = orgId;
        this.virtualOrgName = virtualOrgName;
        this.startDate = startDate;
        this.endDate = endDate;
        this.orgCode = orgCode;
        this.virtuTypeId = virtuTypeId;
    }

    /** default constructor */
    public MVirtuOrg() {
    }

    /** minimal constructor */
    public MVirtuOrg(String orgId) {
        this.orgId = orgId;
    }

    public String getOrgId() {
        return this.orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public String getVirtualOrgName() {
        return this.virtualOrgName;
    }

    public void setVirtualOrgName(String virtualOrgName) {
        this.virtualOrgName = virtualOrgName;
    }

    public String getStartDate() {
        return this.startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return this.endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getOrgCode() {
        return this.orgCode;
    }

    public void setOrgCode(String orgCode) {
        this.orgCode = orgCode;
    }

    public String getVirtuTypeId() {
        return this.virtuTypeId;
    }

    public void setVirtuTypeId(String virtuTypeId) {
        this.virtuTypeId = virtuTypeId;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("orgId", getOrgId())
            .toString();
    }

    public boolean equals(Object other) {
        if ( !(other instanceof MVirtuOrg) ) return false;
        MVirtuOrg castOther = (MVirtuOrg) other;
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
