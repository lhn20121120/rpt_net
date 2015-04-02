package com.cbrc.smis.action;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.cbrc.smis.form.PolicyForm;

/**
 * Updates a policy.
 *
 * @author <a href="http://boss.bekk.no/boss/middlegen/">Middlegen</a>
 *
 * @struts.action
 *    path="/struts/updatePolicy"
 *    name="policyForm"
 *    scope="session"
 *    validate="false"
 *
 * @struts.action-forward
 *    name="view"
 *    path="/struts/viewPolicy.do"
 *    redirect="false"
 *

 */
public final class UpdatePolicyAction extends Action {

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

      PolicyForm policyForm = (PolicyForm)form;

      try {
         policyForm = com.cbrc.smis.adapter.StrutsPolicyDelegate.update(policyForm);
         request.setAttribute("policy", policyForm);
      } catch (Exception e) {
         getServlet().log("Find error", e);
         request.setAttribute("name", "Policy");
         return mapping.findForward("ejb-finder-exception");
      }

      return mapping.findForward("view");
   }
}
