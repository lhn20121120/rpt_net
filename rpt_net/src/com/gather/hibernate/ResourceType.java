package com.gather.hibernate;

import java.io.Serializable;
import java.util.Set;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/** @author Hibernate CodeGenerator */
public class ResourceType implements Serializable {

    /** identifier field */
    private Integer resourceTypeId;

    /** nullable persistent field */
    private String typeName;

    /** persistent field */
    private Set downloadResources;

    /** full constructor */
    public ResourceType(Integer resourceTypeId, String typeName, Set downloadResources) {
        this.resourceTypeId = resourceTypeId;
        this.typeName = typeName;
        this.downloadResources = downloadResources;
    }

    /** default constructor */
    public ResourceType() {
    }

    /** minimal constructor */
    public ResourceType(Integer resourceTypeId, Set downloadResources) {
        this.resourceTypeId = resourceTypeId;
        this.downloadResources = downloadResources;
    }

    public Integer getResourceTypeId() {
        return this.resourceTypeId;
    }

    public void setResourceTypeId(Integer resourceTypeId) {
        this.resourceTypeId = resourceTypeId;
    }

    public String getTypeName() {
        return this.typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public Set getDownloadResources() {
        return this.downloadResources;
    }

    public void setDownloadResources(Set downloadResources) {
        this.downloadResources = downloadResources;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("resourceTypeId", getResourceTypeId())
            .toString();
    }

    public boolean equals(Object other) {
        if ( !(other instanceof ResourceType) ) return false;
        ResourceType castOther = (ResourceType) other;
        return new EqualsBuilder()
            .append(this.getResourceTypeId(), castOther.getResourceTypeId())
            .isEquals();
    }

    public int hashCode() {
        return new HashCodeBuilder()
            .append(getResourceTypeId())
            .toHashCode();
    }

}
