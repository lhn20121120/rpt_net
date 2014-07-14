package com.cbrc.smis.action;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.cbrc.smis.form.CalendarTypeForm;

/**
 * Inserts a calendarType.
 *
 * @author <a href="http://boss.bekk.no/boss/middlegen/">Middlegen</a>
 *
 * @struts.action
 *    path="/struts/insertCalendarType"
 *    name="calendarTypeForm"
 *    scope="session"
 *    validate="false"
 *
 * @struts.action-forward
 *    name="view"
 *    path="/struts/viewCalendarType.do"
 *    redirect="false"
 *

 */
public final class InsertCalendarTypeAction extends Action {

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
         CalendarTypeForm calendarTypeForm = (CalendarTypeForm)form;
         calendarTypeForm = com.cbrc.smis.adapter.StrutsCalendarTypeDelegate.create(calendarTypeForm);
         // Set the newly created vo as a request attribute to be picked up
         request.setAttribute("calendarType", calendarTypeForm);
         return mapping.findForward("view");
      } catch (Exception e) {
         getServlet().log("Create error", e);
         request.setAttribute("name", "CalendarType");
         return mapping.findForward("ejb-create-exception");
      }
   }
}
