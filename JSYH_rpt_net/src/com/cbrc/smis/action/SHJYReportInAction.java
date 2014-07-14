package com.cbrc.smis.action;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
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

import com.cbrc.smis.adapter.Procedure;
import com.cbrc.smis.adapter.StrutsReportInDelegate;
import com.cbrc.smis.common.Config;
import com.cbrc.smis.form.ReportInForm;
import com.cbrc.smis.security.Operator;
import com.cbrc.smis.util.FitechException;
import com.cbrc.smis.util.FitechMessages;
import com.cbrc.smis.util.FitechResource;

/**
 * ��˲���ǰ�������У��
 *
 * @author jcm
 * @serialData 2008-02-21
 */
public final class SHJYReportInAction extends Action {
	private static FitechException log=new FitechException(SHJYReportInAction.class);
	
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
	   
		ReportInForm reportInForm = (ReportInForm)form;	   
		RequestUtils.populate(reportInForm, request);
		
		try {
			Operator operator = null;
			HttpSession session = request.getSession();
	        if(session.getAttribute(Config.OPERATOR_SESSION_ATTRIBUTE_NAME)!=null)
	            operator = (Operator)session.getAttribute(Config.OPERATOR_SESSION_ATTRIBUTE_NAME);
			String repInIds=request.getParameter("repInIds");			
		 
			if(repInIds!=null && !repInIds.equals("")){
				String[] repInIdArr = repInIds.split(Config.SPLIT_SYMBOL_COMMA);
				if(repInIdArr != null && repInIdArr.length != 0){
					for(int i=0;i<repInIdArr.length;i++){
						try{
							Integer repInId = new Integer(repInIdArr[i]);
							Procedure.runBJJY(repInId,operator.getOperatorName());
						}catch(Exception ex){
							ex.printStackTrace();
						}
					}
				}				
			}else{
				 
		        
				List list = StrutsReportInDelegate.selectOfManual(reportInForm,operator);
				if(list != null && list.size() != 0){
					for(Iterator iter=list.iterator();iter.hasNext();){
						Integer repInId = (Integer)iter.next();
						Procedure.runBJJY(repInId,operator.getOperatorName());
					}
				}
			}	
			messages.add("����У����ɣ�");
		} catch (Exception e) {
			log.printStackTrace(e);
			messages.add(FitechResource.getMessage(locale, resources,"errors.system"));
		}	 	
		if(messages.getMessages()!= null && messages.getMessages().size() > 0)		 
			request.setAttribute(Config.MESSAGES,messages);
	 	
	 	String term = mapping.findForward("shjyCheckRep").getPath();	 	
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

	 	return new ActionForward(term);
	}
}

