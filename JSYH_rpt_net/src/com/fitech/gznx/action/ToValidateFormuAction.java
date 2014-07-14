package com.fitech.gznx.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
/**
 * 转向新增校验公式页面操作类
 * */
public class ToValidateFormuAction extends Action {

	@Override
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
			String templateId=request.getParameter("templateId");
			String versionId=request.getParameter("versionId");
			String reportName=request.getParameter("reportName");
			request.setAttribute("templateId", templateId);
			request.setAttribute("versionId", versionId);
			request.setAttribute("reportName", reportName);
		return mapping.findForward("addValidateFormu");
	}

}
