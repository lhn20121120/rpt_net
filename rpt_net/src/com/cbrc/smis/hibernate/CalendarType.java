package com.cbrc.smis.hibernate;

import java.io.Serializable;
import java.util.Set;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/** @author Hibernate CodeGenerator */
public class CalendarType implements Serializable {

    /** identifier field */
    private Integer calTypeId;

    /** nullable persistent field */
    private String calTypeName;

    /** persistent field */
    private Set calendarDetails;

    /** full constructor */
    public CalendarType(Integer calTypeId, String calTypeName, Set calendarDetails) {
        this.calTypeId = calTypeId;
        this.calTypeName = calTypeName;
        this.calendarDetails = calendarDetails;
    }

    /** default constructor */
    public CalendarType() {
    }

    /** minimal constructor */
    public CalendarType(Integer calTypeId, Set calendarDetails) {
        this.calTypeId = calTypeId;
        this.calendarDetails = calendarDetails;
    }

    public Integer getCalTypeId() {
        return this.calTypeId;
    }

    public void setCalTypeId(Integer calTypeId) {
        this.calTypeId = calTypeId;
    }

    public String getCalTypeName() {
        return this.calTypeName;
    }

    public void setCalTypeName(String calTypeName) {
        this.calTypeName = calTypeName;
    }

    public Set getCalendarDetails() {
        return this.calendarDetails;
    }

    public void setCalendarDetails(Set calendarDetails) {
        this.calendarDetails = calendarDetails;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("calTypeId", getCalTypeId())
            .toString();
    }

    public boolean equals(Object other) {
        if ( !(other instanceof CalendarType) ) return false;
        CalendarType castOther = (CalendarType) other;
        return new EqualsBuilder()
            .append(this.getCalTypeId(), castOther.getCalTypeId())
            .isEquals();
    }

    public int hashCode() {
        return new HashCodeBuilder()
            .append(getCalTypeId())
            .toHashCode();
    }

}
