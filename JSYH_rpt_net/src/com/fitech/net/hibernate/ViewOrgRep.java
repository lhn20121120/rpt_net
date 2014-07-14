package com.fitech.net.hibernate;
/**
 *描述：
 *日期：2008-1-2
 *作者：曹发根
 */
public class ViewOrgRep  implements java.io.Serializable {
	private String orgRepId;
	private String orgId;
	private String childRepId;
	private Integer powType;
	private Integer userId;
	/**
	 * 返回 childRepId
	 */
	public String getChildRepId() {
		return childRepId;
	}
	/**
	 * 参数：childRepId 
	 * 设置 childRepId
	 */
	public void setChildRepId(String childRepId) {
		this.childRepId = childRepId;
	}
	/**
	 * 返回 orgId
	 */
	public String getOrgId() {
		return orgId;
	}
	/**
	 * 参数：orgId 
	 * 设置 orgId
	 */
	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}
	/**
	 * 返回 orgRepId
	 */
	public String getOrgRepId() {
		return orgRepId;
	}
	/**
	 * 参数：orgRepId 
	 * 设置 orgRepId
	 */
	public void setOrgRepId(String orgRepId) {
		this.orgRepId = orgRepId;
	}
	/**
	 * 返回 powType
	 */
	public Integer getPowType() {
		return powType;
	}
	/**
	 * 参数：powType 
	 * 设置 powType
	 */
	public void setPowType(Integer powType) {
		this.powType = powType;
	}
	/**
	 * 返回 userId
	 */
	public Integer getUserId() {
		return userId;
	}
	/**
	 * 参数：userId 
	 * 设置 userId
	 */
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
}

