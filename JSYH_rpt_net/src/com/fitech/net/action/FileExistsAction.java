package com.fitech.net.action;

import java.io.File;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class FileExistsAction extends Action {

	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String fileName=request.getParameter("fileName");
		Integer result=0;
		if(fileName!=null && !fileName.equals("")){
			File file=new File(fileName);
			if(!file.exists())
				result=1;
		}
		response.getWriter().print(result);
		return null;
	}
	
}
