package com.fitech.gznx.util;

import javax.servlet.http.HttpSession;

public class OnlineUser {
	private String  sessionId=null;
	private String ipAdd = null;
	private String userName = null;
	
	public String getSessionId() {
		return sessionId;
	}
	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}
	public String getIpAdd() {
		return ipAdd;
	}
	public void setIpAdd(String ipAdd) {
		this.ipAdd = ipAdd;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}

}
