package com.cbrc.auth.hibernate;

/**
 * 贴士提醒
 * @author jcm
 * @2008-02-19
 */
public class RemindTips{
	/**贴士标题名称*/
	private String rtTitle;
	/**贴士设定日期*/
	private String rtDate;
	/**贴士发布用户*/
	private String rtUserName;
	/**贴士发布用户ID*/
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
