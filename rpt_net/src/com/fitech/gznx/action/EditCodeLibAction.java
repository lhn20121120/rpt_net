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
import com.cbrc.smis.util.FitechException;
import com.cbrc.smis.util.FitechMessages;
import com.fitech.gznx.form.CodeLibForm;
import com.fitech.gznx.service.StrutsCodeLibDelegate;


public class EditCodeLibAction extends Action {
	private static FitechException log = new FitechException(EditCodeLibAction.class);

	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException
	{

		MessageResources resources = getResources(request);
		FitechMessages messages = new FitechMessages();
		Locale locale = request.getLocale();
		
		CodeLibForm codeLibForm = new CodeLibForm();
		RequestUtils.populate(codeLibForm, request);
		try
		{
			StrutsCodeLibDelegate.EditCodeLib(codeLibForm);
			//System.out.println("###"+codeLibForm.getCodeTypeValue()+"_"+codeLibForm.getCodeValue()+"_"+codeLibForm.getEffectiveDate());
			if(codeLibForm!=null)
            	request.setAttribute("QueryForm",codeLibForm);
		}
		catch (Exception e)
		{
			/** 错误则返回原页面 */
			log.printStackTrace(e);
			messages.add("参数信息修改失败");
			request.setAttribute(Config.MESSAGES, messages);
			return new ActionForward("codeLibUpdate");
		}

		if (messages.getMessages() != null && messages.getMessages().size() > 0)
			request.setAttribute(Config.MESSAGES, messages);
		
		return mapping.findForward("codeLibUpdate");

	}
}
