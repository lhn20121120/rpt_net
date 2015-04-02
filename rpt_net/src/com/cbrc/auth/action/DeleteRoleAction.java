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

import com.cbrc.auth.adapter.StrutsRoleDelegate;
import com.cbrc.auth.adapter.StrutsUserRoleDelegate;
import com.cbrc.auth.form.RoleForm;
import com.cbrc.smis.common.Config;
import com.cbrc.smis.util.FitechException;
import com.cbrc.smis.util.FitechMessages;
import com.cbrc.smis.util.FitechResource;

/**
 * Deletes a role.
 *
 * @author <a href="http://boss.bekk.no/boss/middlegen/">Middlegen</a>
 *
 * @struts.action
 *    path="/struts/deleteRole"
 *
 * @struts.action-forward
 *    name="all"
 *    path="/struts/getAll.do"
 *    redirect="false"
 *

 */
public final class DeleteRoleAction extends Action {
    private static FitechException log = new FitechException(DeleteRoleAction.class);
   /**
    * ��ʹ��hibernate ���Ը� 2011-12-28
    * Ӱ�����UserRole RoleTool Role
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
       
       MessageResources resources = getResources(request);
       FitechMessages messages = new FitechMessages();
       Locale locale = request.getLocale();
       
       /**ȡ��request��Χ�ڵ�������������������*/
       RoleForm roleForm = new RoleForm(); 
       RequestUtils.populate(roleForm, request);
       
       /**��ʹ��hibernate ���Ը� 2011-12-28
        * Ӱ�����UserRole*/
       int roleUserNum = StrutsUserRoleDelegate.getUserNumFromRoleId(roleForm.getRoleId());
       
       /**�鿴��ǰ�Ƿ����û����ڸý�ɫ*/
       
       if(roleUserNum >0)/**������û����ڸû���,������ɾ��*/ 
       {
           messages.add(FitechResource.getMessage(locale,resources,"role.delete.fail","user.num",String.valueOf(roleUserNum)));
           request.setAttribute(Config.MESSAGES,messages);
           return new ActionForward("/popedom_mgr/viewRole.do");
           
       }
       else/**���û���û����ڸý�ɫ�����ɾ��*/
       {
           try 
           {
        	   /**��ʹ��hibernate ���Ը� 2011-12-28
        	    * Ӱ�����RoleTool Role*/
               boolean result = StrutsRoleDelegate.remove(roleForm);
               if(result==false)/**ɾ��ʧ��*/
                   messages.add(FitechResource.getMessage(locale,resources,"delete.failed","role.info"));
               else/**ɾ���ɹ�*/
                   messages.add(FitechResource.getMessage(locale,resources,"delete.success","role.info"));  
           } 
           catch (Exception e) 
           {
               log.printStackTrace(e);
               messages.add(FitechResource.getMessage(locale,resources,"delete.failed","role.info"));
           }         
       }
       if(messages.getMessages() != null && messages.getMessages().size() != 0)
           request.setAttribute(Config.MESSAGES,messages);
       return new ActionForward("/popedom_mgr/viewRole.do");
   }
}
