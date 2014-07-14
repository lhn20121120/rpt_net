/*
 * Created on 2006-5-25
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.fitech.net.obtain.excel.action;

import java.io.IOException;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.fitech.net.common.CommMethod;
import com.fitech.net.obtain.excel.TemplateObj;
import com.fitech.net.obtain.excel.dao.ObtainManager;

/**
 * @author Administrator
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class templateDataBuildPre extends Action{
	/**
	    * execute action.
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
			
			String repID = request.getParameter("repID");
			String versionID = request.getParameter("versisonID");
			
			ObtainManager om = new ObtainManager();
			TemplateObj to = om.findTemplate(repID, versionID);
			HashMap dtoMap = to.getDataSourceMap();
			request.setAttribute("to", to);
			request.setAttribute("dsoMap", dtoMap);
			
			//将通用的页面元素写入request
			CommMethod.buidPageInfo(request);
			return mapping.findForward("success");
		}
}
