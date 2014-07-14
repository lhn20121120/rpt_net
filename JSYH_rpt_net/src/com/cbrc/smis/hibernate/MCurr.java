package com.cbrc.smis.hibernate;

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
    private Set reportIns;

    /** full constructor */
    public MCurr(Integer curId, String curName, Set reportIns) {
        this.curId = curId;
        this.curName = curName;
        this.reportIns = reportIns;
    }

    /** default constructor */
    public MCurr() {
    }

    public MCurr(Integer curId) {
    	this.curId = curId;
    }
    
    /** minimal constructor */
    public MCurr(Integer curId, Set reportIns) {
        this.curId = curId;
        this.reportIns = reportIns;
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

    public Set getReportIns() {
        return this.reportIns;
    }

    public void setReportIns(Set reportIns) {
        this.reportIns = reportIns;
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
