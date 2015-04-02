package com.cbrc.smis.hibernate;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/** @author Hibernate CodeGenerator */
public class AbnormityChangePK implements Serializable {

    /** identifier field */
    private String childRepId;

    /** identifier field */
    private Integer cellId;

    /** identifier field */
    private String orgId;

    /** identifier field */
    private String versionId;

    /** full constructor */
    public AbnormityChangePK(String childRepId, Integer cellId, String orgId, String versionId) {
        this.childRepId = childRepId;
        this.cellId = cellId;
        this.orgId = orgId;
        this.versionId = versionId;
    }

    /** default constructor */
    public AbnormityChangePK() {
    }

    public String getChildRepId() {
        return this.childRepId;
    }

    public void setChildRepId(String childRepId) {
        this.childRepId = childRepId;
    }

    public Integer getCellId() {
        return this.cellId;
    }

    public void setCellId(Integer cellId) {
        this.cellId = cellId;
    }

    public String getOrgId() {
        return this.orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public String getVersionId() {
        return this.versionId;
    }

    public void setVersionId(String versionId) {
        this.versionId = versionId;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("childRepId", getChildRepId())
            .append("cellId", getCellId())
            .append("orgId", getOrgId())
            .append("versionId", getVersionId())
            .toString();
    }

    public boolean equals(Object other) {
        if ( !(other instanceof AbnormityChangePK) ) return false;
        AbnormityChangePK castOther = (AbnormityChangePK) other;
        return new EqualsBuilder()
            .append(this.getChildRepId(), castOther.getChildRepId())
            .append(this.getCellId(), castOther.getCellId())
            .append(this.getOrgId(), castOther.getOrgId())
            .append(this.getVersionId(), castOther.getVersionId())
            .isEquals();
    }

    public int hashCode() {
        return new HashCodeBuilder()
            .append(getChildRepId())
            .append(getCellId())
            .append(getOrgId())
            .append(getVersionId())
            .toHashCode();
    }

}
