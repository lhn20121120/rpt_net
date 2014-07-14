package com.cbrc.smis.action;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.cbrc.smis.form.ValidateTypeForm;

/**
 * Finds a validateType
 *
 * @author <a href="http://boss.bekk.no/boss/middlegen/">Middlegen</a>
 *
 * @struts.action
 *    path="/struts/selectValidateType"
 *    name="validateTypeForm"
 *    scope="session"
 *    validate="false"
 *
 * @struts.action-forward
 *    name="none"
 *    path="/struts/findValidateType.do"
 *    redirect="false"
 *
 * @struts.action-forward
 *    name="one"
 *    path="/struts/viewValidateType.jsp"
 *    redirect="false"
 *
 * @struts.action-forward
 *    name="many"
 *    path="/struts/listValidateType.jsp"
 *    redirect="false"
 *

 */
public final class SelectValidateTypeAction extends Action {

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

		ValidateTypeForm validateTypeForm = (ValidateTypeForm)form;


		/*try {
  		List validateTypes = com.cbrc.smis.adapter.StrutsValidateTypeDelegate.select(validateTypeForm);

			if( validateTypes.size() == 0 ) {
				return mapping.findForward("none");
			} else if( validateTypes.size() == 1 ) {
				// Found one. Display it.
				ValidateTypeForm validateTypeFormTemp = (ValidateTypeForm) validateTypes.get(0);
				request.setAttribute("validateType", validateTypeFormTemp);

				return mapping.findForward("one");
			} else {
				// Found many. Display all.
				request.setAttribute("validateTypes", validateTypes);
				return mapping.findForward("many");
			}
		} catch (Exception e) {
			getServlet().log("Create error", e);
			return mapping.findForward("ejb-finder-exception");
		}*/
		return mapping.findForward("ejb-finder-exception");
	}
}
