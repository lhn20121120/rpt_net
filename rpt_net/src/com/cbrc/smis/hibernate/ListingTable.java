package com.cbrc.smis.hibernate;

import java.io.Serializable;
import java.util.Date;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/** @author Hibernate CodeGenerator */
public class ListingTable implements Serializable {

    /** identifier field */
    private com.cbrc.smis.hibernate.ListingTablePK comp_id;

    /** nullable persistent field */
    private Date createTime;

    /** nullable persistent field */
    private com.cbrc.smis.hibernate.MChildReport MChildReport;

    /** full constructor */
    public ListingTable(com.cbrc.smis.hibernate.ListingTablePK comp_id, Date createTime, com.cbrc.smis.hibernate.MChildReport MChildReport) {
        this.comp_id = comp_id;
        this.createTime = createTime;
        this.MChildReport = MChildReport;
    }

    /** default constructor */
    public ListingTable() {
    }

    /** minimal constructor */
    public ListingTable(com.cbrc.smis.hibernate.ListingTablePK comp_id) {
        this.comp_id = comp_id;
    }

    public com.cbrc.smis.hibernate.ListingTablePK getComp_id() {
        return this.comp_id;
    }

    public void setComp_id(com.cbrc.smis.hibernate.ListingTablePK comp_id) {
        this.comp_id = comp_id;
    }

    public Date getCreateTime() {
        return this.createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public com.cbrc.smis.hibernate.MChildReport getMChildReport() {
        return this.MChildReport;
    }

    public void setMChildReport(com.cbrc.smis.hibernate.MChildReport MChildReport) {
        this.MChildReport = MChildReport;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("comp_id", getComp_id())
            .toString();
    }

    public boolean equals(Object other) {
        if ( !(other instanceof ListingTable) ) return false;
        ListingTable castOther = (ListingTable) other;
        return new EqualsBuilder()
            .append(this.getComp_id(), castOther.getComp_id())
            .isEquals();
    }

    public int hashCode() {
        return new HashCodeBuilder()
            .append(getComp_id())
            .toHashCode();
    }

}
