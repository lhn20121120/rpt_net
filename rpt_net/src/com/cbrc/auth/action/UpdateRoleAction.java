package com.cbrc.auth.action;

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

import com.cbrc.auth.adapter.StrutsRoleDelegate;
import com.cbrc.auth.form.RoleForm;
import com.cbrc.smis.common.Config;
import com.cbrc.smis.security.Operator;
import com.cbrc.smis.util.FitechException;
import com.cbrc.smis.util.FitechMessages;
import com.cbrc.smis.util.FitechResource;
import com.cbrc.smis.util.FitechUtil;

/**
 * 角色信息修改
 *
 * @author 姚捷
 *
 * @struts.action
 *    path="/struts/updateRole"
 *    name="roleForm"
 *    scope="session"
 *    validate="false"
 *
 * @struts.action-forward
 *    name="view"
 *    path="/struts/viewRole.do"
 *    redirect="false"
 *

 */
public final class UpdateRoleAction extends Action {
    private static FitechException log = new FitechException(UpdateRoleAction.class); 
   /**
    * Performs action.
    * @param mapping Action mapping.
    * @param form Action form.
    * @param request HTTP request.
    * @param response HTTP response.
    * @exception IOException if an input/output error occurs
    * @exception ServletException if a servlet exception occurs
    */
   public ActionForward execute(
      ActionMapping mapping,
      ActionForm form,
      HttpServletRequest request,
      HttpServletResponse response
   )
      throws IOException, ServletException {
       MessageResources resources=getResources(request);
       FitechMessages messages = new FitechMessages();
       Locale locale = request.getLocale();
       
       RoleForm roleForm = new RoleForm();
       RequestUtils.populate(roleForm,request);
       
       HttpSession session = request.getSession();
       Operator operator = null; 
       if(session.getAttribute(Config.OPERATOR_SESSION_ATTRIBUTE_NAME)!=null)
           operator = (Operator)session.getAttribute(Config.OPERATOR_SESSION_ATTRIBUTE_NAME);
       
       boolean result = false;       
       try{       	    
    	   if(StrutsRoleDelegate.isExist(roleForm) == false){	       
    		   result = StrutsRoleDelegate.update(roleForm); 
	           
    		   if(result == false){	           
    			   messages.add(FitechResource.getMessage(locale,resources,"update.fail","role.info"));     	               
    			   request.setAttribute(Config.MESSAGES,messages);	               
    			   return mapping.findForward("role_update");	            
    		   }else	           
    			   messages.add(FitechResource.getMessage(locale,resources,"update.success","role.info"));         	
    	   }else{        	
    		   messages.add(resources.getMessage("save.role.failed.Exist"));					
    		   request.setAttribute(Config.MESSAGES,messages);				
    		   return mapping.findForward("role_update");      	
    	   }        
       }catch (Exception e){       
    	   log.printStackTrace(e);           
    	   messages.add(FitechResource.getMessage(locale,resources,"update.fail","role.info"));                
    	   request.setAttribute(Config.MESSAGES,messages);           
    	   return mapping.findForward("role_update");           
       }
        
       //移除request或session范围内的属性       
       FitechUtil.removeAttribute(mapping,request);
       
       if(messages.getMessages() != null && messages.getMessages().size() > 0)       
    	   request.setAttribute(Config.MESSAGES,messages);
        
       return new ActionForward("/popedom_mgr/viewRole.do");   
   }
}
