package com.cbrc.smis.action;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.MessageResources;

import com.cbrc.smis.form.ReportInForm;
import com.cbrc.smis.other.Aditing;
import com.cbrc.smis.security.Operator;
import com.cbrc.smis.system.cb.InputData;
import com.cbrc.smis.util.FitechMessages;
import com.fitech.net.config.Config;

/**
 * 报表数据入库
 *
 */
public final class IntoTemplateAction extends Action {	
		
   /**
    * Performs action.
    * @param mapping Action mapping.
    * @param form Action form.
    * @param request HTTP request.
    * @param response HTTP response.
    * @exception IOException if an input/output error occurs
    * @exception ServletException if a servlet exception occurs
    */
   public ActionForward execute(ActionMapping mapping,ActionForm form,HttpServletRequest request,
      HttpServletResponse response) throws IOException, ServletException {	   
	  	
	   FitechMessages messages = new FitechMessages();
	   MessageResources resources = getResources(request);	
	   
	   Operator operator=(Operator)request.getSession().getAttribute(Config.OPERATOR_SESSION_ATTRIBUTE_NAME);
	   
	   HashMap map = null;
	   String year = null;
	   String term = null;
	   String curPage = null;
	   curPage = request.getParameter("curPage");
	   String orgId = request.getParameter("orgId");	   
	   map = request.getSession().getAttribute(operator.getOperatorId().toString()) != null ? (HashMap)(request.getSession().getAttribute(operator.getOperatorId().toString())) : new HashMap();
	   request.getSession().setAttribute(operator.getOperatorId().toString(),null);	   	
	   
	   if(orgId == null || orgId.equals("")){
		   messages.add(resources.getMessage("select.upReport.failed"));	   
		   if(messages.getMessages() != null && messages.getMessages().size() > 0)		
				request.setAttribute(com.cbrc.smis.common.Config.MESSAGES,messages);
		   
		   return new ActionForward("/viewDataReport.do?year=" + year + "&setDate=" + term + "&curPage=" + curPage);
	   }
	   
	   File xmlFileDirect=new File(Config.REAL_ROOT_PATH+Config.REPORT_NAME+com.cbrc.smis.common.Config.FILESEPARATOR+Config.SERVICE_UP_TEMP+Config.SERVICE_UP_XML+orgId+File.separator);
	
	   File[] xmlFiles = null;
	   if(xmlFileDirect!=null && xmlFileDirect.isDirectory())
		   xmlFiles = xmlFileDirect.listFiles();  
      
	   if(xmlFiles == null && xmlFiles.length <= 0){
		   messages.add(resources.getMessage("select.upReport.failed"));	   
		   if(messages.getMessages() != null && messages.getMessages().size() > 0)		
				request.setAttribute(com.cbrc.smis.common.Config.MESSAGES,messages);
		   
		   return new ActionForward("/viewDataReport.do?year=" + year + "&setDate=" + term + "&curPage=" + curPage + "&orgId=" + orgId);
	   }
	   	   
	   InputData inputData = new InputData();
	   boolean bool = false;
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
					
				   if(year == null) year = aditing.getYear().toString();					
				   if(term == null) term = aditing.getTerm().toString();					
				   bool = inputData.conductXML(xmlFiles[i],reportInForm,operator.getOperatorName());
			   }				
		   }
		   if(bool == true) messages.add(resources.getMessage("select.upReport.success"));
		   
		   /**上报成功，删除XML文件**/
//		   File zipReleaseFolder = new File(Config.REAL_ROOT_PATH+Config.REPORT_NAME + File.separator + Config.SERVICE_UP_TEMP + Config.SERVICE_UP_XML);			
//		   DownLoadDataToZip dldt = DownLoadDataToZip.newInstance();			
//		   if(zipReleaseFolder != null && zipReleaseFolder.listFiles() != null) dldt.deleteFolder(zipReleaseFolder);
	   }catch(Exception ex){
		   messages.add(resources.getMessage("select.upReport.failed"));	   
		   if(messages.getMessages() != null && messages.getMessages().size() > 0)		
				request.setAttribute(com.cbrc.smis.common.Config.MESSAGES,messages);
		   
		   return new ActionForward("/viewDataReport.do?year=" + year + "&setDate=" + term + "&curPage=" + curPage + "&orgId=" + orgId);
	   }
      
      if(messages.getMessages() != null && messages.getMessages().size() > 0)		
    	  request.setAttribute(Config.MESSAGE,messages);
    
      request.setAttribute("messagenotnull",messages);
      return new ActionForward("/viewDataReport.do?year=" + year + "&setDate=" + term + "&curPage=" + curPage + "&orgId=" + orgId);
   }
}
