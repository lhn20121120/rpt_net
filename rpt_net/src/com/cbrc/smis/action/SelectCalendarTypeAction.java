package com.cbrc.smis.action;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.cbrc.smis.form.CalendarTypeForm;

/**
 * Finds a calendarType
 *
 * @author <a href="http://boss.bekk.no/boss/middlegen/">Middlegen</a>
 *
 * @struts.action
 *    path="/struts/selectCalendarType"
 *    name="calendarTypeForm"
 *    scope="session"
 *    validate="false"
 *
 * @struts.action-forward
 *    name="none"
 *    path="/struts/findCalendarType.do"
 *    redirect="false"
 *
 * @struts.action-forward
 *    name="one"
 *    path="/struts/viewCalendarType.jsp"
 *    redirect="false"
 *
 * @struts.action-forward
 *    name="many"
 *    path="/struts/listCalendarType.jsp"
 *    redirect="false"
 *

 */
public final class SelectCalendarTypeAction extends Action {

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

		CalendarTypeForm calendarTypeForm = (CalendarTypeForm)form;


		try {
  		List calendarTypes = com.cbrc.smis.adapter.StrutsCalendarTypeDelegate.select(calendarTypeForm);

			if( calendarTypes.size() == 0 ) {
				return mapping.findForward("none");
			} else if( calendarTypes.size() == 1 ) {
				// Found one. Display it.
				CalendarTypeForm calendarTypeFormTemp = (CalendarTypeForm) calendarTypes.get(0);
				request.setAttribute("calendarType", calendarTypeFormTemp);

				return mapping.findForward("one");
			} else {
				// Found many. Display all.
				request.setAttribute("calendarTypes", calendarTypes);
				return mapping.findForward("many");
			}
		} catch (Exception e) {
			getServlet().log("Create error", e);
			return mapping.findForward("ejb-finder-exception");
		}
	}
}
