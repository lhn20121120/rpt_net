package com.fitech.net.action;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
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

import com.cbrc.smis.adapter.Procedure;
import com.cbrc.smis.adapter.StrutsDataValidateInfoDelegate;
import com.cbrc.smis.adapter.StrutsMChildReportDelegate;
import com.cbrc.smis.adapter.StrutsQDDataValidateInfoDelegate;
import com.cbrc.smis.adapter.StrutsReportInDelegate;
import com.cbrc.smis.common.Config;
import com.cbrc.smis.form.ReportInForm;
import com.cbrc.smis.other.Aditing;
import com.cbrc.smis.security.Operator;
import com.cbrc.smis.system.cb.InputData;
import com.cbrc.smis.util.FitechException;
import com.cbrc.smis.util.FitechMessages;
import com.cbrc.smis.util.FitechResource;
import com.cbrc.smis.util.FitechUtil;
import com.fitech.net.adapter.StrutsOrgNetDelegate;

/**
 * 在报数前先调用校验
 * jcm
 */
public class DataJYAction extends Action {
	private FitechException log=new FitechException(DataJYAction.class);
	
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

		Operator operator=(Operator)request.getSession().getAttribute(Config.OPERATOR_SESSION_ATTRIBUTE_NAME);
		String userName= operator.getOperatorName();
		
		String year = null;		
		String term = null;		
		String curPage = request.getParameter("curPage");
		String orgId = request.getParameter("orgId");
		HashMap map = null;
		map = request.getSession().getAttribute(operator.getOperatorId().toString()) != null ? (HashMap)(request.getSession().getAttribute(operator.getOperatorId().toString())) : new HashMap();
		
		if(orgId == null || orgId.equals("")){
			   messages.add(resources.getMessage("select.upReport.failed"));	   
			   if(messages.getMessages() != null && messages.getMessages().size() > 0)		
					request.setAttribute(com.cbrc.smis.common.Config.MESSAGES,messages);
			   
			   return new ActionForward("/viewDataReport.do?year=" + year + "&setDate=" + term + "&curPage=" + curPage);
		   }
		
		File xmlFileDirect=new File(com.fitech.net.config.Config.REAL_ROOT_PATH+com.fitech.net.config.Config.REPORT_NAME		
				+com.cbrc.smis.common.Config.FILESEPARATOR+com.fitech.net.config.Config.SERVICE_UP_TEMP				
				+com.fitech.net.config.Config.SERVICE_UP_XML+orgId+File.separator);
				   
		File[] xmlFiles = null;		
		if(xmlFileDirect!=null && xmlFileDirect.isDirectory())		
			xmlFiles = xmlFileDirect.listFiles();  	      
		  
		if(xmlFiles == null && xmlFiles.length <= 0){		
			messages.add(resources.getMessage("select.upReportJY.failed"));	   			
			if(messages.getMessages() != null && messages.getMessages().size() > 0)					
				request.setAttribute(com.cbrc.smis.common.Config.MESSAGES,messages);
			return new ActionForward("/viewDataReport.do?year=" + year + "&setDate=" + term + "&curPage=" + curPage);		   
		}
		
		List jyReportList = new ArrayList();		
		InputData inputData = new InputData();		
		try{	
			for(int i=0;i<xmlFiles.length;i++){			
				String fileName = xmlFiles[i].getName();	
				if(!map.containsKey(fileName)) continue;
								
				Aditing aditing = (Aditing)map.get(fileName);
				ReportInForm reportInForm = null;	
				
				if(aditing != null){					
					reportInForm = new ReportInForm();						
					reportInForm.setChildRepId(aditing.getChildRepId());						
					reportInForm.setVersionId(aditing.getVersionId());						
					reportInForm.setOrgId(aditing.getOrgName());
					reportInForm.setCurName(aditing.getCurrName());
					if(year == null) year = aditing.getYear().toString();						
					if(term == null) term = aditing.getTerm().toString();						
					boolean bool = inputData.conductJYXML(xmlFiles[i],reportInForm);	
					if(bool == false){
//						messages.add(resources.getMessage("select.upReportJY.failed"));	   							
//						if(messages.getMessages() != null && messages.getMessages().size() > 0)					
//							request.setAttribute(Config.MESSAGES,messages);
//						return new ActionForward("/viewDataReport.do?year=" + year + "&setDate=" + term + "&curPage=" + curPage);
						continue;
					}
					jyReportList.add(reportInForm);					
				}				   
			}		   
		}catch(Exception ex){		
			messages.add(resources.getMessage("select.upReportJY.failed"));	   			
			if(messages.getMessages() != null && messages.getMessages().size() > 0)									
				request.setAttribute(Config.MESSAGES,messages);
			return new ActionForward("/viewDataReport.do?year=" + year + "&setDate=" + term + "&curPage=" + curPage);
		   
		}

		int reportStyle=0;
		
