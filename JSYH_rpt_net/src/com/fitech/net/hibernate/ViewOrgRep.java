package com.fitech.net.hibernate;
/**
 *������
 *���ڣ�2008-1-2
 *���ߣ��ܷ���
 */
public class ViewOrgRep  implements java.io.Serializable {
	private String orgRepId;
	private String orgId;
	private String childRepId;
	private Integer powType;
	private Integer userId;
	/**
	 * ���� childRepId
	 */
	public String getChildRepId() {
		return childRepId;
	}
	/**
	 * ������childRepId 
	 * ���� childRepId
	 */
	public void setChildRepId(String childRepId) {
		this.childRepId = childRepId;
	}
	/**
	 * ���� orgId
	 */
	public String getOrgId() {
		return orgId;
	}
	/**
	 * ������orgId 
	 * ���� orgId
	 */
	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}
	/**
	 * ���� orgRepId
	 */
	public String getOrgRepId() {
		return orgRepId;
	}
	/**
	 * ������orgRepId 
	 * ���� orgRepId
	 */
	public void setOrgRepId(String orgRepId) {
		this.orgRepId = orgRepId;
	}
	/**
	 * ���� powType
	 */
	public Integer getPowType() {
		return powType;
	}
	/**
	 * ������powType 
	 * ���� powType
	 */
	public void setPowType(Integer powType) {
		this.powType = powType;
	}
	/**
	 * ���� userId
	 */
	public Integer getUserId() {
		return userId;
	}
	/**
	 * ������userId 
	 * ���� userId
	 */
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
}

