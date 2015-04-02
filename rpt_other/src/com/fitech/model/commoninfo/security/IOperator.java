package com.fitech.model.commoninfo.security;

import java.util.List;

public interface IOperator {

	/**
	 * 取得功能菜单权限url
	 * @return List
	 */
	public abstract List getMenuUrls();

	public abstract void setMenuUrls(List menuUrls);

	/**
	 * @return Returns the childRepCheckPodedom.
	 */
	public abstract String getChildRepCheckPodedom();

	/**
	 * @param childRepCheckPodedom The childRepCheckPodedom to set.
	 */
	public abstract void setChildRepCheckPodedom(String childRepCheckPodedom);

	/**
	 * @return Returns the childRepReportPopedom.
	 */
	public abstract String getChildRepReportPopedom();

	/**
	 * @param childRepReportPopedom The childRepReportPopedom to set.
	 */
	public abstract void setChildRepReportPopedom(String childRepReportPopedom);

	/**
	 * @return Returns the childRepSearchPopedom.
	 */
	public abstract String getChildRepSearchPopedom();

	/**
	 * @param childRepSearchPopedom The childRepSearchPopedom to set.
	 */
	public abstract void setChildRepSearchPopedom(String childRepSearchPopedom);

	/**
	 * 所属部门名称
	 * @return String
	 */
	public abstract String getDeptName();

	public abstract void setDeptName(String deptName);

	/**
	 * 操作员姓名
	 * @return String
	 */
	public abstract String getOperatorName();

	public abstract void setOperatorName(String operatorName);

	/**
	 * 所属机构名称
	 * @return String
	 */
	public abstract String getOrgName();

	public abstract void setOrgName(String orgName);

	/**
	 * 判断该url在url集合中是否存在
	 * @param url String url     
	 * @param url
	 * @return 存在则返回true
	 */
	public abstract boolean isExitsThisUrl(String url);

	/**
	 * 用户组id串
	 * @return
	 */
	public abstract String getUserGrpIds();

	public abstract void setUserGrpIds(String userGrpIds);

	public abstract Long getOperatorId();

	public abstract void setOperatorId(Long operatorId);

	public abstract String getOrgId();

	public abstract void setOrgId(String orgId);

	public abstract String getNoLoginUrl();

	public abstract void setNoLoginUrl(String noLoginUrl);

	public abstract boolean isSuperManager();

	public abstract void setSuperManager(boolean isSuperManager);

	public abstract String getRoleIds();

	public abstract void setRoleIds(String roleIds);

	public abstract String getSubOrgIds();

	public abstract void setSubOrgIds(String subOrgIds);

	public abstract String getChildOrgIds();

	public abstract void setChildOrgIds(String childOrgIds);

	public abstract String getChildRepPodedomSQL();

	public abstract void setChildRepPodedomSQL(String childRepPodedomSQL);

	public abstract String getOrgPopedomSQL();

	public abstract void setOrgPopedomSQL(String orgPopedomSQL);

	public abstract String getSessionId();

	public abstract void setSessionId(String sessionId);

	public abstract String getIpAdd();

	public abstract void setIpAdd(String ipAdd);

	public abstract String getUserName();

	public abstract void setUserName(String userName);

	public abstract String getChildRepVerifyPopedom();

	public abstract void setChildRepVerifyPopedom(String childRepVerifyPopedom);

	public abstract List getOrgPurviewList();

	public abstract void setOrgPurviewList(List orgPurviewList);

	/**
	 * 
	 * title:该方法用于刷新用户系统查询权限机构树(应用查询用) author:chenbing date:2008-3-12
	 * 
	 * @return
	 */
	public abstract void reFreshOrgTree();

	/**
	 * 
	 * title:该方法用于刷新用户系统查询权限机构树(应用查询用) author:chenbing date:2008-3-12
	 * 
	 * @return
	 */
	public abstract void reFreshOrgTreeHZRule();

}