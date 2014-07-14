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

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.upload.FormFile;

import com.fitech.net.common.CommMethod;
import com.fitech.net.common.FileParser;
import com.fitech.net.config.Config;
import com.fitech.net.obtain.excel.SheetHandle;
import com.fitech.net.obtain.excel.dao.ObtainManager;
import com.fitech.net.obtain.excel.form.FileForm;

/**
 * @author Administrator
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class templateConfigureAction  extends Action{
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

			/*
			 *上传文件
			 */
			FileForm mForm = (FileForm)form;
			FormFile file = mForm.getFile();
			ObtainManager om = new ObtainManager();
			String tmpFileName = String.valueOf(System.currentTimeMillis()) + ".xls";
			om.uploadFile(file, tmpFileName);
			String repID = mForm.getRepID();
			String versionID = mForm.getVersionID();
			String templateName = mForm.getReportName();
			
			/*
			 * sheet拷贝
			 */
			String srcName = Config.getTemplateTempFolderRealPath() + File.separator + tmpFileName;
			String objName = Config.getObtainTemplateFolderRealPath() + File.separator + templateName;
			
			SheetHandle sheet = new SheetHandle(repID, versionID);
			sheet.copySheet(tmpFileName, objName, file.getFileName());
			//删除临时文件
			FileParser.deleteFile(srcName);
			
			//将通用的页面元素写入request
			CommMethod.buidPageInfo(request);
			String hrefUrl = CommMethod.getAbsolutePath(request,  "viewTemplateNet.do");
			request.setAttribute("hrefUrl", hrefUrl);			
			String reportUrl = CommMethod.getAbsolutePath(request,  ("Reports/ObtainTemplates/" + templateName));			
			request.setAttribute("reportUrl", reportUrl);
			request.setAttribute("repID", repID);
			request.setAttribute("versionID", versionID);
			request.setAttribute("reportName", templateName);
			request.setAttribute("saveUrl", CommMethod.getAbsolutePath(request, "obtain/uploadDoc.jsp?repID=" + repID + "&versionID=" + versionID));
			
			return mapping.findForward("success");
		}
		
	
		
}
