package com.cbrc.org.hibernate;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/** @author Hibernate CodeGenerator */
public class MVirOrgRltPK implements Serializable {

    /** identifier field */
    private String virOrgId;

    /** identifier field */
    private String orgId;

    /** full constructor */
    public MVirOrgRltPK(String virOrgId, String orgId) {
        this.virOrgId = virOrgId;
        this.orgId = orgId;
    }

    /** default constructor */
    public MVirOrgRltPK() {
    }

    public String getVirOrgId() {
        return this.virOrgId;
    }

    public void setVirOrgId(String virOrgId) {
        this.virOrgId = virOrgId;
    }

    public String getOrgId() {
        return this.orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("virOrgId", getVirOrgId())
            .append("orgId", getOrgId())
            .toString();
    }

    public boolean equals(Object other) {
        if ( !(other instanceof MVirOrgRltPK) ) return false;
        MVirOrgRltPK castOther = (MVirOrgRltPK) other;
        return new EqualsBuilder()
            .append(this.getVirOrgId(), castOther.getVirOrgId())
            .append(this.getOrgId(), castOther.getOrgId())
            .isEquals();
    }

    public int hashCode() {
        return new HashCodeBuilder()
            .append(getVirOrgId())
            .append(getOrgId())
            .toHashCode();
    }

}
