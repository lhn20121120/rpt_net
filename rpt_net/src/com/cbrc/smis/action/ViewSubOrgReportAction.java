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
 * �����������б���Action����
 */
public final class ViewSubOrgReportAction extends Action {
	private FitechException log=new FitechException(ViewSubOrgReportAction.class);
	
	/**
	 * @param result ��ѯ���ر�־,����ɹ�����true,���򷵻�false
	 * @param ReportInInfoForm 
	 * @param request 
	 * @exception Exception ���쳣��׽���׳�
	 */
	public ActionForward execute(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response) throws IOException, ServletException {
		FitechMessages messages = new FitechMessages();
		MessageResources resources = getResources(request);

		// �Ƿ���Request
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
		int recordCount =0; //��¼����		
		int offset=0; //ƫ����
		int limit=0;  //ÿҳ��ʾ�ļ�¼����
		
		List resList=null;
		ApartPage aPage=new ApartPage();
		String strCurPage=request.getParameter("curPage");
		if(strCurPage!=null){
			if(!strCurPage.equals(""))
				aPage.setCurPage(new Integer(strCurPage).intValue());
		}else
			aPage.setCurPage(1);
			
		//����ƫ����
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
		//��ApartPage��������request��Χ��		
		aPage.setTerm(this.getTerm(reportInInfoForm));
		aPage.setCount(recordCount);		
		request.setAttribute(Config.APART_PAGE_OBJECT,aPage);
		
		if(messages.getMessages() != null && messages.getMessages().size() > 0)		
			request.setAttribute(Config.MESSAGES,messages);
	    
		//�򷵻�һ������reslist���ϵ�request����	    
		if(resList!=null && resList.size()>0){	    
			request.setAttribute(Config.RECORDS,resList);  
		    request.setAttribute("form",resList); //�Ƿ���ʾ���ɱ���ť
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