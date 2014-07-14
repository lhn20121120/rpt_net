package com.cbrc.smis.action;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.cbrc.smis.security.Operator;
import com.cbrc.smis.util.FitechException;
import com.cbrc.smis.util.FitechMessages;
import com.fitech.net.config.Config;
import com.fitech.net.form.TemplateDownForm;
import com.fitech.net.template.util.TemplateDownloadUtil;


/**
 * 用来更新模版,单个,所有,所有最新
 * @author masclnj
 *
 */
public class TempletDownloadAction extends Action { 
	
	private static FitechException log = new FitechException(ViewMChildReportAction.class);	
    
	private static  FitechMessages messages=null;
	 
    //登陆机构
    private static Operator oper=null;
    /** 
     * Method execute
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return ActionForward
     */
    public ActionForward execute(
        ActionMapping mapping,
        ActionForm form,
        HttpServletRequest request,
        HttpServletResponse response)  throws IOException, ServletException{   
    	
    	messages=new FitechMessages();
    	
        TemplateDownForm templateDownForm=(TemplateDownForm)form;                  
        
        String ctl=request.getParameter("ctl");
        oper=(Operator)request.getSession().getAttribute(Config.OPERATOR_SESSION_ATTRIBUTE_NAME);
        
        TemplateDownloadUtil.setMessages(messages);
        TemplateDownloadUtil.setRequest(request);
        TemplateDownloadUtil.setResponse(response);
        TemplateDownloadUtil.setMapping(mapping);
        
        String[] strs=templateDownForm.getStrArray();       
        
        try
        {        
        	if(ctl!=null)
            {
        		if(!"".equals(ctl) && ctl.equals("one"))
        		{
        			String name=templateDownForm.getName();
        			
        			if(name!=null && !"".equals(name))
        			{
        				TemplateDownloadUtil.downOneCopy(name,oper);
        			}
        		}
        		else if(!"".equals(ctl) && ctl.equals("all"))
            	{            		
            		TemplateDownloadUtil.downCopyAll(Config.DOWNLOAD_ALL,response,oper);                     		
            	}
            	else if(!"".equals(ctl) && ctl.equals("allnew"))
            	{
            		TemplateDownloadUtil.downCopyAllNew(Config.DOWNLOAD_NEW,response,oper);
            	}
            } 
        	else
        	{        	    			
            	TemplateDownloadUtil.downCopy(strs,response,oper,Config.DOWNLOAD_SELECT);   		      		
        	}
        }
        catch(Exception e)
        { 
        	e.printStackTrace();
        }                
        if(messages.getAlertMsg().length()!=0)
        {
        	request.setAttribute(Config.MESSAGE,messages);  
        }         
       
        return mapping.findForward("view_list");        
}  
}
