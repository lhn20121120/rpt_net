package com.gather.refer.data;

/*
 * @author linfeng
 * @function 把报表和机构信息关联起来的中间bean
 * 
 */

public class MediumOrgReportBean {

	private String childRepId="";
	private String verionid="";
    private String repName="";
    
    private String orgId="";
    private String orgName="";
	/**
	 * @return Returns the childRepId.
	 */
	public String getChildRepId() {
		return childRepId;
	}
	/**
	 * @param childRepId The childRepId to set.
	 */
	public void setChildRepId(String childRepId) {
		this.childRepId = childRepId;
	}
	/**
	 * @return Returns the orgId.
	 */
	public String getOrgId() {
		return orgId;
	}
	/**
	 * @param orgId The orgId to set.
	 */
	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}
	/**
	 * @return Returns the orgName.
	 */
	public String getOrgName() {
		return orgName;
	}
	/**
	 * @param orgName The orgName to set.
	 */
	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}
	/**
	 * @return Returns the repName.
	 */
	public String getRepName() {
		return repName;
	}
	/**
	 * @param repName The repName to set.
	 */
	public void setRepName(String repName) {
		this.repName = repName;
	}
	/**
	 * @return Returns the verionid.
	 */
	public String getVerionid() {
		return verionid;
	}
	/**
	 * @param verionid The verionid to set.
	 */
	public void setVerionid(String verionid) {
		this.verionid = verionid;
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
