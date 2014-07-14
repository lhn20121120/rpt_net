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
 * 报表重报设定前的初始化类
 *  
 * @author Dennis Yee  
 */
public class RepAgainInitNXAction extends Action {
	private FitechException log = new FitechException(RepAgainInitNXAction.class);

	/**
	 * 已使用hibernate 卞以刚 2011-12-27
	 * 影响对象：AfReport AfOrg
	 * @param result 查询返回标志,如果成功返回true,否则返回false
	 * @param ReportInForm 
	 * @param request  
	 * @exception Exception 有异常捕捉并抛出
	 */
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
 
		AFReportForm reportInForm = (AFReportForm)form;	   
		RequestUtils.populate(reportInForm, request);				
		boolean mark = false;
		
		try{
			if(reportInForm.getRepId() != null){
				/**已使用hibernate 卞以刚 2011-12-27
				 * 影响对象：AfReport**/
				ReportInForm _reportInForm = AFReportDelegate.getReportIn(Integer.valueOf(reportInForm.getRepId()));
				
				if(_reportInForm == null)
					mark = false;
				else{
					/**已使用hibernate 卞以刚 2011-12-21
					 * 影响对象：AfOrg**/
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
		
		/**加入报表ID标识符条件*/
		if(reportInForm.getRepId() != null){
			term += (term.indexOf("?")>=0 ? "&" : "?");
			term += "repInId=" + reportInForm.getRepId();
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
		/**加入报送频度条件*/
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
	 	
		return new ActionForward(term);
	}
}
