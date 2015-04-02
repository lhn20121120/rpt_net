package com.gather.hibernate;

import java.io.Serializable;
import java.util.Set;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/** @author Hibernate CodeGenerator */
public class LogType implements Serializable {

    /** identifier field */
    private Integer logTypeId;

    /** nullable persistent field */
    private String logType;

    /** persistent field */
    private Set logs;

    /** full constructor */
    public LogType(Integer logTypeId, String logType, Set logs) {
        this.logTypeId = logTypeId;
        this.logType = logType;
        this.logs = logs;
    }

    /** default constructor */
    public LogType() {
    }

    /** minimal constructor */
    public LogType(Integer logTypeId, Set logs) {
        this.logTypeId = logTypeId;
        this.logs = logs;
    }

    public Integer getLogTypeId() {
        return this.logTypeId;
    }

    public void setLogTypeId(Integer logTypeId) {
        this.logTypeId = logTypeId;
    }

    public String getLogType() {
        return this.logType;
    }

    public void setLogType(String logType) {
        this.logType = logType;
    }

    public Set getLogs() {
        return this.logs;
    }

    public void setLogs(Set logs) {
        this.logs = logs;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("logTypeId", getLogTypeId())
            .toString();
    }

    public boolean equals(Object other) {
        if ( !(other instanceof LogType) ) return false;
        LogType castOther = (LogType) other;
        return new EqualsBuilder()
            .append(this.getLogTypeId(), castOther.getLogTypeId())
            .isEquals();
    }

    public int hashCode() {
        return new HashCodeBuilder()
            .append(getLogTypeId())
            .toHashCode();
    }

}
