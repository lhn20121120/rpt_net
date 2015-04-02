package com.fitech.net.action;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.MessageResources;

import com.cbrc.smis.adapter.Procedure;
import com.cbrc.smis.adapter.StrutsReportInDelegate;
import com.cbrc.smis.common.ApartPage;
import com.cbrc.smis.common.Config;
import com.cbrc.smis.form.ReportInForm;
import com.cbrc.smis.hibernate.ReportIn;
import com.cbrc.smis.other.Aditing;
import com.cbrc.smis.security.Operator;
import com.cbrc.smis.system.cb.InputData;
import com.cbrc.smis.util.FitechException;
import com.cbrc.smis.util.FitechMessages;

/**
 * 在报数前先调用校验
 * jcm
 */
public class DataJYLDAction extends Action {
	private FitechException log=new FitechException(DataJYLDAction.class);
	
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
		
		MessageResources resources = getResources(request);
		FitechMessages messages = new FitechMessages();		

		Operator operator=(Operator)request.getSession().getAttribute(Config.OPERATOR_SESSION_ATTRIBUTE_NAME);
		
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
			return new ActionForward("/viewDataReport.do?year=" + year + "&setDate=" + term + "&curPage=" + curPage + "&orgId=" + orgId);		   
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
					reportInForm.setRepName(aditing.getRepName());
					reportInForm.setChildRepId(aditing.getChildRepId());						
					reportInForm.setVersionId(aditing.getVersionId());						
					reportInForm.setOrgId(aditing.getOrgName());
					reportInForm.setOrgName(aditing.getOrgName());
					reportInForm.setCurName(aditing.getCurrName());
					reportInForm.setDataRangeName(aditing.getDataRgTypeName());
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
					aditing.setRepInId(reportInForm.getRepInId());
					jyReportList.add(aditing);					
				}				   
			}		   
		}catch(Exception ex){		
			messages.add(resources.getMessage("select.upReportJY.failed"));	   			
			if(messages.getMessages() != null && messages.getMessages().size() > 0)									
				request.setAttribute(Config.MESSAGES,messages);
			return new ActionForward("/viewDataReport.do?year=" + year + "&setDate=" + term + "&curPage=" + curPage + "&orgId=" + orgId);
		   
		}
		
		List resultList = new ArrayList();
		
		try{			
			if(jyReportList == null || jyReportList.size() == 0){
				messages.add(resources.getMessage("select.upReportJY.failed"));	   							
				if(messages.getMessages() != null && messages.getMessages().size() > 0)					
					request.setAttribute(Config.MESSAGES,messages);
				return new ActionForward("/viewDataReport.do?year=" + year + "&setDate=" + term + "&curPage=" + curPage + "&orgId=" + orgId);
			}
			
			for(int i=0;i<jyReportList.size();i++){
				Aditing aditing = (Aditing)jyReportList.get(i);
				Integer repInId = aditing.getRepInId();     //实际数据报表ID
				
				if(repInId!=null){
					/**执行表内校验存储过程**/
					boolean b=Procedure.runBNJY(repInId,operator.getOperatorName());
					ReportIn reportIn = StrutsReportInDelegate.getReportIn2(repInId);
					aditing.setCheckFlag(reportIn.getCheckFlag());					
					
					if(reportIn.getCheckFlag().toString().equals("-1")) 
						request.setAttribute("isShowBS","noshow");
					else
						StrutsReportInDelegate.updateReportInCheckFlag(aditing.getRepInId(),com.fitech.net.config.Config.CHECK_FLAG_AFTERJY);
					resultList.add(aditing);
										
				}else{
					messages.add(resources.getMessage("select.upReportJY.failed"));	   			
					if(messages.getMessages() != null && messages.getMessages().size() > 0)									
						request.setAttribute(Config.MESSAGES,messages);
					
					return new ActionForward("/viewDataReport.do?year=" + year + "&setDate=" + term + "&curPage=" + curPage + "&orgId=" + orgId);
				}
			}
		}catch(Exception e){			
			log.printStackTrace(e);
			messages.add(resources.getMessage("select.upReportJY.failed"));	   							
			if(messages.getMessages() != null && messages.getMessages().size() > 0)					
				request.setAttribute(Config.MESSAGES,messages);
			return new ActionForward("/viewDataReport.do?year=" + year + "&setDate=" + term + "&curPage=" + curPage + "&orgId=" + orgId);
		}		
		
		if(messages!=null && messages.getSize()>0) request.setAttribute(Config.MESSAGES,messages);
		if(resultList != null && resultList.size()>0)
			request.setAttribute(com.cbrc.smis.common.Config.RECORDS,resultList);
		
		ApartPage aPage=new ApartPage();
		try{
			aPage.setCurPage(Integer.parseInt(curPage));
		}catch(Exception ex){
			aPage.setCurPage(1);
		}
		request.setAttribute("term",term);
		request.setAttribute("year",year);		
		request.setAttribute("orgId",orgId);		
		request.setAttribute(com.cbrc.smis.common.Config.APART_PAGE_OBJECT,aPage);
		
		return mapping.findForward("view");
	}
}
