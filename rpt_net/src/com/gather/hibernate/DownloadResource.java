package com.gather.hibernate;

import java.io.Serializable;
import java.util.Date;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/** @author Hibernate CodeGenerator */
public class DownloadResource implements Serializable {

    /** identifier field */
    private Integer resourceId;

    /** nullable persistent field */
    private String resourceName;

    /** nullable persistent field */
    private Date updateTime;

    /** nullable persistent field */
    private Integer downTimes;

    /** nullable persistent field */
    private Integer fileSize;

    /** persistent field */
    private com.gather.hibernate.ResourceType resourceType;

    /** full constructor */
    public DownloadResource(Integer resourceId, String resourceName, Date updateTime, Integer downTimes, Integer fileSize, com.gather.hibernate.ResourceType resourceType) {
        this.resourceId = resourceId;
        this.resourceName = resourceName;
        this.updateTime = updateTime;
        this.downTimes = downTimes;
        this.fileSize = fileSize;
        this.resourceType = resourceType;
    }

    /** default constructor */
    public DownloadResource() {
    }

    /** minimal constructor */
    public DownloadResource(Integer resourceId, com.gather.hibernate.ResourceType resourceType) {
        this.resourceId = resourceId;
        this.resourceType = resourceType;
    }

    public Integer getResourceId() {
        return this.resourceId;
    }

    public void setResourceId(Integer resourceId) {
        this.resourceId = resourceId;
    }

    public String getResourceName() {
        return this.resourceName;
    }

    public void setResourceName(String resourceName) {
        this.resourceName = resourceName;
    }

    public Date getUpdateTime() {
        return this.updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Integer getDownTimes() {
        return this.downTimes;
    }

    public void setDownTimes(Integer downTimes) {
        this.downTimes = downTimes;
    }

    public Integer getFileSize() {
        return this.fileSize;
    }

    public void setFileSize(Integer fileSize) {
        this.fileSize = fileSize;
    }

    public com.gather.hibernate.ResourceType getResourceType() {
        return this.resourceType;
    }

    public void setResourceType(com.gather.hibernate.ResourceType resourceType) {
        this.resourceType = resourceType;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("resourceId", getResourceId())
            .toString();
    }

    public boolean equals(Object other) {
        if ( !(other instanceof DownloadResource) ) return false;
        DownloadResource castOther = (DownloadResource) other;
        return new EqualsBuilder()
            .append(this.getResourceId(), castOther.getResourceId())
            .isEquals();
    }

    public int hashCode() {
        return new HashCodeBuilder()
            .append(getResourceId())
            .toHashCode();
    }

}
