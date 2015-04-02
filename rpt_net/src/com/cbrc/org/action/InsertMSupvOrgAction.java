package com.cbrc.org.action;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.cbrc.org.form.MSupvOrgForm;

/**
 * Inserts a MSupvOrg.
 *
 * @author <a href="http://boss.bekk.no/boss/middlegen/">Middlegen</a>
 *
 * @struts.action
 *    path="/struts/insertMSupvOrg"
 *    name="MSupvOrgForm"
 *    scope="session"
 *    validate="false"
 *
 * @struts.action-forward
 *    name="view"
 *    path="/struts/viewMSupvOrg.do"
 *    redirect="false"
 *

 */
public final class InsertMSupvOrgAction extends Action {

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

      try {
         MSupvOrgForm MSupvOrgForm = (MSupvOrgForm)form;
         MSupvOrgForm = com.cbrc.org.adapter.StrutsMSupvOrgDelegate.create(MSupvOrgForm);
         // Set the newly created vo as a request attribute to be picked up
         request.setAttribute("MSupvOrg", MSupvOrgForm);
         return mapping.findForward("view");
      } catch (Exception e) {
         getServlet().log("Create error", e);
         request.setAttribute("name", "MSupvOrg");
         return mapping.findForward("ejb-create-exception");
      }
   }
}
