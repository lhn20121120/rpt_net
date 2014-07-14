package com.cbrc.org.hibernate;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/** @author Hibernate CodeGenerator */
public class MVirOrgType implements Serializable {

    /** identifier field */
    private String virtuTypeId;

    /** nullable persistent field */
    private String virtuTypeNm;

    /** full constructor */
    public MVirOrgType(String virtuTypeId, String virtuTypeNm) {
        this.virtuTypeId = virtuTypeId;
        this.virtuTypeNm = virtuTypeNm;
    }

    /** default constructor */
    public MVirOrgType() {
    }

    /** minimal constructor */
    public MVirOrgType(String virtuTypeId) {
        this.virtuTypeId = virtuTypeId;
    }

    public String getVirtuTypeId() {
        return this.virtuTypeId;
    }

    public void setVirtuTypeId(String virtuTypeId) {
        this.virtuTypeId = virtuTypeId;
    }

    public String getVirtuTypeNm() {
        return this.virtuTypeNm;
    }

    public void setVirtuTypeNm(String virtuTypeNm) {
        this.virtuTypeNm = virtuTypeNm;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("virtuTypeId", getVirtuTypeId())
            .toString();
    }

    public boolean equals(Object other) {
        if ( !(other instanceof MVirOrgType) ) return false;
        MVirOrgType castOther = (MVirOrgType) other;
        return new EqualsBuilder()
            .append(this.getVirtuTypeId(), castOther.getVirtuTypeId())
            .isEquals();
    }

    public int hashCode() {
        return new HashCodeBuilder()
            .append(getVirtuTypeId())
            .toHashCode();
    }

}
