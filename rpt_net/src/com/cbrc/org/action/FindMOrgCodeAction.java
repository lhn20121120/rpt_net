package com.cbrc.org.action;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.cbrc.org.form.MOrgCodeForm;

/**
 * Find a MOrgCode and forward to the form jsp, with select action.
 *
 * @author <a href="http://boss.bekk.no/boss/middlegen/">Middlegen</a>
 *
 * @struts.action
 *    path="/struts/findMOrgCode"
 *
 * @struts.action-forward
 *    name="form"
 *    path="/struts/formMOrgCode.jsp"
 *    redirect="false"
 *

 */
public final class FindMOrgCodeAction extends Action {

   /**
    * Performs action.
    * @param mapping Action mapping.
    * @param form Action form.
    * @param request HTTP request.
    * @param response HTTP response.
    * @exception IOException if an input/output error occurs
    * @exception ServletException if a servlet exception occurs
    */
   public ActionForward perform(ActionMapping mapping,
      ActionForm form,
      HttpServletRequest request,
      HttpServletResponse response)
      throws IOException, ServletException {

      request.setAttribute("action", "select");
		
      request.getSession().setAttribute("MOrgCodeForm", new MOrgCodeForm());

      return mapping.findForward("form");
   }
}