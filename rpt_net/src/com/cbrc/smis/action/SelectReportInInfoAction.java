package com.cbrc.smis.action;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.cbrc.smis.form.ReportInInfoForm;

/**
 * Finds a reportInInfo
 *
 * @author <a href="http://boss.bekk.no/boss/middlegen/">Middlegen</a>
 *
 * @struts.action
 *    path="/struts/selectReportInInfo"
 *    name="reportInInfoForm"
 *    scope="session"
 *    validate="false"
 *
 * @struts.action-forward
 *    name="none"
 *    path="/struts/findReportInInfo.do"
 *    redirect="false"
 *
 * @struts.action-forward
 *    name="one"
 *    path="/struts/viewReportInInfo.jsp"
 *    redirect="false"
 *
 * @struts.action-forward
 *    name="many"
 *    path="/struts/listReportInInfo.jsp"
 *    redirect="false"
 *

 */
public final class SelectReportInInfoAction extends Action {

	/**
	 * Performs action.
	 * @param mapping Action mapping.
	 * @param form Action form.
	 * @param request HTTP request.
	 * @param response HTTP response.
	 * @exception IOException if an input/output error occurs
	 * @exception ServletException if a servlet exception occurs
	 */
	public ActionForward execute(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response
	)
		throws IOException, ServletException {

		ReportInInfoForm reportInInfoForm = (ReportInInfoForm)form;


		try {
  		List reportInInfos = com.cbrc.smis.adapter.StrutsReportInInfoDelegate.select(reportInInfoForm);

			if( reportInInfos.size() == 0 ) {
				return mapping.findForward("none");
			} else if( reportInInfos.size() == 1 ) {
				// Found one. Display it.
				ReportInInfoForm reportInInfoFormTemp = (ReportInInfoForm) reportInInfos.get(0);
				request.setAttribute("reportInInfo", reportInInfoFormTemp);

				return mapping.findForward("one");
			} else {
				// Found many. Display all.
				request.setAttribute("reportInInfos", reportInInfos);
				return mapping.findForward("many");
			}
		} catch (Exception e) {
			getServlet().log("Create error", e);
			return mapping.findForward("ejb-finder-exception");
		}
	}
}
