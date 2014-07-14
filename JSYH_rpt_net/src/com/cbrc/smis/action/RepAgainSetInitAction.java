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
 * 报表重报设定前的初始化类
 *  
 * @author rds  
 */
public class RepAgainSetInitAction extends Action {
	private FitechException log = new FitechException(RepAgainSetInitAction.class);

	/**
	 * @param result 查询返回标志,如果成功返回true,否则返回false
	 * @param ReportInForm 
	 * @param request  
	 * @exception Exception 有异常捕捉并抛出
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
		
		/**加入报表ID标识符条件*/
		if(reportInForm.getRepInId() != null){
			term += (term.indexOf("?")>=0 ? "&" : "?");
			term += "repInId=" + reportInForm.getRepInId();
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
		/**加入报送频度条件*/
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
	 	
		return new ActionForward(term);
	}
}
