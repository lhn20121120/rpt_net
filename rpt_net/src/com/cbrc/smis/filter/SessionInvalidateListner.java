package com.cbrc.smis.filter;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import com.cbrc.smis.common.Config;
import com.cbrc.smis.security.Login;
import com.cbrc.smis.security.Operator;
import com.fitech.gznx.service.OnlineUserUtil;
/*
 * add by 王明明  在session失效时对其进行监控
 */
public class SessionInvalidateListner implements HttpSessionListener {

	@Override
	public void sessionCreated(HttpSessionEvent httpsessionevent) {
		// TODO Auto-generated method stub
		//System.out.println("++++++++++++"+httpsessionevent.getSession().getId());
		
	}

	@Override
	public void sessionDestroyed(HttpSessionEvent httpsessionevent) {
		// TODO Auto-generated method stub
		HttpSession session=httpsessionevent.getSession();
		Operator operator=(Operator)session.getAttribute(Config.OPERATOR_SESSION_ATTRIBUTE_NAME);
		ServletContext application=session.getServletContext();
		if(Config.PORTAL)
			Login.exitPortal(operator.getUserName());
		else{
			//application.removeAttribute(operator.getUserName());
			OnlineUserUtil.removeOnlineUser(operator);
		}
		
	}

}
