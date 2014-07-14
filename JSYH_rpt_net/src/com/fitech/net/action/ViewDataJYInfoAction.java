package com.fitech.net.action;

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

import com.cbrc.smis.adapter.StrutsDataValidateInfoDelegate;
import com.cbrc.smis.adapter.StrutsMChildReportDelegate;
import com.cbrc.smis.adapter.StrutsQDDataValidateInfoDelegate;
import com.cbrc.smis.adapter.StrutsReportInDelegate;
import com.cbrc.smis.common.Config;
import com.cbrc.smis.form.ReportInForm;
import com.cbrc.smis.util.FitechException;
import com.cbrc.smis.util.FitechMessages;
import com.cbrc.smis.util.FitechResource;
import com.cbrc.smis.util.FitechUtil;
import com.fitech.net.adapter.StrutsOrgNetDelegate;
/**
 * 在报数前先调用校验
 * jcm
 */
public class ViewDataJYInfoAction extends Action {
	private FitechException log=new FitechException(ViewDataJYInfoAction.class);
	
	/**
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
			String _repInId = request.getParameter("repInId");
			Integer repInId = new Integer(_repInId);    //实际数据报表ID
			
			if(repInId!=null){		
				ReportInForm reportInForm=StrutsReportInDelegate.getReportIn(repInId);
				if(reportInForm!=null){
					String childRepId=reportInForm.getChildRepId();    //子报表ID
					String versionId=reportInForm.getVersionId();      //版本号
					reportStyle = StrutsMChildReportDelegate.getReportStyle(childRepId,versionId);
					List resList=null;
					
					if(reportStyle==Config.REPORT_STYLE_DD.intValue()){
						resList = StrutsDataValidateInfoDelegate.find(repInId);
					}else if(reportStyle==Config.REPORT_STYLE_QD.intValue()){
						resList = StrutsQDDataValidateInfoDelegate.find(repInId);
					}									
					/**报表名称**/
					request.setAttribute("ReportName",reportInForm.getRepName());
					/**填报机构**/
					request.setAttribute("ReportOrg",StrutsOrgNetDelegate.getOrgName(reportInForm.getOrgId()));
					/**报表日期**/
					request.setAttribute("ReportDate",FitechUtil.getDateString(reportInForm.getReportDate()));
					/**币种**/
					request.setAttribute("currName",reportInForm.getCurName());
					
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
