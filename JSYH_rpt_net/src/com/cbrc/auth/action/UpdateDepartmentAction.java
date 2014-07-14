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

import com.cbrc.auth.adapter.StrutsDepartmentDelegate;
import com.cbrc.auth.form.DepartmentForm;
import com.cbrc.smis.common.Config;
import com.cbrc.smis.security.Operator;
import com.cbrc.smis.util.FitechException;
import com.cbrc.smis.util.FitechMessages;
import com.cbrc.smis.util.FitechResource;

/**
 * 修改部门信息
 *
 * @author 姚捷
 *
 * @struts.action
 *    path="/popedom_mgr/updateDepartment"
 *    name="departmentForm"
 *    scope="request"
 *    validate="false"
 *
 * @struts.action-forward
 *    name="view"
 *    path="/struts/viewDepartment.do"
 *    redirect="false"
 *

 */
public final class UpdateDepartmentAction extends Action {
    private static FitechException log = new FitechException(UpdateDepartmentAction.class); 
   /**
    * 已使用hibernate 卞以刚 2011-12-28
    * 影响对象：Department
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
       
       DepartmentForm deptForm = new DepartmentForm();
       RequestUtils.populate(deptForm,request);
       
       HttpSession session = request.getSession();       
   	   com.cbrc.smis.security.Operator operator = null; 
      
   	   if(session.getAttribute(Config.OPERATOR_SESSION_ATTRIBUTE_NAME)!=null)
   		   operator = (Operator)session.getAttribute(Config.OPERATOR_SESSION_ATTRIBUTE_NAME);
   	
       boolean result = false;
       
       try 
        {
    	   /**已使用hibernate 卞以刚 2011-12-27
    	    * 影响对象：Department*/
       	    if(StrutsDepartmentDelegate.isExist(deptForm,operator) == false){
       	    	/**已使用hibernate 卞以刚 2011-12-28
       	    	 * 影响对象：Department*/
	            result = StrutsDepartmentDelegate.update(deptForm); 
	            if(result == false)
	            {
	                messages.add(FitechResource.getMessage(locale,resources,"update.failed","dept.info"));     
	                request.setAttribute(Config.MESSAGES,messages);
	                return mapping.findForward("dept_update");
	            }
	            else
	                messages.add(FitechResource.getMessage(locale,resources,"update.success","dept.info")); 
        	}else{
        		messages.add(resources.getMessage("save.failed.Exist"));
        		request.setAttribute(Config.MESSAGES,messages); 
                return new ActionForward("/popedom_mgr/viewDepartment.do");   
        	}
        }
        catch (Exception e) 
        {
            log.printStackTrace(e);
            messages.add(FitechResource.getMessage(locale,resources,"update.failed","dept.info"));     
            request.setAttribute(Config.MESSAGES,messages);
            return mapping.findForward("dept_update");   
        }
        
        //移除request或session范围内的属性
        //FitechUtil.removeAttribute(mapping,request);
           
        if(messages.getMessages() != null && messages.getMessages().size() > 0)
          request.setAttribute(Config.MESSAGES,messages);
        return new ActionForward("/popedom_mgr/viewDepartment.do");
   }
}
