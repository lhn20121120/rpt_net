package com.cbrc.auth.hibernate;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/** @author Hibernate CodeGenerator */
public class UserRole implements Serializable {

    /** identifier field */
    private Long userRoleId;

    /** persistent field */
    private com.cbrc.auth.hibernate.Role role;

    /** persistent field */
    private com.cbrc.auth.hibernate.Operator operator;

    /** full constructor */
    public UserRole(Long userRoleId, com.cbrc.auth.hibernate.Role role, com.cbrc.auth.hibernate.Operator operator) {
        this.userRoleId = userRoleId;
        this.role = role;
        this.operator = operator;
    }

    /** default constructor */
    public UserRole() {
    }

    public Long getUserRoleId() {
        return this.userRoleId;
    }

    public void setUserRoleId(Long userRoleId) {
        this.userRoleId = userRoleId;
    }

    public com.cbrc.auth.hibernate.Role getRole() {
        return this.role;
    }

    public void setRole(com.cbrc.auth.hibernate.Role role) {
        this.role = role;
    }

    public com.cbrc.auth.hibernate.Operator getOperator() {
        return this.operator;
    }

    public void setOperator(com.cbrc.auth.hibernate.Operator operator) {
        this.operator = operator;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("userRoleId", getUserRoleId())
            .toString();
    }

    public boolean equals(Object other) {
        if ( !(other instanceof UserRole) ) return false;
        UserRole castOther = (UserRole) other;
        return new EqualsBuilder()
            .append(this.getUserRoleId(), castOther.getUserRoleId())
            .isEquals();
    }

    public int hashCode() {
        return new HashCodeBuilder()
            .append(getUserRoleId())
            .toHashCode();
    }

}
