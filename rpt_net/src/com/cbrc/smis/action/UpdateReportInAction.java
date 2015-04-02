package com.cbrc.smis.action;

import java.io.IOException;
import java.util.Locale;

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
import com.cbrc.smis.common.Config;
import com.cbrc.smis.form.ReportInForm;
import com.cbrc.smis.util.FitechException;
import com.cbrc.smis.util.FitechLog;
import com.cbrc.smis.util.FitechMessages;
import com.cbrc.smis.util.FitechResource;

/**
 * ������˱�־״̬��ACTION
 *
 * @author ����
 *
 */
public final class UpdateReportInAction extends Action {
	private static FitechException log=new FitechException(UpdateReportInAction.class);
	
   /**
    * Performs action.
    * @result ���³ɹ�����true�����򷵻�false
    * @reportInForm FormBean��ʵ����
    * @e Exception ����ʧ�ܲ�׽�쳣���׳�
    */
   
	public ActionForward execute(ActionMapping mapping,ActionForm form,HttpServletRequest request
			,HttpServletResponse response)throws IOException, ServletException {
		
		Locale locale = getLocale(request);	   
		MessageResources resources = getResources(request);	   
		FitechMessages messages = new FitechMessages();
	   
		boolean result=false;

		ReportInForm reportInForm = (ReportInForm)form;	   
		RequestUtils.populate(reportInForm, request);
		String reportFlg = "0";
		HttpSession session = request.getSession();
		if (session.getAttribute(Config.REPORT_SESSION_FLG) != null){
			reportFlg = (String) session.getAttribute(Config.REPORT_SESSION_FLG);
		}
		String updateFlag = request.getParameter("updateFlag");
		String repInIds=request.getParameter("repInIds");
		try {
			
			String flag=request.getParameter("flag");		  
			String cause=request.getParameter("cause");
		 
			if(repInIds!=null && !repInIds.equals("")){			  
				String[] arr=repInIds.split(Config.SPLIT_SYMBOL_COMMA);			  
				Integer[] ids=null;			  
				if(arr!=null && arr.length>0) ids=new Integer[arr.length];
				
				for(int i=0;i<arr.length;i++){				
					ids[i]=Integer.valueOf(arr[i]);  			  
				}			  
				reportInForm.setRepInIdArray(ids);			  
				reportInForm.setCheckFlag(new Short(flag));

				if(flag.equals(String.valueOf(Config.CHECK_FLAG_NO))) {
					reportInForm.setForseReportAgainFlag(Config.FORSE_REPORT_AGAIN_FLAG_1);
					for(int i=0;i<arr.length;i++)
						FitechLog.writeRepLog(new Integer(20), "��˲�ͨ��", request, arr[i] ,reportFlg);
				}else{
					for(int i=0;i<arr.length;i++)
						FitechLog.writeRepLog(new Integer(20), "���ͨ��", request, arr[i] ,reportFlg);
				}
			}    	  
			if (reportInForm != null && reportInForm.getRepInIdArray()!=null) {			
				result = StrutsReportInDelegate.update(reportInForm);				
				if (result == true) {
					 if(com.cbrc.smis.adapter.StrutsReportInDelegate2.insert(reportInForm.getRepInIdArray(),cause) == true){        							  
						 //  messages.add(FitechResource.getMessage(locale, resources,"saveCheckRep.save.success"));    
							messages.add("�ر����óɹ�!");
					 }
				} else {
					messages.add(FitechResource.getMessage(locale, resources,"saveCheckRep.save.failed"));
					return mapping.findForward("saveCheck");
				}
			}		
		} catch (Exception e) {
			log.printStackTrace(e);
			messages.add(FitechResource.getMessage(locale, resources,"errors.system"));
		}	 	
		if(messages.getMessages()!= null && messages.getMessages().size() > 0)		 
			request.setAttribute(Config.MESSAGES,messages);
	 	
	 	String term=mapping.findForward("saveCheck").getPath();
	 	/**���뱨��������*/
		if(reportInForm.getChildRepId() != null && !reportInForm.getChildRepId().equals("")){
			term += (term.indexOf("?")>=0 ? "&" : "?");
			term += "childRepId=" + reportInForm.getChildRepId();
		}
		/**���뱨����������*/
		if(reportInForm.getRepName() != null && !reportInForm.getRepName().equals("")){
			term += (term.indexOf("?")>=0 ? "&" : "?");
			/**����WebLogic����Ҫ����ת�룬ֱ����Ϊ��������*/
			term += "repName=" + reportInForm.getRepName();
			/**����WebSphere����Ҫ�Ƚ���ת�룬����Ϊ��������*/
			//term += "repName=" + new String(reportInForm.getRepName().getBytes("gb2312"), "iso-8859-1");
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
		/**���뱨�ͻ�������*/
		if(reportInForm.getOrgId() != null){
			term += (term.indexOf("?")>=0 ? "&" : "?");
			term += "orgId=" + reportInForm.getOrgId();
		}
	 	/**���뵱ǰҳ����*/
	 	if(reportInForm.getCurPage()!=null && !reportInForm.getCurPage().equals("")){		
	 		term += (term.indexOf("?")>=0 ? "&" : "?");			
	 		term += "curPage=" + reportInForm.getCurPage();   		   
	 	}
	 	if(updateFlag!=null && updateFlag.equals("1")){
	 		if(repInIds!=null && !repInIds.equals("")){	
		 		if(term.indexOf("?")>0)
			 		term+="&schemaRepIds="+repInIds;
			 	else
			 		term+="?schemaRepIds="+repInIds;
		 	}
	 	}
	 	return new ActionForward(term);
	}
}

