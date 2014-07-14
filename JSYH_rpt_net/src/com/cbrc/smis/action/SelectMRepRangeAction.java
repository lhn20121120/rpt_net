package com.cbrc.smis.action;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * Finds a MRepRange
 *
 * @author <a href="http://boss.bekk.no/boss/middlegen/">Middlegen</a>
 *
 * @struts.action
 *    path="/struts/selectMRepRange"
 *    name="MRepRangeForm"
 *    scope="session"
 *    validate="false"
 *
 * @struts.action-forward
 *    name="none"
 *    path="/struts/findMRepRange.do"
 *    redirect="false"
 *
 * @struts.action-forward
 *    name="one"
 *    path="/struts/viewMRepRange.jsp"
 *    redirect="false"
 *
 * @struts.action-forward
 *    name="many"
 *    path="/struts/listMRepRange.jsp"
 *    redirect="false"
 *

 */
public final class SelectMRepRangeAction extends Action {

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

		return mapping.findForward("none");
	}
}
