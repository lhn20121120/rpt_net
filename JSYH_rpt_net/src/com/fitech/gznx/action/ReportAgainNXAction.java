package com.fitech.gznx.action;

import java.io.IOException;

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
import com.cbrc.smis.form.ReportInForm;
import com.cbrc.smis.util.FitechException;
import com.cbrc.smis.util.FitechMessages;
import com.cbrc.smis.util.FitechUtil;
import com.fitech.gznx.form.AFReportForm;
import com.fitech.gznx.po.AfOrg;
import com.fitech.gznx.service.AFOrgDelegate;
import com.fitech.gznx.service.AFReportDelegate;

/**
 * �����ر�
 * @author Dennis Yee
 *
 */
public class ReportAgainNXAction extends Action {
	
	private static FitechException log = new FitechException(ReportAgainNXAction.class);

	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {

		AFReportForm reportInForm = (AFReportForm) form;
		RequestUtils.populate(reportInForm, request);
		MessageResources resources = getResources(request);
		FitechMessages messages = new FitechMessages();
		boolean flag = false; //���ݲ�����־ȷ������ҳ��
		
		try {
			if (reportInForm != null) {
				
				HttpSession session = request.getSession();
				// ȡ��ģ������
				if (session.getAttribute(com.cbrc.smis.common.Config.REPORT_SESSION_FLG) != null)
					reportInForm.setTemplateType(session.getAttribute(
								com.cbrc.smis.common.Config.REPORT_SESSION_FLG).toString());

				boolean result = AFReportDelegate.ForseReportAgainSetting(reportInForm);
           
				if (result == true) {
					messages.add(resources.getMessage("report_again_setting.new.success"));
					flag = true;
				} else 
					messages.add(resources.getMessage("report_again_setting.new.fail"));
		    }else{
			    messages.add(resources.getMessage("report_again_setting.input.err"));				
		    }
		} catch (Exception e) {
			log.printStackTrace(e);
			messages.add(resources.getMessage("report_again_setting.new.fail"));
		    return mapping.getInputForward();
		}
		if(messages.getMessages() != null && messages.getMessages().size() > 0)
		   	  request.setAttribute(Config.MESSAGES,messages);
		
		/**
		 * ǿ���ر��趨�ɹ�ֱ����ת���ر���ѯҳ��
		 * �趨ʧ�ܷ�����ת֮ǰ��ҳ�棨���ر�ԭ����дҳ��
		 */
		String term = null;
		if(flag == true){
			FitechUtil.removeAttribute(mapping,request);
			term = "/selReportAgainNX.do?templateId=&repName=&bak1="+Config.DEFAULT_VALUE+"&repFreqId="
					+Config.DEFAULT_VALUE+"&orgId="+Config.DEFAULT_VALUE+"&curPage=1";			
		}else{
			term = mapping.getInputForward().getPath();
			
			/**���뱨��ID��ʶ������*/
			if(reportInForm.getRepId() != null){
				term += (term.indexOf("?")>=0 ? "&" : "?");
				term += "repId=" + reportInForm.getRepId();
				
				ReportInForm _reportInForm = AFReportDelegate.getReportIn(Integer.valueOf(reportInForm.getRepId()));
				
				AfOrg orgNet = AFOrgDelegate.selectOne(_reportInForm.getOrgId());
				
				if(orgNet != null && orgNet.getOrgName() != null)
					_reportInForm.setOrgName(orgNet.getOrgName());
				
				request.setAttribute("ReportInForm",_reportInForm);
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
			/**����ģ�屨��Ƶ��*/
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
		}
		return new ActionForward(term);
	}
}
