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

import com.cbrc.org.form.MVirOrgRltForm;

/**
 * Finds a MVirOrgRlt
 *
 * @author <a href="http://boss.bekk.no/boss/middlegen/">Middlegen</a>
 *
 * @struts.action
 *    path="/struts/selectMVirOrgRlt"
 *    name="MVirOrgRltForm"
 *    scope="session"
 *    validate="false"
 *
 * @struts.action-forward
 *    name="none"
 *    path="/struts/findMVirOrgRlt.do"
 *    redirect="false"
 *
 * @struts.action-forward
 *    name="one"
 *    path="/struts/viewMVirOrgRlt.jsp"
 *    redirect="false"
 *
 * @struts.action-forward
 *    name="many"
 *    path="/struts/listMVirOrgRlt.jsp"
 *    redirect="false"
 *

 */
public final class SelectMVirOrgRltAction extends Action {

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

		MVirOrgRltForm MVirOrgRltForm = (MVirOrgRltForm)form;


		try {
  		List MVirOrgRlts = com.cbrc.org.adapter.StrutsMVirOrgRltDelegate.select(MVirOrgRltForm);

			if( MVirOrgRlts.size() == 0 ) {
				return mapping.findForward("none");
			} else if( MVirOrgRlts.size() == 1 ) {
				// Found one. Display it.
				MVirOrgRltForm MVirOrgRltFormTemp = (MVirOrgRltForm) MVirOrgRlts.get(0);
				request.setAttribute("MVirOrgRlt", MVirOrgRltFormTemp);

				return mapping.findForward("one");
			} else {
				// Found many. Display all.
				request.setAttribute("MVirOrgRlts", MVirOrgRlts);
				return mapping.findForward("many");
			}
		} catch (Exception e) {
			getServlet().log("Create error", e);
			return mapping.findForward("ejb-finder-exception");
		}
	}
}
