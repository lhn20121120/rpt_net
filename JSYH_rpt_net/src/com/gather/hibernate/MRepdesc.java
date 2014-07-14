package com.gather.hibernate;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/** @author Hibernate CodeGenerator */
public class MRepdesc implements Serializable {

    /** identifier field */
    private com.gather.hibernate.MRepdescPK comp_id;

    /** nullable persistent field */
    private Integer term;

    /** nullable persistent field */
    private String torepdate;

    /** nullable persistent field */
    private String mkreppers;

    /** nullable persistent field */
    private String ckreppers;

    /** nullable persistent field */
    private String cgreppers;

    /** nullable persistent field */
    private com.gather.hibernate.MDataRgType MDataRgType;

    /** nullable persistent field */
    private com.gather.hibernate.MMainRep MMainRep;

    /** full constructor */
    public MRepdesc(com.gather.hibernate.MRepdescPK comp_id, Integer term, String torepdate, String mkreppers, String ckreppers, String cgreppers, com.gather.hibernate.MDataRgType MDataRgType, com.gather.hibernate.MMainRep MMainRep) {
        this.comp_id = comp_id;
        this.term = term;
        this.torepdate = torepdate;
        this.mkreppers = mkreppers;
        this.ckreppers = ckreppers;
        this.cgreppers = cgreppers;
        this.MDataRgType = MDataRgType;
        this.MMainRep = MMainRep;
    }

    /** default constructor */
    public MRepdesc() {
    }

    /** minimal constructor */
    public MRepdesc(com.gather.hibernate.MRepdescPK comp_id) {
        this.comp_id = comp_id;
    }

    public com.gather.hibernate.MRepdescPK getComp_id() {
        return this.comp_id;
    }

    public void setComp_id(com.gather.hibernate.MRepdescPK comp_id) {
        this.comp_id = comp_id;
    }

    public Integer getTerm() {
        return this.term;
    }

    public void setTerm(Integer term) {
        this.term = term;
    }

    public String getTorepdate() {
        return this.torepdate;
    }

    public void setTorepdate(String torepdate) {
        this.torepdate = torepdate;
    }

    public String getMkreppers() {
        return this.mkreppers;
    }

    public void setMkreppers(String mkreppers) {
        this.mkreppers = mkreppers;
    }

    public String getCkreppers() {
        return this.ckreppers;
    }

    public void setCkreppers(String ckreppers) {
        this.ckreppers = ckreppers;
    }

    public String getCgreppers() {
        return this.cgreppers;
    }

    public void setCgreppers(String cgreppers) {
        this.cgreppers = cgreppers;
    }

    public com.gather.hibernate.MDataRgType getMDataRgType() {
        return this.MDataRgType;
    }

    public void setMDataRgType(com.gather.hibernate.MDataRgType MDataRgType) {
        this.MDataRgType = MDataRgType;
    }

    public com.gather.hibernate.MMainRep getMMainRep() {
        return this.MMainRep;
    }

    public void setMMainRep(com.gather.hibernate.MMainRep MMainRep) {
        this.MMainRep = MMainRep;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("comp_id", getComp_id())
            .toString();
    }

    public boolean equals(Object other) {
        if ( !(other instanceof MRepdesc) ) return false;
        MRepdesc castOther = (MRepdesc) other;
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
