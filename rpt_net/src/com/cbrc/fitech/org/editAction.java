package com.cbrc.fitech.org;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.RequestUtils;

import com.cbrc.org.form.MOrgForm;
public class editAction extends Action{
	public ActionForward execute(ActionMapping mapping, ActionForm actionForm,
			HttpServletRequest request, HttpServletResponse httpServletresponse)
			throws Exception {
		MOrgForm form=null;
        form=(MOrgForm)actionForm;
		RequestUtils.populate(form, request);
		StrutsMOrgDelegate strutsMOrgDelegate=new StrutsMOrgDelegate();
		strutsMOrgDelegate.edit(form);
		return mapping.findForward("update");
	}

}
