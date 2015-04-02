package com.cbrc.smis.hibernate;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/** @author Hibernate CodeGenerator */
public class CalendarDetailPK implements Serializable {

    /** identifier field */
    private Integer calYear;

    /** identifier field */
    private Integer calMonth;

    /** identifier field */
    private Integer calDay;

    /** identifier field */
    private Integer calId;

    /** identifier field */
    private Integer calTypeId;

    /** full constructor */
    public CalendarDetailPK(Integer calYear, Integer calMonth, Integer calDay, Integer calId, Integer calTypeId) {
        this.calYear = calYear;
        this.calMonth = calMonth;
        this.calDay = calDay;
        this.calId = calId;
        this.calTypeId = calTypeId;
    }

    /** default constructor */
    public CalendarDetailPK() {
    }

    public Integer getCalYear() {
        return this.calYear;
    }

    public void setCalYear(Integer calYear) {
        this.calYear = calYear;
    }

    public Integer getCalMonth() {
        return this.calMonth;
    }

    public void setCalMonth(Integer calMonth) {
        this.calMonth = calMonth;
    }

    public Integer getCalDay() {
        return this.calDay;
    }

    public void setCalDay(Integer calDay) {
        this.calDay = calDay;
    }

    public Integer getCalId() {
        return this.calId;
    }

    public void setCalId(Integer calId) {
        this.calId = calId;
    }

    public Integer getCalTypeId() {
        return this.calTypeId;
    }

    public void setCalTypeId(Integer calTypeId) {
        this.calTypeId = calTypeId;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("calYear", getCalYear())
            .append("calMonth", getCalMonth())
            .append("calDay", getCalDay())
            .append("calId", getCalId())
            .append("calTypeId", getCalTypeId())
            .toString();
    }

    public boolean equals(Object other) {
        if ( !(other instanceof CalendarDetailPK) ) return false;
        CalendarDetailPK castOther = (CalendarDetailPK) other;
        return new EqualsBuilder()
            .append(this.getCalYear(), castOther.getCalYear())
            .append(this.getCalMonth(), castOther.getCalMonth())
            .append(this.getCalDay(), castOther.getCalDay())
            .append(this.getCalId(), castOther.getCalId())
            .append(this.getCalTypeId(), castOther.getCalTypeId())
            .isEquals();
    }

    public int hashCode() {
        return new HashCodeBuilder()
            .append(getCalYear())
            .append(getCalMonth())
            .append(getCalDay())
            .append(getCalId())
            .append(getCalTypeId())
            .toHashCode();
    }

}
