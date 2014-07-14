package com.gather.hibernate;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/** @author Hibernate CodeGenerator */
public class MUser implements Serializable {

    /** identifier field */
    private String userId;

    /** nullable persistent field */
    private String name;

    /** nullable persistent field */
    private String dept;
    
    /** nullable persistent field */
    private String password;

    /** persistent field */
    private MOrg MOrg;

    public MUser(String userId, String name, String dept,String password, MOrg MOrg) {
            this.userId=userId;
            this.name=name;
            this.dept=dept;
            this.password=password;
            this.MOrg=MOrg;
    }

    /** default constructor */
    public MUser() {
    }

    /** minimal constructor */
    public MUser(String userId, MOrg MOrg) {
        this.userId = userId;
        this.MOrg = MOrg;
    }

    
    /**
	 * @return Returns the dept.
	 */
	public String getDept() {
		return dept;
	}

	/**
	 * @param dept The dept to set.
	 */
	public void setDept(String dept) {
		this.dept = dept;
	}

	/**
	 * @return Returns the mOrgs.
	 */
	public MOrg getMOrg() {
		return this.MOrg;
	}

	/**
	 * @param orgs The mOrgs to set.
	 */
	public void setMOrg(MOrg MOrg) {
		this.MOrg = MOrg;
	}

	/**
	 * @return Returns the name.
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name The name to set.
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return Returns the userId.
	 */
	public String getUserId() {
		return userId;
	}

	/**
	 * @param userId The userId to set.
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}

    public String toString() {
        return new ToStringBuilder(this)
            .append("userId", getUserId())
            .toString();
    }

    public boolean equals(Object other) {
        if ( !(other instanceof MUser) ) return false;
        MUser castOther = (MUser) other;
        return new EqualsBuilder()
            .append(this.getUserId(), castOther.getUserId())
            .isEquals();
    }

    public int hashCode() {
        return new HashCodeBuilder()
            .append(getUserId())
            .toHashCode();
    }

	/**
	 * @return Returns the password.
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param password The password to set.
	 */
	public void setPassword(String password) {
		this.password = password;
	}

}
