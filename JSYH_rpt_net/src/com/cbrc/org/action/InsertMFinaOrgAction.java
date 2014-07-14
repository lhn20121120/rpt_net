package com.cbrc.org.action;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.cbrc.org.form.MFinaOrgForm;

/**
 * Inserts a MFinaOrg.
 *
 * @author <a href="http://boss.bekk.no/boss/middlegen/">Middlegen</a>
 *
 * @struts.action
 *    path="/struts/insertMFinaOrg"
 *    name="MFinaOrgForm"
 *    scope="session"
 *    validate="false"
 *
 * @struts.action-forward
 *    name="view"
 *    path="/struts/viewMFinaOrg.do"
 *    redirect="false"
 *

 */
public final class InsertMFinaOrgAction extends Action {

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
         MFinaOrgForm MFinaOrgForm = (MFinaOrgForm)form;
         MFinaOrgForm = com.cbrc.org.adapter.StrutsMFinaOrgDelegate.create(MFinaOrgForm);
         // Set the newly created vo as a request attribute to be picked up
         request.setAttribute("MFinaOrg", MFinaOrgForm);
         return mapping.findForward("view");
      } catch (Exception e) {
         getServlet().log("Create error", e);
         request.setAttribute("name", "MFinaOrg");
         return mapping.findForward("ejb-create-exception");
      }
   }
}
