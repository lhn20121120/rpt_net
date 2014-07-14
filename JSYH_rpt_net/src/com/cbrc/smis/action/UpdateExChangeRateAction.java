package com.cbrc.smis.action;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.RequestUtils;

import com.cbrc.smis.adapter.StrutsEXChangeRateDelegate;
import com.cbrc.smis.form.EExChangeRateForm;

public class UpdateExChangeRateAction extends Action {
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {

		EExChangeRateForm exform =(EExChangeRateForm)form;
		RequestUtils.populate(exform, request);
		try {
			if(exform!=null ){
				StrutsEXChangeRateDelegate.updateExChangeRate(exform);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return mapping.findForward("view");
	}

}
