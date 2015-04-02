package com.cbrc.smis.hibernate;

import java.io.Serializable;
import java.util.Date;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/** @author Hibernate CodeGenerator */
public class LogIn implements Serializable {

    /** identifier field */
    private Integer logInId;

    /** nullable persistent field */
    private String userName;

    /** nullable persistent field */
    private Date logTime;

    /** nullable persistent field */
    private String operation;

    /** nullable persistent field */
    private String memo;

    /** persistent field */
    private com.cbrc.smis.hibernate.LogType logType;

    /** full constructor */
    public LogIn(Integer logInId, String userName, Date logTime, String operation, String memo, com.cbrc.smis.hibernate.LogType logType) {
        this.logInId = logInId;
        this.userName = userName;
        this.logTime = logTime;
        this.operation = operation;
        this.memo = memo;
        this.logType = logType;
    }

    /** default constructor */
    public LogIn() {
    }

    /** minimal constructor */
    public LogIn(Integer logInId, com.cbrc.smis.hibernate.LogType logType) {
        this.logInId = logInId;
        this.logType = logType;
    }

    public Integer getLogInId() {
        return this.logInId;
    }

    public void setLogInId(Integer logInId) {
        this.logInId = logInId;
    }

    public String getUserName() {
        return this.userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Date getLogTime() {
        return this.logTime;
    }

    public void setLogTime(Date logTime) {
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

    public com.cbrc.smis.hibernate.LogType getLogType() {
        return this.logType;
    }

    public void setLogType(com.cbrc.smis.hibernate.LogType logType) {
        this.logType = logType;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("logInId", getLogInId())
            .toString();
    }

    public boolean equals(Object other) {
        if ( !(other instanceof LogIn) ) return false;
        LogIn castOther = (LogIn) other;
        return new EqualsBuilder()
            .append(this.getLogInId(), castOther.getLogInId())
            .isEquals();
    }

    public int hashCode() {
        return new HashCodeBuilder()
            .append(getLogInId())
            .toHashCode();
    }

}
