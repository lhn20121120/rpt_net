package com.cbrc.auth.hibernate;

import java.io.Serializable;
import java.util.Set;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/** @author Hibernate CodeGenerator */
public class ToolSetting implements Serializable {

    /** identifier field */
    private Long menuId;

    /** nullable persistent field */
    private String menuName;

    /** nullable persistent field */
    private String functionName;

    /** nullable persistent field */
    private String url;

    /** nullable persistent field */
    private Long priorId;

    /** persistent field */
    private Set roleTools;       

    /** full constructor */
    public ToolSetting(Long menuId, String menuName, String functionName, String url, Long priorId, Set roleTools) {
        this.menuId = menuId;
        this.menuName = menuName;
        this.functionName = functionName;
        this.url = url;
        this.priorId = priorId;
        this.roleTools = roleTools;       
    }

    /** default constructor */
    public ToolSetting() {
    }

    /** minimal constructor */
    public ToolSetting(Long menuId, Set roleTools) {
        this.menuId = menuId;
        this.roleTools = roleTools;
    }

    public Long getMenuId() {
        return this.menuId;
    }

    public void setMenuId(Long menuId) {
        this.menuId = menuId;
    }

    public String getMenuName() {
        return this.menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }

    public String getFunctionName() {
        return this.functionName;
    }

    public void setFunctionName(String functionName) {
        this.functionName = functionName;
    }

    public String getUrl() {
        return this.url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Long getPriorId() {
        return this.priorId;
    }

    public void setPriorId(Long priorId) {
        this.priorId = priorId;
    }

    public Set getRoleTools() {
        return this.roleTools;
    }

    public void setRoleTools(Set roleTools) {
        this.roleTools = roleTools;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("menuId", getMenuId())
            .toString();
    }

    public boolean equals(Object other) {
        if ( !(other instanceof ToolSetting) ) return false;
        ToolSetting castOther = (ToolSetting) other;
        return new EqualsBuilder()
            .append(this.getMenuId(), castOther.getMenuId())
            .isEquals();
    }

    public int hashCode() {
        return new HashCodeBuilder()
            .append(getMenuId())
            .toHashCode();
    }	
}
