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

import com.cbrc.smis.form.MCellFormuForm;

/**
 * Finds a MCellFormu
 *
 * @author <a href="http://boss.bekk.no/boss/middlegen/">Middlegen</a>
 *
 * @struts.action
 *    path="/struts/selectMCellFormu"
 *    name="MCellFormuForm"
 *    scope="session"
 *    validate="false"
 *
 * @struts.action-forward
 *    name="none"
 *    path="/struts/findMCellFormu.do"
 *    redirect="false"
 *
 * @struts.action-forward
 *    name="one"
 *    path="/struts/viewMCellFormu.jsp"
 *    redirect="false"
 *
 * @struts.action-forward
 *    name="many"
 *    path="/struts/listMCellFormu.jsp"
 *    redirect="false"
 *

 */
public final class SelectMCellFormuAction extends Action {

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

		MCellFormuForm MCellFormuForm = (MCellFormuForm)form;


		try {
  		List MCellFormus = com.cbrc.smis.adapter.StrutsMCellFormuDelegate.select(MCellFormuForm);

			if( MCellFormus.size() == 0 ) {
				return mapping.findForward("none");
			} else if( MCellFormus.size() == 1 ) {
				// Found one. Display it.
				MCellFormuForm MCellFormuFormTemp = (MCellFormuForm) MCellFormus.get(0);
				request.setAttribute("MCellFormu", MCellFormuFormTemp);

				return mapping.findForward("one");
			} else {
				// Found many. Display all.
				request.setAttribute("MCellFormus", MCellFormus);
				return mapping.findForward("many");
			}
		} catch (Exception e) {
			getServlet().log("Create error", e);
			return mapping.findForward("ejb-finder-exception");
		}
	}
}
