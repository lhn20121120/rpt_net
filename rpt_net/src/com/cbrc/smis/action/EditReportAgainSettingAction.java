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

import com.cbrc.smis.form.ReportInForm;
import com.fitech.net.adapter.StrutsOrgNetDelegate;
import com.fitech.net.hibernate.OrgNet;

public class EditReportAgainSettingAction extends Action {
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		ReportInForm reportInForm = (ReportInForm) form;
		try {
			reportInForm=com.cbrc.smis.adapter.StrutsReportInDelegate.edit(reportInForm);
			if(reportInForm.getOrgId()!=null){
				OrgNet orgNet = StrutsOrgNetDelegate.selectOne(reportInForm.getOrgId());
				if(orgNet != null && orgNet.getOrgName() != null)
					reportInForm.setOrgName(orgNet.getOrgName());			    
			}
			RequestUtils.populate(reportInForm, request);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return mapping.findForward("view");
	}
}
