package com.fitech.gznx.action;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.MessageResources;

import com.cbrc.smis.adapter.StrutsMChildReportDelegate;
import com.cbrc.smis.adapter.StrutsMCurrDelegate;
import com.cbrc.smis.adapter.StrutsQDDataValidateInfoDelegate;
import com.cbrc.smis.common.Config;
import com.cbrc.smis.form.ReportInForm;
import com.cbrc.smis.util.FitechException;
import com.cbrc.smis.util.FitechMessages;
import com.cbrc.smis.util.FitechResource;
import com.fitech.gznx.service.AFDataValidateInfoDelegate;
import com.fitech.gznx.service.AFOrgDelegate;
import com.fitech.gznx.service.AFReportDelegate;

/**
 * 查看校验信息
 * @author Dennis Yee
 *
 */
public class ViewValidateInfoAction extends Action{
	
	private FitechException log=new FitechException(ViewValidateInfoAction.class);
	
	/**
	 * 有一处使用jdbc技术 无特殊oracle语法 不需要修改 卞以刚 2011-12-27
	 * 影响对象：AfReport AfTemplate AfDatavalidateinfo AfOrg MCurr
	 * 影响表：qd_data_validate_info
	 * Performs action.
	 * @param mapping Action mapping.
	 * @param form Action form.
	 * @param request HTTP request.
	 * @param response HTTP response.
	 * @exception IOException if an input/output error occurs
	 * @exception ServletException if a servlet exception occurs
	 */
	public ActionForward execute(ActionMapping mapping, 
			ActionForm form,
			HttpServletRequest request, 
			HttpServletResponse response)
			throws IOException, ServletException {
		
		Locale locale = getLocale(request);
		MessageResources resources = getResources(request);
		FitechMessages messages = new FitechMessages();
		
		int reportStyle=0;
		
		try{
			Integer repInId = Integer.valueOf(request.getParameter("repInId")); //实际数据报表ID
			
			if(repInId!=null){
				/**
				 * 已使用hibernate 卞以刚 2011-12-27
				 * 影响对象：AfReport*/
				ReportInForm reportInForm = AFReportDelegate.getReportIn(repInId);
				
				if(reportInForm!=null){
					
					//String childRepId=reportInForm.getChildRepId();    //子报表ID
					//String versionId=reportInForm.getVersionId();      //版本号
					/**已使用hibernate 卞以刚 2011-12-21
					 * 影响对象：AfReport  AfTemplate**/
					reportStyle = AFReportDelegate.getReportStyle(repInId.longValue());
					
					List resList=null;
					
					if(reportStyle==Config.REPORT_STYLE_DD.intValue()){
						/**已使用hibernate 卞以刚 2011-12-27
						 * 影响对象：AfDatavalidateinfo**/
						resList = AFDataValidateInfoDelegate.find(repInId);
						
					}else if(reportStyle==Config.REPORT_STYLE_QD.intValue()){
						/**jdbc技术 无特殊oracle语法 可能不需要修改 卞以刚 2011-12-27
						 * 影响表：qd_data_validate_info*/
						resList = StrutsQDDataValidateInfoDelegate.find(repInId);
						
					}
					
					/**报表名称**/
					request.setAttribute("ReportName",reportInForm.getRepName());
					/**填报机构**/
					/**已使用hibernate 卞以刚 2011-12-21
					 * 影响对象：AfOrg*/
					request.setAttribute("ReportOrg",AFOrgDelegate.getOrgInfo(reportInForm.getOrgId()).getOrgName());
					/**报表日期**/
					request.setAttribute("ReportDate",reportInForm.getYear()
							+"-"+ reportInForm.getTerm()+"-"+reportInForm.getDay());
					/**币种**/
					/**已使用hibernate 卞以刚 2011-12-21
					 * 影响对象：MCurr**/
					request.setAttribute("currName",StrutsMCurrDelegate.getISMCurr(
							reportInForm.getCurId().toString()).getCurName());
					
					if(resList!=null && resList.size()>0) request.setAttribute(Config.RECORDS,resList);
					
				}else{
					
					messages.add(FitechResource.getMessage(locale,resources,"report.read.info.error"));
				}
			}else{
				messages.add(FitechResource.getMessage(locale,resources,"report.read.info.error"));
			}
			
		}catch(Exception e){
			reportStyle=0;
			log.printStackTrace(e);
		}		
		
		if(messages!=null && messages.getSize()>0) request.setAttribute(Config.MESSAGES,messages);
			
		if(reportStyle==Config.REPORT_STYLE_DD.intValue()){
			// System.out.println(mapping.findForward("dd_view").getPath());
			return mapping.findForward("dd_view");
		}else if(reportStyle==Config.REPORT_STYLE_QD.intValue()){
			// System.out.println(mapping.findForward("qd_view").getPath());
			return mapping.findForward("qd_view");
		}else{
			// System.out.println("error!");
			return mapping.findForward(Config.FORWARD_SYS_ERROR_PAGE);
		}

		
	}
	
}
