package com.cbrc.fitech.org;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.RequestUtils;

import com.cbrc.org.form.MOrgForm;
public class updateAction extends Action{
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
		orgform=strutsMOrgDelegate.getMOrg(orgid);
		String orgid1=orgform.getOrgId();
		String orgname=orgform.getOrgName();
		String orgtype=orgform.getOrgType();
		String isCorp=orgform.getIsCorp();
		request.setAttribute("orgid1",orgid1);
		request.setAttribute("orgname",orgname);
		request.setAttribute("orgtype",orgtype);
		request.setAttribute("isCorp",isCorp);
		return mapping.findForward("update");
	}
}
