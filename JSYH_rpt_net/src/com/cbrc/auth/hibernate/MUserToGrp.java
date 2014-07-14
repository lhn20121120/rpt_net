package com.cbrc.auth.hibernate;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/** @author Hibernate CodeGenerator */
public class MUserToGrp implements Serializable {

    /** identifier field */
    private Long userToGrpId;

    /** nullable persistent field */
    private com.cbrc.auth.hibernate.MUserGrp MUserGrp;

    /** persistent field */
    private com.cbrc.auth.hibernate.Operator operator;

    /** full constructor */
    public MUserToGrp(Long userToGrpId, com.cbrc.auth.hibernate.MUserGrp MUserGrp, com.cbrc.auth.hibernate.Operator operator) {
        this.userToGrpId = userToGrpId;
        this.MUserGrp = MUserGrp;
        this.operator = operator;
    }

    /** default constructor */
    public MUserToGrp() {
    }

    /** minimal constructor */
    public MUserToGrp(Long userToGrpId, com.cbrc.auth.hibernate.Operator operator) {
        this.userToGrpId = userToGrpId;
        this.operator = operator;
    }

    public Long getUserToGrpId() {
        return this.userToGrpId;
    }

    public void setUserToGrpId(Long userToGrpId) {
        this.userToGrpId = userToGrpId;
    }

    public com.cbrc.auth.hibernate.MUserGrp getMUserGrp() {
        return this.MUserGrp;
    }

    public void setMUserGrp(com.cbrc.auth.hibernate.MUserGrp MUserGrp) {
        this.MUserGrp = MUserGrp;
    }

    public com.cbrc.auth.hibernate.Operator getOperator() {
        return this.operator;
    }

    public void setOperator(com.cbrc.auth.hibernate.Operator operator) {
        this.operator = operator;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("userToGrpId", getUserToGrpId())
            .toString();
    }

    public boolean equals(Object other) {
        if ( !(other instanceof MUserToGrp) ) return false;
        MUserToGrp castOther = (MUserToGrp) other;
        return new EqualsBuilder()
            .append(this.getUserToGrpId(), castOther.getUserToGrpId())
            .isEquals();
    }

    public int hashCode() {
        return new HashCodeBuilder()
            .append(getUserToGrpId())
            .toHashCode();
    }

}
