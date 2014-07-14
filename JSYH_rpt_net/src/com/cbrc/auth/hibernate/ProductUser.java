package com.cbrc.auth.hibernate;

import java.io.Serializable;
import java.util.Set;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/** @author Hibernate CodeGenerator */
public class ProductUser implements Serializable {

    /** identifier field */
    private Long productUserId;

    /** nullable persistent field */
    private String productUserName;
    
    /**
     * 是否是当前系统用户标识
     */
    private String flag;
    
    /** persistent field */
    private Set departments;

    /** full constructor */
    public ProductUser(Long productUserId, String productUserName, String flag,Set departments) {
        this.productUserId = productUserId;
        this.productUserName = productUserName;
        this.flag=flag;
        this.departments = departments;
    }

    /** default constructor */
    public ProductUser() {
    }

    /** minimal constructor */
    public ProductUser(Long productUserId, Set departments) {
        this.productUserId = productUserId;
        this.departments = departments;
    }

    public Long getProductUserId() {
        return this.productUserId;
    }

    public void setProductUserId(Long productUserId) {
        this.productUserId = productUserId;
    }

    public String getProductUserName() {
        return this.productUserName;
    }

    public void setProductUserName(String productUserName) {
        this.productUserName = productUserName;
    }

    public Set getDepartments() {
        return this.departments;
    }

    public void setDepartments(Set departments) {
        this.departments = departments;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("productUserId", getProductUserId())
            .toString();
    }

    public boolean equals(Object other) {
        if ( !(other instanceof ProductUser) ) return false;
        ProductUser castOther = (ProductUser) other;
        return new EqualsBuilder()
            .append(this.getProductUserId(), castOther.getProductUserId())
            .isEquals();
    }

    public int hashCode() {
        return new HashCodeBuilder()
            .append(getProductUserId())
            .toHashCode();
    }

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

}
