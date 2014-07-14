package com.cbrc.auth.action;

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

import com.cbrc.auth.adapter.StrutsOperatorDelegate;
import com.cbrc.auth.adapter.StrutsUserRoleDelegate;
import com.cbrc.auth.form.UserRoleForm;
import com.cbrc.smis.common.Config;
import com.cbrc.smis.util.FitechMessages;
import com.cbrc.smis.util.FitechResource;

/**
 * 更新用户角色信息
 *
 * @author 姚捷
 *
 * @struts.action
 *    path="/struts/updateUserRole"
 *    name="userRoleForm"
 *    scope="session"
 *    validate="false"
 *
 * @struts.action-forward
 *    name="view"
 *    path="/struts/viewUserRole.do"
 *    redirect="false"
 *

 */
public final class UpdateUserRoleAction extends Action {

    /**
     * 已使用hibernate 卞以刚 2011-12-28
     * 影响对象：UserRole Operator Role
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
       
       UserRoleForm userRoleForm = (UserRoleForm)form;
       RequestUtils.populate(userRoleForm,request);
       String curPage=request.getParameter("curPage");   
      if(userRoleForm!=null)
      {
         Long userId = userRoleForm.getUserId();
         /**查看该用户是否已经设置角色*/
         /**已使用hibernate 卞以刚 2011-12-28
          * 影响对象：UserRole*/
         if(StrutsUserRoleDelegate.isRoleSetting(userId)==true)
         {
             /**如果设置过角色则删除相关信息*/
        	 /**已使用hibernate 卞以刚 2011-12-28
        	  * 影响对象：UserRole*/
             boolean deleteResult = StrutsUserRoleDelegate.deleteUserSetRole(userId);
             /**删除失败则跳转原页面*/
             if(deleteResult==false)
             {
                 messages.add(FitechResource.getMessage(locale,resources,"save.fail","operator.role.info"));     
                 request.setAttribute(Config.MESSAGES,messages);
                 return mapping.findForward("user_role_setting");
             }
         }
         
         /**已使用hibernate 卞以刚 2011-12-28
          * 影响对象：UserRole Operator Role*/
         boolean insertResult = StrutsUserRoleDelegate.insert(userRoleForm);
         if(insertResult==false)
         {
             messages.add(FitechResource.getMessage(locale,resources,"save.fail","operator.role.info"));     
             request.setAttribute(Config.MESSAGES,messages);
             return mapping.findForward("user_role_setting");    
         }
         else
             messages.add(FitechResource.getMessage(locale,resources,"save.success","operator.role.info"));  
      }
      if(messages.getMessages() != null && messages.getMessages().size() > 0)
          request.setAttribute(Config.MESSAGES,messages);
      request.setAttribute("curPage",curPage);
      /**已使用Hibernate 卞以刚 2011-12-28
       * 影响对象：Operator*/
      request.setAttribute("userName",StrutsOperatorDelegate.getUserName(userRoleForm.getUserId()));
      return mapping.findForward("view");
   }
 }
