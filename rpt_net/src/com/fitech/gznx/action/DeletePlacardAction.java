package com.fitech.gznx.action;

import java.io.IOException;
import java.util.Locale;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.MessageResources;
import org.apache.struts.util.RequestUtils;

import com.cbrc.smis.common.Config;
import com.cbrc.smis.util.FitechMessages;
import com.cbrc.smis.util.FitechResource;
import com.fitech.gznx.form.PlacardForm;
import com.fitech.gznx.service.StrutsPlacardDelegate;



public class DeletePlacardAction extends Action {
	
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException
	{

		MessageResources resources = getResources(request);
		FitechMessages messages = new FitechMessages();
		Locale locale = request.getLocale();

		/**��request����ȡ����������������*/
		PlacardForm placardForm = new PlacardForm();
		RequestUtils.populate(placardForm, request);
		try
		{
			/**
			 * ɾ������
			 */
			boolean result = StrutsPlacardDelegate.delete(placardForm.getPlacardId());
			
			/**ɾ���ɹ�*/
			if (result==true)
			{
				messages.add("ɾ������ɹ�");
			}
			/**ɾ��ʧ��*/
			else
			{
				messages.add("ɾ������ʧ��");
			}
			
		}
		catch (Exception e)
		{
			e.printStackTrace();
			messages.add("ɾ������ʧ��");

		}
		if (messages.getMessages() != null && messages.getMessages().size() != 0)
			request.setAttribute(Config.MESSAGES, messages);
		return new ActionForward("/placard_mgr/viewPlacardAction.do"+placardForm.getQueryTerm());
	}
}
