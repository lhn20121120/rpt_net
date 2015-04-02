package com.cbrc.smis.hibernate;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/** @author Hibernate CodeGenerator */
public class PolicyExecDetailPK implements Serializable {

    /** identifier field */
    private Integer repInId;

    /** identifier field */
    private Integer policyId;

    /** full constructor */
    public PolicyExecDetailPK(Integer repInId, Integer policyId) {
        this.repInId = repInId;
        this.policyId = policyId;
    }

    /** default constructor */
    public PolicyExecDetailPK() {
    }

    public Integer getRepInId() {
        return this.repInId;
    }

    public void setRepInId(Integer repInId) {
        this.repInId = repInId;
    }

    public Integer getPolicyId() {
        return this.policyId;
    }

    public void setPolicyId(Integer policyId) {
        this.policyId = policyId;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("repInId", getRepInId())
            .append("policyId", getPolicyId())
            .toString();
    }

    public boolean equals(Object other) {
        if ( !(other instanceof PolicyExecDetailPK) ) return false;
        PolicyExecDetailPK castOther = (PolicyExecDetailPK) other;
        return new EqualsBuilder()
            .append(this.getRepInId(), castOther.getRepInId())
            .append(this.getPolicyId(), castOther.getPolicyId())
            .isEquals();
    }

    public int hashCode() {
        return new HashCodeBuilder()
            .append(getRepInId())
            .append(getPolicyId())
            .toHashCode();
    }

}
