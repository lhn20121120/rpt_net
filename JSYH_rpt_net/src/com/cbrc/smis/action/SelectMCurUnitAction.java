package com.cbrc.smis.action;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.cbrc.smis.form.MCurUnitForm;
import com.cbrc.smis.util.FitechException;

/**
 * Finds a MCurUnit
 *
 * @author <a href="http://boss.bekk.no/boss/middlegen/">Middlegen</a>
 *
 * @struts.action
 *    path="/struts/selectMCurUnit"
 *    name="MCurUnitForm"
 *    scope="session"
 *    validate="false"
 *
 * @struts.action-forward
 *    name="none"
 *    path="/struts/findMCurUnit.do"
 *    redirect="false"
 *
 * @struts.action-forward
 *    name="one"
 *    path="/struts/viewMCurUnit.jsp"
 *    redirect="false"
 *
 * @struts.action-forward
 *    name="many"
 *    path="/struts/listMCurUnit.jsp"
 *    redirect="false"
 *

 */
public final class SelectMCurUnitAction extends Action {
	private static FitechException log = new FitechException(DeleteLogInAction.class);
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
		MCurUnitForm mCurUnitForm = (MCurUnitForm)form;



		/*try {
			if (mCurUnitForm==null)
			List MCurUnits =com.cbrc.smis.adapter.StrutsMCurUnitDelegate.select(MCurUnitForm);

			if( list.size() == 0 ) {
				return mapping.findForward("view");
			} else if( MCurUnits.size() == 1 ) {
				// Found one. Display it.
				MCurUnitForm MCurUnitFormTemp = (MCurUnitForm) MCurUnits.get(0);
				request.setAttribute(Config.RECORDS, MCurUnitFormTemp);

				return mapping.findForward("one");
			} else {
				// Found many. Display all.
				request.setAttribute("MCurUnits", MCurUnits);
				return mapping.findForward("many");
			}
		} catch (Exception e) {
			log.printStackTrace( e);
			return mapping.findForward("ejb-finder-exception");
		}*/
		return mapping.findForward("ejb-finder-exception");


	}
}
