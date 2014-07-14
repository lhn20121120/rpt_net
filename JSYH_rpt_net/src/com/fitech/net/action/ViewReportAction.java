package com.fitech.net.action;

import java.io.IOException;
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

import com.cbrc.smis.adapter.StrutsReportInDelegate;
import com.cbrc.smis.common.ApartPage;
import com.cbrc.smis.common.Config;
import com.cbrc.smis.form.ReportInForm;
import com.cbrc.smis.security.Operator;
import com.cbrc.smis.util.FitechException;
import com.cbrc.smis.util.FitechMessages;

public class ViewReportAction extends Action {
    private static FitechException log = new FitechException(ViewReportAction.class);

    /** 
     * Method execute
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return ActionForward
     */
    public ActionForward execute(ActionMapping mapping,ActionForm form,HttpServletRequest request
    		,HttpServletResponse response)  throws IOException, ServletException{
        
        MessageResources resources = getResources(request);
        FitechMessages messages = new FitechMessages();
        ReportInForm reportInForm = (ReportInForm) form;
        RequestUtils.populate(reportInForm, request);
        
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, -1);
        if(reportInForm.getYear() == null || reportInForm.getYear().equals(""))			   
			reportInForm.setYear(new Integer(calendar.get(Calendar.YEAR)));		   
		if(reportInForm.getTerm() == null || reportInForm.getTerm().equals(""))	
			reportInForm.setTerm(new Integer(calendar.get(Calendar.MONTH)+1));	    
	    
		int recordCount = 0; // ��¼����
		int offset = 0; // ƫ����
		int limit = 0; // ÿҳ��ʾ�ļ�¼����
		
		List resList = null;
		ApartPage aPage = new ApartPage();	   	
		String strCurPage = request.getParameter("curPage");
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
        
        try{
        	//ȡ�ü�¼����
	   		recordCount = StrutsReportInDelegate.getDataRecordsPageCount(reportInForm,operator);	  
	   		//��ʾ��ҳ��ļ�¼
	   		if(recordCount > 0)
		   	    resList = StrutsReportInDelegate.getDataRecordsPageUse(reportInForm,offset,limit,operator);
         
        }catch(Exception ex){
        	log.printStackTrace(ex);
        	messages.add(resources.getMessage("select.dataReport.failed"));  
        }
        
        //��ApartPage��������request��Χ��
	 	aPage.setTerm(this.getTerm(reportInForm));
	 	aPage.setCount(recordCount);
	 	request.setAttribute(Config.APART_PAGE_OBJECT,aPage);	 	
         
        if(messages.getMessages() != null && messages.getMessages().size() > 0)
        	request.setAttribute(Config.MESSAGES,messages);
        if(resList != null && resList.size() > 0)
        	request.setAttribute("form",resList);
                
        return mapping.findForward("view");
    }
    
    /**
	 * ���ò�ѯ���ݲ���
	 * 
	 * @param reportInForm
	 * @return String ��ѯ��URL
	 */
	public String getTerm(ReportInForm reportInForm){	   
		String term="";
		
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
		if(reportInForm.getTerm() != null){
			term += (term.indexOf("?")>=0 ? "&" : "?");
			term += "term=" + reportInForm.getTerm();
		}		
		if(term.indexOf("?")>=0)		
			term = term.substring(term.indexOf("?")+1);
		
		return term;   
	}
}
