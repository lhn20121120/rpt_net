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
import com.fitech.net.adapter.StrutsTargetDefineDelegate;
import com.fitech.net.form.TargetDefineForm;

public final class EditTargetDefineAction extends Action {
	private static FitechException log=new FitechException(EditTargetDefineAction.class);

   /**
    * Performs action.
    * @param mapping Action mapping.
    * @param form Action form.
    * @param request HTTP request.
    * @param response HTTP response.
    * @exception IOException  是否有IO异常，如有则捕捉并抛出
    * @exception ServletException 是否有Servlet异常，如有则捕捉并抛出
    */
   public ActionForward execute(
      ActionMapping mapping,
      ActionForm form,
      HttpServletRequest request,
      HttpServletResponse response
   )
      throws IOException, ServletException {

	  FitechMessages messages=new FitechMessages();
      TargetDefineForm  targetDefineForm = (TargetDefineForm)form;
      RequestUtils.populate(targetDefineForm, request);
      
      String curPage="";
      if(request.getParameter("curPage")!=null) curPage=(String)request.getParameter("curPage");
      try{
    	  
    	  HttpSession session = request.getSession();						 
    	  Operator operator = null; 
    	  
    	  if(session.getAttribute(Config.OPERATOR_SESSION_ATTRIBUTE_NAME)!=null)  						
    		  operator = (Operator)session.getAttribute(Config.OPERATOR_SESSION_ATTRIBUTE_NAME); 				
							 
    	  targetDefineForm = StrutsTargetDefineDelegate.selectone(targetDefineForm);				
			
    	  if(targetDefineForm != null && targetDefineForm.getSetOrgId() != null){			
    		  if(targetDefineForm.getSetOrgId().trim().equals(operator.getOrgId().trim())){
    			  request.setAttribute(Config.CUR_PAGE_OBJECT,curPage);    	   
    	    	  request.setAttribute("ObjForm",targetDefineForm);
    		  }else{
    			  messages.add("您无权修改该指标！");
    			  targetDefineForm = null;
    		  }
    	  }
    	  
    	  request.setAttribute(Config.CUR_PAGE_OBJECT,curPage);    	   
    	  request.setAttribute("ObjForm",targetDefineForm);
      }
      catch(Exception e){
    	  log.printStackTrace(e);    	  
      }
      
      if (messages != null && messages.getSize() > 0)
          request.setAttribute(Config.MESSAGES, messages);
      
      if(targetDefineForm!=null)
    	   return mapping.findForward("edit");
      else return new ActionForward("/target/viewTargetDefine.do?curPage=" + curPage + "&defineName=&businessId=&normalId=");
   }
}