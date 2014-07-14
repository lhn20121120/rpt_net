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

import com.cbrc.smis.form.ReportDataForm;

/**
 * 
 */
public final class SelectReportDataAction extends Action {

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

		ReportDataForm reportDataForm = (ReportDataForm)form;


		try {
			List reportDatas = com.cbrc.smis.adapter.StrutsReportDataDelegate.select(reportDataForm);

			if( reportDatas.size() == 0 ) {
				return mapping.findForward("none");
			} else if( reportDatas.size() == 1 ) {
				// Found one. Display it.
				ReportDataForm reportDataFormTemp = (ReportDataForm) reportDatas.get(0);
				request.setAttribute("reportData", reportDataFormTemp);

				return mapping.findForward("one");
			} else {
				// Found many. Display all.
				request.setAttribute("reportDatas", reportDatas);
				return mapping.findForward("many");
			}
		} catch (Exception e) {
			getServlet().log("Create error", e);
			return mapping.findForward("ejb-finder-exception");
		}
	}
}
