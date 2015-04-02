package com.fitech.gznx.action;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.MessageResources;
import org.apache.struts.util.RequestUtils;

import com.cbrc.smis.common.Config;
import com.cbrc.smis.util.FitechException;
import com.cbrc.smis.util.FitechMessages;
import com.fitech.gznx.form.AFReportForm;
import com.fitech.gznx.service.AFReportDelegate;

public class ReportStatAction extends Action {
	
	private FitechException log = new FitechException(ReportStatAction.class);

	/**
	 * @param result
	 *            查询返回标志,如果成功返回true,否则返回false
	 * @param ReportInForm
	 * @param request
	 * @exception Exception
	 *                有异常捕捉并抛出
	 */
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {

		MessageResources resources = null;
		FitechMessages messages = null;
		AFReportForm reportInForm = null;
		String returnname = "view";
		String searchType = request.getParameter("searchType");
		
		List resList = null;

		try {
			resources = getResources(request);
			messages = new FitechMessages();
			reportInForm = (AFReportForm) form;
			RequestUtils.populate(reportInForm, request);

			//分别处理报表统计
			HttpSession session = request.getSession();
			if (session.getAttribute(Config.REPORT_SESSION_FLG) != null)
				reportInForm.setTemplateType(session.getAttribute(Config.REPORT_SESSION_FLG).toString()); 
			if(searchType==null){
				resList = AFReportDelegate.getReortStat(reportInForm);
			}else if(searchType.equals("repRep")){
				returnname = "repRep";
				resList = AFReportDelegate.getRepReortStat(reportInForm);
			}else if(searchType.equals("laterRep")){
				returnname = "laterRep";
				resList = AFReportDelegate.getLaterReortStat(reportInForm);
			}
			
		} catch (Exception e) {
			log.printStackTrace(e);
			messages.add(resources.getMessage("select.data.stat.failed"));
		}

		String orgName = request.getParameter("orgName");
		
		if (messages.getMessages() != null && messages.getMessages().size() > 0)
			request.setAttribute(Config.MESSAGES, messages);
		if (resList != null && resList.size() > 0)
			request.setAttribute("Records", resList);
		
		request.setAttribute("form", reportInForm);
		request.setAttribute("orgName", orgName);

		return mapping.findForward(returnname);
	}
}
