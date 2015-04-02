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
import com.cbrc.smis.form.ReportInForm;
import com.cbrc.smis.security.Operator;
import com.cbrc.smis.util.FitechException;
import com.cbrc.smis.util.FitechMessages;

/**
 * ����У���ѯAction
 * @author ����
 *
 */
public final class ViewSHJYReportInAction extends Action {
	private FitechException log=new FitechException(ViewSHJYReportInAction.class);
	     
	/**
	 * ����oracle�﷨(upper) ��Ҫ�޸� ���Ը� 2011-12-21
	 * @param result ��ѯ���ر�־,����ɹ�����true,���򷵻�false
	 * @param ReportInForm 
	 * @param request 
	 * @exception Exception ���쳣��׽���׳�
	 */
	public ActionForward execute(
      ActionMapping mapping,
      ActionForm form,
      HttpServletRequest request,
      HttpServletResponse response   
	)throws IOException, ServletException {
		
		FitechMessages messages = new FitechMessages();	   
		MessageResources resources = getResources(request);	   
		ReportInForm reportInForm = (ReportInForm)form;
	    RequestUtils.populate(reportInForm, request);
	    HttpSession session = request.getSession();
	    String yestoday = (String) session.getAttribute(Config.USER_LOGIN_DATE);
	    if(reportInForm.getDate() ==null || reportInForm.getDate().equals("")){
	    	reportInForm.setDate(yestoday.substring(0, 10));
	    	reportInForm.setYear(new Integer(yestoday.substring(0,4)));
	    	reportInForm.setTerm(new Integer(yestoday.substring(5,7)));
	    	reportInForm.setDay(new Integer(yestoday.substring(8,10)));
	    }else{
	    	reportInForm.setYear(new Integer(reportInForm.getDate().split("-")[0]));
	    	reportInForm.setTerm(new Integer(reportInForm.getDate().split("-")[1]));
	    	reportInForm.setDay(new Integer(reportInForm.getDate().split("-")[2]));
	    }
	    if(reportInForm.getYear()!=null && reportInForm.getYear().intValue()==0) reportInForm.setYear(null);
	    if(reportInForm.getTerm()!=null && reportInForm.getTerm().intValue()==0) reportInForm.setTerm(null);	
	    if(reportInForm.getDay()!=null && reportInForm.getDay().intValue()==0) reportInForm.setDay(null);
	    
		int recordCount = 0; // ��¼����
		int offset = 0; // ƫ����
		int limit = 0; // ÿҳ��ʾ�ļ�¼����
		
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
		
        Operator operator = null; 
        if(session.getAttribute(Config.OPERATOR_SESSION_ATTRIBUTE_NAME)!=null)
            operator = (Operator)session.getAttribute(Config.OPERATOR_SESSION_ATTRIBUTE_NAME);
        
        try {			
	   		//ȡ�ü�¼����
        	/**����oracle�﷨(upper) ��Ҫ�޸� ���Ը� 2011-12-21**/
	   		recordCount = StrutsReportInDelegate.getRecordCountOfmanual(reportInForm,operator);	  
	   		//��ʾ��ҳ��ļ�¼
	   		if(recordCount > 0)
	   			/**��ʹ��hibernate ���Ը� 2011-12-21**/
		   	    resList = StrutsReportInDelegate.selectOfManual(reportInForm,offset,limit,operator);
        }catch (Exception e){			
        	log.printStackTrace(e);
			messages.add(resources.getMessage("manualCheck.get.failed"));		
		}        	
                
		//��ApartPage��������request��Χ��
	 	aPage.setTerm(this.getTerm(reportInForm));
	 	aPage.setCount(recordCount);
	 	request.setAttribute(Config.APART_PAGE_OBJECT,aPage);
	 		 	 
	 	if(messages.getMessages() != null && messages.getMessages().size() > 0)		
	 		request.setAttribute(Config.MESSAGES,messages);
	 	 	 	
	 	request.setAttribute("SelectedFlag",reportInForm.getAllFlags());
	 	
	 	if(resList!=null && resList.size()>0){    	
	 		request.setAttribute("form",resList);     
	 	}     	 	
	 	return mapping.findForward("viewCheck");  
	}
   
	/**
	 * ���ò�ѯ���ݲ���
	 * 
	 * @param reportInForm
	 * @return String ��ѯ��URL
	 */
	public String getTerm(ReportInForm reportInForm){	   
		String term="";
		String date = String.valueOf(reportInForm.getDate());
		/**���뱨��������*/
		if(reportInForm.getChildRepId() != null && !reportInForm.getChildRepId().equals("")){
			term += (term.indexOf("?")>=0 ? "&" : "?");
			term += "childRepId=" + reportInForm.getChildRepId();
		}
		/**���뱨����������*/
		if(reportInForm.getRepName() != null && !reportInForm.getRepName().equals("")){
			term += (term.indexOf("?")>=0 ? "&" : "?");
			term += "repName=" + reportInForm.getRepName();
		}
		/**����ģ����������*/
		if(reportInForm.getFrOrFzType() != null){
			term += (term.indexOf("?")>=0 ? "&" : "?");
			term += "frOrFzType=" + reportInForm.getFrOrFzType();
		}
		/**���뱨��Ƶ������*/
		if(reportInForm.getRepFreqId() != null){
			term += (term.indexOf("?")>=0 ? "&" : "?");
			term += "repFreqId=" + reportInForm.getRepFreqId();
		}
		/**���뱨���������*/
		if(reportInForm.getYear() != null){
			term += (term.indexOf("?")>=0 ? "&" : "?");
			term += "year=" + reportInForm.getYear();
		}
		/**���뱨����������*/
		if(date!=null&&!date.equals("")){		
			term += (term.indexOf("?")>=0 ? "&" : "?");			
			term += "date="+date.toString();   		   
		}
				
		/**���뱨����������*/
		if(reportInForm.getTerm() != null){
			term += (term.indexOf("?")>=0 ? "&" : "?");
			term += "term=" + reportInForm.getTerm();
		}
		/**���뱨�ͻ�������*/
		if(reportInForm.getOrgId() != null){
			term += (term.indexOf("?")>=0 ? "&" : "?");
			term += "orgId=" + reportInForm.getOrgId();
		}
		String orgName = reportInForm.getOrgName();
		if(orgName!=null&&!orgName.equals("")){		
			term += (term.indexOf("?")>=0 ? "&" : "?");			
			term += "orgName="+orgName;    
		}
		if(term.indexOf("?")>=0)		
			term = term.substring(term.indexOf("?")+1);
		
		return term;   
	}
}