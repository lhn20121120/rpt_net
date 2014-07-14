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
/**
 * 在报数前先调用校验
 * jcm
 */
public class ViewJYNotPassInfoAction extends Action {
	private FitechException log=new FitechException(ViewJYNotPassInfoAction.class);
	
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
					if(request.getParameter("tblOuterValidateFlag") != null) 
						reportInForm.setTblOuterValidateFlag(new Short(request.getParameter("tblOuterValidateFlag")));
					Integer dataValidateType = reportInForm.getTblOuterValidateFlag() != null ? 
							new Integer(reportInForm.getTblOuterValidateFlag().toString()) : Config.CELL_CHECK_INNER;
					
					List resList=null;
					if(reportStyle==Config.REPORT_STYLE_DD.intValue()){
						resList = StrutsDataValidateInfoDelegate.findNotPass(repInId,null);
					}else if(reportStyle==Config.REPORT_STYLE_QD.intValue()){
						resList=StrutsQDDataValidateInfoDelegate.findNotPass(repInId,dataValidateType);
					}
					
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
			return mapping.findForward("dd_view");
		}else if(reportStyle==Config.REPORT_STYLE_QD.intValue()){
			return mapping.findForward("qd_view");
		}else{
			// System.out.println("error!");
			return mapping.findForward(Config.FORWARD_SYS_ERROR_PAGE);
		}

		
	}
	
}
