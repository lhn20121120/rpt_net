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

import com.cbrc.smis.form.MChildReportForm;

/**
 * Finds a MChildReport
 *
 * @author <a href="http://boss.bekk.no/boss/middlegen/">Middlegen</a>
 *
 * @struts.action
 *    path="/struts/selectMChildReport"
 *    name="MChildReportForm"
 *    scope="session"
 *    validate="false"
 *
 * @struts.action-forward
 *    name="none"
 *    path="/struts/findMChildReport.do"
 *    redirect="false"
 *
 * @struts.action-forward
 *    name="one"
 *    path="/struts/viewMChildReport.jsp"
 *    redirect="false"
 *
 * @struts.action-forward
 *    name="many"
 *    path="/struts/listMChildReport.jsp"
 *    redirect="false"
 *

 */
public final class SelectMChildReportAction extends Action {

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

		MChildReportForm MChildReportForm = (MChildReportForm)form;


		try {
  		List MChildReports = null;//com.cbrc.smis.adapter.StrutsMChildReportDelegate.select(MChildReportForm);
 
			if( MChildReports.size() == 0 ) {
				return mapping.findForward("none");
			} else if( MChildReports.size() == 1 ) {
				// Found one. Display it.
				MChildReportForm MChildReportFormTemp = (MChildReportForm) MChildReports.get(0);
				request.setAttribute("MChildReport", MChildReportFormTemp);

				return mapping.findForward("one");
			} else {
				// Found many. Display all.
				request.setAttribute("MChildReports", MChildReports);
				return mapping.findForward("many");
			}
		} catch (Exception e) {
			getServlet().log("Create error", e);
			return mapping.findForward("ejb-finder-exception");
		}
	}
}
