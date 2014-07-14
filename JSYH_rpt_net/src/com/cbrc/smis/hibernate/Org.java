package com.cbrc.smis.hibernate;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/** @author Hibernate CodeGenerator */
public class Org implements Serializable {

    /** identifier field */
    private String orgId;

    /** nullable persistent field */
    private String orgName;

    /** nullable persistent field */
    private String orgType;

    /** nullable persistent field */
    private String isCorp;

    /** nullable persistent field */
    private String orgClsId;

    private String orgClsName;
    
    /** nullable persistent field */
    private String orgCode;

    private OrgActuType orgActuType;
    
    /** full constructor */
    public Org(String orgId, String orgName, String orgType, String isCorp, String orgClsId, String orgClsName,String orgCode,OrgActuType orgActuType) {
        this.orgId = orgId;
        this.orgName = orgName;
        this.orgType = orgType;
        this.isCorp = isCorp;
        this.orgClsId = orgClsId;
        this.orgClsName=orgClsName;
        this.orgCode = orgCode;
        this.orgActuType=orgActuType;
    }

    /** default constructor */
    public Org() {
    }

    /** minimal constructor */
    public Org(String orgId) {
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

    public String getOrgType() {
        return this.orgType;
    }

    public void setOrgType(String orgType) {
        this.orgType = orgType;
    }

    public String getIsCorp() {
        return this.isCorp;
    }

    public void setIsCorp(String isCorp) {
        this.isCorp = isCorp;
    }

    public String getOrgClsId() {
        return this.orgClsId;
    }

    public void setOrgClsId(String orgClsId) {
        this.orgClsId = orgClsId;
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
        if ( !(other instanceof Org) ) return false;
        Org castOther = (Org) other;
        return new EqualsBuilder()
            .append(this.getOrgId(), castOther.getOrgId())
            .isEquals();
    }

    public int hashCode() {
        return new HashCodeBuilder()
            .append(getOrgId())
            .toHashCode();
    }

	public OrgActuType getOrgActuType() {
		return this.orgActuType;
	}

	public void setOrgActuType(OrgActuType orgActuType) {
		this.orgActuType = orgActuType;
	}

	public String getOrgClsName() {
		return orgClsName;
	}

	public void setOrgClsName(String orgClsName) {
		this.orgClsName = orgClsName;
	}

}
