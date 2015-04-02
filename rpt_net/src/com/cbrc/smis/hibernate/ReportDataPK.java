package com.cbrc.smis.hibernate;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/** @author Hibernate CodeGenerator */
public class ReportDataPK implements Serializable {

    /** identifier field */
    private String childRepId;

    /** identifier field */
    private String versionId;

    /** full constructor */
    public ReportDataPK(String childRepId, String versionId) {
        this.childRepId = childRepId;
        this.versionId = versionId;
    }

    /** default constructor */
    public ReportDataPK() {
    }

    public String getChildRepId() {
        return this.childRepId;
    }

    public void setChildRepId(String childRepId) {
        this.childRepId = childRepId;
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
            .append("versionId", getVersionId())
            .toString();
    }

    public boolean equals(Object other) {
        if ( !(other instanceof ReportDataPK) ) return false;
        ReportDataPK castOther = (ReportDataPK) other;
        return new EqualsBuilder()
            .append(this.getChildRepId(), castOther.getChildRepId())
            .append(this.getVersionId(), castOther.getVersionId())
            .isEquals();
    }

    public int hashCode() {
        return new HashCodeBuilder()
            .append(getChildRepId())
            .append(getVersionId())
            .toHashCode();
    }

}
