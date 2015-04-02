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
		String fowardFlag = request.getParameter("fowardFlag");
		HttpSession session = request.getSession();
		if(StringUtil.isEmpty(reportFlg) || reportFlg.equals("0")){
			if (session.getAttribute(Config.REPORT_SESSION_FLG) != null)
				session.removeAttribute(Config.REPORT_SESSION_FLG);
		}else{
			if (session.getAttribute(Config.REPORT_SESSION_FLG) != null)
				session.removeAttribute(Config.REPORT_SESSION_FLG);
			session.setAttribute(Config.REPORT_SESSION_FLG, reportFlg);			
		}
		/**20131025 By LuYueFei:新增分支判断条件--Begin*/
		String flag1104=request.getParameter("Flag1104");
		if(flag1104!=null){
			session.setAttribute(Config.FLAG_1104, flag1104);	
		}
		/**20131025 By LuYueFei:新增分支判断条件--End*/
		if(!StringUtil.isEmpty(fowardFlag))
			return null;
		return mapping.findForward("index");
	}

}
