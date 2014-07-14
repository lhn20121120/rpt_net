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
 * 新增重报
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
		boolean flag = false; //根据操作标志确定返回页面
		
		try {
			if (reportInForm != null) {
				
				HttpSession session = request.getSession();
				// 取得模板类型
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
		 * 强制重报设定成功直接跳转到重报查询页面
		 * 设定失败返回跳转之前的页面（即重报原因填写页）
		 */
		String term = null;
		if(flag == true){
			FitechUtil.removeAttribute(mapping,request);
			term = "/selReportAgainNX.do?templateId=&repName=&bak1="+Config.DEFAULT_VALUE+"&repFreqId="
					+Config.DEFAULT_VALUE+"&orgId="+Config.DEFAULT_VALUE+"&curPage=1";			
		}else{
			term = mapping.getInputForward().getPath();
			
			/**加入报表ID标识符条件*/
			if(reportInForm.getRepId() != null){
				term += (term.indexOf("?")>=0 ? "&" : "?");
				term += "repId=" + reportInForm.getRepId();
				
				ReportInForm _reportInForm = AFReportDelegate.getReportIn(Integer.valueOf(reportInForm.getRepId()));
				
				AfOrg orgNet = AFOrgDelegate.selectOne(_reportInForm.getOrgId());
				
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
			if(reportInForm.getTemplateId() != null && !reportInForm.getTemplateId().equals("")){
				term += (term.indexOf("?")>=0 ? "&" : "?");
				term += "templateId=" + reportInForm.getTemplateId();
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
			if(reportInForm.getBak1() != null){
				term += (term.indexOf("?")>=0 ? "&" : "?");
				term += "bak1=" + reportInForm.getBak1();
			}
			/**加入模板报送频度*/
			if(reportInForm.getRepFreqId() != null){
				term += (term.indexOf("?")>=0 ? "&" : "?");
				term += "repFreqId=" + reportInForm.getRepFreqId();
			}
			/**加入报表期数条件*/
			if(reportInForm.getDate() != null){
				term += (term.indexOf("?")>=0 ? "&" : "?");
				term += "date=" + reportInForm.getDate();
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
