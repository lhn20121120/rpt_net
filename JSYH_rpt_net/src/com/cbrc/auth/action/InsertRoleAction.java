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

/**
 * 角色信息添加
 * @author 姚捷
 *
 * @struts.action
 *    path="/struts/insertRole"
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
public final class InsertRoleAction extends Action {
    private static FitechException log = new FitechException(InsertRoleAction.class); 
   /**
    * 已使用Hibernate 卞以刚 2011-12-28
    * 影响对象：Role
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
       
       /**取得request范围内的请求参数，并存放在内*/
       RoleForm roleForm = new RoleForm(); 
       RequestUtils.populate(roleForm, request);
      
       HttpSession session = request.getSession();
       Operator operator = null; 
       if(session.getAttribute(Config.OPERATOR_SESSION_ATTRIBUTE_NAME)!=null)
           operator = (Operator)session.getAttribute(Config.OPERATOR_SESSION_ATTRIBUTE_NAME);

       boolean result = false;
       try{
    	   /**已使用Hibernate 卞以刚 2011-12-28
    	    * 影响对象：Role*/
           if(StrutsRoleDelegate.isExist(roleForm) == false){
        	   roleForm.setSetOrgId(operator.getOrgId());
	           
        	   /**插入记录*/	      
        	   /**已使用hibernate 卞以刚 2011-12-28
        	    * 影响对象：Role*/
        	   result = StrutsRoleDelegate.create(roleForm);
	           
        	   if(result==true)	           
        		   messages.add(FitechResource.getMessage(locale,resources,"role.save.success"));	            
        	   else{ /**错误则返回原页面*/	                
        		   messages.add(FitechResource.getMessage(locale,resources,"save.failed","role.info"));	               
        		   request.setAttribute(Config.MESSAGES,messages);	               
        		   return mapping.findForward("role_add");	            
        	   }           
           }else{ //角色已经存在	       
        	   messages.add(resources.getMessage("save.role.failed.Exist"));					
        	   request.setAttribute(Config.MESSAGES,messages);				
        	   return mapping.findForward("role_add");           
           }       
       }catch (Exception e){          
    	   /**错误则返回原页面*/          
    	   log.printStackTrace(e);          
    	   messages.add(FitechResource.getMessage(locale,resources,"save.failed","role.info"));           
    	   request.setAttribute(Config.MESSAGES,messages);          
    	   return mapping.findForward("role_add");       
       }
              
       if(messages.getMessages() != null && messages.getMessages().size() > 0)       
    	   request.setAttribute(Config.MESSAGES,messages);
       
       return new ActionForward("/popedom_mgr/viewRole.do");   
   }
}
