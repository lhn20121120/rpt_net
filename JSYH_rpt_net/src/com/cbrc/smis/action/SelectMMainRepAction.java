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

import com.cbrc.smis.form.MMainRepForm;

/**
 * Finds a MMainRep
 *
 * @author <a href="http://boss.bekk.no/boss/middlegen/">Middlegen</a>
 *
 * @struts.action
 *    path="/struts/selectMMainRep"
 *    name="MMainRepForm"
 *    scope="session"
 *    validate="false"
 *
 * @struts.action-forward
 *    name="none"
 *    path="/struts/findMMainRep.do"
 *    redirect="false"
 *
 * @struts.action-forward
 *    name="one"
 *    path="/struts/viewMMainRep.jsp"
 *    redirect="false"
 *
 * @struts.action-forward
 *    name="many"
 *    path="/struts/listMMainRep.jsp"
 *    redirect="false"
 *

 */
public final class SelectMMainRepAction extends Action {

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

		MMainRepForm MMainRepForm = (MMainRepForm)form;


		try {
  		List MMainReps = com.cbrc.smis.adapter.StrutsMMainRepDelegate.select(MMainRepForm);

			if( MMainReps.size() == 0 ) {
				return mapping.findForward("none");
			} else if( MMainReps.size() == 1 ) {
				// Found one. Display it.
				MMainRepForm MMainRepFormTemp = (MMainRepForm) MMainReps.get(0);
				request.setAttribute("MMainRep", MMainRepFormTemp);

				return mapping.findForward("one");
			} else {
				// Found many. Display all.
				request.setAttribute("MMainReps", MMainReps);
				return mapping.findForward("many");
			}
		} catch (Exception e) {
			getServlet().log("Create error", e);
			return mapping.findForward("ejb-finder-exception");
		}
	}
}
