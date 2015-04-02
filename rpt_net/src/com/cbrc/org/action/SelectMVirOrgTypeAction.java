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

import com.cbrc.org.form.MVirOrgTypeForm;

/**
 * Finds a MVirOrgType
 *
 * @author <a href="http://boss.bekk.no/boss/middlegen/">Middlegen</a>
 *
 * @struts.action
 *    path="/struts/selectMVirOrgType"
 *    name="MVirOrgTypeForm"
 *    scope="session"
 *    validate="false"
 *
 * @struts.action-forward
 *    name="none"
 *    path="/struts/findMVirOrgType.do"
 *    redirect="false"
 *
 * @struts.action-forward
 *    name="one"
 *    path="/struts/viewMVirOrgType.jsp"
 *    redirect="false"
 *
 * @struts.action-forward
 *    name="many"
 *    path="/struts/listMVirOrgType.jsp"
 *    redirect="false"
 *

 */
public final class SelectMVirOrgTypeAction extends Action {

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

		MVirOrgTypeForm MVirOrgTypeForm = (MVirOrgTypeForm)form;


		try {
  		List MVirOrgTypes = com.cbrc.org.adapter.StrutsMVirOrgTypeDelegate.select(MVirOrgTypeForm);

			if( MVirOrgTypes.size() == 0 ) {
				return mapping.findForward("none");
			} else if( MVirOrgTypes.size() == 1 ) {
				// Found one. Display it.
				MVirOrgTypeForm MVirOrgTypeFormTemp = (MVirOrgTypeForm) MVirOrgTypes.get(0);
				request.setAttribute("MVirOrgType", MVirOrgTypeFormTemp);

				return mapping.findForward("one");
			} else {
				// Found many. Display all.
				request.setAttribute("MVirOrgTypes", MVirOrgTypes);
				return mapping.findForward("many");
			}
		} catch (Exception e) {
			getServlet().log("Create error", e);
			return mapping.findForward("ejb-finder-exception");
		}
	}
}
