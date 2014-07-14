package com.cbrc.smis.action;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.RequestUtils;

import com.cbrc.smis.adapter.StrutsReportInDelegate;
import com.cbrc.smis.form.ReportInForm;
import com.cbrc.smis.util.FitechException;
import com.fitech.net.adapter.StrutsOrgNetDelegate;
import com.fitech.net.hibernate.OrgNet;

/**
 * �����ر��趨ǰ�ĳ�ʼ����
 *  
 * @author rds  
 */
public class RepAgainSetInitAction extends Action {
	private FitechException log = new FitechException(RepAgainSetInitAction.class);

	/**
	 * @param result ��ѯ���ر�־,����ɹ�����true,���򷵻�false
	 * @param ReportInForm 
	 * @param request  
	 * @exception Exception ���쳣��׽���׳�
	 */
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
 
		ReportInForm reportInForm = (ReportInForm)form;	   
		RequestUtils.populate(reportInForm, request);				
		boolean mark = false;
		
		try{
			if(reportInForm.getRepInId() != null){
				ReportInForm _reportInForm = StrutsReportInDelegate.getReportIn(reportInForm.getRepInId());
				if(_reportInForm == null)
					mark = false;
				else{
					OrgNet orgNet = StrutsOrgNetDelegate.selectOne(_reportInForm.getOrgId());
					if(orgNet != null && orgNet.getOrgName() != null)
						_reportInForm.setOrgName(orgNet.getOrgName());
					
					mark = true;
					request.setAttribute("ReportInForm",_reportInForm);
				}
			}else{
				mark = false;
			}
		}catch(Exception ex){
			mark = false;
			log.printStackTrace(ex);			
		}
		
		String term = "";
		if(mark == true)
			term = mapping.findForward("add").getPath();
		else
			term = mapping.findForward("search").getPath();
		
		/**���뱨��ID��ʶ������*/
		if(reportInForm.getRepInId() != null){
			term += (term.indexOf("?")>=0 ? "&" : "?");
			term += "repInId=" + reportInForm.getRepInId();
		}
		/**�����쳣��־״̬����*/
		if(reportInForm.getAllFlags() != null && !reportInForm.getAllFlags().equals("")){
			term += (term.indexOf("?")>=0) ? "&" : "?";
			term += "allFlags=" + reportInForm.getAllFlags();
		}
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
