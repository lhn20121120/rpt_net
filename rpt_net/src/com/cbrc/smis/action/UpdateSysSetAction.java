package com.cbrc.smis.action;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.cbrc.smis.form.SysSetForm;

/**
 * Updates a sysSet.
 *
 * @author <a href="http://boss.bekk.no/boss/middlegen/">Middlegen</a>
 *
 * @struts.action
 *    path="/struts/updateSysSet"
 *    name="sysSetForm"
 *    scope="session"
 *    validate="false"
 *
 * @struts.action-forward
 *    name="view"
 *    path="/struts/viewSysSet.do"
 *    redirect="false"
 *

 */
public final class UpdateSysSetAction extends Action {

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

      SysSetForm sysSetForm = (SysSetForm)form;

      try {
         sysSetForm = com.cbrc.smis.adapter.StrutsSysSetDelegate.update(sysSetForm);
         request.setAttribute("sysSet", sysSetForm);
      } catch (Exception e) {
         getServlet().log("Find error", e);
         request.setAttribute("name", "SysSet");
         return mapping.findForward("ejb-finder-exception");
      }

      return mapping.findForward("view");
   }
}
