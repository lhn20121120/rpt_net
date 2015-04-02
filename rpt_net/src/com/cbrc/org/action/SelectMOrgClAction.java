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

import com.cbrc.org.form.MOrgClForm;

/**
 * Finds a MOrgCl
 *
 * @author <a href="http://boss.bekk.no/boss/middlegen/">Middlegen</a>
 *
 * @struts.action
 *    path="/struts/selectMOrgCl"
 *    name="MOrgClForm"
 *    scope="session"
 *    validate="false"
 *
 * @struts.action-forward
 *    name="none"
 *    path="/struts/findMOrgCl.do"
 *    redirect="false"
 *
 * @struts.action-forward
 *    name="one"
 *    path="/struts/viewMOrgCl.jsp"
 *    redirect="false"
 *
 * @struts.action-forward
 *    name="many"
 *    path="/struts/listMOrgCl.jsp"
 *    redirect="false"
 *

 */
public final class SelectMOrgClAction extends Action {

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

		MOrgClForm MOrgClForm = (MOrgClForm)form;


		try {
  		List MOrgCls = null;
  		//com.cbrc.org.adapter.StrutsMOrgClDelegate.select(MOrgClForm);

			if( MOrgCls.size() == 0 ) {
				return mapping.findForward("none");
			} else if( MOrgCls.size() == 1 ) {
				// Found one. Display it.
				MOrgClForm MOrgClFormTemp = (MOrgClForm) MOrgCls.get(0);
				request.setAttribute("MOrgCl", MOrgClFormTemp);

				return mapping.findForward("one");
			} else {
				// Found many. Display all.
				request.setAttribute("MOrgCls", MOrgCls);
				return mapping.findForward("many");
			}
		} catch (Exception e) {
			getServlet().log("Create error", e);
			return mapping.findForward("ejb-finder-exception");
		}
	}
}
