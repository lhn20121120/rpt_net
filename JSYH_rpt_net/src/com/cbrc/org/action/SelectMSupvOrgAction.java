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

import com.cbrc.org.form.MSupvOrgForm;

/**
 * Finds a MSupvOrg
 *
 * @author <a href="http://boss.bekk.no/boss/middlegen/">Middlegen</a>
 *
 * @struts.action
 *    path="/struts/selectMSupvOrg"
 *    name="MSupvOrgForm"
 *    scope="session"
 *    validate="false"
 *
 * @struts.action-forward
 *    name="none"
 *    path="/struts/findMSupvOrg.do"
 *    redirect="false"
 *
 * @struts.action-forward
 *    name="one"
 *    path="/struts/viewMSupvOrg.jsp"
 *    redirect="false"
 *
 * @struts.action-forward
 *    name="many"
 *    path="/struts/listMSupvOrg.jsp"
 *    redirect="false"
 *

 */
public final class SelectMSupvOrgAction extends Action {

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

		MSupvOrgForm MSupvOrgForm = (MSupvOrgForm)form;


		try {
  		List MSupvOrgs = com.cbrc.org.adapter.StrutsMSupvOrgDelegate.select(MSupvOrgForm);

			if( MSupvOrgs.size() == 0 ) {
				return mapping.findForward("none");
			} else if( MSupvOrgs.size() == 1 ) {
				// Found one. Display it.
				MSupvOrgForm MSupvOrgFormTemp = (MSupvOrgForm) MSupvOrgs.get(0);
				request.setAttribute("MSupvOrg", MSupvOrgFormTemp);

				return mapping.findForward("one");
			} else {
				// Found many. Display all.
				request.setAttribute("MSupvOrgs", MSupvOrgs);
				return mapping.findForward("many");
			}
		} catch (Exception e) {
			getServlet().log("Create error", e);
			return mapping.findForward("ejb-finder-exception");
		}
	}
}
