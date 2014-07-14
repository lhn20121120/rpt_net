package com.fitech.net.region.hibernate;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/** @author Hibernate CodeGenerator */
public class Region implements Serializable {

    /** identifier field */
    private String regionId;

    /** nullable persistent field */
    private String regionTypId;

    /** nullable persistent field */
    private String regionName;

    /** full constructor */
    public Region(String regionId, String regionTypId, String regionName) {
        this.regionId = regionId;
        this.regionTypId = regionTypId;
        this.regionName = regionName;
    }

    /** default constructor */
    public Region() {
    }

    /** minimal constructor */
    public Region(String regionId) {
        this.regionId = regionId;
    }

    public String getRegionId() {
        return this.regionId;
    }

    public void setRegionId(String regionId) {
        this.regionId = regionId;
    }

    public String getRegionTypId() {
        return this.regionTypId;
    }

    public void setRegionTypId(String regionTypId) {
        this.regionTypId = regionTypId;
    }

    public String getRegionName() {
        return this.regionName;
    }

    public void setRegionName(String regionName) {
        this.regionName = regionName;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("regionId", getRegionId())
            .toString();
    }

    public boolean equals(Object other) {
        if ( !(other instanceof Region) ) return false;
        Region castOther = (Region) other;
        return new EqualsBuilder()
            .append(this.getRegionId(), castOther.getRegionId())
            .isEquals();
    }

    public int hashCode() {
        return new HashCodeBuilder()
            .append(getRegionId())
            .toHashCode();
    }

}