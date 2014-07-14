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
import com.cbrc.smis.common.DateUtil;
import com.cbrc.smis.security.Operator;
import com.cbrc.smis.util.FitechLog;
import com.cbrc.smis.util.FitechMessages;
import com.cbrc.smis.util.FitechResource;
import com.fitech.gznx.form.PlacardForm;
import com.fitech.gznx.service.StrutsPlacardDelegate;



public class UpdatePlacardAction extends Action {

	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException
	{

		MessageResources resources = getResources(request);
		FitechMessages messages = new FitechMessages();
		Locale locale = request.getLocale();
		PlacardForm placardForm = (PlacardForm)form;
		RequestUtils.populate(placardForm, request);
		boolean result = false;

		try
		{
			/** �����¼* */
			HttpSession session = request.getSession();
			Operator operator = null;
			if (session.getAttribute(Config.OPERATOR_SESSION_NAME) != null)
				operator = (Operator) session.getAttribute(Config.OPERATOR_SESSION_NAME);
			else
				operator = new Operator();
			
			placardForm.setPublicUserId(String.valueOf(operator.getOperatorId()));
			placardForm.setPublicDate(DateUtil.getToday("yyyy-mm-dd"));
			if(placardForm.getPlacardFile().getFileSize() < Config.FILE_MAX_SIZE){
				result = StrutsPlacardDelegate.update(placardForm);

				if (result == true)
				{
					messages.add("�����޸ĳɹ�");
					request.setAttribute(Config.MESSAGES, messages);
					FitechLog.writeLog(Config.LOG_APPLICATION,operator.getUserName(),"���湫����Ϣ�ɹ�");
	            	
				}
				else
				{
					messages.add("�����޸�ʧ��");
					request.setAttribute(Config.MESSAGES, messages);
					return mapping.findForward("update");
				}
			}else
			{
				messages.add("�ļ���С�������ƣ�ֻ������С��4M���ļ���");
			}
			
		}
		catch (Exception e)
		{
			e.printStackTrace();
			messages.add("�����޸�ʧ��");
			request.setAttribute(Config.MESSAGES, messages);
			return mapping.findForward("update");
		}
		return new ActionForward("/placard_mgr/viewPlacardAction.do?title=&startDate=&endDate=");
	}
}
