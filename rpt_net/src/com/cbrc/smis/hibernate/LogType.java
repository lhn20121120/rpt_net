package com.cbrc.smis.hibernate;

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

    /**
     * 日志类型标识
     */
    private String logTypeFlag;
    
    /** persistent field */
    private Set logIns;
    
    /** full constructor */
    public LogType(Integer logTypeId, String logType, String logTypeFlag,Set logIns) {
        this.logTypeId = logTypeId;
        this.logType = logType;
        this.logTypeFlag=logTypeFlag;
        this.logIns = logIns;
    }

    /** default constructor */
    public LogType() {
    }

    /** minimal constructor */
    public LogType(Integer logTypeId, Set logIns) {
        this.logTypeId = logTypeId;
        this.logIns = logIns;
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

    public Set getLogIns() {
        return this.logIns;
    }

    public void setLogIns(Set logIns) {
        this.logIns = logIns;
    }
    
    public void setLogTypeFlag(String logTypeFlag){
    	this.logTypeFlag=logTypeFlag;
    }
    
    public String getLogTypeFlag(){
    	return this.logTypeFlag;
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
