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

import com.cbrc.smis.form.ReportAgainSetForm;

/**
 * Finds a reportAgainSet
 *
 * @author <a href="http://boss.bekk.no/boss/middlegen/">Middlegen</a>
 *
 * @struts.action
 *    path="/struts/selectReportAgainSet"
 *    name="reportAgainSetForm"
 *    scope="session"
 *    validate="false"
 *
 * @struts.action-forward
 *    name="none"
 *    path="/struts/findReportAgainSet.do"
 *    redirect="false"
 *
 * @struts.action-forward
 *    name="one"
 *    path="/struts/viewReportAgainSet.jsp"
 *    redirect="false"
 *
 * @struts.action-forward
 *    name="many"
 *    path="/struts/listReportAgainSet.jsp"
 *    redirect="false"
 *

 */
public final class SelectReportAgainSetAction extends Action {

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

		ReportAgainSetForm reportAgainSetForm = (ReportAgainSetForm)form;


		try {
  		List reportAgainSets = com.cbrc.smis.adapter.StrutsReportAgainSetDelegate.select(reportAgainSetForm);

			if( reportAgainSets.size() == 0 ) {
				return mapping.findForward("none");
			} else if( reportAgainSets.size() == 1 ) {
				// Found one. Display it.
				ReportAgainSetForm reportAgainSetFormTemp = (ReportAgainSetForm) reportAgainSets.get(0);
				request.setAttribute("reportAgainSet", reportAgainSetFormTemp);

				return mapping.findForward("one");
			} else {
				// Found many. Display all.
				request.setAttribute("reportAgainSets", reportAgainSets);
				return mapping.findForward("many");
			}
		} catch (Exception e) {
			getServlet().log("Create error", e);
			return mapping.findForward("ejb-finder-exception");
		}
	}
}
