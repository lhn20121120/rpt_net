package com.cbrc.org.hibernate;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/** @author Hibernate CodeGenerator */
public class MVirOrgRlt implements Serializable {

    /** identifier field */
    private com.cbrc.org.hibernate.MVirOrgRltPK comp_id;

    /** nullable persistent field */
    private String sumProp;

    /** nullable persistent field */
    private String startDate;

    /** nullable persistent field */
    private String endDate;

    /** full constructor */
    public MVirOrgRlt(com.cbrc.org.hibernate.MVirOrgRltPK comp_id, String sumProp, String startDate, String endDate) {
        this.comp_id = comp_id;
        this.sumProp = sumProp;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    /** default constructor */
    public MVirOrgRlt() {
    }

    /** minimal constructor */
    public MVirOrgRlt(com.cbrc.org.hibernate.MVirOrgRltPK comp_id) {
        this.comp_id = comp_id;
    }

    public com.cbrc.org.hibernate.MVirOrgRltPK getComp_id() {
        return this.comp_id;
    }

    public void setComp_id(com.cbrc.org.hibernate.MVirOrgRltPK comp_id) {
        this.comp_id = comp_id;
    }

    public String getSumProp() {
        return this.sumProp;
    }

    public void setSumProp(String sumProp) {
        this.sumProp = sumProp;
    }

    public String getStartDate() {
        return this.startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return this.endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("comp_id", getComp_id())
            .toString();
    }

    public boolean equals(Object other) {
        if ( !(other instanceof MVirOrgRlt) ) return false;
        MVirOrgRlt castOther = (MVirOrgRlt) other;
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
