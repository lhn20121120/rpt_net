package com.cbrc.org.entity;

public class SysUserRole {

	public Integer roleId;
	public String userName;
	public Integer getRoleId() {
		return roleId;
	}
	public void setRoleId(Integer roleId) {
		this.roleId = roleId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public SysUserRole(Integer roleId, String userName) {
		this.roleId = roleId;
		this.userName = userName;
	}
	public SysUserRole() {
	}
	
}
