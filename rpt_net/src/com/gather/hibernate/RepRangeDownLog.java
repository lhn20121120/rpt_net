package com.gather.hibernate;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/** @author Hibernate CodeGenerator */
public class RepRangeDownLog implements Serializable {

    /** identifier field */
    private com.gather.hibernate.RepRangeDownLogPK comp_id;
    

    /** nullable persistent field */
    private Integer state;

    /** nullable persistent field */
    private com.gather.hibernate.MOrg MOrg;

    /** persistent field */
    private com.gather.hibernate.MChildReport MChildReport;

    /** full constructor */
    public RepRangeDownLog(com.gather.hibernate.RepRangeDownLogPK comp_id, Integer state, com.gather.hibernate.MOrg MOrg, com.gather.hibernate.MChildReport MChildReport) {
        this.comp_id = comp_id;
        this.state = state;
        this.MOrg = MOrg;
        this.MChildReport = MChildReport;
    }

    /** default constructor */
    public RepRangeDownLog() {
    }

    /** minimal constructor */
    public RepRangeDownLog(com.gather.hibernate.RepRangeDownLogPK comp_id, com.gather.hibernate.MChildReport MChildReport) {
        this.comp_id = comp_id;
        this.MChildReport = MChildReport;
    }

    public com.gather.hibernate.RepRangeDownLogPK getComp_id() {
        return this.comp_id;
    }

    public void setComp_id(com.gather.hibernate.RepRangeDownLogPK comp_id) {
        this.comp_id = comp_id;
    }

    
    public Integer getState() {
        return this.state;
    }

    public void setState(Integer state) {
        this.state = state;
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
        if ( !(other instanceof RepRangeDownLog) ) return false;
        RepRangeDownLog castOther = (RepRangeDownLog) other;
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
