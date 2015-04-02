package com.cbrc.smis.action;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.cbrc.smis.adapter.StrutsReportInRelaseETLDelegate;

public class RelationExcelAction  extends Action{
	public ActionForward execute(ActionMapping mapping,ActionForm form,HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {
		try{
			Integer id=(Integer)request.getAttribute("");
	//		String fileName=(new StrutsReportInRelaseETLDelegate()).Analyse(id,request.getSession().getId());
	//		request.setAttribute("fileName",fileName);
		}
		catch(Exception e){
			
		}
		return mapping.findForward("");
	}

}
