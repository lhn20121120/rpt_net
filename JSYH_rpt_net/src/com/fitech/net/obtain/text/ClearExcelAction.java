package com.fitech.net.obtain.text;

import java.io.File;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.cbrc.smis.util.FitechMessages;
import com.fitech.net.config.Config;


public class ClearExcelAction extends Action{

	public ActionForward execute(ActionMapping mapping, 
			ActionForm form,
			HttpServletRequest request, 
			HttpServletResponse response)
			throws IOException, ServletException {
		
		FitechMessages messages = new FitechMessages();
		//清除releaseTemplates
		String path=Config.getReleaseTemplatePath ();
		File file=new File(path);
	
		File f[]=file.listFiles();
		// System.out.println(f.length);
		for(int i=f.length-1;i>-1;i--)
		{	
			if(f[i].isDirectory())
			{
			File tempf[]=f[i].listFiles();
			for(int j=tempf.length-1;j>-1;j--)
			{
				if(tempf[j].isFile())
				tempf[j].delete();
			}
			}  
            

		  f[i].delete();
                
		}
		
		String message="清除成功";
		request.setAttribute("message",message);
        
		return new ActionForward("/obtain/text/viewformula.do");
	}
}
