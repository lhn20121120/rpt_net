package com.cbrc.smis.action;

import java.io.IOException;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.RequestUtils;

import com.cbrc.smis.form.PolicyExecDetailForm;

/**
 * Displays a policyExecDetail. We'll first try to look for a policyExecDetail
 * object on the request attribute (which should be set if an insert, 
 * update or select action forwarded to us). If this attribute is not set,
 * we're probably called directly from a page, and we'll look up
 * the person by its id which should be passed as a request parameter.
 *
 * @author <a href="http://boss.bekk.no/boss/middlegen/">Middlegen</a>
 *
 * @struts.action
 *    path="/struts/viewPolicyExecDetail"
 *
 * @struts.action-forward
 *    name="view"
 *    path="/struts/viewPolicyExecDetail.jsp"
 *    redirect="false"
 *

 */
public final class ViewPolicyExecDetailAction extends Action {

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

      // First see if there is a vo as request attribute.
      PolicyExecDetailForm policyExecDetailForm = (PolicyExecDetailForm)request.getAttribute("policyExecDetail");
      if( policyExecDetailForm == null ) {
         // The vo wasn't on the request. We came via a direct link.
         // Get pk data from request object and use delegate to lookup
         try {
            policyExecDetailForm = new PolicyExecDetailForm();
            // Now set the form members with request values 
            RequestUtils.populate(policyExecDetailForm, request);

            policyExecDetailForm = com.cbrc.smis.adapter.StrutsPolicyExecDetailDelegate.edit(policyExecDetailForm);
            request.setAttribute("policyExecDetail", policyExecDetailForm);
         } catch (Exception e) {
            getServlet().log("Create error", e);
            request.setAttribute("name", "PolicyExecDetail");
            return mapping.findForward("ejb-finder-exception");
         }
      }

      // Make a map that can be used to identify this PolicyExecDetail in any links
      HashMap urlParams = new HashMap();
  urlParams.put("repInId",policyExecDetailForm.getRepInId());
  urlParams.put("policyId",policyExecDetailForm.getPolicyId());
      request.setAttribute("__pk", urlParams);

      return mapping.findForward("view");
   }
   
   /**
    * Get the named value as a string from the request parameter, or from the
    * request obj if not found.
    * 
    * @param req The request object.
    * @param name The name of the parameter.
    *
    * @return The value of the parameter as a String.
    */
   private String getParameter(HttpServletRequest req, String name) {
      String value = req.getParameter(name);
      if (value == null) {
         value = req.getAttribute(name).toString();
      }
      return value;
   }
}
