package com.cbrc.org.action;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.cbrc.org.form.MActuOrgForm;

/**
 * Finds a MActuOrg
 *
 * @author <a href="http://boss.bekk.no/boss/middlegen/">Middlegen</a>
 *
 * @struts.action
 *    path="/struts/selectMActuOrg"
 *    name="MActuOrgForm"
 *    scope="session"
 *    validate="false"
 *
 * @struts.action-forward
 *    name="none"
 *    path="/struts/findMActuOrg.do"
 *    redirect="false"
 *
 * @struts.action-forward
 *    name="one"
 *    path="/struts/viewMActuOrg.jsp"
 *    redirect="false"
 *
 * @struts.action-forward
 *    name="many"
 *    path="/struts/listMActuOrg.jsp"
 *    redirect="false"
 *

 */
public final class SelectMActuOrgAction extends Action {

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

		MActuOrgForm MActuOrgForm = (MActuOrgForm)form;


		try {
  		List MActuOrgs = com.cbrc.org.adapter.StrutsMActuOrgDelegate.select(MActuOrgForm);

			if( MActuOrgs.size() == 0 ) {
				return mapping.findForward("none");
			} else if( MActuOrgs.size() == 1 ) {
				// Found one. Display it.
				MActuOrgForm MActuOrgFormTemp = (MActuOrgForm) MActuOrgs.get(0);
				request.setAttribute("MActuOrg", MActuOrgFormTemp);

				return mapping.findForward("one");
			} else {
				// Found many. Display all.
				request.setAttribute("MActuOrgs", MActuOrgs);
				return mapping.findForward("many");
			}
		} catch (Exception e) {
			getServlet().log("Create error", e);
			return mapping.findForward("ejb-finder-exception");
		}
	}
}
