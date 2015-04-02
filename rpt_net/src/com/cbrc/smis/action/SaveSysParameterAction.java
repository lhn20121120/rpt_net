package com.cbrc.smis.action;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.cbrc.smis.adapter.StrutsSysSetDelegate;
import com.cbrc.smis.form.SysParameterForm;
import com.cbrc.smis.util.FitechException;

/**
 * 保存系统参数
 * 
 * @author wh
 * 
 */
public class SaveSysParameterAction extends Action
{
	private FitechException log = new FitechException(SaveSysParameterAction.class);

	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException
	{
		
		boolean result = false ;

		try
		{
			
			SysParameterForm sysParameterForm = new SysParameterForm();
			
			String parType = request.getParameter("parType");
			String parName = request.getParameter("parName");
			Integer parValue = new Integer(request.getParameter("parValue"));
			
			if (parType != null && !parType.equals(""))
				parType = java.net.URLDecoder.decode(parType, "UTF-8");
			if (parName != null && !parName.equals(""))
				parName = java.net.URLDecoder.decode(parName, "UTF-8");
			
			 result = StrutsSysSetDelegate.updateSysParameter(parType,parName,parValue);
		     if(result){
		    	 if(parName.trim().equals("BN_VALIDATE"))
		    	 {
		    		 com.cbrc.smis.common.Config.SYS_BN_VALIDATE=parValue;
		    	 }else if(parName.trim().equals("BJ_VALIDATE"))
		    	 {
			    		 com.cbrc.smis.common.Config.SYS_BJ_VALIDATE=parValue;
		    	 }else if(parName.trim().equals("UP_VALIDATE_BN"))
		    	 {
		    		 com.cbrc.smis.common.Config.UP_VALIDATE_BN=parValue;
	    	 }else if(parName.trim().equals("UP_VALIDATE_BJ"))
	    	 {
	    		 com.cbrc.smis.common.Config.UP_VALIDATE_BJ=parValue;
	    	 }else if(parName.trim().equals("ENCRYPT")){
	    		 com.cbrc.smis.common.Config.ENCRYPT=parValue;
	    		 
	    	 }else if(parName.trim().equals("UP_BATCH_VALIDATE")){
	    		 com.cbrc.smis.common.Config.UP_BATCH_VALIDATE = parValue;
	    	 }
		     }
		}
		catch (Exception e)
		{
			e.printStackTrace();
			result = false;
		}

		PrintWriter out = response.getWriter();

		response.setContentType("text/xml");
		response.setHeader("Cache-control", "no-cache");

		out.println("<response><result>" + result + "</result></response>");
		out.close();

		return null;
	}
}
