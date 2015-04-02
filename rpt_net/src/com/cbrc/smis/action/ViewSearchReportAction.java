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
 * ��Ϣ��ѯ�е�'����鿴'
 * ��ԭ����'����鿴'��'�鿴����'��'�鿴��֧'���ܺϲ�
 * 
 * @author jcm
 * @2008-01-14
 */
public final class ViewSearchReportAction extends Action {
	
	/**�쳣������*/
	private FitechException log=new FitechException(ViewSearchReportAction.class);

	public ActionForward execute(ActionMapping mapping,ActionForm form,HttpServletRequest request
			,HttpServletResponse response)throws IOException, ServletException {
		
		FitechMessages messages = new FitechMessages();		
		MessageResources resources = getResources(request);		  
		ReportInInfoForm reportInInfoForm = (ReportInInfoForm)form ;
		RequestUtils.populate(reportInInfoForm, request);
		
		if(reportInInfoForm.getYear()!=null && reportInInfoForm.getYear().intValue()==0)
			reportInInfoForm.setYear(null);		   
		if(reportInInfoForm.getTerm()!=null && reportInInfoForm.getTerm().intValue()==0)		
			reportInInfoForm.setTerm(null);

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
		
		/**��ǰ�û���Ϣ*/
		HttpSession session = request.getSession();        
		Operator operator = null;         
		if(session.getAttribute(Config.OPERATOR_SESSION_ATTRIBUTE_NAME)!=null)        
			operator = (Operator)session.getAttribute(Config.OPERATOR_SESSION_ATTRIBUTE_NAME);
                        
		try {		
			//ȡ�ü�¼����		   	
			recordCount = StrutsReportInInfoDelegate.searchReportCount(reportInInfoForm,operator);
			//��ʾ��ҳ��ļ�¼		   	
			if(recordCount > 0)			
				resList = StrutsReportInInfoDelegate.searchReportRecord(reportInInfoForm,offset,limit,operator); 
		}catch (Exception e){				
			log.printStackTrace(e);			
			messages.add(resources.getMessage("manualCheck.get.failed"));		
		}
			
		request.setAttribute("repInIds",reportInInfoForm.getRepInIds());
		//��ApartPage��������request��Χ��		
		aPage.setTerm(this.getTerm(reportInInfoForm));		
		aPage.setCount(recordCount);		
		request.setAttribute(Config.APART_PAGE_OBJECT,aPage);
		
		if(messages.getMessages() != null && messages.getMessages().size() > 0)			
			request.setAttribute(Config.MESSAGES,messages);
		
		if(resList!=null && resList.size()>0){	    
			request.setAttribute(Config.RECORDS,resList);	     
		}
			    
		return mapping.findForward("viewSearch");	  
	}
	  
	/**
	 * ���ò�ѯ���ݲ���
	 * 
	 * @param reportInInfoForm
	 * @return String ��ѯ��URL
	 */
	public String getTerm(ReportInInfoForm reportInInfoForm){
		String term="";
		
		/**���뱨��������*/
		if(reportInInfoForm.getChildRepId() != null && !reportInInfoForm.getChildRepId().equals("")){
			term += (term.indexOf("?")>=0 ? "&" : "?");
			term += "childRepId=" + reportInInfoForm.getChildRepId();
		}
		/**���뱨����������*/
		if(reportInInfoForm.getRepName() != null && !reportInInfoForm.getRepName().equals("")){
			term += (term.indexOf("?")>=0 ? "&" : "?");
			term += "repName=" + reportInInfoForm.getRepName();
		}
		/**����ģ����������*/
		if(reportInInfoForm.getFrOrFzType() != null){
			term += (term.indexOf("?")>=0 ? "&" : "?");
			term += "frOrFzType=" + reportInInfoForm.getFrOrFzType();
		}
		/**���뱨��Ƶ������*/
		if(reportInInfoForm.getRepFreqId() != null){
			term += (term.indexOf("?")>=0 ? "&" : "?");
			term += "repFreqId=" + reportInInfoForm.getRepFreqId();
		}
		/**���뱨���������*/
		if(reportInInfoForm.getYear() != null){
			term += (term.indexOf("?")>=0 ? "&" : "?");
			term += "year=" + reportInInfoForm.getYear();
		}
		/**���뱨����������*/
		if(reportInInfoForm.getTerm() != null){
			term += (term.indexOf("?")>=0 ? "&" : "?");
			term += "term=" + reportInInfoForm.getTerm();
		}
		/**���뱨�ͻ�������*/
		if(reportInInfoForm.getOrgId() != null){
			term += (term.indexOf("?")>=0 ? "&" : "?");
			term += "orgId=" + reportInInfoForm.getOrgId();
		}
		/**���뱨��״̬����*/
		if(reportInInfoForm.getCheckFlag() != null){
			term += (term.indexOf("?")>=0 ? "&" : "?");
			term += "checkFlag=" + reportInInfoForm.getCheckFlag();
		}		
		if(term.indexOf("?")>=0)		
			term = term.substring(term.indexOf("?")+1);
		    
		return term;  
	}	   
}