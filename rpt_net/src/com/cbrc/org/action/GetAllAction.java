package com.cbrc.org.action;

import java.io.IOException;
import java.util.Collection;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * List records from the tables.
 *
 * @author <a href="http://boss.bekk.no/boss/middlegen/">Middlegen</a>
 * @version 1.0
 *
 * @struts.action
 *    path="/struts/getAll"
 *
 * @struts.action-forward
 *    name="success"
 *    path="/struts/index.jsp"
 *    redirect="false"
 */
public final class GetAllAction extends Action {

	/**
	 * Performs action.
	 * @param mapping Action mapping.
	 * @param form Action form.
	 * @param request HTTP request.
	 * @param response HTTP response.
	 * @exception IOException if an input/output error occurs
	 * @exception ServletException if a servlet exception occurs
	 */
	public ActionForward perform(ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws IOException, ServletException {


		try {
    			Collection M_ORG = com.cbrc.org.adapter.StrutsMOrgDelegate.findAll();

			request.setAttribute("M_ORG", M_ORG);
    			Collection M_REGION = com.cbrc.org.adapter.StrutsMRegionDelegate.findAll();

			request.setAttribute("M_REGION", M_REGION);
    			Collection M_SUPV_ORG = com.cbrc.org.adapter.StrutsMSupvOrgDelegate.findAll();

			request.setAttribute("M_SUPV_ORG", M_SUPV_ORG);
    			Collection M_REGION_TYP = com.cbrc.org.adapter.StrutsMRegionTypDelegate.findAll();

			request.setAttribute("M_REGION_TYP", M_REGION_TYP);
    			Collection M_ACTU_ORG = com.cbrc.org.adapter.StrutsMActuOrgDelegate.findAll();

			request.setAttribute("M_ACTU_ORG", M_ACTU_ORG);
    			Collection M_VIR_ORG_TYPE = com.cbrc.org.adapter.StrutsMVirOrgTypeDelegate.findAll();

			request.setAttribute("M_VIR_ORG_TYPE", M_VIR_ORG_TYPE);
    			Collection M_ORG_CODE = com.cbrc.org.adapter.StrutsMOrgCodeDelegate.findAll();

			request.setAttribute("M_ORG_CODE", M_ORG_CODE);
    			Collection M_VIRTU_ORG = com.cbrc.org.adapter.StrutsMVirtuOrgDelegate.findAll();

			request.setAttribute("M_VIRTU_ORG", M_VIRTU_ORG);
    			Collection M_ORG_CLS = com.cbrc.org.adapter.StrutsMOrgClDelegate.findAll();

			request.setAttribute("M_ORG_CLS", M_ORG_CLS);
    			Collection M_VIR_ORG_RLT = com.cbrc.org.adapter.StrutsMVirOrgRltDelegate.findAll();

			request.setAttribute("M_VIR_ORG_RLT", M_VIR_ORG_RLT);
    			Collection M_TO_REP_ORG = com.cbrc.org.adapter.StrutsMToRepOrgDelegate.findAll();

			request.setAttribute("M_TO_REP_ORG", M_TO_REP_ORG);
    			Collection M_FINA_ORG = com.cbrc.org.adapter.StrutsMFinaOrgDelegate.findAll();

			request.setAttribute("M_FINA_ORG", M_FINA_ORG);
    		} catch (Exception e) {
			getServlet().log("Can't find entity", e);
			return mapping.findForward("ejb-finder-exception");
		}

		return mapping.findForward("success");
	}
}
