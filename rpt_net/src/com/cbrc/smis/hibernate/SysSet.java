package com.cbrc.smis.hibernate;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/** @author Hibernate CodeGenerator */
public class SysSet implements Serializable {

    /** identifier field */
    private Integer sysSetId;

    /** nullable persistent field */
    private String sysSetKey;

    /** nullable persistent field */
    private String sysSetValue;

    /** nullable persistent field */
    private String memo;

    /** full constructor */
    public SysSet(Integer sysSetId, String sysSetKey, String sysSetValue, String memo) {
        this.sysSetId = sysSetId;
        this.sysSetKey = sysSetKey;
        this.sysSetValue = sysSetValue;
        this.memo = memo;
    }

    /** default constructor */
    public SysSet() {
    }

    /** minimal constructor */
    public SysSet(Integer sysSetId) {
        this.sysSetId = sysSetId;
    }

    public Integer getSysSetId() {
        return this.sysSetId;
    }

    public void setSysSetId(Integer sysSetId) {
        this.sysSetId = sysSetId;
    }

    public String getSysSetKey() {
        return this.sysSetKey;
    }

    public void setSysSetKey(String sysSetKey) {
        this.sysSetKey = sysSetKey;
    }

    public String getSysSetValue() {
        return this.sysSetValue;
    }

    public void setSysSetValue(String sysSetValue) {
        this.sysSetValue = sysSetValue;
    }

    public String getMemo() {
        return this.memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("sysSetId", getSysSetId())
            .toString();
    }

    public boolean equals(Object other) {
        if ( !(other instanceof SysSet) ) return false;
        SysSet castOther = (SysSet) other;
        return new EqualsBuilder()
            .append(this.getSysSetId(), castOther.getSysSetId())
            .isEquals();
    }

    public int hashCode() {
        return new HashCodeBuilder()
            .append(getSysSetId())
            .toHashCode();
    }

}
