package com.fitech.gznx.action;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Blob;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.cbrc.smis.common.Config;
import com.cbrc.smis.util.FitechException;
import com.fitech.gznx.po.AFFileInfo;
import com.fitech.gznx.service.StrutsFileInfoDelegate;



public class DownLoadBolbFileAction extends Action {
	private static FitechException log = new FitechException(AddPlacardAction.class);
	
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response){
		InputStream bin = null;
		OutputStream bout = null;
		try{
			String fileId=request.getParameter("fileId");
			if(fileId!=null){
				AFFileInfo fileinfo = StrutsFileInfoDelegate.load(new Integer(fileId));
				String fileName=fileinfo.getFileName();
				String filePath=Config.WEBROOTPATH  +"temp"+Config.FILESEPARATOR+String.valueOf(System.currentTimeMillis());
				Blob blob = fileinfo.getFileContent();
				
				/* 读文件 */
				bin = blob.getBinaryStream();
				/* 写文件 */
				bout = new FileOutputStream(filePath);

				byte[] line = new byte[((Long)blob.length()).intValue()];
				bin.read(line);
				bout.write(line);
				String url = "/servlets/DownloadServlet?filePath=" + filePath + "&fileName=" + fileName+"&deleteFile=1";
				
				return new ActionForward(url);
			}
			

        }catch (Exception e) 
        {
            log.printStackTrace(e);  
            return new ActionForward("/error.jsp");
        }finally{
        	try {
				bin.close();
				bout.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
        }
        
        return mapping.findForward("view");
		
		
	}

}
