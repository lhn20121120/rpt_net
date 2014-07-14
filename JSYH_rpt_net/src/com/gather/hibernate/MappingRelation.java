package com.gather.hibernate;

import java.io.Serializable;
import java.util.Date;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/** @author Hibernate CodeGenerator */
public class MappingRelation implements Serializable {

    /** identifier field */
    private com.gather.hibernate.MappingRelationPK comp_id;

    /** nullable persistent field */
    private Date startDate;

    /** nullable persistent field */
    private Date endDate;

    /** nullable persistent field */
    private Integer state;

    /** nullable persistent field */
    private com.gather.hibernate.MOrg MOrg;

    /** full constructor */
 public MappingRelation(com.gather.hibernate.MappingRelationPK comp_id, Date startDate, Date endDate, Integer state, com.gather.hibernate.MOrg MOrg) {

        this.comp_id = comp_id;
        this.startDate = startDate;
        this.endDate = endDate;
        this.state = state;
        this.MOrg = MOrg;
    }

    /** default constructor */
    public MappingRelation() {
    }

    /** minimal constructor */
    public MappingRelation(com.gather.hibernate.MappingRelationPK comp_id) {
        this.comp_id = comp_id;
    }

    public com.gather.hibernate.MappingRelationPK getComp_id() {
        return this.comp_id;
    }

    public void setComp_id(com.gather.hibernate.MappingRelationPK comp_id) {
        this.comp_id = comp_id;
    }

    public Date getStartDate() {
        return this.startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return this.endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
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

    public String toString() {
        return new ToStringBuilder(this)
            .append("comp_id", getComp_id())
            .toString();
    }

    public boolean equals(Object other) {
        if ( !(other instanceof MappingRelation) ) return false;
        MappingRelation castOther = (MappingRelation) other;
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
