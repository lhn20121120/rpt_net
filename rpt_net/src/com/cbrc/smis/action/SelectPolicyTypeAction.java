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

import com.cbrc.smis.form.PolicyTypeForm;

/**
 * Finds a policyType
 *
 * @author <a href="http://boss.bekk.no/boss/middlegen/">Middlegen</a>
 *
 * @struts.action
 *    path="/struts/selectPolicyType"
 *    name="policyTypeForm"
 *    scope="session"
 *    validate="false"
 *
 * @struts.action-forward
 *    name="none"
 *    path="/struts/findPolicyType.do"
 *    redirect="false"
 *
 * @struts.action-forward
 *    name="one"
 *    path="/struts/viewPolicyType.jsp"
 *    redirect="false"
 *
 * @struts.action-forward
 *    name="many"
 *    path="/struts/listPolicyType.jsp"
 *    redirect="false"
 *

 */
public final class SelectPolicyTypeAction extends Action {

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

		PolicyTypeForm policyTypeForm = (PolicyTypeForm)form;


		try {
  		List policyTypes = com.cbrc.smis.adapter.StrutsPolicyTypeDelegate.select(policyTypeForm);

			if( policyTypes.size() == 0 ) {
				return mapping.findForward("none");
			} else if( policyTypes.size() == 1 ) {
				// Found one. Display it.
				PolicyTypeForm policyTypeFormTemp = (PolicyTypeForm) policyTypes.get(0);
				request.setAttribute("policyType", policyTypeFormTemp);

				return mapping.findForward("one");
			} else {
				// Found many. Display all.
				request.setAttribute("policyTypes", policyTypes);
				return mapping.findForward("many");
			}
		} catch (Exception e) {
			getServlet().log("Create error", e);
			return mapping.findForward("ejb-finder-exception");
		}
	}
}
