package com.cbrc.smis.hibernate;

import java.io.Serializable;
import java.util.Set;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/** @author Hibernate CodeGenerator */
public class PolicyType implements Serializable {

    /** identifier field */
    private Integer policyTypeId;

    /** nullable persistent field */
    private String policyTypeName;

    /** nullable persistent field */
    private String memo;

    /** persistent field */
    private Set policies;

    /** full constructor */
    public PolicyType(Integer policyTypeId, String policyTypeName, String memo, Set policies) {
        this.policyTypeId = policyTypeId;
        this.policyTypeName = policyTypeName;
        this.memo = memo;
        this.policies = policies;
    }

    /** default constructor */
    public PolicyType() {
    }

    /** minimal constructor */
    public PolicyType(Integer policyTypeId, Set policies) {
        this.policyTypeId = policyTypeId;
        this.policies = policies;
    }

    public Integer getPolicyTypeId() {
        return this.policyTypeId;
    }

    public void setPolicyTypeId(Integer policyTypeId) {
        this.policyTypeId = policyTypeId;
    }

    public String getPolicyTypeName() {
        return this.policyTypeName;
    }

    public void setPolicyTypeName(String policyTypeName) {
        this.policyTypeName = policyTypeName;
    }

    public String getMemo() {
        return this.memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public Set getPolicies() {
        return this.policies;
    }

    public void setPolicies(Set policies) {
        this.policies = policies;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("policyTypeId", getPolicyTypeId())
            .toString();
    }

    public boolean equals(Object other) {
        if ( !(other instanceof PolicyType) ) return false;
        PolicyType castOther = (PolicyType) other;
        return new EqualsBuilder()
            .append(this.getPolicyTypeId(), castOther.getPolicyTypeId())
            .isEquals();
    }

    public int hashCode() {
        return new HashCodeBuilder()
            .append(getPolicyTypeId())
            .toHashCode();
    }

}
