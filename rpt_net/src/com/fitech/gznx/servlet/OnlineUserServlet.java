package com.fitech.gznx.servlet;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import com.fitech.gznx.service.OnlineUserUtil;
import com.cbrc.smis.security.Operator;

public class OnlineUserServlet implements HttpSessionListener {

	private Operator operator = null;

	public void sessionCreated(HttpSessionEvent hse) {

	}

	public void sessionDestroyed(HttpSessionEvent hse) {
		HttpSession session = hse.getSession();
		String sessionId = session.getId();
		OnlineUserUtil.removeOnlineUser(sessionId);
	}
}