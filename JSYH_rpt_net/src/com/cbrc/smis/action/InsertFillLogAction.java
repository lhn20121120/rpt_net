package com.cbrc.smis.action;

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
import com.cbrc.smis.util.FitechException;
import com.cbrc.smis.util.FitechLog;

public class InsertFillLogAction extends Action {
	private static FitechException log = new FitechException(InsertFillLogAction.class);  
	public ActionForward execute(
		      ActionMapping mapping,
		      ActionForm form,
		      HttpServletRequest request,
		      HttpServletResponse response
		   )
		      throws IOException, ServletException {
		String repInId = request.getParameter("repInId");
		String reportFlg = "0";
		HttpSession session = request.getSession();
		if (session.getAttribute(Config.REPORT_SESSION_FLG) != null){
			reportFlg = (String) session.getAttribute(Config.REPORT_SESSION_FLG);
		} 
		FitechLog.writeRepLog(new Integer(12), "ÐÞ¸Ä³É¹¦", request, repInId ,reportFlg);
		return null;
	}
	
}
