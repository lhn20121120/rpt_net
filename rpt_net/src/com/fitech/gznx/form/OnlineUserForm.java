package com.fitech.gznx.form;

import org.apache.struts.action.ActionForm;

public class OnlineUserForm extends ActionForm {

	/**�û�ID*/
	private String operatorId;
	
	/**�û���*/
	private String operatorName;
	
	/**����ID*/
	private String orgId;
	
	/**������*/
	private String orgName;
	
	/**������*/
	private String deptName;
	
	/**�û�����ID*/
	private Integer userTypeId;
	
	/**�û�������*/
	private String userTypeName;
	
	/**IP��ַ*/
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
