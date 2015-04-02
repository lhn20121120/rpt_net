package com.cbrc.auth.hibernate;

import java.io.Serializable;
import java.util.Set;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/** @author Hibernate CodeGenerator */
public class Department implements Serializable {

    /** identifier field */
    private Long departmentId;

    /** nullable persistent field */
    private String deptName;

    private String orgId;
    
    /** persistent field */
    private com.cbrc.auth.hibernate.ProductUser productUser;

    /** persistent field */
    private Set operators;

    /** full constructor */
    public Department(Long departmentId, String deptName,String orgId, com.cbrc.auth.hibernate.ProductUser productUser, Set operators) {
        this.departmentId = departmentId;
        this.deptName = deptName;
        this.productUser = productUser;
        this.operators = operators;
        this.orgId = orgId;
    }

    /** default constructor */
    public Department() {
    }

    public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

	/** minimal constructor */
    public Department(Long departmentId, com.cbrc.auth.hibernate.ProductUser productUser, Set operators) {
        this.departmentId = departmentId;
        this.productUser = productUser;
        this.operators = operators;
    }

    public Long getDepartmentId() {
        return this.departmentId;
    }

    public void setDepartmentId(Long departmentId) {
        this.departmentId = departmentId;
    }

    public String getDeptName() {
        return this.deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public com.cbrc.auth.hibernate.ProductUser getProductUser() {
        return this.productUser;
    }

    public void setProductUser(com.cbrc.auth.hibernate.ProductUser productUser) {
        this.productUser = productUser;
    }

    public Set getOperators() {
        return this.operators;
    }

    public void setOperators(Set operators) {
        this.operators = operators;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("departmentId", getDepartmentId())
            .toString();
    }

    public boolean equals(Object other) {
        if ( !(other instanceof Department) ) return false;
        Department castOther = (Department) other;
        return new EqualsBuilder()
            .append(this.getDepartmentId(), castOther.getDepartmentId())
            .isEquals();
    }

    public int hashCode() {
        return new HashCodeBuilder()
            .append(getDepartmentId())
            .toHashCode();
    }

}
