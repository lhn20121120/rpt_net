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

import com.cbrc.smis.form.ColAbnormityChangeForm;

/**
 * Finds a colAbnormityChange
 *
 * @author <a href="http://boss.bekk.no/boss/middlegen/">Middlegen</a>
 *
 * @struts.action
 *    path="/struts/selectColAbnormityChange"
 *    name="colAbnormityChangeForm"
 *    scope="session"
 *    validate="false"
 *
 * @struts.action-forward
 *    name="none"
 *    path="/struts/findColAbnormityChange.do"
 *    redirect="false"
 *
 * @struts.action-forward
 *    name="one"
 *    path="/struts/viewColAbnormityChange.jsp"
 *    redirect="false"
 *
 * @struts.action-forward
 *    name="many"
 *    path="/struts/listColAbnormityChange.jsp"
 *    redirect="false"
 *

 */
public final class SelectColAbnormityChangeAction extends Action {

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

		ColAbnormityChangeForm colAbnormityChangeForm = (ColAbnormityChangeForm)form;


		try {
  		List colAbnormityChanges = null; //com.cbrc.smis.adapter.StrutsColAbnormityChangeDelegate.select(colAbnormityChangeForm);

			if( colAbnormityChanges.size() == 0 ) {
				return mapping.findForward("none");
			} else if( colAbnormityChanges.size() == 1 ) {
				// Found one. Display it.
				ColAbnormityChangeForm colAbnormityChangeFormTemp = (ColAbnormityChangeForm) colAbnormityChanges.get(0);
				request.setAttribute("colAbnormityChange", colAbnormityChangeFormTemp);

				return mapping.findForward("one");
			} else {
				// Found many. Display all.
				request.setAttribute("colAbnormityChanges", colAbnormityChanges);
				return mapping.findForward("many");
			}
		} catch (Exception e) {
			getServlet().log("Create error", e);
			return mapping.findForward("ejb-finder-exception");
		}
	}
}
