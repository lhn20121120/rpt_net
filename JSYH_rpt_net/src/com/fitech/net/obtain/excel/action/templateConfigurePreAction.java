/*
 * Created on 2006-5-15
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

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.MessageResources;

import com.cbrc.smis.util.FitechMessages;
import com.fitech.net.common.CommMethod;
import com.fitech.net.common.FileParser;
import com.fitech.net.config.Config;
import com.fitech.net.obtain.excel.SheetHandle;

/**
 * @author Administrator
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class templateConfigurePreAction  extends Action{
	static Logger log = Logger.getLogger(templateConfigurePreAction.class);
	
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
			//将通用的页面元素写入request
			CommMethod.buidPageInfo(request);
			String childRepId = request.getParameter("repID") == null ? "" : request.getParameter("repID").trim();
			String versionId = request.getParameter("versionID");
			String templateName = childRepId + "_" + versionId + ".xls";//ObtainManager.getExcelName(childRepId, versionID);
			
			SheetHandle sheet = new SheetHandle(childRepId, versionId);
			/*
			 * 判断该模板是否在ObtainTemplates目录存在,如果不存在则到templates目录中复制
			 */
			String obtainTemplatesPath = Config.getObtainTemplateFolderRealPath() + File.separator + templateName;
			String templatesPath = Config.getTemplateFolderRealPath() + File.separator + templateName;
			String releaseTemplatePath = Config.getReleaseTemplatePath() + File.separator + templateName;
			if (FileParser.isFileExist(obtainTemplatesPath) == false) {
				FileParser.copyFile(templatesPath, obtainTemplatesPath);
			}
			/*
			 * 判断该模板是否在releaseTemplates目录存在,如果不存在则到templates目录中复制
			 */
			if (FileParser.isFileExist(releaseTemplatePath) == false) {
				FileParser.copyFile(templatesPath, releaseTemplatePath);
			}

			/*
			 *如果是第一次配置则设置该报表的GUID,并检查ｘｌｓ的合法性和配置文件合法性
			 */
			sheet.setDataSourceGuid(obtainTemplatesPath);
			String hrefUrl = CommMethod.getAbsolutePath(request,  "viewTemplateNet.do");
			request.setAttribute("hrefUrl", hrefUrl);
			String reportUrl = CommMethod.getAbsolutePath(request,  ("Reports/ObtainTemplates/" + templateName));			
			request.setAttribute("reportUrl", reportUrl);
			request.setAttribute("repID", childRepId);
			request.setAttribute("versionID", versionId);
			request.setAttribute("reportName", templateName);
			request.setAttribute("saveUrl", CommMethod.getAbsolutePath(request, "obtain/uploadDoc.jsp?repID=" + childRepId + "&versionID=" + versionId));
			
			return mapping.findForward("success");
			
		}
}
