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
 * �����û���ɫ��Ϣ
 *
 * @author Ҧ��
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
     * ��ʹ��hibernate ���Ը� 2011-12-28
     * Ӱ�����UserRole Operator Role
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
         /**�鿴���û��Ƿ��Ѿ����ý�ɫ*/
         /**��ʹ��hibernate ���Ը� 2011-12-28
          * Ӱ�����UserRole*/
         if(StrutsUserRoleDelegate.isRoleSetting(userId)==true)
         {
             /**������ù���ɫ��ɾ�������Ϣ*/
        	 /**��ʹ��hibernate ���Ը� 2011-12-28
        	  * Ӱ�����UserRole*/
             boolean deleteResult = StrutsUserRoleDelegate.deleteUserSetRole(userId);
             /**ɾ��ʧ������תԭҳ��*/
             if(deleteResult==false)
             {
                 messages.add(FitechResource.getMessage(locale,resources,"save.fail","operator.role.info"));     
                 request.setAttribute(Config.MESSAGES,messages);
                 return mapping.findForward("user_role_setting");
             }
         }
         
         /**��ʹ��hibernate ���Ը� 2011-12-28
          * Ӱ�����UserRole Operator Role*/
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
      /**��ʹ��Hibernate ���Ը� 2011-12-28
       * Ӱ�����Operator*/
      request.setAttribute("userName",StrutsOperatorDelegate.getUserName(userRoleForm.getUserId()));
      return mapping.findForward("view");
   }
 }
