package com.cbrc.smis.action;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.cbrc.smis.form.MRepFreqForm;

/**
 * Finds a MRepFreq
 *
 * @author <a href="http://boss.bekk.no/boss/middlegen/">Middlegen</a>
 *
 * @struts.action
 *    path="/struts/selectMRepFreq"
 *    name="MRepFreqForm"
 *    scope="session"
 *    validate="false"
 *
 * @struts.action-forward
 *    name="none"
 *    path="/struts/findMRepFreq.do"
 *    redirect="false"
 *
 * @struts.action-forward
 *    name="one"
 *    path="/struts/viewMRepFreq.jsp"
 *    redirect="false"
 *
 * @struts.action-forward
 *    name="many"
 *    path="/struts/listMRepFreq.jsp"
 *    redirect="false"
 *

 */
public final class SelectMRepFreqAction extends Action {

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

		MRepFreqForm MRepFreqForm = (MRepFreqForm)form;


		/*try {
  		List MRepFreqs = com.cbrc.smis.adapter.StrutsMRepFreqDelegate.select(MRepFreqForm);

			if( MRepFreqs.size() == 0 ) {
				return mapping.findForward("none");
			} else if( MRepFreqs.size() == 1 ) {
				// Found one. Display it.
				MRepFreqForm MRepFreqFormTemp = (MRepFreqForm) MRepFreqs.get(0);
				request.setAttribute("MRepFreq", MRepFreqFormTemp);

				return mapping.findForward("one");
			} else {
				// Found many. Display all.
				request.setAttribute("MRepFreqs", MRepFreqs);
				return mapping.findForward("many");
			}
		} catch (Exception e) {
			getServlet().log("Create error", e);
			return mapping.findForward("ejb-finder-exception");
		}*/
		return mapping.findForward("ejb-finder-exception");
	}
}
