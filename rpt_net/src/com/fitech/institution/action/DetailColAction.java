package com.fitech.institution.action;

import java.io.IOException;
import java.net.URLDecoder;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.cbrc.smis.hibernate.SysParameter;
import com.fitech.institution.adapter.AfTemplateColDefineDelegate;
import com.fitech.institution.adapter.StrutsSysSetDelegate;
import com.fitech.institution.form.SysParameterForm;
import com.fitech.institution.po.AfTemplateColDefine;

public class DetailColAction extends Action {

	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		SysParameterForm sysForm = (SysParameterForm)form ;
		HttpSession session = request.getSession();
		String templateId = request.getParameter("templateId");
		String versionId = request.getParameter("versionId");
		String reportName = request.getParameter("reportName");
		if(templateId!=null){
			request.setAttribute("templateId" ,templateId);
		}
		if(templateId!=null){
			request.setAttribute("versionId" ,versionId);
		}
		if(reportName!=null){
			request.setAttribute("reportName" ,new String(URLDecoder.decode(reportName,"UTF-8")));
		}
		System.out.println("DetailColAction>>>");
		
		List <SysParameter>parameters =StrutsSysSetDelegate.loadSysCol();
		List < AfTemplateColDefine> colDefines  = AfTemplateColDefineDelegate.findById(templateId);
		if(colDefines!=null){
			request.setAttribute("colDefines",colDefines);
		}
		sysForm.setParameters(parameters);
		if(parameters !=null && parameters.size()>0){
			request.setAttribute("parameters",parameters);
		}
		
		
		return mapping.findForward("detailCol");
	}

}
