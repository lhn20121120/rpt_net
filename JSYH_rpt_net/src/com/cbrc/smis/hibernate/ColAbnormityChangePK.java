package com.cbrc.smis.hibernate;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/** @author Hibernate CodeGenerator */
public class ColAbnormityChangePK implements Serializable {

    /** identifier field */
    private String childRepId;

    /** identifier field */
    private String colName;

    /** identifier field */
    private String orgId;

    /** identifier field */
    private String versionId;

    /** full constructor */
    public ColAbnormityChangePK(String childRepId, String colName, String orgId, String versionId) {
        this.childRepId = childRepId;
        this.colName = colName;
        this.orgId = orgId;
        this.versionId = versionId;
    }

    /** default constructor */
    public ColAbnormityChangePK() {
    }

    public String getChildRepId() {
        return this.childRepId;
    }

    public void setChildRepId(String childRepId) {
        this.childRepId = childRepId;
    }

    public String getColName() {
        return this.colName;
    }

    public void setColName(String colName) {
        this.colName = colName;
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
            .append("colName", getColName())
            .append("orgId", getOrgId())
            .append("versionId", getVersionId())
            .toString();
    }

    public boolean equals(Object other) {
        if ( !(other instanceof ColAbnormityChangePK) ) return false;
        ColAbnormityChangePK castOther = (ColAbnormityChangePK) other;
        return new EqualsBuilder()
            .append(this.getChildRepId(), castOther.getChildRepId())
            .append(this.getColName(), castOther.getColName())
            .append(this.getOrgId(), castOther.getOrgId())
            .append(this.getVersionId(), castOther.getVersionId())
            .isEquals();
    }

    public int hashCode() {
        return new HashCodeBuilder()
            .append(getChildRepId())
            .append(getColName())
            .append(getOrgId())
            .append(getVersionId())
            .toHashCode();
    }

}
