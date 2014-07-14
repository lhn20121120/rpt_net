package com.cbrc.auth.hibernate;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/** @author Hibernate CodeGenerator */
public class RoleTool implements Serializable {

    /** identifier field */
    private Long roleToolId;

    /** persistent field */
    private com.cbrc.auth.hibernate.ToolSetting toolSetting;

    /** persistent field */
    private com.cbrc.auth.hibernate.Role role;

    /** full constructor */
    public RoleTool(Long roleToolId, com.cbrc.auth.hibernate.ToolSetting toolSetting, com.cbrc.auth.hibernate.Role role) {
        this.roleToolId = roleToolId;
        this.toolSetting = toolSetting;
        this.role = role;
    }

    /** default constructor */
    public RoleTool() {
    }

    public Long getRoleToolId() {
        return this.roleToolId;
    }

    public void setRoleToolId(Long roleToolId) {
        this.roleToolId = roleToolId;
    }

    public com.cbrc.auth.hibernate.ToolSetting getToolSetting() {
        return this.toolSetting;
    }

    public void setToolSetting(com.cbrc.auth.hibernate.ToolSetting toolSetting) {
        this.toolSetting = toolSetting;
    }

    public com.cbrc.auth.hibernate.Role getRole() {
        return this.role;
    }

    public void setRole(com.cbrc.auth.hibernate.Role role) {
        this.role = role;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("roleToolId", getRoleToolId())
            .toString();
    }

    public boolean equals(Object other) {
        if ( !(other instanceof RoleTool) ) return false;
        RoleTool castOther = (RoleTool) other;
        return new EqualsBuilder()
            .append(this.getRoleToolId(), castOther.getRoleToolId())
            .isEquals();
    }

    public int hashCode() {
        return new HashCodeBuilder()
            .append(getRoleToolId())
            .toHashCode();
    }

}
