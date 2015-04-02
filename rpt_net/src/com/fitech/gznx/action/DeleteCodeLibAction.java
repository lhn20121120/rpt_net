package com.fitech.gznx.action;

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
import org.apache.struts.util.RequestUtils;

import com.cbrc.smis.common.Config;
import com.cbrc.smis.security.Operator;
import com.cbrc.smis.util.FitechException;
import com.cbrc.smis.util.FitechLog;
import com.cbrc.smis.util.FitechMessages;
import com.fitech.gznx.form.CodeLibForm;
import com.fitech.gznx.service.StrutsCodeLibDelegate;


public class DeleteCodeLibAction extends Action {


	/***
	 * 已使用hibernate 卞以刚 2011-12-28
	 * 影响对象：AfCodelib AfCodelibId LogIn
	 */
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws IOException,
			ServletException
	{
		MessageResources resources = getResources(request);
		FitechMessages messages = new FitechMessages();
		Locale locale = request.getLocale();

		HttpSession session = request.getSession();
		Operator operator = null;
		if (session.getAttribute(Config.OPERATOR_SESSION_NAME) != null)
			operator = (Operator) session
					.getAttribute(Config.OPERATOR_SESSION_NAME);
		String userId=String.valueOf(operator.getOperatorId());
		String ipAddress=operator.getIpAdd();
		
		CodeLibForm codeLibForm = new CodeLibForm();
		RequestUtils.populate(codeLibForm, request);
		
		try{
			//删除参数
			/**已使用hibernate 卞以刚 2011-12-28
			 * 影响对象：AfCodelib AfCodelibId*/
			boolean result = StrutsCodeLibDelegate.deleteCodeLib(codeLibForm); 
			if (result == false){
				/**删除失败*/
				messages.add("参数信息删除失败");
				FitechLog.writeLog(Config.LOG_OPERATION,operator.getUserName(), "删除系统参数失败", ipAddress);
			}
			else{
				/**删除成功*/
				messages.add("参数信息删除成功");
				/* 写日志 */
				/**已使用hibernate 卞以刚 2011-12-28
				 * 影响对象：LogIn*/
				FitechLog.writeLog(Config.LOG_OPERATION,operator.getUserName(), "删除系统参数成功", ipAddress);
			}
			if(codeLibForm!=null){
            	request.setAttribute("QueryForm",codeLibForm);
			}
		}
		catch(Exception e){
			e.printStackTrace();
			messages.add("参数信息删除失败");
			FitechLog.writeLog(Config.LOG_OPERATION,operator.getUserName(), "删除系统参数失败", ipAddress);
		}
		
		if (messages.getMessages() != null && messages.getMessages().size() != 0)
			request.setAttribute(Config.MESSAGES, messages);
		return new ActionForward("/system_mgr/viewCodeLib.do");
	}
}
