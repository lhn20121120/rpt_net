package com.cbrc.smis.action;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.cbrc.smis.form.MRepTypeForm;

/**
 * Finds a MRepType
 *
 * @author <a href="http://boss.bekk.no/boss/middlegen/">Middlegen</a>
 *
 * @struts.action
 *    path="/struts/selectMRepType"
 *    name="MRepTypeForm"
 *    scope="session"
 *    validate="false"
 *
 * @struts.action-forward
 *    name="none"
 *    path="/struts/findMRepType.do"
 *    redirect="false"
 *
 * @struts.action-forward
 *    name="one"
 *    path="/struts/viewMRepType.jsp"
 *    redirect="false"
 *
 * @struts.action-forward
 *    name="many"
 *    path="/struts/listMRepType.jsp"
 *    redirect="false"
 *

 */
public final class SelectMRepTypeAction extends Action {

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

		MRepTypeForm MRepTypeForm = (MRepTypeForm)form;


		/*try {
  		List MRepTypes = com.cbrc.smis.adapter.StrutsMRepTypeDelegate.select(MRepTypeForm);

			if( MRepTypes.size() == 0 ) {
				return mapping.findForward("none");
			} else if( MRepTypes.size() == 1 ) {
				// Found one. Display it.
				MRepTypeForm MRepTypeFormTemp = (MRepTypeForm) MRepTypes.get(0);
				request.setAttribute("MRepType", MRepTypeFormTemp);

				return mapping.findForward("one");
			} else {
				// Found many. Display all.
				request.setAttribute("MRepTypes", MRepTypes);
				return mapping.findForward("many");
			}
		} catch (Exception e) {
			getServlet().log("Create error", e);
			return mapping.findForward("ejb-finder-exception");
		}*/
		return mapping.findForward("ejb-finder-exception");
	}
}
