package com.cbrc.smis.hibernate;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/** @author Hibernate CodeGenerator */
public class Policy implements Serializable {

    /** identifier field */
    private Integer policyId;

    /** persistent field */
    private int calId;

    /** nullable persistent field */
    private Date defineTime;

    /** nullable persistent field */
    private String policyName;

    /** persistent field */
    private com.cbrc.smis.hibernate.PolicyType policyType;

    /** persistent field */
    private Set policyExecDetails;

    /** full constructor */
    public Policy(Integer policyId, int calId, Date defineTime, String policyName, com.cbrc.smis.hibernate.PolicyType policyType, Set policyExecDetails) {
        this.policyId = policyId;
        this.calId = calId;
        this.defineTime = defineTime;
        this.policyName = policyName;
        this.policyType = policyType;
        this.policyExecDetails = policyExecDetails;
    }

    /** default constructor */
    public Policy() {
    }

    /** minimal constructor */
    public Policy(Integer policyId, int calId, com.cbrc.smis.hibernate.PolicyType policyType, Set policyExecDetails) {
        this.policyId = policyId;
        this.calId = calId;
        this.policyType = policyType;
        this.policyExecDetails = policyExecDetails;
    }

    public Integer getPolicyId() {
        return this.policyId;
    }

    public void setPolicyId(Integer policyId) {
        this.policyId = policyId;
    }

    public int getCalId() {
        return this.calId;
    }

    public void setCalId(int calId) {
        this.calId = calId;
    }

    public Date getDefineTime() {
        return this.defineTime;
    }

    public void setDefineTime(Date defineTime) {
        this.defineTime = defineTime;
    }

    public String getPolicyName() {
        return this.policyName;
    }

    public void setPolicyName(String policyName) {
        this.policyName = policyName;
    }

    public com.cbrc.smis.hibernate.PolicyType getPolicyType() {
        return this.policyType;
    }

    public void setPolicyType(com.cbrc.smis.hibernate.PolicyType policyType) {
        this.policyType = policyType;
    }

    public Set getPolicyExecDetails() {
        return this.policyExecDetails;
    }

    public void setPolicyExecDetails(Set policyExecDetails) {
        this.policyExecDetails = policyExecDetails;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("policyId", getPolicyId())
            .toString();
    }

    public boolean equals(Object other) {
        if ( !(other instanceof Policy) ) return false;
        Policy castOther = (Policy) other;
        return new EqualsBuilder()
            .append(this.getPolicyId(), castOther.getPolicyId())
            .isEquals();
    }

    public int hashCode() {
        return new HashCodeBuilder()
            .append(getPolicyId())
            .toHashCode();
    }

}
