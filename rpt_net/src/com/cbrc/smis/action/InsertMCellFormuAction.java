package com.cbrc.smis.action;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.cbrc.smis.form.MCellFormuForm;

/**
 * Inserts a MCellFormu.
 *
 * @author <a href="http://boss.bekk.no/boss/middlegen/">Middlegen</a>
 *
 * @struts.action
 *    path="/struts/insertMCellFormu"
 *    name="MCellFormuForm"
 *    scope="session"
 *    validate="false"
 *
 * @struts.action-forward
 *    name="view"
 *    path="/struts/viewMCellFormu.do"
 *    redirect="false"
 *

 */
public final class InsertMCellFormuAction extends Action {

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
         MCellFormuForm MCellFormuForm = (MCellFormuForm)form;
         MCellFormuForm = com.cbrc.smis.adapter.StrutsMCellFormuDelegate.create(MCellFormuForm);
         // Set the newly created vo as a request attribute to be picked up
         request.setAttribute("MCellFormu", MCellFormuForm);
         return mapping.findForward("view");
      } catch (Exception e) {
         getServlet().log("Create error", e);
         request.setAttribute("name", "MCellFormu");
         return mapping.findForward("ejb-create-exception");
      }
   }
}
