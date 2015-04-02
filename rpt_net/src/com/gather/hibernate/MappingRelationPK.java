package com.gather.hibernate;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/** @author Hibernate CodeGenerator */
public class MappingRelationPK implements Serializable {

    /** identifier field */
    private String orgid;

    /** identifier field */
    private String replaceOrgId;

    /** full constructor */
    public MappingRelationPK(String orgid, String replaceOrgId) {
        this.orgid = orgid;
        this.replaceOrgId = replaceOrgId;
    }

    /** default constructor */
    public MappingRelationPK() {
    }

    public String getOrgid() {
        return this.orgid;
    }

    public void setOrgid(String orgid) {
        this.orgid = orgid;
    }

    public String getReplaceOrgId() {
        return this.replaceOrgId;
    }

    public void setReplaceOrgId(String replaceOrgId) {
        this.replaceOrgId = replaceOrgId;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("orgid", getOrgid())
            .append("replaceOrgId", getReplaceOrgId())
            .toString();
    }

    public boolean equals(Object other) {
        if ( !(other instanceof MappingRelationPK) ) return false;
        MappingRelationPK castOther = (MappingRelationPK) other;
        return new EqualsBuilder()
            .append(this.getOrgid(), castOther.getOrgid())
            .append(this.getReplaceOrgId(), castOther.getReplaceOrgId())
            .isEquals();
    }

    public int hashCode() {
        return new HashCodeBuilder()
            .append(getOrgid())
            .append(getReplaceOrgId())
            .toHashCode();
    }

}
