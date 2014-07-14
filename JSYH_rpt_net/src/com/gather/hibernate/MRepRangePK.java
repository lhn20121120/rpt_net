package com.gather.hibernate;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/** @author Hibernate CodeGenerator */
public class MRepRangePK implements Serializable {

    /** identifier field */
    private String childRepId;

    /** identifier field */
    private String orgId;
    
    private String versionId;

    /**
	 * @return Returns the versionId.
	 */
	public String getVersionId() {
		return this.versionId;
	}

	/**
	 * @param versionId The versionId to set.
	 */
	public void setVersionId(String versionId) {
		this.versionId = versionId;
	}

	/** full constructor */
    public MRepRangePK(String childRepId, String orgId,String versionId) {
        this.childRepId = childRepId;
        this.orgId = orgId;
        this.versionId=versionId;
    }

    /** default constructor */
    public MRepRangePK() {
    }

    public String getChildRepId() {
        return this.childRepId;
    }

    public void setChildRepId(String childRepId) {
        this.childRepId = childRepId;
    }

    public String getOrgId() {
        return this.orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("childRepId", getChildRepId())
            .append("orgId", getOrgId())
            .append("versionId", getVersionId())
            .toString();
    }

    public boolean equals(Object other) {
        if ( !(other instanceof MRepRangePK) ) return false;
        MRepRangePK castOther = (MRepRangePK) other;
        return new EqualsBuilder()
            .append(this.getChildRepId(), castOther.getChildRepId())
            .append(this.getOrgId(), castOther.getOrgId())
            .append(this.getVersionId(),castOther.getVersionId())
            .isEquals();
    }

    public int hashCode() {
        return new HashCodeBuilder()
            .append(getChildRepId())
            .append(getOrgId())
            .append(getVersionId())
            .toHashCode();
    }

}
