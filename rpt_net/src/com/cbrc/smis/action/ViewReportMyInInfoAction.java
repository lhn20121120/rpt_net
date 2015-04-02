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

import com.cbrc.smis.adapter.StrutsReportInInfoDelegate;
import com.cbrc.smis.common.ApartPage;
import com.cbrc.smis.common.Config;
import com.cbrc.smis.form.ReportInInfoForm;
import com.cbrc.smis.security.Operator;
import com.cbrc.smis.util.FitechException;
import com.cbrc.smis.util.FitechMessages;
/**
 * @author 
 *
 */
public final class ViewReportMyInInfoAction extends Action {
	private FitechException log=new FitechException(ViewReportInInfoAction.class);
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
			
            
            HttpSession session = request.getSession();
            Operator operator = null; 
            if(session.getAttribute(Config.OPERATOR_SESSION_ATTRIBUTE_NAME)!=null)
                operator = (Operator)session.getAttribute(Config.OPERATOR_SESSION_ATTRIBUTE_NAME);
            
            try 
			{
		   		//ȡ�ü�¼����
		   		recordCount = StrutsReportInInfoDelegate.getMyOrgReportInCount(reportInInfoForm,operator);
		   		
		   		//��ʾ��ҳ��ļ�¼
		   		if(recordCount > 0)
			   	    resList = StrutsReportInInfoDelegate.getMyOrgReportInList(reportInInfoForm,offset,limit,operator); 
		   		}
			catch (Exception e) 
			{
				log.printStackTrace(e);
				messages.add(resources.getMessage("manualCheck.get.failed"));		
			}
			//��ApartPage��������request��Χ��
		 	aPage.setTerm(this.getTerm(reportInInfoForm));
		 	aPage.setCount(recordCount);
		 	request.setAttribute(Config.APART_PAGE_OBJECT,aPage);
		 	
		 	 if(messages.getMessages() != null && messages.getMessages().size() > 0)
			   	  request.setAttribute(Config.MESSAGES,messages);
		 	 
	     //���StrutsMReportInDelegate���з��ص�reslist����Ϊ�ղ��Ҷ���Ĵ�С����0��
	     //�򷵻�һ������reslist���ϵ�request����
	     if(resList!=null && resList.size()>0){
	    	 request.setAttribute(Config.RECORDS,resList);
	     }
	     
	     request.setAttribute("SelectedFlag",reportInInfoForm.getAllFlags());
	     
	     //���ص�ҳ��view     
	     return mapping.findForward("viewSearch");
	  }
	      // First see if there is a vo as request attribute.
	   public String getTerm(ReportInInfoForm reportInInfoForm)
	   {
		   String term="";
		   String repName = reportInInfoForm.getRepName();
		   String allFlag = reportInInfoForm.getAllFlags();
		   String startDate = reportInInfoForm.getStartDate();
		   String endDate = reportInInfoForm.getEndDate();  
		   String orgClsId=reportInInfoForm.getOrgClsId();
		   Short checkFlag=reportInInfoForm.getCheckFlag();
		   String childRepId=reportInInfoForm.getChildRepId();
		   String versionId=reportInInfoForm.getVersionId();
		   String orgName=reportInInfoForm.getOrgName();
		   //String reportDate=reportInInfoForm.getReportDate().toString();
		   
		   /*if(reportDate!=null&&!reportDate.equals(""))
		   {
			   term += (term.indexOf("?")>=0 ? "&" : "?");
			   term += "reportDate="+reportDate.toString();   
		   }*/
		   if(reportInInfoForm.getYear() != null&&!reportInInfoForm.getYear().equals("")){		
				term += (term.indexOf("?")>=0 ? "&" : "?");			
				term += "year="+reportInInfoForm.getYear().toString();   		  
			}  
			if(reportInInfoForm.getTerm() != null&&!reportInInfoForm.getTerm().equals("")){		
				term += (term.indexOf("?")>=0 ? "&" : "?");			
				term += "term="+reportInInfoForm.getTerm().toString();   		  
			}   
		   
		   if(childRepId!=null&&!childRepId.equals(""))
		   {
			   term += (term.indexOf("?")>=0 ? "&" : "?");
			   term += "childRepId="+childRepId.toString();   
		   }
		   
		   if(versionId!=null&&!versionId.equals(""))
		   {
			   term += (term.indexOf("?")>=0 ? "&" : "?");
			   term += "versionId="+versionId.toString();   
		   }
		   
		   if(orgName!=null&&!orgName.equals(""))
		   {
			   term += (term.indexOf("?")>=0 ? "&" : "?");
			   term += "orgName="+orgName.toString();   
		   }
		   
		   if(checkFlag!=null&&!checkFlag.equals(""))
		   {
			   term += (term.indexOf("?")>=0 ? "&" : "?");
			   term += "checkFlag="+checkFlag.toString();   
		   }
		   
		   if(repName!=null&&!repName.equals(""))
		   {
			   term += (term.indexOf("?")>=0 ? "&" : "?");
			   term += "repName="+repName.toString();   
		   }
		   if(allFlag!=null && !allFlag.equals(""))
		   {
			   term += (term.indexOf("?")>=0 ? "&" : "?");
			   term += "allFlags="+allFlag.toString();   
		   }
		   if(startDate!=null && !startDate.equals(""))
		   {
			   term += (term.indexOf("?")>=0 ? "&" : "?");
			   term += "startDate="+startDate.toString();   
		   }
		   
		   if(endDate!=null && !endDate.equals(""))
		   {
			   term += (term.indexOf("?")>=0 ? "&" : "?");
			   term += "endDate="+endDate.toString();   
		   }
		   if(orgClsId!=null && orgClsId.equals(""))
		   {
			   term += (term.indexOf("?")>=0 ? "&" : "?");
			   term += "orgClsId="+orgClsId.toString();   
		   }
		   
		   if(term.indexOf("?")>=0)
			   term = term.substring(term.indexOf("?")+1);		     
		   return term;
	   }
	   }