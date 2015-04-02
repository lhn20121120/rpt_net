package com.cbrc.org.hibernate;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/** @author Hibernate CodeGenerator */
public class MRegionTyp implements Serializable {

    /** identifier field */
    private String regionTypId;

    /** nullable persistent field */
    private String regionTypNm;

    /** full constructor */
    public MRegionTyp(String regionTypId, String regionTypNm) {
        this.regionTypId = regionTypId;
        this.regionTypNm = regionTypNm;
    }

    /** default constructor */
    public MRegionTyp() {
    }

    /** minimal constructor */
    public MRegionTyp(String regionTypId) {
        this.regionTypId = regionTypId;
    }

    public String getRegionTypId() {
        return this.regionTypId;
    }

    public void setRegionTypId(String regionTypId) {
        this.regionTypId = regionTypId;
    }

    public String getRegionTypNm() {
        return this.regionTypNm;
    }

    public void setRegionTypNm(String regionTypNm) {
        this.regionTypNm = regionTypNm;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("regionTypId", getRegionTypId())
            .toString();
    }

    public boolean equals(Object other) {
        if ( !(other instanceof MRegionTyp) ) return false;
        MRegionTyp castOther = (MRegionTyp) other;
        return new EqualsBuilder()
            .append(this.getRegionTypId(), castOther.getRegionTypId())
            .isEquals();
    }

    public int hashCode() {
        return new HashCodeBuilder()
            .append(getRegionTypId())
            .toHashCode();
    }

}
