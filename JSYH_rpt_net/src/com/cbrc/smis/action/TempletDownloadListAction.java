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

import com.cbrc.smis.adapter.StrutsMRepRangeDelegate;
import com.cbrc.smis.common.ApartPage;
import com.cbrc.smis.common.Config;
import com.cbrc.smis.form.ReportInForm;
import com.cbrc.smis.other.Aditing;
import com.cbrc.smis.security.Operator;
import com.cbrc.smis.util.FitechException;
import com.cbrc.smis.util.FitechMessages;

/** 
 * ��ʾtemplate���б�
 */
public class TempletDownloadListAction extends Action {
    private static FitechException log = new FitechException(ViewMChildReportAction.class);

    /** 
     * Method execute
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return ActionForward
     */
    public ActionForward execute(
        ActionMapping mapping,
        ActionForm form,
        HttpServletRequest request,
        HttpServletResponse response)  throws IOException, ServletException{
        
    	
        MessageResources resources = getResources(request);
        FitechMessages messages = new FitechMessages();
        //ȡ��request��Χ�ڵ�����������������reportForm��
        ReportInForm reportInForm = (ReportInForm)form ;
        RequestUtils.populate(reportInForm, request);
                 
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
		}else
			aPage.setCurPage(1);
			
		//����ƫ����   
		offset=(aPage.getCurPage()-1)*Config.PER_PAGE_ROWS; 
		limit = Config.PER_PAGE_ROWS;	
			
        /**
         * ȡ�õ�ǰ�û���Ȩ����Ϣ
         */   
		HttpSession session = request.getSession();
		Operator operator = null; 
		if(session.getAttribute(Config.OPERATOR_SESSION_ATTRIBUTE_NAME)!=null)  
			operator = (Operator)session.getAttribute(Config.OPERATOR_SESSION_ATTRIBUTE_NAME);          
 
		Calendar calendar = Calendar.getInstance();
		//calendar.add(Calendar.MONTH,-1); yql�޸�
		String yestoday = (String) session.getAttribute(Config.USER_LOGIN_DATE);

		 if(reportInForm.getDate() ==null || reportInForm.getDate().equals("")){
		    	reportInForm.setDate(yestoday.substring(0, 7));
		    	reportInForm.setYear(new Integer(yestoday.substring(0,4)));
		    	reportInForm.setSetDate(yestoday.substring(5,7));
		    }else{
		    	reportInForm.setYear(new Integer(reportInForm.getDate().split("-")[0]));
		    	reportInForm.setSetDate(reportInForm.getDate().split("-")[1]);
		    }
		if(reportInForm.getOrgId() == null) reportInForm.setOrgId(operator.getOrgId());
		
		try{  	 			
		//	List list = StrutsMRepRangeDelegate.selectYBSJList(reportInForm,operator);
			List list = StrutsMRepRangeDelegate.selectYBSJListNew(reportInForm,operator);
			if(list != null && list.size() > 0){      	
				//ȡ�ü�¼����       		 
				recordCount = list.size();      	 
			}
			if(recordCount > 0){
				resList = new ArrayList();
				int arraySize = limit + offset;
				if(list.size() < (limit + offset)) arraySize = list.size();
				for(int i=offset;i<arraySize;i++){	
					Aditing aditing = (Aditing)list.get(i);	
					resList.add(aditing);   
				}
			}
		}catch(Exception ex){
			log.printStackTrace(ex);
			messages.add(resources.getMessage("select.dataReport.failed"));		
		}
  
         //��ApartPage��������request��Χ��
		aPage.setTerm(this.getTerm(reportInForm));		 	
		aPage.setCount(recordCount);
		request.setAttribute(Config.APART_PAGE_OBJECT,aPage);
		request.setAttribute("orgId",reportInForm.getOrgId());
		request.setAttribute("reportName", reportInForm.getRepName());

		if(messages.getMessages() != null && messages.getMessages().size() > 0)		
			request.setAttribute(Config.MESSAGES,messages);
		  
		if(resList!=null && resList.size()>0){
			request.setAttribute(Config.RECORDS,resList);   
		}
		return mapping.findForward("view");	 
    }
    public String getTerm(ReportInForm reportInForm){
    	
		String term="";  
		String orgId = reportInForm.getOrgId();
		String year = String.valueOf(reportInForm.getYear());
		String setDate = String.valueOf(reportInForm.getSetDate());	
		String date = String.valueOf(reportInForm.getDate());
		if(year!=null&&!year.equals("")){		
			term += (term.indexOf("?")>=0 ? "&" : "?");			
			term += "year="+year.toString();   		   
		}	
		
		if(date!=null&&!date.equals("")){		
			term += (term.indexOf("?")>=0 ? "&" : "?");			
			term += "date="+date.toString();   		   
		}
		
		if(setDate!=null&&!setDate.equals("")){		
			term += (term.indexOf("?")>=0 ? "&" : "?");			
			term += "setDate="+setDate.toString();    
		}
		
		if(orgId!=null&&!orgId.equals("")){		
			term += (term.indexOf("?")>=0 ? "&" : "?");			
			term += "orgId="+orgId;    
		}
		
		if(term.indexOf("?")>=0)		
			term = term.substring(term.indexOf("?")+1);
		
		return term;
	}	
}
