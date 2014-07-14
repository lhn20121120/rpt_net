package com.fitech.gznx.action;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.RequestUtils;

import com.cbrc.smis.form.ReportInForm;
import com.cbrc.smis.util.FitechException;
import com.fitech.gznx.form.AFReportForm;
import com.fitech.gznx.po.AfOrg;
import com.fitech.gznx.service.AFOrgDelegate;
import com.fitech.gznx.service.AFReportDelegate;

/**
 * �����ر��趨ǰ�ĳ�ʼ����
 *  
 * @author Dennis Yee  
 */
public class RepAgainInitNXAction extends Action {
	private FitechException log = new FitechException(RepAgainInitNXAction.class);

	/**
	 * ��ʹ��hibernate ���Ը� 2011-12-27
	 * Ӱ�����AfReport AfOrg
	 * @param result ��ѯ���ر�־,����ɹ�����true,���򷵻�false
	 * @param ReportInForm 
	 * @param request  
	 * @exception Exception ���쳣��׽���׳�
	 */
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
 
		AFReportForm reportInForm = (AFReportForm)form;	   
		RequestUtils.populate(reportInForm, request);				
		boolean mark = false;
		
		try{
			if(reportInForm.getRepId() != null){
				/**��ʹ��hibernate ���Ը� 2011-12-27
				 * Ӱ�����AfReport**/
				ReportInForm _reportInForm = AFReportDelegate.getReportIn(Integer.valueOf(reportInForm.getRepId()));
				
				if(_reportInForm == null)
					mark = false;
				else{
					/**��ʹ��hibernate ���Ը� 2011-12-21
					 * Ӱ�����AfOrg**/
					AfOrg orgNet = AFOrgDelegate.selectOne(_reportInForm.getOrgId());
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
		if(reportInForm.getRepId() != null){
			term += (term.indexOf("?")>=0 ? "&" : "?");
			term += "repInId=" + reportInForm.getRepId();
		}
		/**�����쳣��־״̬����*/
		if(reportInForm.getAllFlags() != null && !reportInForm.getAllFlags().equals("")){
			term += (term.indexOf("?")>=0) ? "&" : "?";
			term += "allFlags=" + reportInForm.getAllFlags();
		}
		/**���뱨��������*/
		if(reportInForm.getTemplateId() != null && !reportInForm.getTemplateId().equals("")){
			term += (term.indexOf("?")>=0 ? "&" : "?");
			term += "templateId=" + reportInForm.getTemplateId();
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

		/**���뱨����������*/
		if(reportInForm.getDate() != null){
			term += (term.indexOf("?")>=0 ? "&" : "?");
			term += "date=" + reportInForm.getDate();
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
