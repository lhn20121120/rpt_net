package com.cbrc.smis.security;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;

import com.cbrc.smis.common.Config;
import com.cbrc.smis.util.FitechException;
import com.fitech.gznx.service.OnlineUserUtil;
import com.fitech.gznx.service.webservice.OperatorService;
;
/**
 * 用户登录操作类
 * 
 * @author rds
 * @date 2006-01-04
 */
public class Login {
	private FitechException log=new FitechException(Login.class);
	
	/**
	 * 用户注销操作
	 * 
	 * @return void
	 */
	public void exit(HttpServletRequest request){
		try{
			HttpSession session=request.getSession();
			
			if(session.getAttribute(Config.OPERATOR_SESSION_ATTRIBUTE_NAME)!=null){
				Operator operator = (Operator)session.getAttribute(Config.OPERATOR_SESSION_ATTRIBUTE_NAME);
				OnlineUserUtil.removeOnlineUser(operator);
				session.removeAttribute(Config.OPERATOR_SESSION_ATTRIBUTE_NAME);
				session.invalidate();
			}
		}catch(Exception e){
			log.printStackTrace(e);
		}
	}
	public static void exitPortal(String userName){
		String wbsurl = "http://localhost:" + Config.WEB_SERVER_PORT + "/portal/services/OperatorService";
		JaxWsProxyFactoryBean factory = new JaxWsProxyFactoryBean();  
        factory.setServiceClass(OperatorService.class);  
        factory.setAddress(wbsurl);
        OperatorService operatorService = (OperatorService)factory.create();
        operatorService.logOutAll(userName);
        operatorService.logOut(userName);
	}
}
