package com.cbrc.smis.action;

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

import com.cbrc.smis.adapter.StrutsReportInDelegate;
import com.cbrc.smis.common.Config;
import com.cbrc.smis.form.ReportInForm;
import com.cbrc.smis.util.FitechException;
import com.cbrc.smis.util.FitechLog;
import com.cbrc.smis.util.FitechMessages;
import com.cbrc.smis.util.FitechUtil;
import com.fitech.net.adapter.StrutsOrgNetDelegate;
import com.fitech.net.hibernate.OrgNet;

/**
 * �趨ǿ���ر�Action
 * 
 * @author jcm
 * @2008-01-16
 */
public class NewReportAgainSettingAction extends Action {
	private static FitechException log = new FitechException(NewReportAgainSettingAction.class);  

	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {

		ReportInForm reportInForm = (ReportInForm) form;
		RequestUtils.populate(reportInForm, request);
		MessageResources resources = getResources(request);
		FitechMessages messages = new FitechMessages();
		boolean flag = false; //���ݲ�����־ȷ������ҳ��
		String reportFlg = "0";
		HttpSession session = request.getSession();
		if (session.getAttribute(Config.REPORT_SESSION_FLG) != null){
			reportFlg = (String) session.getAttribute(Config.REPORT_SESSION_FLG);
		} 
		try {
			if(reportInForm != null) {
				boolean result=StrutsReportInDelegate.newForseReportAgainSetting(reportInForm);
//			boolean 	result=StrutsReportInDelegate.updateForseReportAgain(reportInForm);
           
				if(result == true){
					messages.add(resources.getMessage("report_again_setting.new.success"));
					flag = true;
					FitechLog.writeRepLog(new Integer(21), "�����ر��ɹ�", request, reportInForm.getRepInId().toString(),reportFlg);
				}else messages.add(resources.getMessage("report_again_setting.new.fail"));
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
			term = "/selForseReportAgain.do?childRepId=&repName=&frOrFzType="+Config.DEFAULT_VALUE+"&repFreqId="
					+Config.DEFAULT_VALUE+"&orgId="+Config.DEFAULT_VALUE+"&curPage=1";			
		}else{
			term = mapping.getInputForward().getPath();
			
			/**���뱨��ID��ʶ������*/
			if(reportInForm.getRepInId() != null){
				term += (term.indexOf("?")>=0 ? "&" : "?");
				term += "repInId=" + reportInForm.getRepInId();
				
				ReportInForm _reportInForm = StrutsReportInDelegate.getReportIn(reportInForm.getRepInId());
				OrgNet orgNet = StrutsOrgNetDelegate.selectOne(_reportInForm.getOrgId());
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
			/**����ģ�屨��Ƶ��*/
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
		}
		return new ActionForward(term);
	}
}
