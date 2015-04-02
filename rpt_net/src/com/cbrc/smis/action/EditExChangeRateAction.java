package com.cbrc.smis.action;

import java.io.IOException;
import java.util.List;

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

public class EditExChangeRateAction extends Action {
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {

		EExChangeRateForm exform = (EExChangeRateForm)form;
		
		try {
			if(exform!=null ){
				if(exform.getVcenname()!=null)
				exform.setVdname(exform.getVcenname().replace('-',' ').trim());
			}
			List list=StrutsEXChangeRateDelegate.findExChangeRate(exform);
			if(list.size()>0){
				exform=(EExChangeRateForm)list.get(0);
				RequestUtils.populate(exform, request);
				request.setAttribute("exChangeRateForm",exform);
				// System.out.println("----------------------------");
			}
			

		} catch (Exception e) {
			e.printStackTrace();
		}

		return mapping.findForward("view");
	}

}
