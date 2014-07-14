package com.cbrc.smis.hibernate;

import java.io.Serializable;
import java.util.Set;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/** @author Hibernate CodeGenerator */
public class Calendar implements Serializable {

    /** identifier field */
    private Integer calId;

    /** nullable persistent field */
    private String calName;

    /** nullable persistent field */
    private String calMethod;

    /** persistent field */
    private Set calendarDetails;

    /** full constructor */
    public Calendar(Integer calId, String calName, String calMethod, Set calendarDetails) {
        this.calId = calId;
        this.calName = calName;
        this.calMethod = calMethod;
        this.calendarDetails = calendarDetails;
    }

    /** default constructor */
    public Calendar() {
    }

    /** minimal constructor */
    public Calendar(Integer calId, Set calendarDetails) {
        this.calId = calId;
        this.calendarDetails = calendarDetails;
    }

    public Integer getCalId() {
        return this.calId;
    }

    public void setCalId(Integer calId) {
        this.calId = calId;
    }

    public String getCalName() {
        return this.calName;
    }

    public void setCalName(String calName) {
        this.calName = calName;
    }

    public String getCalMethod() {
        return this.calMethod;
    }

    public void setCalMethod(String calMethod) {
        this.calMethod = calMethod;
    }

    public Set getCalendarDetails() {
        return this.calendarDetails;
    }

    public void setCalendarDetails(Set calendarDetails) {
        this.calendarDetails = calendarDetails;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("calId", getCalId())
            .toString();
    }

    public boolean equals(Object other) {
        if ( !(other instanceof Calendar) ) return false;
        Calendar castOther = (Calendar) other;
        return new EqualsBuilder()
            .append(this.getCalId(), castOther.getCalId())
            .isEquals();
    }

    public int hashCode() {
        return new HashCodeBuilder()
            .append(getCalId())
            .toHashCode();
    }

}
