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
 * Finds a MActuRep
 *
 * @author <a href="http://boss.bekk.no/boss/middlegen/">Middlegen</a>
 *
 * @struts.action
 *    path="/struts/selectMActuRep"
 *    name="MActuRepForm"
 *    scope="session"
 *    validate="false"
 *
 * @struts.action-forward
 *    name="none"
 *    path="/struts/findMActuRep.do"
 *    redirect="false"
 *
 * @struts.action-forward
 *    name="one"
 *    path="/struts/viewMActuRep.jsp"
 *    redirect="false"
 *
 * @struts.action-forward
 *    name="many"
 *    path="/struts/listMActuRep.jsp"
 *    redirect="false"
 *

 */
public final class SelectMActuRepAction extends Action {

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
/*
		MActuRepForm MActuRepForm = (MActuRepForm)form;


		try {
  		List MActuReps = null; //com.cbrc.smis.adapter.StrutsMActuRepDelegate.select(MActuRepForm);

			if( MActuReps.size() == 0 ) {
				return mapping.findForward("none");
			} else if( MActuReps.size() == 1 ) {
				// Found one. Display it.
				MActuRepForm MActuRepFormTemp = (MActuRepForm) MActuReps.get(0);
				request.setAttribute("MActuRep", MActuRepFormTemp);

				return mapping.findForward("one");
			} else {
				// Found many. Display all.
				request.setAttribute("MActuReps", MActuReps);
				return mapping.findForward("many");
			}
		} catch (Exception e) {
			getServlet().log("Create error", e);
			
		}
	*/
        return mapping.findForward("ejb-finder-exception");
    }
}
    
