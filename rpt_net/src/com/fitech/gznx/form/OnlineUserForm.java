package com.fitech.gznx.form;

import org.apache.struts.action.ActionForm;

public class OnlineUserForm extends ActionForm {

	/**用户ID*/
	private String operatorId;
	
	/**用户名*/
	private String operatorName;
	
	/**机构ID*/
	private String orgId;
	
	/**机构名*/
	private String orgName;
	
	/**部门名*/
	private String deptName;
	
	/**用户类型ID*/
	private Integer userTypeId;
	
	/**用户类型名*/
	private String userTypeName;
	
	/**IP地址*/
	private String ipAdd;
	
	private String treeOrgContent;

	public String getDeptName() {
		return deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}

	public String getIpAdd() {
		return ipAdd;
	}

	public void setIpAdd(String ipAdd) {
		this.ipAdd = ipAdd;
	}

	public String getOperatorId() {
		return operatorId;
	}

	public void setOperatorId(String operatorId) {
		this.operatorId = operatorId;
	}

	public String getOperatorName() {
		return operatorName;
	}

	public void setOperatorName(String operatorName) {
		this.operatorName = operatorName;
	}

	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

	public String getOrgName() {
		return orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

	
	public Integer getUserTypeId() {
		return userTypeId;
	}

	public void setUserTypeId(Integer userTypeId) {
		this.userTypeId = userTypeId;
	}

	public String getUserTypeName() {
		return userTypeName;
	}

	public void setUserTypeName(String userTypeName) {
		this.userTypeName = userTypeName;
	}
	public String getTreeOrgContent() {
		return treeOrgContent;
	}

	public void setTreeOrgContent(String treeOrgContent) {
		this.treeOrgContent = treeOrgContent;
	}

}
