package com.cbrc.fitech.org;

import java.io.IOException;
import java.util.Locale;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.MessageResources;
import org.apache.struts.util.RequestUtils;

import com.cbrc.org.adapter.StrutsMOrgClDelegate;
import com.cbrc.org.form.MOrgClForm;
import com.cbrc.smis.common.Config;
import com.cbrc.smis.util.FitechException;
import com.cbrc.smis.util.FitechMessages;
import com.cbrc.smis.util.FitechUtil;

public class UpdateOrgClsZxkAction extends Action{
	
	 private static FitechException log = new FitechException(UpdateOrgClsZxkAction.class);
	 
	 public ActionForward execute(
		      ActionMapping mapping,
		      ActionForm form,
		      HttpServletRequest request,
		      HttpServletResponse response
		   )throws IOException, ServletException {
		 
		 MessageResources resources=getResources(request);
	     FitechMessages messages = new FitechMessages();
	     Locale locale = request.getLocale();
	     
	     MOrgClForm mOrgClForm = new MOrgClForm();
	     RequestUtils.populate(mOrgClForm,request);
	     
	     boolean result = false;
	     try 
	        {
	            result = StrutsMOrgClDelegate.update2(mOrgClForm); 
	            // System.out.println("The result is" +  result );
	            if(result == false)
	            {
	                messages.add("修改子行分类失败！");     
	                request.setAttribute(Config.MESSAGES,messages);
	                return mapping.findForward("update");
	            }
	            else
	                messages.add("修改子行分类成功！");  
	        }
	        catch (Exception e) 
	        {
	            log.printStackTrace(e);
	            messages.add("修改子行分类失败！");     
	            request.setAttribute(Config.MESSAGES,messages);
	            return mapping.findForward("update");   
	        }
	        
	        //移除request或session范围内的属性
	        FitechUtil.removeAttribute(mapping,request);
	           
	        if(messages.getMessages() != null && messages.getMessages().size() > 0)
	          request.setAttribute(Config.MESSAGES,messages);
	        return new ActionForward("/orgcls/orgCls.do");
	  
	 }

}
