package com.fitech.gznx.action;

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

import com.cbrc.smis.common.Config;
import com.cbrc.smis.security.Operator;
import com.cbrc.smis.util.FitechException;
import com.cbrc.smis.util.FitechMessages;
import com.cbrc.smis.util.FitechResource;
import com.fitech.gznx.form.AFReportForm;
import com.fitech.gznx.service.AFReportAgainDelegate;
import com.fitech.gznx.service.AFReportDelegate;

/**
 * ����NX���˱�־״̬��ACTION
 *
 * @author ����
 *
 */
public final class UpdateNXReportRecheckAction extends Action {
	private static FitechException log=new FitechException(UpdateNXReportRecheckAction.class);
	
   /**
    * ��ʹ��hibernate ���Ը� 2011-12-27
    * Ӱ�����AfReport  AfTemplate AfReportAgain
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

		AFReportForm reportInForm = (AFReportForm)form;	   
		RequestUtils.populate(reportInForm, request);
		String flag=request.getParameter("flag");
		try {
			String repInIds=request.getParameter("repInIds");
			
			String cause=request.getParameter("cause");
		 
			if(repInIds!=null && !repInIds.equals("")){			  
				String[] arr=repInIds.split(Config.SPLIT_SYMBOL_COMMA);
				Integer[] ids=null;
				if(arr!=null && arr.length>0) ids=new Integer[arr.length];
				
				for(int i=0;i<arr.length;i++){				
					ids[i]=Integer.valueOf(arr[i]);  			  
				}			  
				reportInForm.setRepInIdArray(ids);			  
				

				if(flag.equals(String.valueOf(Config.CHECK_FLAG_NO))) {
					
					reportInForm.setCheckFlag(com.fitech.net.config.Config.CHECK_FLAG_FAILED.toString());
					reportInForm.setForseReportAgainFlag(Config.FORSE_REPORT_AGAIN_FLAG_1.toString());
					
				}else{
					//����ͨ��
					reportInForm.setCheckFlag(com.fitech.net.config.Config.CHECK_FLAG_PASS.toString());
				}
			}else{//ȫ������ͨ��
				
				HttpSession session = request.getSession();
				Operator operator = (Operator) session.getAttribute(Config.OPERATOR_SESSION_ATTRIBUTE_NAME);
				String templateType = session.getAttribute(Config.REPORT_SESSION_FLG).toString();
				
				reportInForm.setTemplateType(templateType);
				reportInForm.setTblOuterValidateFlag(Config.TBL_OUTER_VALIDATE_FLAG.toString());
				
				
				//��õ���У��ͨ�������ϱ��ı���
				/**��ʹ��hibernate ���Ը� 2011-12-27
				 * Ӱ�����AfReport  AfTemplate**/
		   	    Integer[] ids = AFReportDelegate.reRecheckPassReportIds(reportInForm,
	   	    			operator,Config.CHECK_FLAG_UNCHECK.intValue()); 	  
		   	    
		   	    reportInForm.setRepInIdArray(ids);
		   	    reportInForm.setCheckFlag(com.fitech.net.config.Config.CHECK_FLAG_PASS.toString());
			}
			
			if (reportInForm != null && reportInForm.getRepInIdArray()!=null) {
				// ȡ��ģ������
				HttpSession session = request.getSession();
				Integer templateType = null;
				if (session.getAttribute(com.cbrc.smis.common.Config.REPORT_SESSION_FLG) != null)
					templateType = Integer.valueOf(session.getAttribute(
							com.cbrc.smis.common.Config.REPORT_SESSION_FLG).toString());
				AFReportDelegate.setMessages(messages);
				//���±���״̬
				/**��ʹ��hibernate ���Ը� 2011-12-27
				 * Ӱ�����AfReport**/
				result = AFReportDelegate.update(reportInForm);
				if(AFReportDelegate.getMessages().getSize()>0){
					messages = AFReportDelegate.getMessages();
				} 
				if(flag.equals(String.valueOf(Config.CHECK_FLAG_NO)) ) {
					/**��ʹ��hibernate ���Ը� 2011-12-27
					 * Ӱ�����AfReportAgain**/
					 if(AFReportAgainDelegate.insert(reportInForm.getRepInIdArray(),cause,templateType)){        							  
							messages.add("�ر����óɹ�!");
					 }
				} else {
					if(!result){
						messages.add("������ǩͨ�����ɹ�");
					} else {
						messages.add("������ǩͨ���ɹ�");
					}
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
		if(reportInForm.getTemplateId() != null && !reportInForm.getTemplateId().equals("")){
			term += (term.indexOf("?")>=0 ? "&" : "?");
			term += "childRepId=" + reportInForm.getTemplateId();
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
		if(reportInForm.getBak1() != null){
			term += (term.indexOf("?")>=0 ? "&" : "?");
			term += "bak1=" + reportInForm.getBak1();
		}
		/**���뱨��Ƶ������*/
		if(reportInForm.getRepFreqId() != null){
			term += (term.indexOf("?")>=0 ? "&" : "?");
			term += "repFreqId=" + reportInForm.getRepFreqId();
		}
		/**������������*/
		if(reportInForm.getDate() != null){
			term += (term.indexOf("?")>=0 ? "&" : "?");
			term += "term=" + reportInForm.getDate();
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