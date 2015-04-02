package com.fitech.gznx.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
/**
 * 银监会条线
 * @author 姜明青
 *
 */
public class PortalBSPTAction extends Action {

	@Override
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String userName = request.getParameter("username");
		String outerPortalReturnPath = request.getParameter("outerPortalReturnPath");
		if(outerPortalReturnPath!=null && !outerPortalReturnPath.trim().equals(""))
			request.getSession().setAttribute("portal_url", outerPortalReturnPath);
		else
			request.getSession().setAttribute("portal_url", "/portal");
		return new ActionForward("/userLogin.do?name="+userName);
	}
}
