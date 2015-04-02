package com.cbrc.smis.action;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.MessageResources;
import org.apache.struts.util.RequestUtils;

import com.cbrc.smis.adapter.StrutsLogInDelegate;
import com.cbrc.smis.common.Config;
import com.cbrc.smis.form.LogInForm;
import com.cbrc.smis.util.FitechException;
import com.cbrc.smis.util.FitechMessages;
import com.cbrc.smis.util.FitechUtil;

/**
 * @author 姚捷
 * 删除日志
 * 模块名:日志管理 
 * @struts.action
 *    path="/system_mgr/deleteLogIn"
 *
 * @struts.action-forward
 *    name="log_mgr"
 *    path="/system_mgr/log_mgr.jsp"
 *    redirect="false"
 *

 */
public final class DeleteLogInAction extends Action {

   /**
    * 已使用hibernate 卞以刚 2011-12-28
    * 影响对象：LogIn
    * Performs action.
    * @param mapping Action mapping.
    * @param form Action form.
    * @param request HTTP request.
    * @param response HTTP response.
    * @exception IOException if an input/output error occurs
    * @exception ServletException if a servlet exception occurs
    */
	private static FitechException log = new FitechException(DeleteLogInAction.class);
  
	public ActionForward execute(
      ActionMapping mapping,
      ActionForm form,
      HttpServletRequest request,
      HttpServletResponse response
	)
      throws IOException, ServletException {
	   
		MessageResources resources=getResources(request);
	   FitechMessages messages = new FitechMessages();
		
	   LogInForm logInForm = new LogInForm();
	   RequestUtils.populate(logInForm, request);
	   
	   try 
	   {
		   //根据传过来的条件，删除相应的日志
		   /**已使用hibernate 卞以刚 2011-12-28
		    * 影响对象：LogIn*/
		   boolean result = StrutsLogInDelegate.remove(logInForm);
		   if(result==false)
			   messages.add(resources.getMessage("log.delete.fail"));
		   else
			   messages.add(resources.getMessage("log.delete.success"));
	   } 
	   catch (Exception e) 
	   {
		   log.printStackTrace(e);
		   messages.add(resources.getMessage("log.delete.fail"));	   
	   }
	   
	   String path="/system_mgr/viewLogIn.do";
	   	//取得查询条件
	   Integer logTypeId = logInForm.getLogTypeId();
	   String operation = logInForm.getOperation();
	   String userName = logInForm.getUserName();
	   String startDate = logInForm.getStartDate();
	   String endDate = logInForm.getEndDate();
	   
	   if(logTypeId!=null)
	   {
		   path += (path.indexOf("?")>=0 ? "&" : "?");
		   path += "logTypeId="+logTypeId.toString();   
	   }
	   if(operation!=null && !operation.equals(""))
	   {
		   path += (path.indexOf("?")>=0 ? "&" : "?");
		   path += "operation="+operation.toString();   
	   }
	   if(userName!=null && !userName.equals(""))
	   {
		   path += (path.indexOf("?")>=0 ? "&" : "?");
		   path += "userName="+userName.toString();   
	   }
	   if(startDate!=null && !startDate.equals(""))
	   {
		   path += (path.indexOf("?")>=0 ? "&" : "?");
		   path += "startDate="+startDate.toString();   
	   }
	   if(endDate!=null && !endDate.equals(""))
	   {
		   path += (path.indexOf("?")>=0 ? "&" : "?");
		   path += "endDate="+endDate.toString();   
	   }
	   
	   //移除session或request范围内的属性
	   FitechUtil.removeAttribute(mapping,request);
	   //将信息放入request范围内
	   if(messages.getMessages() != null && messages.getMessages().size() > 0)
		   	  request.setAttribute(Config.MESSAGES,messages);
	   return new ActionForward(path);
   }
}
