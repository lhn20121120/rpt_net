package com.cbrc.auth.action;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * Finds a userRole
 *
 * @author <a href="http://boss.bekk.no/boss/middlegen/">Middlegen</a>
 *
 * @struts.action
 *    path="/struts/selectUserRole"
 *    name="userRoleForm"
 *    scope="session"
 *    validate="false"
 *
 * @struts.action-forward
 *    name="none"
 *    path="/struts/findUserRole.do"
 *    redirect="false"
 *
 * @struts.action-forward
 *    name="one"
 *    path="/struts/viewUserRole.jsp"
 *    redirect="false"
 *
 * @struts.action-forward
 *    name="many"
 *    path="/struts/listUserRole.jsp"
 *    redirect="false"
 *

 */
public final class SelectUserRoleAction extends Action {

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

		return mapping.findForward("none");
	}
}
