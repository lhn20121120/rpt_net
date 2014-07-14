package com.cbrc.auth.action;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.cbrc.auth.form.ProductUserForm;

/**
 * Finds a productUser
 *
 * @author <a href="http://boss.bekk.no/boss/middlegen/">Middlegen</a>
 *
 * @struts.action
 *    path="/struts/selectProductUser"
 *    name="productUserForm"
 *    scope="session"
 *    validate="false"
 *
 * @struts.action-forward
 *    name="none"
 *    path="/struts/findProductUser.do"
 *    redirect="false"
 *
 * @struts.action-forward
 *    name="one"
 *    path="/struts/viewProductUser.jsp"
 *    redirect="false"
 *
 * @struts.action-forward
 *    name="many"
 *    path="/struts/listProductUser.jsp"
 *    redirect="false"
 *

 */
public final class SelectProductUserAction extends Action {

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

		ProductUserForm productUserForm = (ProductUserForm)form;


		try {
  		List productUsers = com.cbrc.auth.adapter.StrutsProductUserDelegate.select(productUserForm);

			if( productUsers.size() == 0 ) {
				return mapping.findForward("none");
			} else if( productUsers.size() == 1 ) {
				// Found one. Display it.
				ProductUserForm productUserFormTemp = (ProductUserForm) productUsers.get(0);
				request.setAttribute("productUser", productUserFormTemp);

				return mapping.findForward("one");
			} else {
				// Found many. Display all.
				request.setAttribute("productUsers", productUsers);
				return mapping.findForward("many");
			}
		} catch (Exception e) {
			getServlet().log("Create error", e);
			return mapping.findForward("ejb-finder-exception");
		}
	}
}
