package com.cbrc.auth.action;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.RequestUtils;

import com.cbrc.auth.form.MUserToGrpForm;

/**
 * Deletes a MUserToGrp.
 *
 * @author <a href="http://boss.bekk.no/boss/middlegen/">Middlegen</a>
 *
 * @struts.action
 *    path="/struts/deleteMUserToGrp"
 *
 * @struts.action-forward
 *    name="all"
 *    path="/struts/getAll.do"
 *    redirect="false"
 *

 */
public final class DeleteMUserToGrpAction extends Action {

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

      MUserToGrpForm MUserToGrpForm = new MUserToGrpForm();

      // Now set the form members with request values 
      RequestUtils.populate(MUserToGrpForm, request);
      try {
         //com.cbrc.auth.adapter.StrutsMUserToGrpDelegate.remove(MUserToGrpForm);
      } catch (Exception e) {
         getServlet().log("Remove error", e);
         request.setAttribute("name", "MUserToGrp");
         return mapping.findForward("ejb-remove-exception");
      }

      return mapping.findForward("all");
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
//   private String getParameter(HttpServletRequest req, String name) {
//      String value = req.getParameter(name);
//      if (value == null) {
//         value = (String)req.getAttribute(name);
//      }
//      return value;
//   }
}
