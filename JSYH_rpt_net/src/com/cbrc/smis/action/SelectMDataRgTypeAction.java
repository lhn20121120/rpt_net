package com.cbrc.smis.action;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.cbrc.smis.form.MDataRgTypeForm;

/**
 * Finds a MDataRgType
 *
 * @author <a href="http://boss.bekk.no/boss/middlegen/">Middlegen</a>
 *
 * @struts.action
 *    path="/struts/selectMDataRgType"
 *    name="MDataRgTypeForm"
 *    scope="session"
 *    validate="false"
 *
 * @struts.action-forward
 *    name="none"
 *    path="/struts/findMDataRgType.do"
 *    redirect="false"
 *
 * @struts.action-forward
 *    name="one"
 *    path="/struts/viewMDataRgType.jsp"
 *    redirect="false"
 *
 * @struts.action-forward
 *    name="many"
 *    path="/struts/listMDataRgType.jsp"
 *    redirect="false"
 *

 */
public final class SelectMDataRgTypeAction extends Action {

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

		MDataRgTypeForm MDataRgTypeForm = (MDataRgTypeForm)form;


		/*try {
  		List MDataRgTypes = com.cbrc.smis.adapter.StrutsMDataRgTypeDelegate.select(MDataRgTypeForm);

			if( MDataRgTypes.size() == 0 ) {
				return mapping.findForward("none");
			} else if( MDataRgTypes.size() == 1 ) {
				// Found one. Display it.
				MDataRgTypeForm MDataRgTypeFormTemp = (MDataRgTypeForm) MDataRgTypes.get(0);
				request.setAttribute("MDataRgType", MDataRgTypeFormTemp);

				return mapping.findForward("one");
			} else {
				// Found many. Display all.
				request.setAttribute("MDataRgTypes", MDataRgTypes);
				return mapping.findForward("many");
			}
		} catch (Exception e) {
			getServlet().log("Create error", e);
			return mapping.findForward("ejb-finder-exception");
		}*/
		return mapping.findForward("ejb-finder-exception");
	}
}
