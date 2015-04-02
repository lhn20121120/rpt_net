package com.cbrc.fitech.org;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.RequestUtils;

import com.cbrc.org.form.MOrgForm;
public class deleteAction extends Action{
	public ActionForward execute(ActionMapping mapping, ActionForm actionForm,
			HttpServletRequest request, HttpServletResponse httpServletresponse)
			throws Exception {
		MOrgForm form=null;
        form=(MOrgForm)actionForm;
		RequestUtils.populate(form, request);
		String orgid=request.getParameter("Id");
		// System.out.println("orgid is "+orgid);	
		StrutsMOrgDelegate strutsMOrgDelegate=new StrutsMOrgDelegate();
		MOrgForm orgform=null;
	    strutsMOrgDelegate.delete(orgid);
		return mapping.findForward("delete");
	}
}
