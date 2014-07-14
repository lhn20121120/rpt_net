package com.fitech.gznx.action;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.RequestUtils;

import com.cbrc.smis.common.Config;

import com.cbrc.smis.util.FitechMessages;

import com.fitech.gznx.form.QDValidateForm;
import com.fitech.gznx.service.AFQDValidateFormulaDelegate;


public class AddQDValidateAction extends Action {
	

	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException
	{
		QDValidateForm qdValidateForm=(QDValidateForm) form;
		RequestUtils.populate(qdValidateForm,request);
		
		FitechMessages messages = new FitechMessages();

		boolean result = true;
		result = AFQDValidateFormulaDelegate.insertValidateFormula(qdValidateForm);
		
		if(result){
			messages.add("增加信息成功");
			if (messages.getMessages() != null && messages.getMessages().size() > 0)
				request.setAttribute(Config.MESSAGES, messages);
			ActionForward af = new ActionForward("/gznx/viewQDValidateList.do?templateId=" +qdValidateForm.getTemplateId()+"&versionId=" +
					qdValidateForm.getVersionId());
			return af;
		} else{
			messages.add("增加信息失败");
			if (messages.getMessages() != null && messages.getMessages().size() > 0)
				request.setAttribute(Config.MESSAGES, messages);
			return mapping.findForward("add");
		}
		
	}

}
