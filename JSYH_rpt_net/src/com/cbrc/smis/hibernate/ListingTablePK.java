package com.cbrc.smis.hibernate;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/** @author Hibernate CodeGenerator */
public class ListingTablePK implements Serializable {

    /** identifier field */
    private String childRepId;

    /** identifier field */
    private String versionId;

    /** identifier field */
    private String tableName;

    /**
     * @param childRepId
     * @param versionId
     * @param tableName
     */
    public ListingTablePK(String childRepId, String versionId, String tableName) {
        super();
        this.childRepId = childRepId;
        this.versionId = versionId;
        this.tableName = tableName;
    }

    /** default constructor */
    public ListingTablePK() {
    }

    /**
     * @return Returns the childRepId.
     */
    public String getChildRepId() {
        return childRepId;
    }

    /**
     * @param childRepId
     *            The childRepId to set.
     */
    public void setChildRepId(String childRepId) {
        this.childRepId = childRepId;
    }

    public String getVersionId() {
        return this.versionId;
    }

    public void setVersionId(String versionId) {
        this.versionId = versionId;
    }

    public String getTableName() {
        return this.tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String toString() {
        return new ToStringBuilder(this).append("childRepId", getChildRepId())
                .append("versionId", getVersionId()).append("tableName",
                        getTableName()).toString();
    }

    public boolean equals(Object other) {
        if (!(other instanceof ListingTablePK))
            return false;
        ListingTablePK castOther = (ListingTablePK) other;
        return new EqualsBuilder().append(this.getChildRepId(),
                castOther.getChildRepId()).append(this.getVersionId(),
                castOther.getVersionId()).append(this.getTableName(),
                castOther.getTableName()).isEquals();
    }

    public int hashCode() {
        return new HashCodeBuilder().append(getChildRepId()).append(
                getVersionId()).append(getTableName()).toHashCode();
    }

}