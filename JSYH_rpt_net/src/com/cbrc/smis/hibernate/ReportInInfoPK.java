package com.cbrc.smis.hibernate;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/** @author Hibernate CodeGenerator */
public class ReportInInfoPK implements Serializable {

    /** identifier field */
    private Integer cellId;

    /** identifier field */
    private Integer repInId;

    /** full constructor */
    public ReportInInfoPK(Integer cellId, Integer repInId) {
        this.cellId = cellId;
        this.repInId = repInId;
    }

    /** default constructor */
    public ReportInInfoPK() {
    }

    public Integer getCellId() {
        return this.cellId;
    }

    public void setCellId(Integer cellId) {
        this.cellId = cellId;
    }

    public Integer getRepInId() {
        return this.repInId;
    }

    public void setRepInId(Integer repInId) {
        this.repInId = repInId;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("cellId", getCellId())
            .append("repInId", getRepInId())
            .toString();
    }

    public boolean equals(Object other) {
        if ( !(other instanceof ReportInInfoPK) ) return false;
        ReportInInfoPK castOther = (ReportInInfoPK) other;
        return new EqualsBuilder()
            .append(this.getCellId(), castOther.getCellId())
            .append(this.getRepInId(), castOther.getRepInId())
            .isEquals();
    }

    public int hashCode() {
        return new HashCodeBuilder()
            .append(getCellId())
            .append(getRepInId())
            .toHashCode();
    }

}
