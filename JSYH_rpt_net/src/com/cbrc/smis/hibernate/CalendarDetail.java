package com.cbrc.smis.hibernate;

import java.io.Serializable;
import java.util.Date;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/** @author Hibernate CodeGenerator */
public class CalendarDetail implements Serializable {

    /** identifier field */
    private com.cbrc.smis.hibernate.CalendarDetailPK comp_id;

    /** nullable persistent field */
    private com.cbrc.smis.hibernate.CalendarType calendarType;

    /** nullable persistent field */
    private com.cbrc.smis.hibernate.Calendar calendar;

    /**
     * 日历日期
     */
    private Date calDate;
    
    /** full constructor */
    public CalendarDetail(com.cbrc.smis.hibernate.CalendarDetailPK comp_id, 
    		com.cbrc.smis.hibernate.CalendarType calendarType, 
    		com.cbrc.smis.hibernate.Calendar calendar,
    		Date calDate) {
        this.comp_id = comp_id;
        this.calendarType = calendarType;
        this.calendar = calendar;
        this.calDate=calDate;
    }

    /** default constructor */
    public CalendarDetail() {
    }

    /** minimal constructor */
    public CalendarDetail(com.cbrc.smis.hibernate.CalendarDetailPK comp_id) {
        this.comp_id = comp_id;
    }

    public com.cbrc.smis.hibernate.CalendarDetailPK getComp_id() {
        return this.comp_id;
    }

    public void setComp_id(com.cbrc.smis.hibernate.CalendarDetailPK comp_id) {
        this.comp_id = comp_id;
    }

    public com.cbrc.smis.hibernate.CalendarType getCalendarType() {
        return this.calendarType;
    }

    public void setCalendarType(com.cbrc.smis.hibernate.CalendarType calendarType) {
        this.calendarType = calendarType;
    }

    public com.cbrc.smis.hibernate.Calendar getCalendar() {
        return this.calendar;
    }

    public void setCalendar(com.cbrc.smis.hibernate.Calendar calendar) {
        this.calendar = calendar;
    }
        
    public Date getCalDate() {
		return calDate;
	}

	public void setCalDate(Date calDate) {
		this.calDate = calDate;
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
