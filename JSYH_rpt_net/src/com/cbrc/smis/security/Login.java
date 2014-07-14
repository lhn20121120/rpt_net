package com.cbrc.smis.security;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.cbrc.smis.common.Config;
import com.cbrc.smis.util.FitechException;
import com.fitech.gznx.service.OnlineUserUtil;
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
			}
		}catch(Exception e){
			log.printStackTrace(e);
		}
	}
}
