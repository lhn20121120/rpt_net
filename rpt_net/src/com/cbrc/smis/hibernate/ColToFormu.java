package com.cbrc.smis.hibernate;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/** @author Hibernate CodeGenerator */
public class ColToFormu implements Serializable {

    /** identifier field */
    private com.cbrc.smis.hibernate.ColToFormuPK comp_id;

    /** nullable persistent field */
    private com.cbrc.smis.hibernate.MCellFormu MCellFormu;

    /** nullable persistent field */
    private com.cbrc.smis.hibernate.MChildReport MChildReport;

    /** full constructor */
    public ColToFormu(com.cbrc.smis.hibernate.ColToFormuPK comp_id, com.cbrc.smis.hibernate.MCellFormu MCellFormu, com.cbrc.smis.hibernate.MChildReport MChildReport) {
        this.comp_id = comp_id;
        this.MCellFormu = MCellFormu;
        this.MChildReport = MChildReport;
    }

    /** default constructor */
    public ColToFormu() {
    }

    /** minimal constructor */
    public ColToFormu(com.cbrc.smis.hibernate.ColToFormuPK comp_id) {
        this.comp_id = comp_id;
    }

    public com.cbrc.smis.hibernate.ColToFormuPK getComp_id() {
        return this.comp_id;
    }

    public void setComp_id(com.cbrc.smis.hibernate.ColToFormuPK comp_id) {
        this.comp_id = comp_id;
    }

    public com.cbrc.smis.hibernate.MCellFormu getMCellFormu() {
        return this.MCellFormu;
    }

    public void setMCellFormu(com.cbrc.smis.hibernate.MCellFormu MCellFormu) {
        this.MCellFormu = MCellFormu;
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
        if ( !(other instanceof ColToFormu) ) return false;
        ColToFormu castOther = (ColToFormu) other;
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
