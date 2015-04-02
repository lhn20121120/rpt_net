package com.fitech.net.action;

import java.io.IOException;
import java.util.Locale;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.MessageResources;
import org.apache.struts.util.RequestUtils;

import com.cbrc.smis.common.Config;
import com.cbrc.smis.security.Operator;
import com.cbrc.smis.util.FitechException;
import com.cbrc.smis.util.FitechMessages;
import com.cbrc.smis.util.FitechUtil;
import com.fitech.net.adapter.StrutsTargetRangeDelegate;
import com.fitech.net.form.TargetDefineForm;

/**
 * 
 *
 * @author 
 *
 */
public final class SaveTargetSelectAction extends Action {
	private static FitechException log=new FitechException(SaveTargetSelectAction.class);
	
   /**
    * Performs action.
    * @result 更新成功返回true，否则返回false
    * @reportInForm FormBean的实例化
    * @e Exception 更新失败捕捉异常并抛出
    */
   public ActionForward execute(
      ActionMapping mapping,
      ActionForm form,
      HttpServletRequest request,
      HttpServletResponse response
   )
      throws IOException, ServletException {
	   
	   Locale locale = getLocale(request);
	   MessageResources resources = getResources(request);
	   FitechMessages messages = new FitechMessages();
	   boolean result=false;
		
	   TargetDefineForm targetDefineForm = (TargetDefineForm)form;
	   RequestUtils.populate(targetDefineForm, request);
	   
	   HttpSession session=request.getSession();
	   Operator operator=(Operator)session.getAttribute(Config.OPERATOR_SESSION_NAME);
	   
	   String orgId=operator.getOrgId();
	//   String orgId=targetDefineForm.getSetOrgId();
	   String curPage="";		
	   if(request.getParameter("curPage")!=null) curPage=(String)request.getParameter("curPage");
		
		
	   String targetIds=request.getParameter("targetIds");
		
	   if(targetIds!=null && !targetIds.equals("")){		
		   String[] arr=targetIds.split(Config.SPLIT_SYMBOL_COMMA);

		   for(int i=0;i<arr.length;i++){	
			   int temp=arr[i].indexOf("|");
				String flag=arr[i].substring(temp+1,arr[i].length());
				String targetId=arr[i].substring(0,temp);

			   if(flag.trim().equals("0")){
				   if(StrutsTargetRangeDelegate.Exit(orgId,Integer.valueOf(targetId.trim())))													
					   result=	StrutsTargetRangeDelegate.delete(orgId,Integer.valueOf(targetId.trim()));							
				   else result = true;
			   }else{				
				   if(!StrutsTargetRangeDelegate.Exit(orgId,Integer.valueOf(targetId.trim())))													
					   result=StrutsTargetRangeDelegate.add(orgId,Integer.valueOf(targetId.trim()));						
				   else result = true;					
			   }
					//	 result=StrutsTargetDefineDelegate.UpdateFlag(arr[i].substring(0,temp),arr[i].substring(temp+1,arr[i].length()));					
		   }		 
	   }
		
	   if(result) messages.add("指标设定保存成功！");		  
	   else messages.add("指标设定保存失败！");
		  
	   //判断有无提示信息，如有将其存储在Request对象中，返回请求
	   FitechUtil.removeAttribute(mapping,request);	   
		
	   if(messages!=null && messages.getSize()>0)		
		   request.setAttribute(Config.MESSAGES,messages);
		   
	   String path="";	
	   path = mapping.findForward("view").getPath() + "?curPage=" + curPage ;
	   
	   return new ActionForward(path);   
   }
}

