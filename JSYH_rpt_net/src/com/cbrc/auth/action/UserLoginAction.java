package com.cbrc.auth.action;

import java.io.IOException;
import java.util.Locale;

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
import com.cbrc.auth.util.ApplicationPropertiesUtil;
import com.cbrc.smis.common.Config;
import com.cbrc.smis.security.Operator;
import com.cbrc.smis.security.OperatorHandler;
import com.cbrc.smis.util.FitechMessages;
import com.cbrc.smis.util.FitechResource;
import com.fitech.gznx.common.DateUtil;
import com.fitech.papp.webservice.client.AuthWebServiceStub;
import com.fitech.papp.webservice.client.AuthWebServiceStub.SetSession;
import com.fitech.papp.webservice.client.AuthWebServiceStub.SetSessionResponse;
import com.fitech.papp.webservice.util.Blowfish;

/**
 * 
 * 
 * @struts.action path="/userLogin" name="operatorForm" scope="request"
 * @struts.action-forward name="index" path="/index.jsp"
 * @struts.action-forward name="login" path="/login.jsp"
 * @struts.action-forward name="ssoLogin" path="/ssoLogin.jsp"
 */
public class UserLoginAction extends Action {
	static boolean first = false;

	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		boolean flag = false;
		String userName = request.getParameter("username");
		if (userName != null && !userName.equals("")) {
			Blowfish bf = new Blowfish();
			userName = bf.decode(userName);
		}
		if (request.getSession().getAttribute(
				Config.OPERATOR_SESSION_ATTRIBUTE_NAME) == null) {
			flag = true;
		} else {
			flag = false;
		}

		if (Config.PORTAL) {
			if (flag) {
				if (!isTokenValid(request) && userName == null) {// 如果两个值相等,即表单重复提交
					this.saveToken(request);
					return new ActionForward("/prelogin.jsp");
				}
			}
			// 当用户首次提交时返!isTokenValid()返回true
			this.resetToken(request);// 将用户session中的token清空
		}

		first = true;
		MessageResources resources = getResources(request);
		FitechMessages messages = new FitechMessages();
		Locale locale = request.getLocale();
		if (Config.WEBROOTULR.equals("")
				|| Config.WEBROOTULR.indexOf("localhost") > -1) {
			Config.WEBROOTULR = "http://" + request.getServerName() + ":"
					+ request.getServerPort() + request.getContextPath();
			if (!Config.WEBROOTULR.endsWith("/"))
				Config.WEBROOTULR += "/";
			Config.PDF_TEMPLATE_URL = Config.WEBROOTULR + "template/pdf/";
			Config.TEMP_DIR_WEB_PATH = Config.WEBROOTULR + "tmp/";
		}

		Config.FITOSA_URL = "http://" + request.getServerName() + ":"
				+ request.getServerPort() + "/FITOSA/login.action";
		OperatorForm operatorForm = (OperatorForm) form;
		// operatorForm.setPassword("null");
		// 判断是否从报送平台登录的
		if (userName != null) {
			operatorForm.setUserName(userName);
		}
		OperatorForm resultForm = StrutsOperatorDelegate.userLoginValidate(
				operatorForm, userName);
		// operatorForm.setPassword("null");
		// resultForm.setPassword("null");
		if (resultForm == null) {
			messages.add(FitechResource.getMessage(locale, resources,
					"userLogin.false"));
			request.setAttribute(Config.MESSAGES, messages);
			return mapping.findForward("login");
		} else {
			// if(OnlineUserUtil.viewOnlineUser(resultForm.getUserName())) {
			// OnlineUserUtil.removeUserName(resultForm.getUserName());
			// }
			OperatorHandler operatorHandler = new OperatorHandler();
			Operator operator = operatorHandler.saveUserInfo(resultForm);
			HttpSession session = request.getSession();

			operator.setSessionId(session.getId());
			operator.setIpAdd(request.getRemoteAddr());
			if (session.getAttribute(Config.REPORT_SESSION_FLG) != null)
				session.removeAttribute(Config.REPORT_SESSION_FLG);
			if (session.getAttribute(Config.OPERATOR_SESSION_ATTRIBUTE_NAME) != null)
				session.removeAttribute(Config.OPERATOR_SESSION_ATTRIBUTE_NAME);
			if (session.getAttribute(Config.USER_LOGIN_DATE) != null) {
				session.removeAttribute(Config.USER_LOGIN_DATE);
			}
			if (operatorForm.getLoginDate() == null
					|| operatorForm.getLoginDate().equals("")) {
				String yestoday = DateUtil.getYestoday();
				operatorForm.setLoginDate(yestoday);
			}
			session.setAttribute(Config.USER_LOGIN_DATE,
					operatorForm.getLoginDate());
			session.setAttribute(Config.OPERATOR_SESSION_ATTRIBUTE_NAME,
					operator);
			session.setAttribute("OPERATORFORM", operatorForm);
			// todo...
			if (userName != null && !userName.equals("")) {
				AuthWebServiceStub authStub = new AuthWebServiceStub(
						ApplicationPropertiesUtil.getValue("new_portal_url")
								+ "/services/AuthWebService?wsdl");
				SetSession setSession = new SetSession();
				setSession.setSessionId(session.getId());
				setSession.setUsername(new Blowfish().encode(operator
						.getUserName()));
				SetSessionResponse ssr = authStub.setSession(setSession);
				String rt = ssr.get_return();
				if (rt != null && rt.equals("1")) {
					return mapping.findForward("login_self");// 连接异常
				}
			}
			return mapping.findForward("index");
		}
	}
}
