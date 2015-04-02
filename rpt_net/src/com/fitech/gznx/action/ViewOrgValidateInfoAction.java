package com.fitech.gznx.action;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.MessageResources;

import com.cbrc.smis.form.ReportInForm;
import com.cbrc.smis.util.FitechException;
import com.cbrc.smis.util.FitechMessages;
import com.fitech.gznx.po.OrgValiInfo;
import com.fitech.gznx.service.OrgValiInfoDelegate;

public class ViewOrgValidateInfoAction extends Action {
	private FitechException log=new FitechException(ViewOrgValidateInfoAction.class);
	
	/**
	 * Performs action.
	 * @param mapping Action mapping.
	 * @param form Action form.
	 * @param request HTTP request.
	 * @param response HTTP response.
	 * @exception IOException if an input/output error occurs
	 * @exception ServletException if a servlet exception occurs
	 */
	public ActionForward execute(ActionMapping mapping, 
			ActionForm form,
			HttpServletRequest request, 
			HttpServletResponse response)
			throws IOException, ServletException {
		
		Locale locale = getLocale(request);
		MessageResources resources = getResources(request);
		FitechMessages messages = new FitechMessages();
		String repInIds = null;
		List<OrgValiInfo> list=new ArrayList<OrgValiInfo>();
		int reportStyle=0;
		
		try{ 
			repInIds = (String)request.getParameter("repInIds"); 
			list=OrgValiInfoDelegate.getValiResults(repInIds);	 
			request.setAttribute("valiInfoList", list); 
		}catch(Exception e){
			reportStyle=0;
			log.printStackTrace(e);
		}
			return mapping.findForward("success");
	}
}
