package com.cbrc.org.action;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.cbrc.org.form.MRegionTypForm;

/**
 * Finds a MRegionTyp
 *
 * @author <a href="http://boss.bekk.no/boss/middlegen/">Middlegen</a>
 *
 * @struts.action
 *    path="/struts/selectMRegionTyp"
 *    name="MRegionTypForm"
 *    scope="session"
 *    validate="false"
 *
 * @struts.action-forward
 *    name="none"
 *    path="/struts/findMRegionTyp.do"
 *    redirect="false"
 *
 * @struts.action-forward
 *    name="one"
 *    path="/struts/viewMRegionTyp.jsp"
 *    redirect="false"
 *
 * @struts.action-forward
 *    name="many"
 *    path="/struts/listMRegionTyp.jsp"
 *    redirect="false"
 *

 */
public final class SelectMRegionTypAction extends Action {

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

		MRegionTypForm MRegionTypForm = (MRegionTypForm)form;


		try {
  		List MRegionTyps = com.cbrc.org.adapter.StrutsMRegionTypDelegate.select(MRegionTypForm);

			if( MRegionTyps.size() == 0 ) {
				return mapping.findForward("none");
			} else if( MRegionTyps.size() == 1 ) {
				// Found one. Display it.
				MRegionTypForm MRegionTypFormTemp = (MRegionTypForm) MRegionTyps.get(0);
				request.setAttribute("MRegionTyp", MRegionTypFormTemp);

				return mapping.findForward("one");
			} else {
				// Found many. Display all.
				request.setAttribute("MRegionTyps", MRegionTyps);
				return mapping.findForward("many");
			}
		} catch (Exception e) {
			getServlet().log("Create error", e);
			return mapping.findForward("ejb-finder-exception");
		}
	}
}
