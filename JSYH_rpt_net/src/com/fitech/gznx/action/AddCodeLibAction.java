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



public class AddCodeLibAction extends Action {

	private static FitechException log = new FitechException(
			AddCodeLibAction.class);
	
	/***
	 * ��ʹ��hibernate ���Ը� 2011-12-28
	 * Ӱ�����AfCodelib AfCodelibId LogIn
	 */
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
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
		if (codeLibForm != null)
			request.setAttribute("QueryForm", codeLibForm);
		try {
			/**��ʹ��hibernate ���Ը� 2011-12-28
			 * Ӱ�����AfCodelib*/
			boolean result=StrutsCodeLibDelegate.isExist(codeLibForm);
			
			if(!result){
				/**��ʹ��hibernate ���Ը� 2011-12-28
				 * Ӱ�����AfCodelib AfCodelibId*/
				boolean rs=StrutsCodeLibDelegate.addCodeLib(codeLibForm);
				if(rs==false){
					messages.add("������Ϣ����ʧ��");
					FitechLog.writeLog(Config.LOG_OPERATION,operator.getUserName(), "����ϵͳ����ʧ��", ipAddress);
				}else{
					messages.add("������Ϣ���ӳɹ�");
					/* д��־ */
					/**��ʹ��hibernate ���Ը� 2011-12-28
					 * Ӱ�����LogIn*/
					FitechLog.writeLog(Config.LOG_OPERATION,operator.getUserName(), "����ϵͳ�����ɹ�", ipAddress);
				}
			}else{
				messages.add("�����Ѵ���");
			}
		}catch (Exception e) {
			/** �����򷵻�ԭҳ�� */
			log.printStackTrace(e);
			messages.add("������Ϣ����ʧ��");
			request.setAttribute(Config.MESSAGES, messages);
			return mapping.findForward("addCodeLib");
		}
		
		if (messages.getMessages() != null && messages.getMessages().size() > 0)
			request.setAttribute(Config.MESSAGES, messages);
		
		return new ActionForward("/system_mgr/viewCodeLib.do");
	}

}
