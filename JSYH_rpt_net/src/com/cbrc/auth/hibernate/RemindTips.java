package com.cbrc.auth.hibernate;

/**
 * ��ʿ����
 * @author jcm
 * @2008-02-19
 */
public class RemindTips{
	/**��ʿ��������*/
	private String rtTitle;
	/**��ʿ�趨����*/
	private String rtDate;
	/**��ʿ�����û�*/
	private String rtUserName;
	/**��ʿ�����û�ID*/
	private String rtUserId;
	
	/**
	 * @param date
	 * @param title
	 * @param id
	 * @param name
	 */
	public RemindTips(String date, String title, String id, String name) {
		super();
		// TODO Auto-generated constructor stub
		rtDate = date;
		rtTitle = title;
		rtUserId = id;
		rtUserName = name;
	}

	/**
	 * @return Returns the rtDate.
	 */
	public String getRtDate() {
		return rtDate;
	}

	/**
	 * @param rtDate The rtDate to set.
	 */
	public void setRtDate(String rtDate) {
		this.rtDate = rtDate;
	}

	/**
	 * @return Returns the rtTitle.
	 */
	public String getRtTitle() {
		return rtTitle;
	}

	/**
	 * @param rtTitle The rtTitle to set.
	 */
	public void setRtTitle(String rtTitle) {
		this.rtTitle = rtTitle;
	}

	/**
	 * @return Returns the rtUserId.
	 */
	public String getRtUserId() {
		return rtUserId;
	}

	/**
	 * @param rtUserId The rtUserId to set.
	 */
	public void setRtUserId(String rtUserId) {
		this.rtUserId = rtUserId;
	}

	/**
	 * @return Returns the rtUserName.
	 */
	public String getRtUserName() {
		return rtUserName;
	}

	/**
	 * @param rtUserName The rtUserName to set.
	 */
	public void setRtUserName(String rtUserName) {
		this.rtUserName = rtUserName;
	}
}
