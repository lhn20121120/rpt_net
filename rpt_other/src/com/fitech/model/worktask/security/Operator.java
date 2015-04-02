package com.fitech.model.worktask.security;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class Operator implements IOperator,Serializable {

	private String business;
	
	private String roleIds;
	
	private Long operatorId;
	
	private String operatorName;
	
	private boolean superManager;
	
	private Map roles;
	
	private String telephone;
	
	private String userName;

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}
	public String getBusiness() {
		return business;
	}

	public void setBusiness(String business) {
		this.business = business;
	}

	/**
     * 机构ID
     */
    private String orgId=null;
	 /**
     * 机构名称
     */
    private String orgName = null;
	
	public Map getRoles() {
		return roles;
	}

	public void setRoles(Map roles) {
		this.roles = roles;
	}

	@Override
	public String getChildOrgIds() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getChildRepCheckPodedom() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getChildRepPodedomSQL() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getChildRepReportPopedom() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getChildRepSearchPopedom() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getChildRepVerifyPopedom() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getDeptName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getIpAdd() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List getMenuUrls() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getNoLoginUrl() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Long getOperatorId() {
		// TODO Auto-generated method stub
		return this.operatorId;
	}

	@Override
	public String getOperatorName() {
		// TODO Auto-generated method stub
		return this.operatorName;
	}

	@Override
	public String getOrgId() {
		// TODO Auto-generated method stub
		return this.orgId;
	}

	@Override
	public String getOrgName() {
		// TODO Auto-generated method stub
		return this.orgName;
	}

	@Override
	public String getOrgPopedomSQL() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List getOrgPurviewList() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getRoleIds() {
		// TODO Auto-generated method stub
		return this.roleIds;
	}

	@Override
	public String getSessionId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getSubOrgIds() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getUserGrpIds() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getUserName() {
		// TODO Auto-generated method stub
		return this.userName;
	}

	@Override
	public boolean isExitsThisUrl(String url) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isSuperManager() {
		// TODO Auto-generated method stub
		return superManager;
	}

	@Override
	public void reFreshOrgTree() {
		// TODO Auto-generated method stub

	}

	@Override
	public void reFreshOrgTreeHZRule() {
		// TODO Auto-generated method stub

	}

	@Override
	public void setChildOrgIds(String childOrgIds) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setChildRepCheckPodedom(String childRepCheckPodedom) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setChildRepPodedomSQL(String childRepPodedomSQL) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setChildRepReportPopedom(String childRepReportPopedom) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setChildRepSearchPopedom(String childRepSearchPopedom) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setChildRepVerifyPopedom(String childRepVerifyPopedom) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setDeptName(String deptName) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setIpAdd(String ipAdd) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setMenuUrls(List menuUrls) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setNoLoginUrl(String noLoginUrl) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setOperatorId(Long operatorId) {
		// TODO Auto-generated method stub
		this.operatorId=operatorId;
	}

	@Override
	public void setOperatorName(String operatorName) {
		// TODO Auto-generated method stub
		this.operatorName = operatorName;
	}

	@Override
	public void setOrgId(String orgId) {
		// TODO Auto-generated method stub
		this.orgId= orgId;
	}

	@Override
	public void setOrgName(String orgName) {
		// TODO Auto-generated method stub
		this.orgName= orgName;
	}

	@Override
	public void setOrgPopedomSQL(String orgPopedomSQL) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setOrgPurviewList(List orgPurviewList) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setRoleIds(String roleIds) {
		// TODO Auto-generated method stub
		this.roleIds=roleIds;
	}

	@Override
	public void setSessionId(String sessionId) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setSubOrgIds(String subOrgIds) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setSuperManager(boolean isSuperManager) {
		// TODO Auto-generated method stub
		this.superManager = isSuperManager;
	}

	@Override
	public void setUserGrpIds(String userGrpIds) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setUserName(String userName) {
		// TODO Auto-generated method stub
		this.userName=userName;
	}

}
