package com.gather.hibernate;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/** @author Hibernate CodeGenerator */
public class RepRangeDownLogPK implements Serializable {

    /** identifier field */
    private String orgId;

    /** identifier field */
    private String childRepId;
    
    private java.lang.String versionId;

    /** full constructor */
    public RepRangeDownLogPK(String orgId, String childRepId,String versionId) {
        this.orgId = orgId;
        this.childRepId = childRepId;
        this.versionId=versionId;
    }

    /** default constructor */
    public RepRangeDownLogPK() {
    }

    public String getOrgId() {
        return this.orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public String getChildRepId() {
        return this.childRepId;
    }

    public void setChildRepId(String childRepId) {
        this.childRepId = childRepId;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("orgId", getOrgId())
            .append("childRepId", getChildRepId())
            .append("versionId",getVersionId())
            .toString();
    }

    public boolean equals(Object other) {
        if ( !(other instanceof RepRangeDownLogPK) ) return false;
        RepRangeDownLogPK castOther = (RepRangeDownLogPK) other;
        return new EqualsBuilder()
            .append(this.getOrgId(), castOther.getOrgId())
            .append(this.getChildRepId(), castOther.getChildRepId())
            .append(this.getVersionId(),castOther.getVersionId())
            .isEquals();
    }

    public int hashCode() {
        return new HashCodeBuilder()
            .append(getOrgId())
            .append(getChildRepId())
            .append(getVersionId())
            .toHashCode();
    }

	/**
	 * @return Returns the versionId.
	 */
	public java.lang.String getVersionId() {
		return versionId;
	}

	/**
	 * @param versionId The versionId to set.
	 */
	public void setVersionId(java.lang.String versionId) {
		this.versionId = versionId;
	}

}
