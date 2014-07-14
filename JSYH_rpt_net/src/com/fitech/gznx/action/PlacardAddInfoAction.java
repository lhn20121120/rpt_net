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
import com.cbrc.smis.util.FitechMessages;
import com.cbrc.smis.util.FitechResource;
import com.fitech.gznx.form.PlacardForm;
import com.fitech.gznx.service.StrutsPlacardDelegate;
import com.fitech.gznx.service.StrutsPlacardUserViewDelegate;

public class PlacardAddInfoAction extends Action {


	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException
	{

		MessageResources resources = getResources(request);
		FitechMessages messages = new FitechMessages();
		Locale locale = request.getLocale();

		PlacardForm placardForm = new PlacardForm();
		RequestUtils.populate(placardForm, request);

				HttpSession session = request.getSession();
		Operator operator = null;
		if (session.getAttribute(Config.OPERATOR_SESSION_NAME) != null)
			operator = (Operator) session.getAttribute(Config.OPERATOR_SESSION_NAME);
		else
			operator = new Operator();
		PlacardForm record = null;
		

		
		try
		{
			record = StrutsPlacardDelegate.getPlacardInfo(placardForm.getPlacardId());
			if(record!=null)
			{
				request.setAttribute(Config.RECORDS, record);
				if(record.getUserIdList()!=null)
					request.setAttribute("UserIDList",record.getUserIdList());
				if(record.getUserViewList()!=null)
					request.setAttribute("UserViewList",record.getUserViewList());
			}
			
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		if (messages.getMessages() != null && messages.getMessages().size() > 0)
			request.setAttribute(Config.MESSAGES, messages);	
		

		return mapping.findForward("view");
	}

}
