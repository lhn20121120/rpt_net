package com.gather.hibernate;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/** @author Hibernate CodeGenerator */
public class Log implements Serializable {

    /** identifier field */
    private Integer logId;

    /** nullable persistent field */
    private String userName;

    /** nullable persistent field */
    private java.util.Date logTime;

    /** nullable persistent field */
    private String operation;

    /** nullable persistent field */
    private String memo;
    
    private String orgId;

    /** persistent field */
    private com.gather.hibernate.LogType logType;

    /** full constructor */
    public Log(Integer logId, String userName, java.util.Date logTime, String operation, String memo,String orgId, com.gather.hibernate.LogType logType) {
        this.logId = logId;
        this.userName = userName;
        this.logTime = logTime;
        this.operation = operation;
        this.memo = memo;
        this.orgId= orgId;
        this.logType = logType;
    }

    /** default constructor */
    public Log() {
    }

    /** minimal constructor */
    public Log(Integer logId, com.gather.hibernate.LogType logType) {
        this.logId = logId;
        this.logType = logType;
    }

    public Integer getLogId() {
        return this.logId;
    }

    public void setLogId(Integer logId) {
        this.logId = logId;
    }

    public String getUserName() {
        return this.userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public java.util.Date getLogTime() {
        return this.logTime;
    }

    public void setLogTime(java.util.Date logTime) {
        this.logTime = logTime;
    }

    public String getOperation() {
        return this.operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public String getMemo() {
        return this.memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public com.gather.hibernate.LogType getLogType() {
        return this.logType;
    }

    public void setLogType(com.gather.hibernate.LogType logType) {
        this.logType = logType;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("logId", getLogId())
            .toString();
    }

    public boolean equals(Object other) {
        if ( !(other instanceof Log) ) return false;
        Log castOther = (Log) other;
        return new EqualsBuilder()
            .append(this.getLogId(), castOther.getLogId())
            .isEquals();
    }

    public int hashCode() {
        return new HashCodeBuilder()
            .append(getLogId())
            .toHashCode();
    }

	/**
	 * @return Returns the orgId.
	 */
	public String getOrgId() {
		return orgId;
	}

	/**
	 * @param orgId The orgId to set.
	 */
	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

}
