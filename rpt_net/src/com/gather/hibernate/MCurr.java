package com.gather.hibernate;

import java.io.Serializable;
import java.util.Set;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/** @author Hibernate CodeGenerator */
public class MCurr implements Serializable {

    /** identifier field */
    private Integer curId;

    /** nullable persistent field */
    private String curName;

    /** persistent field */
    private Set reports;

    /** full constructor */
    public MCurr(Integer curId, String curName, Set reports) {
        this.curId = curId;
        this.curName = curName;
        this.reports = reports;
    }

    /** default constructor */
    public MCurr() {
    }

    /** minimal constructor */
    public MCurr(Integer curId, Set reports) {
        this.curId = curId;
        this.reports = reports;
    }

    public Integer getCurId() {
        return this.curId;
    }

    public void setCurId(Integer curId) {
        this.curId = curId;
    }

    public String getCurName() {
        return this.curName;
    }

    public void setCurName(String curName) {
        this.curName = curName;
    }

    public Set getReports() {
        return this.reports;
    }

    public void setReports(Set reports) {
        this.reports = reports;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("curId", getCurId())
            .toString();
    }

    public boolean equals(Object other) {
        if ( !(other instanceof MCurr) ) return false;
        MCurr castOther = (MCurr) other;
        return new EqualsBuilder()
            .append(this.getCurId(), castOther.getCurId())
            .isEquals();
    }

    public int hashCode() {
        return new HashCodeBuilder()
            .append(getCurId())
            .toHashCode();
    }

}
