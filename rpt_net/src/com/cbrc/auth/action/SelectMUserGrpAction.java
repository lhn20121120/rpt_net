package com.cbrc.auth.action;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.cbrc.auth.form.MUserGrpForm;

/**
 * Finds a MUserGrp
 *
 * @author <a href="http://boss.bekk.no/boss/middlegen/">Middlegen</a>
 *
 * @struts.action
 *    path="/struts/selectMUserGrp"
 *    name="MUserGrpForm"
 *    scope="session"
 *    validate="false"
 *
 * @struts.action-forward
 *    name="none"
 *    path="/struts/findMUserGrp.do"
 *    redirect="false"
 *
 * @struts.action-forward
 *    name="one"
 *    path="/struts/viewMUserGrp.jsp"
 *    redirect="false"
 *
 * @struts.action-forward
 *    name="many"
 *    path="/struts/listMUserGrp.jsp"
 *    redirect="false"
 *

 */
public final class SelectMUserGrpAction extends Action {

	/**
	 * Performs action.
	 * @param mapping Action mapping.
	 * @param form Action form.
	 * @param request HTTP request.
	 * @param response HTTP response.
	 * @exception IOException if an input/output error occurs
	 * @exception ServletException if a servlet exception occurs
	 */
	public ActionForward perform(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response
	)
		throws IOException, ServletException {

		MUserGrpForm MUserGrpForm = (MUserGrpForm)form;


		try {
  		List MUserGrps = null;//com.cbrc.auth.adapter.StrutsMUserGrpDelegate.select(MUserGrpForm);

			if( MUserGrps.size() == 0 ) {
				return mapping.findForward("none");
			} else if( MUserGrps.size() == 1 ) {
				// Found one. Display it.
				MUserGrpForm MUserGrpFormTemp = (MUserGrpForm) MUserGrps.get(0);
				request.setAttribute("MUserGrp", MUserGrpFormTemp);

				return mapping.findForward("one");
			} else {
				// Found many. Display all.
				request.setAttribute("MUserGrps", MUserGrps);
				return mapping.findForward("many");
			}
		} catch (Exception e) {
			getServlet().log("Create error", e);
			return mapping.findForward("ejb-finder-exception");
		}
	}
}
