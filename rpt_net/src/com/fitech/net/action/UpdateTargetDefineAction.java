package com.fitech.net.action;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.MessageResources;

import com.cbrc.smis.common.Config;
import com.cbrc.smis.security.Operator;
import com.cbrc.smis.util.FitechException;
import com.cbrc.smis.util.FitechMessages;
import com.cbrc.smis.util.FitechUtil;
import com.fitech.net.adapter.StrutsTargetDefineDelegate;
import com.fitech.net.form.TargetDefineForm;
public final class UpdateTargetDefineAction extends Action {
	private static FitechException log = new FitechException(UpdateTargetDefineAction.class);
   /**
    * @param mapping Action mapping.
    * @param form Action form.
    * @param request HTTP request.
    * @param response HTTP response.
    * @exception IOException是否有输入/输出的异常
    * @exception ServletException是否有servlet的异常占用
    */
 
	public ActionForward execute(
	      ActionMapping mapping,
	      ActionForm form,
	      HttpServletRequest request,
	      HttpServletResponse response
	   )throws IOException, ServletException {
		   		   
		MessageResources resources=this.getResources(request);		
		FitechMessages messages=new FitechMessages();		
		TargetDefineForm targetDefineForm = (TargetDefineForm) form;
								
		String curPage="";					
		if(request.getParameter("curPage")!=null) curPage=(String)request.getParameter("curPage");

		boolean updateResult = false;
					
		try {	
			/***
	         * 取得当前用户的权限信息
	         */   
			HttpSession session = request.getSession();
			Operator operator = null; 
			if(session.getAttribute(Config.OPERATOR_SESSION_ATTRIBUTE_NAME)!=null)  
				operator = (Operator)session.getAttribute(Config.OPERATOR_SESSION_ATTRIBUTE_NAME);  
			
			if (targetDefineForm != null) {
				targetDefineForm.setSetOrgId(operator.getOrgId());
				updateResult = StrutsTargetDefineDelegate.update(targetDefineForm);
				System.out.println("updateResult="+updateResult);
				if (updateResult == true){	//更新成功													
					messages.add("指标定义修改成功！");										   
				}else{	                				
					messages.add("指标定义修改失败！");	
				}			  
			}				
		}catch (Exception e){					
			updateResult=false;							
			messages.add("指标定义修改失败！");								
			log.printStackTrace(e);  
		}	       
		
		if(updateResult==true)	       	
			FitechUtil.removeAttribute(mapping,request);
		
System.out.println(messages.getAlertMsg());		
		//判断有无提示信息，如有将其存储在Request对象中，返回请求			
		if(messages!=null && messages.getSize()>0)			
			request.setAttribute(Config.MESSAGES,messages);

		String path="";
		
		if(updateResult==true){
			form = null;
			path = mapping.findForward("view").getPath() + "?curPage=" + curPage + "&defineName=&businessId=&normalId=" ;
		}else{			
			path = mapping.findForward("edit").getPath() + "?defineName=&businessId=&normalId=";
		}
		return new ActionForward(path);
	}
}