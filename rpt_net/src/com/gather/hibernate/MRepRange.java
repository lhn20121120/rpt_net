package com.gather.hibernate;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/** @author Hibernate CodeGenerator */
public class MRepRange implements Serializable {

    /** identifier field */
    private com.gather.hibernate.MRepRangePK comp_id;

    /** nullable persistent field */
    private com.gather.hibernate.MOrg MOrg;

    /** persistent field */
    private com.gather.hibernate.MChildReport MChildReport;

    /** full constructor */
    public MRepRange(com.gather.hibernate.MRepRangePK comp_id, com.gather.hibernate.MOrg MOrg, com.gather.hibernate.MChildReport MChildReport) {
        this.comp_id = comp_id;
        this.MOrg = MOrg;
        this.MChildReport = MChildReport;
    }

    /** default constructor */
    public MRepRange() {
    }

    /** minimal constructor */
    public MRepRange(com.gather.hibernate.MRepRangePK comp_id, com.gather.hibernate.MChildReport MChildReport) {
        this.comp_id = comp_id;
        this.MChildReport = MChildReport;
    }

    public com.gather.hibernate.MRepRangePK getComp_id() {
        return this.comp_id;
    }

    public void setComp_id(com.gather.hibernate.MRepRangePK comp_id) {
        this.comp_id = comp_id;
    }

    public com.gather.hibernate.MOrg getMOrg() {
        return this.MOrg;
    }

    public void setMOrg(com.gather.hibernate.MOrg MOrg) {
        this.MOrg = MOrg;
    }

    public com.gather.hibernate.MChildReport getMChildReport() {
        return this.MChildReport;
    }

    public void setMChildReport(com.gather.hibernate.MChildReport MChildReport) {
        this.MChildReport = MChildReport;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("comp_id", getComp_id())
            .toString();
    }

    public boolean equals(Object other) {
        if ( !(other instanceof MRepRange) ) return false;
        MRepRange castOther = (MRepRange) other;
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
