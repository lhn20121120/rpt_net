package com.cbrc.auth.hibernate;

import java.io.Serializable;
import java.util.Set;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/** @author Hibernate CodeGenerator */
public class Role implements Serializable {

    /** identifier field */
    private Long roleId;

    /** nullable persistent field */
    private String roleName;
    
    private String setOrgId;

    /** persistent field */
    private Set roleTools;

    /** persistent field */
    private Set userRoles;
    
    private String userGrpNm;
    
    private String userGrpId;

    public String getUserGrpNm() {
		return userGrpNm;
	}

	public void setUserGrpNm(String userGrpNm) {
		this.userGrpNm = userGrpNm;
	}

	public String getUserGrpId() {
		return userGrpId;
	}

	public void setUserGrpId(String userGrpId) {
		this.userGrpId = userGrpId;
	}

	/** full constructor */
    public Role(Long roleId, String roleName,String setOrgId,Set roleTools, Set userRoles) {
        this.roleId = roleId;
        this.roleName = roleName;
        this.roleTools = roleTools;
        this.userRoles = userRoles;
        this.setOrgId = setOrgId;
    }

    /** default constructor */
    public Role() {
    }

    /** minimal constructor */
    public Role(Long roleId, Set roleTools, Set userRoles) {
        this.roleId = roleId;
        this.roleTools = roleTools;
        this.userRoles = userRoles;
    }

    public Long getRoleId() {
        return this.roleId;
    }

	public String getSetOrgId() {
		return setOrgId;
	}

	public void setSetOrgId(String setOrgId) {
		this.setOrgId = setOrgId;
	}

	public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public String getRoleName() {
        return this.roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public Set getRoleTools() {
        return this.roleTools;
    }

    public void setRoleTools(Set roleTools) {
        this.roleTools = roleTools;
    }

    public Set getUserRoles() {
        return this.userRoles;
    }

    public void setUserRoles(Set userRoles) {
        this.userRoles = userRoles;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("roleId", getRoleId())
            .toString();
    }

    public boolean equals(Object other) {
        if ( !(other instanceof Role) ) return false;
        Role castOther = (Role) other;
        return new EqualsBuilder()
            .append(this.getRoleId(), castOther.getRoleId())
            .isEquals();
    }

    public int hashCode() {
        return new HashCodeBuilder()
            .append(getRoleId())
            .toHashCode();
    }

}
