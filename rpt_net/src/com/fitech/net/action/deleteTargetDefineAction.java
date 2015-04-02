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
import org.apache.struts.util.RequestUtils;

import com.cbrc.smis.common.Config;
import com.cbrc.smis.security.Operator;
import com.cbrc.smis.util.FitechException;
import com.cbrc.smis.util.FitechMessages;
import com.fitech.net.adapter.StrutsActuTargetResultDelegate;
import com.fitech.net.adapter.StrutsTargetDefineDelegate;
import com.fitech.net.form.TargetDefineForm;

public final class deleteTargetDefineAction extends Action {
	private static FitechException log = new FitechException(deleteTargetDefineAction.class);

	 public ActionForward execute(ActionMapping mapping,ActionForm form,
			 HttpServletRequest request,HttpServletResponse response)throws IOException, ServletException{

		// MessageResources resources=this.getResources(request);		   
		 FitechMessages messages=new FitechMessages();
		    
		 TargetDefineForm targetDefineForm=new TargetDefineForm();		 
		 RequestUtils.populate(targetDefineForm, request);		    
		 
		 String curPage="";	      
		 if(request.getParameter("curPage")!=null) curPage=(String)request.getParameter("curPage");
	      
		 try{			 
			 /***
		      * 取得当前用户的权限信息
		      */				
			 HttpSession session = request.getSession();			
			 Operator operator = null; 
			
			 if(session.getAttribute(Config.OPERATOR_SESSION_ATTRIBUTE_NAME)!=null)  			
				 operator = (Operator)session.getAttribute(Config.OPERATOR_SESSION_ATTRIBUTE_NAME); 				
				
			 TargetDefineForm tForm = StrutsTargetDefineDelegate.selectone(targetDefineForm);
				
			 if(tForm != null && tForm.getSetOrgId() != null){
				 if(tForm.getSetOrgId().trim().equals(operator.getOrgId().trim())){
					 if(StrutsActuTargetResultDelegate.remove(targetDefineForm) == true){
						    
						 if(StrutsTargetDefineDelegate.remove(targetDefineForm))
							 messages.add("删除指标定义成功");
						 else
							 messages.add("删除指标定义失败");
					 }else{		    
						 messages.add("删除指标定义失败");		    	
					 }		    
				 }else{
					 messages.add("您无权删除该指标！");
					 if (messages != null && messages.getSize() > 0)
						 request.setAttribute(Config.MESSAGES, messages);
					 return new ActionForward("/target/viewTargetDefine.do?curPage=" + curPage + "&defineName=&businessId=&normalId=");
				 }
			 }
			 			    
		 }catch(Exception e){		 
			 log.printStackTrace(e);		    
			 messages.add("删除指标定义失败");		 
		 }
		 
		 if (messages != null && messages.getSize() > 0)	           
			 request.setAttribute(Config.MESSAGES, messages);
		 
		 if(targetDefineForm.getDefineName()!=null &&  targetDefineForm.getDefineName().equals(""))		 
			 request.setAttribute("defineName",targetDefineForm.getDefineName());
		 
		 return mapping.findForward("view");	 
	 }
}