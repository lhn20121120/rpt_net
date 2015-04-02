package com.cbrc.smis.action;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.MessageResources;
import org.apache.struts.util.RequestUtils;

import com.cbrc.smis.adapter.StrutsReportInInfoDelegate;
import com.cbrc.smis.common.ApartPage;
import com.cbrc.smis.common.Config;
import com.cbrc.smis.form.ReportInInfoForm;
import com.cbrc.smis.security.Operator;
import com.cbrc.smis.util.FitechException;
import com.cbrc.smis.util.FitechMessages;
/**
 * @author jcm
 * 查找所有子行报表Action方法
 */
public final class ViewSubOrgReportAction extends Action {
	private FitechException log=new FitechException(ViewSubOrgReportAction.class);
	
	/**
	 * @param result 查询返回标志,如果成功返回true,否则返回false
	 * @param ReportInInfoForm 
	 * @param request 
	 * @exception Exception 有异常捕捉并抛出
	 */
	public ActionForward execute(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response) throws IOException, ServletException {
		FitechMessages messages = new FitechMessages();
		MessageResources resources = getResources(request);

		// 是否有Request
		ReportInInfoForm reportInInfoForm = (ReportInInfoForm)form ;
		RequestUtils.populate(reportInInfoForm, request);
		
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.MONTH,-1);
		if(reportInInfoForm.getYear() == null || reportInInfoForm.getYear().intValue() == 0) 
			reportInInfoForm.setYear(new Integer(calendar.get(Calendar.YEAR)));
		if(reportInInfoForm.getTerm() == null || reportInInfoForm.getTerm().intValue() == 0)
			reportInInfoForm.setTerm(new Integer(calendar.get(Calendar.MONTH)+1));
		if(reportInInfoForm.getTimes() == null || reportInInfoForm.getTimes().intValue() == 0){			
			reportInInfoForm.setTimes(new Integer(calendar.get(Calendar.MONTH)+1));
		}
		int recordCount =0; //记录总数		
		int offset=0; //偏移量
		int limit=0;  //每页显示的记录条数
		
		List resList=null;
		ApartPage aPage=new ApartPage();
		String strCurPage=request.getParameter("curPage");
		if(strCurPage!=null){
			if(!strCurPage.equals(""))
				aPage.setCurPage(new Integer(strCurPage).intValue());
		}else
			aPage.setCurPage(1);
			
		//计算偏移量
		offset=(aPage.getCurPage()-1)*Config.PER_PAGE_ROWS; 
		limit = Config.PER_PAGE_ROWS;	
		
		HttpSession session = request.getSession();
		Operator operator = null; 
		if(session.getAttribute(Config.OPERATOR_SESSION_ATTRIBUTE_NAME)!=null)
			operator = (Operator)session.getAttribute(Config.OPERATOR_SESSION_ATTRIBUTE_NAME);
            
		try{		
			if(reportInInfoForm.getOrgId() != null && reportInInfoForm.getOrgId().equals("0")) reportInInfoForm.setOrgId("");

			recordCount=StrutsReportInInfoDelegate.getSubOrgReportRecordCount(reportInInfoForm, operator);
			if(recordCount>0){
				resList=StrutsReportInInfoDelegate.getSubOrgReportRecord(reportInInfoForm, offset, limit, operator);
			}
		}catch (Exception e){		
			log.printStackTrace(e);			
			messages.add(resources.getMessage("search.subOrgReport.failed"));					
		}
		//把ApartPage对象存放在request范围内		
		aPage.setTerm(this.getTerm(reportInInfoForm));
		aPage.setCount(recordCount);		
		request.setAttribute(Config.APART_PAGE_OBJECT,aPage);
		
		if(messages.getMessages() != null && messages.getMessages().size() > 0)		
			request.setAttribute(Config.MESSAGES,messages);
	    
		//则返回一个包含reslist集合的request对象	    
		if(resList!=null && resList.size()>0){	    
			request.setAttribute(Config.RECORDS,resList);  
		    request.setAttribute("form",resList); //是否显示生成报表按钮
		}	     
	        
		return mapping.findForward("view");	  
	}
	
	public String getTerm(ReportInInfoForm reportInInfoForm){	
		String term="";		
		String repName = reportInInfoForm.getRepName();				
		String orgId = reportInInfoForm.getOrgId();		
		String month = reportInInfoForm.getTerm() != null ? reportInInfoForm.getTerm().toString() : "";
		String times = reportInInfoForm.getTimes() != null ? reportInInfoForm.getTimes().toString() : "";
		String year = reportInInfoForm.getYear() != null ? reportInInfoForm.getYear().toString() : "";
		
		if(orgId != null && !orgId.equals("")){		
			term += (term.indexOf("?") >= 0 ? "&" : "?");			
			term += "orgId=" + orgId.toString();		   
		}
		if(repName != null && !repName.equals("")){		
			term += (term.indexOf("?") >= 0 ? "&" : "?");			
			term += "repName=" + repName.toString();   		
		}
		if(month != null && !month.equals("")){
			term += (term.indexOf("?") >= 0 ? "&" : "?");
			term += "term=" + month.toString();
		}
		if(times != null && !times.equals("")){
			term += (term.indexOf("?") >= 0 ? "&" : "?");
			term += "times=" + times.toString();
		}
		if(year != null && !year.equals("")){
			term += (term.indexOf("?") >= 0 ? "&" : "?");
			term += "year=" + year.toString();
		}
		if(term.indexOf("?")>=0)		
			term = term.substring(term.indexOf("?")+1);
		
		return term;	   
	}	
}