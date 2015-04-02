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

import com.cbrc.org.form.MFinaOrgForm;

/**
 * Finds a MFinaOrg
 *
 * @author <a href="http://boss.bekk.no/boss/middlegen/">Middlegen</a>
 *
 * @struts.action
 *    path="/struts/selectMFinaOrg"
 *    name="MFinaOrgForm"
 *    scope="session"
 *    validate="false"
 *
 * @struts.action-forward
 *    name="none"
 *    path="/struts/findMFinaOrg.do"
 *    redirect="false"
 *
 * @struts.action-forward
 *    name="one"
 *    path="/struts/viewMFinaOrg.jsp"
 *    redirect="false"
 *
 * @struts.action-forward
 *    name="many"
 *    path="/struts/listMFinaOrg.jsp"
 *    redirect="false"
 *

 */
public final class SelectMFinaOrgAction extends Action {

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

		MFinaOrgForm MFinaOrgForm = (MFinaOrgForm)form;


		try {
  		List MFinaOrgs = com.cbrc.org.adapter.StrutsMFinaOrgDelegate.select(MFinaOrgForm);

			if( MFinaOrgs.size() == 0 ) {
				return mapping.findForward("none");
			} else if( MFinaOrgs.size() == 1 ) {
				// Found one. Display it.
				MFinaOrgForm MFinaOrgFormTemp = (MFinaOrgForm) MFinaOrgs.get(0);
				request.setAttribute("MFinaOrg", MFinaOrgFormTemp);

				return mapping.findForward("one");
			} else {
				// Found many. Display all.
				request.setAttribute("MFinaOrgs", MFinaOrgs);
				return mapping.findForward("many");
			}
		} catch (Exception e) {
			getServlet().log("Create error", e);
			return mapping.findForward("ejb-finder-exception");
		}
	}
}
