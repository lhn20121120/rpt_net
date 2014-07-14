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
import com.cbrc.smis.util.FitechUtil;
import com.fitech.gznx.form.CodeLibForm;
import com.fitech.gznx.service.StrutsCodeLibDelegate;



public class UpdateCodeLibAction extends Action {
	private static FitechException log = new FitechException(UpdateCodeLibAction.class);

	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException
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
		
		String codeTypeId=codeLibForm.getCodeTypeId();
		String codeId=codeLibForm.getCodeId();
		String effectiveDate=codeLibForm.getEffectiveDate();
		String codeTypeId_upd=codeLibForm.getCodeTypeId_upd();
		String codeId_upd=codeLibForm.getCodeId_upd();
		String effectiveDate_upd=codeLibForm.getEffectiveDate_upd();
		
		boolean result = false;
		try
		{
			if(codeTypeId.equals(codeTypeId_upd)&&codeId.equals(codeId_upd)){
				result = StrutsCodeLibDelegate.updateCodeLib(codeLibForm);
				if (result == false)
				{
					messages.add("������Ϣ�޸�ʧ��");
					FitechLog.writeLog(Config.LOG_OPERATION,operator.getUserName(), "�޸�ϵͳ����ʧ��", ipAddress);
					request.setAttribute(Config.MESSAGES, messages);
					return mapping.findForward("codeLibUpdate");
				}
				else{
					messages.add("������Ϣ�޸ĳɹ�");
					/* д��־ */
					FitechLog.writeLog(Config.LOG_OPERATION,operator.getUserName(), "�޸�ϵͳ�����ɹ�", ipAddress);
				}
			}else{
				boolean rs=StrutsCodeLibDelegate.isExist(codeLibForm);
				if (rs == false){
					/**�޸�ϵͳ����*/
					result = StrutsCodeLibDelegate.updateCodeLib(codeLibForm);
					if (result == false)
					{
						messages.add("������Ϣ�޸�ʧ��");
						FitechLog.writeLog(Config.LOG_OPERATION,operator.getUserName(), "�޸�ϵͳ����ʧ��", ipAddress);
						request.setAttribute(Config.MESSAGES, messages);
						return mapping.findForward("codeLibUpdate");
					}
					else{
						messages.add("������Ϣ�޸ĳɹ�");
						/* д��־ */
						FitechLog.writeLog(Config.LOG_OPERATION,operator.getUserName(), "�޸�ϵͳ�����ɹ�", ipAddress);
					}
				}else{
					messages.add("�����Ѵ���");
				}
			}
			if(codeLibForm!=null)
            	request.setAttribute("QueryForm",codeLibForm);
		}catch (Exception e)
		{
			log.printStackTrace(e);
			messages.add("������Ϣ�޸�ʧ��");
			FitechLog.writeLog(Config.LOG_OPERATION,operator.getUserName(), "�޸�ϵͳ����ʧ��", ipAddress);
			request.setAttribute(Config.MESSAGES, messages);
			return mapping.findForward("codeLibUpdate");
		}

		// �Ƴ�request��session��Χ�ڵ�����
		FitechUtil.removeAttribute(mapping, request);

		if (messages.getMessages() != null && messages.getMessages().size() > 0)
			request.setAttribute(Config.MESSAGES, messages);
		return new ActionForward("/system_mgr/viewCodeLib.do");
	}
}