		try{
			
			if(jyReportList == null || jyReportList.size() == 0){
				messages.add(resources.getMessage("select.upReportJY.failed"));	   							
				if(messages.getMessages() != null && messages.getMessages().size() > 0)					
					request.setAttribute(Config.MESSAGES,messages);
				return new ActionForward("/viewDataReport.do?year=" + year + "&setDate=" + term + "&curPage=" + curPage);
			}
			
			if(jyReportList.size() == 1){
				ReportInForm reportInForm = (ReportInForm)jyReportList.get(0);
				Integer repInId = reportInForm.getRepInId();     //实际数据报表ID
				
				if(repInId!=null){
					/**执行表内、表间、跨频度校验存储过程**/
					Procedure.runBNJY(repInId,userName);				
				//	Procedure.runKPDJY(repInId);				
					Procedure.runBJJY(repInId,userName);
				
					StrutsReportInDelegate.updateReportInCheckFlag(reportInForm.getRepInId(),com.fitech.net.config.Config.CHECK_FLAG_AFTERJY);
					
					if(reportInForm!=null){
						String childRepId=reportInForm.getChildRepId();    //子报表ID
						String versionId=reportInForm.getVersionId();      //版本号
						reportStyle = StrutsMChildReportDelegate.getReportStyle(childRepId,versionId);
						List resList=null;
						
						if(reportStyle==Config.REPORT_STYLE_DD.intValue()){
							resList = StrutsDataValidateInfoDelegate.find(repInId);
						}else if(reportStyle==Config.REPORT_STYLE_QD.intValue()){
							resList=StrutsQDDataValidateInfoDelegate.find(repInId);
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
				
				if(messages!=null && messages.getSize()>0) request.setAttribute(Config.MESSAGES,messages);
				
				if(reportStyle==Config.REPORT_STYLE_DD.intValue()){					
					return mapping.findForward("dd_view");
				}else if(reportStyle==Config.REPORT_STYLE_QD.intValue()){					
					return mapping.findForward("qd_view");
				}else{					
					return mapping.findForward(Config.FORWARD_SYS_ERROR_PAGE);
				}
			}else{
				for(int i=0;i<jyReportList.size();i++){
					ReportInForm reportInForm = (ReportInForm)jyReportList.get(i);
					Integer repInId = reportInForm.getRepInId();     //实际数据报表ID
					
					if(repInId!=null){
						/**执行表内、表间、跨频度校验存储过程**/
						Procedure.runBNJY(repInId,userName);				
						//	Procedure.runKPDJY(repInId);				
							Procedure.runBJJY(repInId,userName);
					
						StrutsReportInDelegate.updateReportInCheckFlag(reportInForm.getRepInId(),com.fitech.net.config.Config.CHECK_FLAG_AFTERJY);
						
						if(reportInForm!=null){
							String childRepId=reportInForm.getChildRepId();    //子报表ID
							String versionId=reportInForm.getVersionId();      //版本号
							reportStyle = StrutsMChildReportDelegate.getReportStyle(childRepId,versionId);
							List resList=null;
							
							if(reportStyle==Config.REPORT_STYLE_DD.intValue()){
								resList = StrutsDataValidateInfoDelegate.find(repInId);
							}else if(reportStyle==Config.REPORT_STYLE_QD.intValue()){
								resList=StrutsQDDataValidateInfoDelegate.find(repInId);
							}									
							/**报表名称**/
							request.setAttribute("ReportName"+i,reportInForm.getRepName()+"["+reportInForm.getChildRepId()+"]");
							/**填报机构**/
							if(request.getAttribute("ReportOrg") == null)
								request.setAttribute("ReportOrg",StrutsOrgNetDelegate.getOrgName(reportInForm.getOrgId()));
							/**报表日期**/
							request.setAttribute("ReportDate"+i,FitechUtil.getDateString(reportInForm.getReportDate()));
							/**币种**/
							request.setAttribute("currName"+i,reportInForm.getCurName());
							
							if(resList!=null && resList.size()>0) request.setAttribute(Config.RECORDS+i,resList);
						}else{
							messages.add(FitechResource.getMessage(locale,resources,"report.read.info.error"));
						}
					}else{
						messages.add(FitechResource.getMessage(locale,resources,"report.read.info.error"));
					}
				}
			}

		}catch(Exception e){
			reportStyle=0;
			log.printStackTrace(e);
			messages.add(resources.getMessage("select.upReportJY.failed"));	   							
			if(messages.getMessages() != null && messages.getMessages().size() > 0)					
				request.setAttribute(Config.MESSAGES,messages);
			return new ActionForward("/viewDataReport.do?year=" + year + "&setDate=" + term + "&curPage=" + curPage);
		}		
		
		if(messages!=null && messages.getSize()>0) request.setAttribute(Config.MESSAGES,messages);
			
		request.setAttribute("count",String.valueOf(jyReportList.size()));
		
		if(reportStyle==Config.REPORT_STYLE_DD.intValue()){			
			return mapping.findForward("dd_more_view"); 
		}else if(reportStyle==Config.REPORT_STYLE_QD.intValue()){			
			return mapping.findForward("qd_view");
		}else{			
			return mapping.findForward(Config.FORWARD_SYS_ERROR_PAGE);
		}
	}
	
}
