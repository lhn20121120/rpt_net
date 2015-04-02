package com.gather.hibernate;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/** @author Hibernate CodeGenerator */
public class CalendarDetail implements Serializable {

    /** identifier field */
    private com.gather.hibernate.CalendarDetailPK comp_id;

    /** nullable persistent field */
    private com.gather.hibernate.CalendarType calendarType;

    /** nullable persistent field */
    private com.gather.hibernate.Calendar calendar;
    
    private java.util.Date calDate;

    /**
	 * @return Returns the calDate.
	 */
	public java.util.Date getCalDate() {
		return calDate;
	}

	/**
	 * @param calDate The calDate to set.
	 */
	public void setCalDate(java.util.Date calDate) {
		this.calDate = calDate;
	}

	/** full constructor */
    public CalendarDetail(com.gather.hibernate.CalendarDetailPK comp_id, com.gather.hibernate.CalendarType calendarType, com.gather.hibernate.Calendar calendar) {
        this.comp_id = comp_id;
        this.calendarType = calendarType;
        this.calendar = calendar;
    }

    /** default constructor */
    public CalendarDetail() {
    }

    /** minimal constructor */
    public CalendarDetail(com.gather.hibernate.CalendarDetailPK comp_id) {
        this.comp_id = comp_id;
    }

    public com.gather.hibernate.CalendarDetailPK getComp_id() {
        return this.comp_id;
    }

    public void setComp_id(com.gather.hibernate.CalendarDetailPK comp_id) {
        this.comp_id = comp_id;
    }

    public com.gather.hibernate.CalendarType getCalendarType() {
        return this.calendarType;
    }

    public void setCalendarType(com.gather.hibernate.CalendarType calendarType) {
        this.calendarType = calendarType;
    }

    public com.gather.hibernate.Calendar getCalendar() {
        return this.calendar;
    }

    public void setCalendar(com.gather.hibernate.Calendar calendar) {
        this.calendar = calendar;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("comp_id", getComp_id())
            .toString();
    }

    public boolean equals(Object other) {
        if ( !(other instanceof CalendarDetail) ) return false;
        CalendarDetail castOther = (CalendarDetail) other;
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
