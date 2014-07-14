package com.fitech.gznx.action;

import java.io.IOException;


import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;


import com.cbrc.smis.common.Config;
import com.fitech.gznx.common.StringUtil;


public class ReportPortalAction extends Action {

	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException
	{
		String reportFlg=request.getParameter("reportFlg");
		
		HttpSession session = request.getSession();
		if(StringUtil.isEmpty(reportFlg) || reportFlg.equals("0")){
			if (session.getAttribute(Config.REPORT_SESSION_FLG) != null)
				session.removeAttribute(Config.REPORT_SESSION_FLG);
		}else{
			if (session.getAttribute(Config.REPORT_SESSION_FLG) != null)
				session.removeAttribute(Config.REPORT_SESSION_FLG);
			session.setAttribute(Config.REPORT_SESSION_FLG, reportFlg);			
		}
		
		return mapping.findForward("index");
	}

}
