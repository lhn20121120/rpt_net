package com.fitech.net.action;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.RequestUtils;

import com.cbrc.smis.util.FitechException;
import com.fitech.net.adapter.StrutsFoundETLDelegate;
import com.fitech.net.form.ETLReportForm;

public class ETLReportFoundAction extends Action {
	/*
	 *  查询txt类型的ETL模版
	 */
	private FitechException log=new FitechException(ETLReportFoundAction.class);
	public ActionForward execute(
		      ActionMapping mapping,
		      ActionForm form,
		      HttpServletRequest request,
		      HttpServletResponse response
			)throws IOException, ServletException {
		
		List list=new ArrayList();
		try{
			ETLReportForm etlForm = new ETLReportForm();
			RequestUtils.populate(etlForm, request); 
			
			String year=etlForm.getYear();
			String month=etlForm.getMonth();
			System.out.println(year+"___"+month);
			System.out.println(etlForm.getOrgName()+"~~~~~~~");
			
			
			StrutsFoundETLDelegate del=new StrutsFoundETLDelegate();
			list=del.found(etlForm);
			
			request.setAttribute("result",list);
			if(etlForm.getPage()!=null)
				request.setAttribute("page",new Integer(etlForm.getPage()));
			else
				request.setAttribute("page",new Integer(1));
			request.setAttribute("form",etlForm);
			request.setAttribute("maxPage",del.allPage);
			System.out.println(list.size());
		}
		catch(Exception e){
			log.printStackTrace(e);
		}
		
		return mapping.findForward("found");
	}
}
