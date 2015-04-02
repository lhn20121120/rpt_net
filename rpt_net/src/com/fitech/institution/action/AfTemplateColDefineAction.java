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
import com.fitech.institution.form.AfTemplateColDefineForm;
import com.fitech.institution.po.AfTemplateColDefine;
import com.fitech.institution.util.TranslatorUtil;

	

public class AfTemplateColDefineAction extends Action {

	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		AfTemplateColDefineForm sysForm = (AfTemplateColDefineForm)form ;
		HttpSession session = request.getSession();
//		String childRepId = request.getParameter("childRepId");
//		String versionId = request.getParameter("versionId");
//		String orgId = request.getParameter("orgId");
//		String orgIds = request.getParameter("orgIds");
		System.out.println("AfTemplateColDefindAction>>>");
		String reportName = sysForm.getReportName();
		if(reportName!=null)
			request.setAttribute("reportName" ,reportName);
		String  templateId = sysForm.getTemplateId();
		if(templateId !=null)
			request.setAttribute("templateId" ,templateId);
		String  versionId = sysForm.getVersionId();
		if(versionId !=null)
			request.setAttribute("versionId" ,versionId);
		
		List <SysParameter>parameters =StrutsSysSetDelegate.loadSysCol();
		
		if(parameters !=null && parameters.size()>0){
			request.setAttribute("parameters",parameters);
		}
		sysForm.setParameters(parameters);
		AfTemplateColDefine  ad  = new AfTemplateColDefine();
		TranslatorUtil.copyPersistenceToVo(sysForm,ad);
		AfTemplateColDefineDelegate.saveOrUpate(ad);
		List < AfTemplateColDefine> colDefines  = AfTemplateColDefineDelegate.findById(sysForm.getTemplateId());
		if(colDefines!=null){
			request.setAttribute("colDefines",colDefines);
		}
		return mapping.findForward("save");
	}

}
