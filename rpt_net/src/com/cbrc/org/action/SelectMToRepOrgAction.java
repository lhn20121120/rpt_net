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

import com.cbrc.org.form.MToRepOrgForm;

/**
 * Finds a MToRepOrg
 *
 * @author <a href="http://boss.bekk.no/boss/middlegen/">Middlegen</a>
 *
 * @struts.action
 *    path="/struts/selectMToRepOrg"
 *    name="MToRepOrgForm"
 *    scope="session"
 *    validate="false"
 *
 * @struts.action-forward
 *    name="none"
 *    path="/struts/findMToRepOrg.do"
 *    redirect="false"
 *
 * @struts.action-forward
 *    name="one"
 *    path="/struts/viewMToRepOrg.jsp"
 *    redirect="false"
 *
 * @struts.action-forward
 *    name="many"
 *    path="/struts/listMToRepOrg.jsp"
 *    redirect="false"
 *

 */
public final class SelectMToRepOrgAction extends Action {

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

		MToRepOrgForm MToRepOrgForm = (MToRepOrgForm)form;


		try {
  		List MToRepOrgs = com.cbrc.org.adapter.StrutsMToRepOrgDelegate.select(MToRepOrgForm);

			if( MToRepOrgs.size() == 0 ) {
				return mapping.findForward("none");
			} else if( MToRepOrgs.size() == 1 ) {
				// Found one. Display it.
				MToRepOrgForm MToRepOrgFormTemp = (MToRepOrgForm) MToRepOrgs.get(0);
				request.setAttribute("MToRepOrg", MToRepOrgFormTemp);

				return mapping.findForward("one");
			} else {
				// Found many. Display all.
				request.setAttribute("MToRepOrgs", MToRepOrgs);
				return mapping.findForward("many");
			}
		} catch (Exception e) {
			getServlet().log("Create error", e);
			return mapping.findForward("ejb-finder-exception");
		}
	}
}
