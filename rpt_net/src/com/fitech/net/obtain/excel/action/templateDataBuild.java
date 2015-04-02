/*
 * Created on 2006-5-25
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.fitech.net.obtain.excel.action;

import java.io.File;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.MessageResources;

import com.cbrc.smis.util.FitechMessages;
import com.fitech.net.adapter.StrutsOrgNetDelegate;
import com.fitech.net.common.FileParser;
import com.fitech.net.config.Config;
import com.fitech.net.obtain.excel.SheetHandle;

/**
 * @author Administrator
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class templateDataBuild  extends Action{
	/**
	    * execute action.
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
			
			FitechMessages messages = new FitechMessages();
			MessageResources resources = getResources(request);
			
			String repID = request.getParameter("repID");
			String versionID = request.getParameter("versionID");
			String guid = request.getParameter("guid");

			String excelName = repID + "_" + versionID + ".xls";// request.getParameter("excelName");
			String fileName = Config.getObtainTemplateFolderRealPath() + File.separator + excelName;
			String backPath = Config.getObtainTemplateFolderRealPath() + File.separator + "backup";
			File backDir = new File(backPath);
			if(!backDir.exists()) backDir.mkdir();
			FileParser.copyFile(fileName,backPath + File.separator + excelName);
			
			SheetHandle sh = new SheetHandle(repID, versionID);
//			boolean resultFlag = sh.copySheetsValue(guid, fileName, excelName);
//			
//			sh.copySheetToRelease(excelName,excelName);
			//sh.createOrgExcel(Config.getReleaseTemplatePath(),Config.getReleaseTemplatePath(),excelName);
			
			
			String mostLowerOrgIds = StrutsOrgNetDelegate.getMostLowerOrgIds();
			String[] orgIds = mostLowerOrgIds.split(",");
			
			try{
				for(int i=0;i<orgIds.length;i++){
					boolean bool = sh.copySheetsValue2(guid,fileName,excelName,orgIds[i]);
					if(!bool) continue;
					String releaseFileName = sh.copySheetValueToRelease(excelName,excelName);
					
					String orgWhere = Config.getReleaseTemplatePath() + File.separator + orgIds[i];
					File file = new File(orgWhere);
					if(!file.exists()){
						file.mkdir();
					}
					FileParser.copyFile(releaseFileName,orgWhere + File.separator + excelName);
				}
				messages.add(resources.getMessage("excel.create.success"));		
			}catch(Exception ex){
				messages.add(resources.getMessage("excel.create.failed"));
				ex.printStackTrace();
			}
			
			FileParser.copyFile(backPath + File.separator + excelName,fileName);
			//将通用的页面元素写入request
//			String reportUrl = CommMethod.getAbsolutePath(request,  ("Reports/releaseTemplates/" + excelName));
//			request.setAttribute("reportUrl", reportUrl);
//			request.setAttribute("excelName", excelName);
//			request.setAttribute("resultFlag", String.valueOf(resultFlag));
//			CommMethod.buidPageInfo(request);
			//return mapping.findForward("success");
			if(messages.getMessages() != null && messages.getMessages().size() > 0)
			   	  request.setAttribute(com.cbrc.smis.common.Config.MESSAGES,messages);
			return new ActionForward("/viewTemplateNet.do");
			//response.sendRedirect("/navi.jsp");
			//return null;
		}
}
