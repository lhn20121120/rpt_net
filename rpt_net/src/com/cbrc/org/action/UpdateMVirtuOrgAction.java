package com.cbrc.org.action;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.cbrc.org.form.MVirtuOrgForm;

/**
 * Updates a MVirtuOrg.
 *
 * @author <a href="http://boss.bekk.no/boss/middlegen/">Middlegen</a>
 *
 * @struts.action
 *    path="/struts/updateMVirtuOrg"
 *    name="MVirtuOrgForm"
 *    scope="session"
 *    validate="false"
 *
 * @struts.action-forward
 *    name="view"
 *    path="/struts/viewMVirtuOrg.do"
 *    redirect="false"
 *

 */
public final class UpdateMVirtuOrgAction extends Action {

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

      MVirtuOrgForm MVirtuOrgForm = (MVirtuOrgForm)form;

      try {
         MVirtuOrgForm = com.cbrc.org.adapter.StrutsMVirtuOrgDelegate.update(MVirtuOrgForm);
         request.setAttribute("MVirtuOrg", MVirtuOrgForm);
      } catch (Exception e) {
         getServlet().log("Find error", e);
         request.setAttribute("name", "MVirtuOrg");
         return mapping.findForward("ejb-finder-exception");
      }

      return mapping.findForward("view");
   }
}
