package com.cbrc.smis.action;

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
import org.apache.struts.util.MessageResources;
import org.apache.struts.util.RequestUtils;

import com.cbrc.smis.adapter.StrutsReportInDelegate;
import com.cbrc.smis.common.ApartPage;
import com.cbrc.smis.common.Config;
import com.cbrc.smis.form.ReportInInfoForm;
import com.cbrc.smis.security.Operator;
import com.cbrc.smis.util.FitechException;
import com.cbrc.smis.util.FitechMessages;



/**
 * ���ڲ�ѯ����
 * @author masclnj
 *
 */
public final class SearchTemplateAction extends Action {
	
	private static FitechException log=new FitechException(SearchTemplateAction.class);
	   /**
	    * @param result ��ѯ���ر�־,����ɹ�����true,���򷵻�false
	    * @param ReportInInfoForm 
	    * @param request 
	    * @exception Exception ���쳣��׽���׳�
	    */
	   public ActionForward execute(
	      ActionMapping mapping,
	      ActionForm form,
	      HttpServletRequest request,
	      HttpServletResponse response
	   )
	      throws IOException, ServletException {
		   
		   FitechMessages messages = new FitechMessages();
		   MessageResources resources = getResources(request);		   
		  
		   
		   // �Ƿ���Request
		   ReportInInfoForm reportInInfoForm = (ReportInInfoForm)form ;
		     
		   RequestUtils.populate(reportInInfoForm, request);
		   if(reportInInfoForm.getYear()!=null && reportInInfoForm.getYear().intValue()==0) 
			   reportInInfoForm.setYear(null);
		   if(reportInInfoForm.getTerm()!=null && reportInInfoForm.getTerm().intValue()==0)
			   reportInInfoForm.setTerm(null);
		   
		   int recordCount =0; //��¼����
		   int offset=0; //ƫ����
		   int limit=0;  //ÿҳ��ʾ�ļ�¼����
		   
		   //List����ĳ�ʼ��
		   List resList=null;
		   
		   ApartPage aPage=new ApartPage();
		   	String strCurPage=request.getParameter("curPage");
			if(strCurPage!=null){
			    if(!strCurPage.equals(""))
			      aPage.setCurPage(new Integer(strCurPage).intValue());
			}
			else
				aPage.setCurPage(1);
			//����ƫ����
			offset=(aPage.getCurPage()-1)*Config.PER_PAGE_ROWS; 
			limit = Config.PER_PAGE_ROWS;	
			
             /***
             * ȡ�õ�ǰ�û���Ȩ����Ϣ
             * @author Ҧ��
             */
            HttpSession session = request.getSession();
            Operator operator = null; 
            if(session.getAttribute(Config.OPERATOR_SESSION_ATTRIBUTE_NAME)!=null)
                operator = (Operator)session.getAttribute(Config.OPERATOR_SESSION_ATTRIBUTE_NAME);          
           
            
            try{
//            	ȡ�ü�¼����
		   		recordCount = StrutsReportInDelegate.getRecordCountOfmanual2(reportInInfoForm,operator);
		   		if(recordCount > 0)
			   	    resList = StrutsReportInDelegate.selectOfManual2(reportInInfoForm,offset,limit,operator);		   		
		   		}
            catch(Exception ex){
           	 	log.printStackTrace(ex);
                messages.add(resources.getMessage("log.select.fail"));  
            }
            
            
//          ��ApartPage��������request��Χ��
		 	aPage.setTerm(this.getTerm(reportInInfoForm));
		 	
		 	aPage.setCount(recordCount);
		 	request.setAttribute(Config.APART_PAGE_OBJECT,aPage);
		 	
		 	 if(messages.getMessages() != null && messages.getMessages().size() > 0)
			   	  request.setAttribute(Config.MESSAGES,messages);
		 	 
		 	 
//		 	���StrutsMReportInDelegate���з��ص�reslist����Ϊ�ղ��Ҷ���Ĵ�С����0��
		     //�򷵻�һ������reslist���ϵ�request����
		     if(resList!=null && resList.size()>0){
		    	 request.setAttribute(Config.RECORDS,resList);
		     }
		     
		     request.setAttribute("SelectedFlag",reportInInfoForm.getAllFlags());	     
		     request.setAttribute("form",reportInInfoForm);
	     
	     return mapping.findForward("viewTemSearch");	     
	  }
	      // First see if there is a vo as request attribute.
	  public String getTerm(ReportInInfoForm reportInInfoForm){
		   String term="";
		   String repName = reportInInfoForm.getRepName();		   
		   String year = String.valueOf(reportInInfoForm.getYear());
		   String terms = String.valueOf(reportInInfoForm.getTerm());
		   
		   if(repName!=null&&!repName.equals(""))
		   {
			   term += (term.indexOf("?")>=0 ? "&" : "?");
			   term += "repName="+repName.toString();   
		   }
		   
		   if(year!=null&&!year.equals(""))
		   {
			   term += (term.indexOf("?")>=0 ? "&" : "?");
			   term += "year="+year.toString();   
		   }
		   
		   if(terms!=null&&!terms.equals(""))
		   {
			   term += (term.indexOf("?")>=0 ? "&" : "?");
			   term += "term="+terms.toString();   
		   }
		   
		   if(term.indexOf("?")>=0)
			   term = term.substring(term.indexOf("?")+1);
		      // System.out.println("term"+term);
		   return term;
	   }	
}