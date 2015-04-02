//Created by MyEclipse Struts
// XSL source (default): platform:/plugin/com.genuitec.eclipse.cross.easystruts.eclipse_4.0.1/xslt/JavaClass.xsl

package com.cbrc.auth.action;

import java.io.IOException;
import java.util.Locale;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.MessageResources;

import com.cbrc.auth.adapter.StrutsOperatorDelegate;
import com.cbrc.auth.form.OperatorForm;
import com.cbrc.smis.common.Config;
import com.cbrc.smis.security.Operator;
import com.cbrc.smis.security.OperatorHandler;
import com.cbrc.smis.util.FitechLog;
import com.cbrc.smis.util.FitechMessages;
import com.cbrc.smis.util.FitechResource;
import com.fitech.gznx.common.DateUtil;
import com.fitech.gznx.service.OnlineUserUtil;





/** 
 *  用户登录
 * @author 姚捷
 * 
 * @struts.action path="/userLogin" name="operatorForm" scope="request"
 * @struts.action-forward name="index" path="/index.jsp"
 * @struts.action-forward name="login" path="/login.jsp"
 * @struts.action-forward name="ssoLogin" path="/ssoLogin.jsp"
 */
public class UserLoginAction extends Action
{
	static boolean first=false;
	private  ServletContext application=null;
	/** 
	 * 已使用hibernate 卞以刚 2011-12-21 
	 * 影响对象：Operator OrgNet RoleTool MUserToGrp UserRole
	 * Method execute
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return ActionForward
	 */
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException
	{  
		String userName = request.getParameter("name");
		request.getSession().setAttribute("reportPortalFlag",request.getParameter("flag"));
		request.getSession().setAttribute("business", request.getParameter("business"));
		HttpSession session = request.getSession(false);	
		if (application==null) {
			
			 application=request.getSession().getServletContext();
		}
		
		boolean flag = false;
		if (request.getSession().getAttribute(Config.OPERATOR_SESSION_ATTRIBUTE_NAME) == null) {
			flag = true;
		} else {
			flag = false;
		}
		if(Config.PORTAL){//徽商银行联动判断
			if(flag){
				if(!isTokenValid(request) && userName==null){//如果两个值相等,即表单重复提交   
		
		            this.saveToken(request);   
		            
		            return new ActionForward("/prelogin.jsp");   
		
		        }
				
			}
			//当用户首次提交时返!isTokenValid()返回true   
	        this.resetToken(request);//将用户session中的token清空 
		}
		
		first=true;
		MessageResources resources = getResources(request);
		FitechMessages messages = new FitechMessages();
		Locale locale = request.getLocale();
		if(Config.WEBROOTULR.equals("")||Config.WEBROOTULR.indexOf("localhost")>-1){
			Config.WEBROOTULR="http://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath();
			if(!Config.WEBROOTULR.endsWith("/"))
				Config.WEBROOTULR += "/";
			Config.PDF_TEMPLATE_URL = Config.WEBROOTULR + "template/pdf/";
			Config.TEMP_DIR_WEB_PATH = Config.WEBROOTULR + "tmp/" ;
		}
		
		Config.FITOSA_URL = "http://"+request.getServerName()+":"+request.getServerPort()+"/FITOSA/login.action";
		
		OperatorForm operatorForm = (OperatorForm) form;
		//判断用户名对应的session是否已存在
//		operatorForm.setPassword("null");
		/**查询该用户是否存在，如果存在则返回他的form,否则则返回null*/
		/**已使用hibernate 卞以刚 2011-12-27
		 * 影响对象：Operator*/
		//判断是否从报送平台登录的 
		if(userName!=null){
			operatorForm.setUserName(userName);
		}
		OperatorForm resultForm = StrutsOperatorDelegate.userLoginValidate(operatorForm,userName);
//		operatorForm.setPassword("null");
//		resultForm.setPassword("null");
		/**如果不存在，则登录错误*/
		if (resultForm == null)
		{
			messages.add(FitechResource.getMessage(locale, resources, "userLogin.false"));
			request.setAttribute(Config.MESSAGES, messages);
			if(userName!=null){
				response.sendRedirect((String) request.getSession().getAttribute("portal_url"));
				return null;
			}
			return mapping.findForward("login");
		} else
		{
			HttpSession otherSession=(HttpSession)application.getAttribute(operatorForm.getUserName());
			if (otherSession!=null) {
				//session.removeAttribute(Config.REPORT_SESSION_FLG);
				//session.removeAttribute(Config.OPERATOR_SESSION_ATTRIBUTE_NAME);
				//session.removeAttribute(Config.USER_LOGIN_DATE);
				//session.removeAttribute("OPERATORFORM");
				
				if(session!=otherSession){
					OnlineUserUtil.removeOnlineUser(otherSession.getId());
					try{
						
						otherSession.invalidate();
					}catch(Exception e){
						System.out.println("session已注销");
					}
					otherSession=null;
				}
				application.removeAttribute(operatorForm.getUserName().trim());
			}
			
//			if(OnlineUserUtil.viewOnlineUser(resultForm.getUserName())) {
//				OnlineUserUtil.removeUserName(resultForm.getUserName());
//			} 
			/**登录成功则保存用户信息在Session内*/
			OperatorHandler operatorHandler = new OperatorHandler();
			/**已使用hibernate 卞以刚 2011-12-27
			 * 影响对象：OrgNet RoleTool MUserToGrp UserRole**/
			Operator operator = operatorHandler.saveUserInfo(resultForm);
						
			
			operator.setSessionId(session.getId());
			operator.setIpAdd(request.getRemoteAddr());
			if (session.getAttribute(Config.REPORT_SESSION_FLG) != null)
				session.removeAttribute(Config.REPORT_SESSION_FLG);
			if (session.getAttribute(Config.OPERATOR_SESSION_ATTRIBUTE_NAME) != null)
				session.removeAttribute(Config.OPERATOR_SESSION_ATTRIBUTE_NAME);
			if(session.getAttribute(Config.USER_LOGIN_DATE) != null){
				session.removeAttribute(Config.USER_LOGIN_DATE);
			}
			if(operatorForm.getLoginDate() ==null || operatorForm.getLoginDate().equals("")){
				String yestoday = DateUtil.getYestoday();
				operatorForm.setLoginDate(yestoday);
			}
			session.setAttribute(Config.USER_LOGIN_DATE, operatorForm.getLoginDate());
			
			if(null==operatorForm.getPassword()|| "".equals(operatorForm.getPassword())){
				request.getSession().setAttribute(Config.USER_LOGIN_DATE,DateUtil.getLastMonth(DateUtil.getToday()));
			}
			session.setAttribute(Config.OPERATOR_SESSION_ATTRIBUTE_NAME, operator);
			session.setAttribute("OPERATORFORM", operatorForm);
			//将用户对应的session保存到application中
			OnlineUserUtil.addOnlineUser(operator,request.getSession());
			application.setAttribute(operator.getUserName().trim(), session);
			FitechLog.writeLoginLog(com.gather.common.Config.LOG_LOGIN,operatorForm.getUserName(),operatorForm.getUserName()+"用户成功登录","");
			return mapping.findForward("index");			
		}
	}
	
	
}
