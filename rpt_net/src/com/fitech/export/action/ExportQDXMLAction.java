package com.fitech.export.action;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.RequestUtils;

import com.cbrc.smis.common.Config;
import com.cbrc.smis.other.Aditing;
import com.cbrc.smis.security.Operator;
import com.cbrc.smis.util.FitechMessages;
import com.fitech.export.service.ExportQDReportDelegate;
import com.fitech.gznx.form.AFReportForm;
import com.fitech.gznx.service.AFReportProductDelegate;


public class ExportQDXMLAction extends Action {
	

	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException
	{
		AFReportForm afReportForm=(AFReportForm) form;
		RequestUtils.populate(afReportForm,request);
		HttpSession session = request.getSession();
		Operator operator = null; 
		if(session.getAttribute(Config.OPERATOR_SESSION_ATTRIBUTE_NAME)!=null)
			operator = (Operator)session.getAttribute(Config.OPERATOR_SESSION_ATTRIBUTE_NAME);         
		
		List<Aditing> qdLlist = ExportQDReportDelegate.selectQDReportList(afReportForm,operator,0,1000,"3",1);
		request.setAttribute("Records", qdLlist);
		FitechMessages messages = new FitechMessages();

		
		return mapping.findForward("list");
	
		
	}

}
