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
 * List records from the tables.
 *
 * @author <a href="http://boss.bekk.no/boss/middlegen/">Middlegen</a>
 * @version 1.0
 *
 * @struts.action
 *    path="/struts/getAll"
 *
 * @struts.action-forward
 *    name="success"
 *    path="/struts/index.jsp"
 *    redirect="false"
 */
public final class GetAllAction extends Action {

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
/*

		try {
    			Collection DEPARTMENT = com.cbrc.auth.adapter.StrutsDepartmentDelegate.findAll();

			request.setAttribute("DEPARTMENT", DEPARTMENT);
    			Collection ROLE_TOOL = com.cbrc.auth.adapter.StrutsRoleToolDelegate.findAll();

			request.setAttribute("ROLE_TOOL", ROLE_TOOL);
    			Collection PRODUCT_USER = com.cbrc.auth.adapter.StrutsProductUserDelegate.findAll();

			request.setAttribute("PRODUCT_USER", PRODUCT_USER);
    			Collection OPERATOR = com.cbrc.auth.adapter.StrutsOperatorDelegate.findAll();

			request.setAttribute("OPERATOR", OPERATOR);
    			Collection M_USER_TO_GRP = com.cbrc.auth.adapter.StrutsMUserToGrpDelegate.findAll();

			request.setAttribute("M_USER_TO_GRP", M_USER_TO_GRP);
    			Collection USER_ROLE = com.cbrc.auth.adapter.StrutsUserRoleDelegate.findAll();

			request.setAttribute("USER_ROLE", USER_ROLE);
    			Collection M_PUR_VIEW = com.cbrc.auth.adapter.StrutsMPurViewDelegate.findAll();

			request.setAttribute("M_PUR_VIEW", M_PUR_VIEW);
    			Collection M_USER_GRP = com.cbrc.auth.adapter.StrutsMUserGrpDelegate.findAll();

			request.setAttribute("M_USER_GRP", M_USER_GRP);
    			Collection ROLE = com.cbrc.auth.adapter.StrutsRoleDelegate.findAll();

			request.setAttribute("ROLE", ROLE);
    			Collection TOOL_SETTING = com.cbrc.auth.adapter.StrutsToolSettingDelegate.findAll();

			request.setAttribute("TOOL_SETTING", TOOL_SETTING);
    		} catch (Exception e) {
			getServlet().log("Can't find entity", e);
			return mapping.findForward("ejb-finder-exception");
		}
*/
		return mapping.findForward("success");
	}
}
