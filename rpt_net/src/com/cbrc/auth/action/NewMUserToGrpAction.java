package com.cbrc.auth.action;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.cbrc.auth.form.MUserToGrpForm;

/**
 * Open form for new MUserToGrp.
 *
 * @author <a href="http://boss.bekk.no/boss/middlegen/">Middlegen</a>
 *
 * @struts.action
 *    path="/struts/newMUserToGrp"
 *
 * @struts.action-forward
 *    name="form"
 *    path="/struts/formMUserToGrp.jsp"
 *    redirect="false"
 *

 */
public final class NewMUserToGrpAction extends Action {

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

      request.setAttribute("action", "insert");
      
      request.getSession().setAttribute("MUserToGrpForm", new MUserToGrpForm());

      return mapping.findForward("form");
   }
}
