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

import com.cbrc.smis.form.PolicyExecDetailForm;

/**
 * Finds a policyExecDetail
 *
 * @author <a href="http://boss.bekk.no/boss/middlegen/">Middlegen</a>
 *
 * @struts.action
 *    path="/struts/selectPolicyExecDetail"
 *    name="policyExecDetailForm"
 *    scope="session"
 *    validate="false"
 *
 * @struts.action-forward
 *    name="none"
 *    path="/struts/findPolicyExecDetail.do"
 *    redirect="false"
 *
 * @struts.action-forward
 *    name="one"
 *    path="/struts/viewPolicyExecDetail.jsp"
 *    redirect="false"
 *
 * @struts.action-forward
 *    name="many"
 *    path="/struts/listPolicyExecDetail.jsp"
 *    redirect="false"
 *

 */
public final class SelectPolicyExecDetailAction extends Action {

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

		PolicyExecDetailForm policyExecDetailForm = (PolicyExecDetailForm)form;


		try {
  		List policyExecDetails = com.cbrc.smis.adapter.StrutsPolicyExecDetailDelegate.select(policyExecDetailForm);

			if( policyExecDetails.size() == 0 ) {
				return mapping.findForward("none");
			} else if( policyExecDetails.size() == 1 ) {
				// Found one. Display it.
				PolicyExecDetailForm policyExecDetailFormTemp = (PolicyExecDetailForm) policyExecDetails.get(0);
				request.setAttribute("policyExecDetail", policyExecDetailFormTemp);

				return mapping.findForward("one");
			} else {
				// Found many. Display all.
				request.setAttribute("policyExecDetails", policyExecDetails);
				return mapping.findForward("many");
			}
		} catch (Exception e) {
			getServlet().log("Create error", e);
			return mapping.findForward("ejb-finder-exception");
		}
	}
}
