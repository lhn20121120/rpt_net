package com.gather.down.reports;

public class AgentRelationBean {

	private int id=0;     //�������к�(java)
	private String parentAffiliationID="";   //������id(��ͬ��ֵ)
	private String sonAffiliationID="";   //����������id
	private String startTime="";       //��ʼʱ��
	private String endTime="";         //����ʱ��
	private String vailid="";           //��
	/**
	 * @return Returns the endTime.
	 */
	public String getEndTime() {
		return endTime;
	}
	/**
	 * @param endTime The endTime to set.
	 */
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	/**
	 * @return Returns the id.
	 */
	public int getId() {
		return id;
	}
	/**
	 * @param id The id to set.
	 */
	public void setId(int id) {
		this.id = id;
	}
	/**
	 * @return Returns the parentAffiliationID.
	 */
	public String getParentAffiliationID() {
		return parentAffiliationID;
	}
	/**
	 * @param parentAffiliationID The parentAffiliationID to set.
	 */
	public void setParentAffiliationID(String parentAffiliationID) {
		this.parentAffiliationID = parentAffiliationID;
	}
	/**
	 * @return Returns the sonAffiliationID.
	 */
	public String getSonAffiliationID() {
		return sonAffiliationID;
	}
	/**
	 * @param sonAffiliationID The sonAffiliationID to set.
	 */
	public void setSonAffiliationID(String sonAffiliationID) {
		this.sonAffiliationID = sonAffiliationID;
	}
	/**
	 * @return Returns the startTime.
	 */
	public String getStartTime() {
		return startTime;
	}
	/**
	 * @param startTime The startTime to set.
	 */
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	/**
	 * @return Returns the vailid.
	 */
	public String getVailid() {
		return vailid;
	}
	/**
	 * @param vailid The vailid to set.
	 */
	public void setVailid(String vailid) {
		this.vailid = vailid;
	}

}
