package com.gather.security;

public class Org {
	
	/**
	 * ��ǰ�û��Ļ���id
	 */
	private String OrgId;
	/**
	 * ��ǰ�û��Ļ�������
	 */
	private String OrgName;
	/**
	 * ��ǰ�û��Ļ�������
	 */
	private int OrgType;
	public Org(){}
	public Org(String OrgId,String OrgName,int OrgType){
		this.OrgId=OrgId;
		this.OrgName=OrgName;
		this.OrgType=OrgType;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}



	/**
	 * @return Returns the orgId.
	 */
	public String getOrgId() {
		return OrgId;
	}



	/**
	 * @param orgId The orgId to set.
	 */
	public void setOrgId(String orgId) {
		OrgId = orgId;
	}



	/**
	 * @return Returns the orgName.
	 */
	public String getOrgName() {
		return OrgName;
	}



	/**
	 * @param orgName The orgName to set.
	 */
	public void setOrgName(String orgName) {
		OrgName = orgName;
	}



	/**
	 * @return Returns the orgType.
	 */
	public int getOrgType() {
		return OrgType;
	}



	/**
	 * @param orgType The orgType to set.
	 */
	public void setOrgType(int orgType) {
		OrgType = orgType;
	}

}
