package com.fitech.net.action;

import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
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

import com.cbrc.smis.common.ApartPage;
import com.cbrc.smis.common.Config;
import com.cbrc.smis.security.Operator;
import com.cbrc.smis.util.FitechException;
import com.cbrc.smis.util.FitechMessages;
import com.fitech.net.adapter.StrutsAnalysisTemplateDelegate;
import com.fitech.net.form.AAnalysisTPForm;

public class viewAnalysisTemplateAction extends Action{
	private static FitechException log = new FitechException(viewAnalysisTemplateAction.class);

	public ActionForward execute(ActionMapping mapping,ActionForm form,HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {

		  MessageResources resources = getResources(request);
	      FitechMessages messages = new FitechMessages();
	      //ȡ��request��Χ�ڵ�����������������reportForm��
	      AAnalysisTPForm aAnalysisTPForm = (AAnalysisTPForm)form ;
	      RequestUtils.populate(aAnalysisTPForm, request);
		
		
		int recordCount =0; //��¼����		
		int offset=0; //ƫ����
		int limit=0;  //ÿҳ��ʾ�ļ�¼����
		   
		//List����ĳ�ʼ��
		List resList=null;
		HashMap map = null;
		ApartPage aPage=new ApartPage();
		String strCurPage=request.getParameter("curPage");
		if(strCurPage!=null){
			if(!strCurPage.equals(""))
				aPage.setCurPage(new Integer(strCurPage).intValue());
		}else
			aPage.setCurPage(1);
			

		/**
         * ȡ�õ�ǰ�û���Ȩ����Ϣ
         */   
		HttpSession session = request.getSession();
		Operator operator = null; 
		if(session.getAttribute(Config.OPERATOR_SESSION_ATTRIBUTE_NAME)!=null)  
			operator = (Operator)session.getAttribute(Config.OPERATOR_SESSION_ATTRIBUTE_NAME);     
		Calendar calendar = Calendar.getInstance();		
		if(aAnalysisTPForm.getYear() == null || "".equals(aAnalysisTPForm.getYear()))			   
			aAnalysisTPForm.setYear(new Integer(calendar.get(Calendar.YEAR)));		   
		if(aAnalysisTPForm.getTerm() == null || "".equals(aAnalysisTPForm.getTerm()))			   
			aAnalysisTPForm.setTerm(new Integer(calendar.get(Calendar.MONTH)+1));
		if(aAnalysisTPForm.getOrgId() == null) aAnalysisTPForm.setOrgId(operator.getOrgId());	 		
			//			����ƫ���� 
			offset=(aPage.getCurPage()-1)*Config.PER_PAGE_ROWS; 
			limit = Config.PER_PAGE_ROWS;	
			
	        try{
	        	//ȡ�ü�¼����
	           // recordCount = StrutsAnalysisTemplateDelegate.getAnalysisCount(aAnalysisTPForm);
	        	
	        	//��ʾ��ҳ��ļ�¼
	        //    if(recordCount > 0)
	            //	resList = StrutsAnalysisTemplateDelegate.selectAnalysisReport(aAnalysisTPForm,offset,limit); 
	            	resList = StrutsAnalysisTemplateDelegate.selectAnalysisReport(aAnalysisTPForm); 
	            	
	            	// ����һ���������ж�������¼
	            	map = StrutsAnalysisTemplateDelegate.selectOneAnalysisCount(aAnalysisTPForm); 
	            	
	    	}catch(Exception ex){
				log.printStackTrace(ex);
				messages.add(resources.getMessage("select.dataReport.failed"));		
			}

         //��ApartPage��������request��Χ��
//		aPage.setTerm(this.getTerm(aAnalysisTPForm));		 	
		aPage.setCount(recordCount);
		request.setAttribute(Config.APART_PAGE_OBJECT,aPage);
		request.setAttribute("orgId",aAnalysisTPForm.getOrgId());
		if(aAnalysisTPForm.getATName()!=null && !"".equals(aAnalysisTPForm.getATName())){
			request.setAttribute("ATName", aAnalysisTPForm.getATName());
		}
		

		if(messages.getMessages() != null && messages.getMessages().size() > 0)		
			request.setAttribute(Config.MESSAGES,messages);
		
		if(resList!=null && resList.size()>0){	
			request.setAttribute(Config.RECORDS,resList);   
		}
		if(map != null && map.size()>0){
			request.setAttribute("MAP",map);  
		}
		return mapping.findForward("view");	 
    }
    public String getTerm(AAnalysisTPForm aAnalysisTPForm){
    	
		String url="";  
		String orgId = aAnalysisTPForm.getOrgId();
		String year = String.valueOf(aAnalysisTPForm.getYear());
		String term = String.valueOf(aAnalysisTPForm.getTerm());	
		
		if(year!=null&&!year.equals("")){		
			url += (url.indexOf("?")>=0 ? "&" : "?");			
			url += "year="+year.toString();   		   
		}	
		
		if(term!=null&&!term.equals("")){		
			url += (url.indexOf("?")>=0 ? "&" : "?");			
			url += "term="+term.toString();    
		}
		
		if(orgId!=null&&!orgId.equals("")){		
			url += (url.indexOf("?")>=0 ? "&" : "?");			
			url += "orgId="+orgId;    
		}
		
		if(term.indexOf("?")>=0)		
			url = url.substring(url.indexOf("?")+1);
		
		return url;
	}	
}

/**
 * ����ͳ�Ʒ���ģ��
 * 
 * @author wh
 * 
 */

