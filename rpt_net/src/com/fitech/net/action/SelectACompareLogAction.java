package com.fitech.net.action;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.RequestUtils;

import com.cbrc.smis.common.ApartPage;
import com.cbrc.smis.common.Config;
import com.cbrc.smis.util.FitechException;
import com.cbrc.smis.util.FitechMessages;
import com.fitech.net.adapter.StrutsACompareLogDelegate;
import com.fitech.net.adapter.StrutsCollectTypeDelegate;
import com.fitech.net.form.ACompareLogFrom;
import com.fitech.net.form.CollectTypeForm;

/**
 *���������ݱȶ��б�
 *���ڣ�2007-12-12
 *���ߣ��ܷ���
 */
public class SelectACompareLogAction  extends Action {
	private FitechException log=new FitechException(SelectACompareLogAction.class);
	
	
	   /**
	    * @param result 
	    * @param CollectTypeForm 
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
		   
			// �Ƿ���Request
			ACompareLogFrom aCompareLogFrom = (ACompareLogFrom)form ;	   
			RequestUtils.populate(aCompareLogFrom, request);
				    
			int recordCount = 0; // ��¼����
			int offset = 0; // ƫ����
			int limit = 10; // ÿҳ��ʾ�ļ�¼����

			List resList=null;
		   
			ApartPage aPage=new ApartPage();
		   	String strCurPage=request.getParameter("curPage");
			if(strCurPage!=null){
			    if(!strCurPage.equals(""))
			      aPage.setCurPage(new Integer(strCurPage).intValue());
			}
			else{
				strCurPage=(String)request.getAttribute("curPage") ;
				if(strCurPage==null)
				aPage.setCurPage(1);
			}
				
			//����ƫ����
			offset=(aPage.getCurPage()-1)*Config.PER_PAGE_ROWS; 
			limit = Config.PER_PAGE_ROWS;	
			
			try{
				
		   		//ȡ�ü�¼����
		   		recordCount = StrutsACompareLogDelegate.getExistRecordsCount(aCompareLogFrom.getYear(), aCompareLogFrom.getTerm(), aCompareLogFrom.getOrgId(), aCompareLogFrom.getRepName(), aCompareLogFrom.getAcType());
		   			   		
		   		//��ʾ��ҳ��ļ�¼
		   		if(recordCount > 0)
			   	    resList = StrutsACompareLogDelegate.getExistRecords(aCompareLogFrom.getYear(), aCompareLogFrom.getTerm(), aCompareLogFrom.getOrgId(), aCompareLogFrom.getRepName(), aCompareLogFrom.getAcType(), offset, limit);
		   		aPage.setCount(recordCount);
		   		aPage.setTerm(getTerm(aCompareLogFrom));
		   	}catch (Exception e){
				log.printStackTrace(e);	
			}
		 	request.setAttribute(Config.APART_PAGE_OBJECT,aPage);		 	
		 	 if(messages.getMessages() != null && messages.getMessages().size() > 0)
			   	  request.setAttribute(Config.MESSAGES,messages);
		 	 
		 	 if(resList!=null && resList.size()>0)
		 		 request.setAttribute(Config.RECORDS,resList); 
		 	 
		 	 return mapping.findForward("view");
		}

	   
		public String getTerm(ACompareLogFrom aCompareLogFrom){
			String term="";
			if(aCompareLogFrom.getOrgId() != null){
				term += (term.equals("")? "" : "&");
				term += "orgId=" + aCompareLogFrom.getOrgId();
			}
			if(aCompareLogFrom.getYear() != null ){
				term += (term.equals("") ? "" : "&");
				term += "year=" + aCompareLogFrom.getYear();
			}
			if(aCompareLogFrom.getTerm() != null ){
				term += (term.equals("") ? "" : "&");
				term += "term=" + aCompareLogFrom.getTerm();
			}
			if(aCompareLogFrom.getRepName()!= null&&! aCompareLogFrom.getRepName().equals("")){
				term += (term.equals("") ? "" : "&");
				term += "repName=" + aCompareLogFrom.getRepName();
			}
			if(aCompareLogFrom.getAcType()!= null ){
				term += (term.equals("") ? "" : "&");
				term += "acType=" + aCompareLogFrom.getAcType();
			}
			if(term.indexOf("?")>=0)
				term = term.substring(term.indexOf("?")+1);
		   return term;
		}
}

