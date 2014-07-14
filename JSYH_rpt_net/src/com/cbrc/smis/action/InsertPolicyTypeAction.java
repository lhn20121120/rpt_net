package com.cbrc.smis.action;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.cbrc.smis.form.PolicyTypeForm;

/**
 * Inserts a policyType.
 *
 * @author <a href="http://boss.bekk.no/boss/middlegen/">Middlegen</a>
 *
 * @struts.action
 *    path="/struts/insertPolicyType"
 *    name="policyTypeForm"
 *    scope="session"
 *    validate="false"
 *
 * @struts.action-forward
 *    name="view"
 *    path="/struts/viewPolicyType.do"
 *    redirect="false"
 *

 */
public final class InsertPolicyTypeAction extends Action {

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

      try {
         PolicyTypeForm policyTypeForm = (PolicyTypeForm)form;
         policyTypeForm = com.cbrc.smis.adapter.StrutsPolicyTypeDelegate.create(policyTypeForm);
         // Set the newly created vo as a request attribute to be picked up
         request.setAttribute("policyType", policyTypeForm);
         return mapping.findForward("view");
      } catch (Exception e) {
         getServlet().log("Create error", e);
         request.setAttribute("name", "PolicyType");
         return mapping.findForward("ejb-create-exception");
      }
   }
}
