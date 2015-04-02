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
 * 设定强制重报Action
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
		boolean flag = false; //根据操作标志确定返回页面
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
					FitechLog.writeRepLog(new Integer(21), "新增重报成功", request, reportInForm.getRepInId().toString(),reportFlg);
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
		 * 强制重报设定成功直接跳转到重报查询页面
		 * 设定失败返回跳转之前的页面（即重报原因填写页）
		 */
		String term = null;
		if(flag == true){
			FitechUtil.removeAttribute(mapping,request);
			term = "/selForseReportAgain.do?childRepId=&repName=&frOrFzType="+Config.DEFAULT_VALUE+"&repFreqId="
					+Config.DEFAULT_VALUE+"&orgId="+Config.DEFAULT_VALUE+"&curPage=1";			
		}else{
			term = mapping.getInputForward().getPath();
			
			/**加入报表ID标识符条件*/
			if(reportInForm.getRepInId() != null){
				term += (term.indexOf("?")>=0 ? "&" : "?");
				term += "repInId=" + reportInForm.getRepInId();
				
				ReportInForm _reportInForm = StrutsReportInDelegate.getReportIn(reportInForm.getRepInId());
				OrgNet orgNet = StrutsOrgNetDelegate.selectOne(_reportInForm.getOrgId());
				if(orgNet != null && orgNet.getOrgName() != null)
					_reportInForm.setOrgName(orgNet.getOrgName());
				
				request.setAttribute("ReportInForm",_reportInForm);				
			}
			/**加入异常标志状态条件*/
			if(reportInForm.getAllFlags() != null && !reportInForm.getAllFlags().equals("")){
				term += (term.indexOf("?")>=0) ? "&" : "?";
				term += "allFlags=" + reportInForm.getAllFlags();
			}
			/**加入报表编号条件*/
			if(reportInForm.getChildRepId() != null && !reportInForm.getChildRepId().equals("")){
				term += (term.indexOf("?")>=0 ? "&" : "?");
				term += "childRepId=" + reportInForm.getChildRepId();
			}
			/**加入报表名称条件*/
			if(reportInForm.getRepName() != null && !reportInForm.getRepName().equals("")){
				term += (term.indexOf("?")>=0 ? "&" : "?");
				/**若是WebLogic则不需要进行转码，直接作为参数传递*/
				term += "repName=" + reportInForm.getRepName();
				/**若是WebSphere则需要先进行转码，再作为参数传递*/
				//term += "repName=" + new String(reportInForm.getRepName().getBytes("gb2312"), "iso-8859-1");
			}
			/**加入模板类型条件*/
			if(reportInForm.getFrOrFzType() != null){
				term += (term.indexOf("?")>=0 ? "&" : "?");
				term += "frOrFzType=" + reportInForm.getFrOrFzType();
			}
			/**加入模板报送频度*/
			if(reportInForm.getRepFreqId() != null){
				term += (term.indexOf("?")>=0 ? "&" : "?");
				term += "repFreqId=" + reportInForm.getRepFreqId();
			}
			/**加入报表年份条件*/
			if(reportInForm.getYear() != null){
				term += (term.indexOf("?")>=0 ? "&" : "?");
				term += "year=" + reportInForm.getYear();
			}
			/**加入报表期数条件*/
			if(reportInForm.getTerm() != null){
				term += (term.indexOf("?")>=0 ? "&" : "?");
				term += "term=" + reportInForm.getTerm();
			}
			/**加入报送机构条件*/
			if(reportInForm.getOrgId() != null){
				term += (term.indexOf("?")>=0 ? "&" : "?");
				term += "orgId=" + reportInForm.getOrgId();
			}
		 	/**加入当前页条件*/
		 	if(reportInForm.getCurPage()!=null && !reportInForm.getCurPage().equals("")){		
		 		term += (term.indexOf("?")>=0 ? "&" : "?");			
		 		term += "curPage=" + reportInForm.getCurPage();   		   
		 	}
		}
		return new ActionForward(term);
	}
}
