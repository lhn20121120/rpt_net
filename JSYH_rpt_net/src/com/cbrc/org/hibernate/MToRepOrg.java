package com.cbrc.org.hibernate;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/** @author Hibernate CodeGenerator */
public class MToRepOrg implements Serializable {

    /** identifier field */
    private String orgId;

    /** nullable persistent field */
    private String orgName;

    /** nullable persistent field */
    private String startDate;

    /** nullable persistent field */
    private String endDate;

    /** nullable persistent field */
    private String licence;

    /** nullable persistent field */
    private String orgCode;

    /** nullable persistent field */
    private String orgClsId;

    /** full constructor */
    public MToRepOrg(String orgId, String orgName, String startDate, String endDate, String licence, String orgCode, String orgClsId) {
        this.orgId = orgId;
        this.orgName = orgName;
        this.startDate = startDate;
        this.endDate = endDate;
        this.licence = licence;
        this.orgCode = orgCode;
        this.orgClsId = orgClsId;
    }

    /** default constructor */
    public MToRepOrg() {
    }

    /** minimal constructor */
    public MToRepOrg(String orgId) {
        this.orgId = orgId;
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

    public String getLicence() {
        return this.licence;
    }

    public void setLicence(String licence) {
        this.licence = licence;
    }

    public String getOrgCode() {
        return this.orgCode;
    }

    public void setOrgCode(String orgCode) {
        this.orgCode = orgCode;
    }

    public String getOrgClsId() {
        return this.orgClsId;
    }

    public void setOrgClsId(String orgClsId) {
        this.orgClsId = orgClsId;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("orgId", getOrgId())
            .toString();
    }

    public boolean equals(Object other) {
        if ( !(other instanceof MToRepOrg) ) return false;
        MToRepOrg castOther = (MToRepOrg) other;
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
