package com.cbrc.smis.hibernate;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/** @author Hibernate CodeGenerator */
public class ColToFormuPK implements Serializable {

    /** identifier field */
    private Integer cellFormuId;

    /** identifier field */
    private Integer childRepId;

    /** identifier field */
    private String versionId;

    /** identifier field */
    private String colName;

    /** full constructor */
    public ColToFormuPK(Integer cellFormuId, Integer childRepId, String versionId, String colName) {
        this.cellFormuId = cellFormuId;
        this.childRepId = childRepId;
        this.versionId = versionId;
        this.colName = colName;
    }

    /** default constructor */
    public ColToFormuPK() {
    }

    public Integer getCellFormuId() {
        return this.cellFormuId;
    }

    public void setCellFormuId(Integer cellFormuId) {
        this.cellFormuId = cellFormuId;
    }

    public Integer getChildRepId() {
        return this.childRepId;
    }

    public void setChildRepId(Integer childRepId) {
        this.childRepId = childRepId;
    }

    public String getVersionId() {
        return this.versionId;
    }

    public void setVersionId(String versionId) {
        this.versionId = versionId;
    }

    public String getColName() {
        return this.colName;
    }

    public void setColName(String colName) {
        this.colName = colName;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("cellFormuId", getCellFormuId())
            .append("childRepId", getChildRepId())
            .append("versionId", getVersionId())
            .append("colName", getColName())
            .toString();
    }

    public boolean equals(Object other) {
        if ( !(other instanceof ColToFormuPK) ) return false;
        ColToFormuPK castOther = (ColToFormuPK) other;
        return new EqualsBuilder()
            .append(this.getCellFormuId(), castOther.getCellFormuId())
            .append(this.getChildRepId(), castOther.getChildRepId())
            .append(this.getVersionId(), castOther.getVersionId())
            .append(this.getColName(), castOther.getColName())
            .isEquals();
    }

    public int hashCode() {
        return new HashCodeBuilder()
            .append(getCellFormuId())
            .append(getChildRepId())
            .append(getVersionId())
            .append(getColName())
            .toHashCode();
    }

}
