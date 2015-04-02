package com.fitech.gznx.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.cbrc.smis.common.Config;
/**
 * 转向新增校验公式页面操作类
 * */
public class ToValidateFormuAction extends Action {

	@Override
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
			/** 报表选中标志 **/
			String reportFlg = "0";
			HttpSession session = request.getSession();
			if (session.getAttribute(Config.REPORT_SESSION_FLG) != null){
				reportFlg = (String) session.getAttribute(Config.REPORT_SESSION_FLG);
			}
			String templateId=request.getParameter("templateId");
			String versionId=request.getParameter("versionId");
			String reportName=request.getParameter("reportName");
			request.setAttribute("templateId", templateId);
			request.setAttribute("versionId", versionId);
			request.setAttribute("reportName", reportName);
			if(reportFlg.equals("1"))
				return mapping.findForward("addValidateFormu_yj");
		return mapping.findForward("addValidateFormu");
	}

}
