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

public class FindExChangeRateAction extends Action {
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {

		EExChangeRateForm exform = (EExChangeRateForm)form;
		RequestUtils.populate(exform, request);
		try {
			if(exform!=null ){
//				if(exform.!=null&&!exform.getVcenname().equals("")){
//					exform.setVdname(exform.getVcenname().replace('-',' ').trim());
//					//exform.setVdid(StrutsEXChangeRateDelegate.getVdidbyname(exform.getVcenname()));
//				}
				if(exform.getVdname()!=null&&!exform.getVdname().equals("")){
				//	exform.setVdname(exform.getVdname().replaceAll("-", "").trim());
					exform.setVdid(StrutsEXChangeRateDelegate.getVdidbyname(exform.getVdname()));
				}
			}
			List list=StrutsEXChangeRateDelegate.findExChangeRate(exform);
			request.setAttribute("Records",list);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return mapping.findForward("view");
	}

}
