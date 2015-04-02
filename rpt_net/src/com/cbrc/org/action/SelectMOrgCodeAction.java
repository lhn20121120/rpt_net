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

import com.cbrc.org.form.MOrgCodeForm;

/**
 * Finds a MOrgCode
 *
 * @author <a href="http://boss.bekk.no/boss/middlegen/">Middlegen</a>
 *
 * @struts.action
 *    path="/struts/selectMOrgCode"
 *    name="MOrgCodeForm"
 *    scope="session"
 *    validate="false"
 *
 * @struts.action-forward
 *    name="none"
 *    path="/struts/findMOrgCode.do"
 *    redirect="false"
 *
 * @struts.action-forward
 *    name="one"
 *    path="/struts/viewMOrgCode.jsp"
 *    redirect="false"
 *
 * @struts.action-forward
 *    name="many"
 *    path="/struts/listMOrgCode.jsp"
 *    redirect="false"
 *

 */
public final class SelectMOrgCodeAction extends Action {

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

		MOrgCodeForm MOrgCodeForm = (MOrgCodeForm)form;


		try {
  		List MOrgCodes = com.cbrc.org.adapter.StrutsMOrgCodeDelegate.select(MOrgCodeForm);

			if( MOrgCodes.size() == 0 ) {
				return mapping.findForward("none");
			} else if( MOrgCodes.size() == 1 ) {
				// Found one. Display it.
				MOrgCodeForm MOrgCodeFormTemp = (MOrgCodeForm) MOrgCodes.get(0);
				request.setAttribute("MOrgCode", MOrgCodeFormTemp);

				return mapping.findForward("one");
			} else {
				// Found many. Display all.
				request.setAttribute("MOrgCodes", MOrgCodes);
				return mapping.findForward("many");
			}
		} catch (Exception e) {
			getServlet().log("Create error", e);
			return mapping.findForward("ejb-finder-exception");
		}
	}
}
