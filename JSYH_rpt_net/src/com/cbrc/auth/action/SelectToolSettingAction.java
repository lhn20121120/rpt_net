package com.cbrc.auth.action;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.cbrc.auth.form.ToolSettingForm;

/**
 * Finds a toolSetting
 *
 * @author <a href="http://boss.bekk.no/boss/middlegen/">Middlegen</a>
 *
 * @struts.action
 *    path="/struts/selectToolSetting"
 *    name="toolSettingForm"
 *    scope="session"
 *    validate="false"
 *
 * @struts.action-forward
 *    name="none"
 *    path="/struts/findToolSetting.do"
 *    redirect="false"
 *
 * @struts.action-forward
 *    name="one"
 *    path="/struts/viewToolSetting.jsp"
 *    redirect="false"
 *
 * @struts.action-forward
 *    name="many"
 *    path="/struts/listToolSetting.jsp"
 *    redirect="false"
 *

 */
public final class SelectToolSettingAction extends Action {

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

		ToolSettingForm toolSettingForm = (ToolSettingForm)form;


		try {
  		List toolSettings = null;//com.cbrc.auth.adapter.StrutsToolSettingDelegate.select(toolSettingForm);

			if( toolSettings.size() == 0 ) {
				return mapping.findForward("none");
			} else if( toolSettings.size() == 1 ) {
				// Found one. Display it.
				ToolSettingForm toolSettingFormTemp = (ToolSettingForm) toolSettings.get(0);
				request.setAttribute("toolSetting", toolSettingFormTemp);

				return mapping.findForward("one");
			} else {
				// Found many. Display all.
				request.setAttribute("toolSettings", toolSettings);
				return mapping.findForward("many");
			}
		} catch (Exception e) {
			getServlet().log("Create error", e);
			return mapping.findForward("ejb-finder-exception");
		}
	}
}
