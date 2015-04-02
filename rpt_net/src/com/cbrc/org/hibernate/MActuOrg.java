package com.cbrc.org.hibernate;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/** @author Hibernate CodeGenerator */
public class MActuOrg implements Serializable {

    /** identifier field */
    private String actuOrgTypeId;

    /** nullable persistent field */
    private String actuOrgTypeName;

    /** full constructor */
    public MActuOrg(String actuOrgTypeId, String actuOrgTypeName) {
        this.actuOrgTypeId = actuOrgTypeId;
        this.actuOrgTypeName = actuOrgTypeName;
    }

    /** default constructor */
    public MActuOrg() {
    }

    /** minimal constructor */
    public MActuOrg(String actuOrgTypeId) {
        this.actuOrgTypeId = actuOrgTypeId;
    }

    public String getActuOrgTypeId() {
        return this.actuOrgTypeId;
    }

    public void setActuOrgTypeId(String actuOrgTypeId) {
        this.actuOrgTypeId = actuOrgTypeId;
    }

    public String getActuOrgTypeName() {
        return this.actuOrgTypeName;
    }

    public void setActuOrgTypeName(String actuOrgTypeName) {
        this.actuOrgTypeName = actuOrgTypeName;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("actuOrgTypeId", getActuOrgTypeId())
            .toString();
    }

    public boolean equals(Object other) {
        if ( !(other instanceof MActuOrg) ) return false;
        MActuOrg castOther = (MActuOrg) other;
        return new EqualsBuilder()
            .append(this.getActuOrgTypeId(), castOther.getActuOrgTypeId())
            .isEquals();
    }

    public int hashCode() {
        return new HashCodeBuilder()
            .append(getActuOrgTypeId())
            .toHashCode();
    }

}
