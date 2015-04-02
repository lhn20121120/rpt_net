/*
 * Created on 2005-12-21
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.cbrc.smis.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.MessageResources;

import com.cbrc.smis.adapter.StrutsInfoFilesDelegate;
import com.cbrc.smis.common.Config;
import com.cbrc.smis.form.InfoFilesForm;
import com.cbrc.smis.util.FitechMessages;

/**
 * @author 曹发根
 *删除文件
 */
public class DeleteInfoFilesAction extends Action {
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
	   	
        MessageResources resources = getResources(request);
        FitechMessages messages = new FitechMessages();
        
        InfoFilesForm infoFilesForm=(InfoFilesForm)form;
        
        String path = "/system_mgr/viewOutFile.do";
		try {
			infoFilesForm=StrutsInfoFilesDelegate.find(infoFilesForm.getInfoFilesId());
			            
			if(infoFilesForm != null){
				
				if(infoFilesForm.getInfoFileStyle().equals(Config.INFO_FILES_STYLE_UP))
					path = "/viewUpInfoFiles.do";
				
				if(StrutsInfoFilesDelegate.delete(infoFilesForm.getInfoFilesId())) 
	            {
	                // System.out.println("数据库删除成功！");
	                messages.add(resources.getMessage("info_files.delete.success"));
	                request.setAttribute(Config.MESSAGES,messages);   
	                
	            }
	            else{
	                messages.add(resources.getMessage("info_files.delete.failed"));
	                request.setAttribute(Config.MESSAGES,messages);       
	            }
			}else{
				messages.add(resources.getMessage("info_files.delete.failed"));
                request.setAttribute(Config.MESSAGES,messages);   
			}				
		} catch (Exception e) {
            messages.add(resources.getMessage("info_files.delete.failed"));
            request.setAttribute(Config.MESSAGES,messages);   
			e.printStackTrace();
		}
		return new ActionForward(path);

	}

}