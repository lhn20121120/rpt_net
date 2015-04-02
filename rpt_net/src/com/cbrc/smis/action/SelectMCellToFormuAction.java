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

import com.cbrc.smis.form.MCellToFormuForm;

/**
 * Finds a MCellToFormu
 *
 * @author <a href="http://boss.bekk.no/boss/middlegen/">Middlegen</a>
 *
 * @struts.action
 *    path="/struts/selectMCellToFormu"
 *    name="MCellToFormuForm"
 *    scope="session"
 *    validate="false"
 *
 * @struts.action-forward
 *    name="none"
 *    path="/struts/findMCellToFormu.do"
 *    redirect="false"
 *
 * @struts.action-forward
 *    name="one"
 *    path="/struts/viewMCellToFormu.jsp"
 *    redirect="false"
 *
 * @struts.action-forward
 *    name="many"
 *    path="/struts/listMCellToFormu.jsp"
 *    redirect="false"
 *

 */
public final class SelectMCellToFormuAction extends Action {

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

		MCellToFormuForm MCellToFormuForm = (MCellToFormuForm)form;


		try {
  		List MCellToFormus = null; //com.cbrc.smis.adapter.StrutsMCellToFormuDelegate.select(MCellToFormuForm);


			if( MCellToFormus.size() == 0 ) {
				return mapping.findForward("none");
			} else if( MCellToFormus.size() == 1 ) {
				// Found one. Display it.
				MCellToFormuForm MCellToFormuFormTemp = (MCellToFormuForm) MCellToFormus.get(0);
				request.setAttribute("MCellToFormu", MCellToFormuFormTemp);

				return mapping.findForward("one");
			} else {
				// Found many. Display all.
				request.setAttribute("MCellToFormus", MCellToFormus);
				return mapping.findForward("many");
			}
		} catch (Exception e) {
			getServlet().log("Create error", e);
			return mapping.findForward("ejb-finder-exception");
		}
	}
}
