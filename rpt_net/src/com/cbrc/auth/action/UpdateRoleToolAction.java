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

import com.cbrc.auth.adapter.StrutsRoleToolDelegate;
import com.cbrc.auth.form.RoleToolForm;
import com.cbrc.smis.common.Config;
import com.cbrc.smis.util.FitechMessages;
import com.cbrc.smis.util.FitechResource;

/**
 * ���½�ɫ��Ȩ�� 
 *
 * @author Ҧ��
 *
 * @struts.action
 *    path="/struts/updateRoleTool"
 *    name="roleToolForm"
 *    scope="session"
 *    validate="false"
 *
 * @struts.action-forward
 *    name="view"
 *    path="/struts/viewRoleTool.do"
 *    redirect="false"
 *

 */
public final class UpdateRoleToolAction extends Action {

   /**
    * ��ʹ��hibernate ���Ը� 2011-12-28
    * Ӱ�����RoleTool Role
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
      
      System.out.println(request.getParameter("roleId")); 
      RoleToolForm roleToolForm = (RoleToolForm)form;
      RequestUtils.populate(roleToolForm,request);
      
      if(roleToolForm!=null)
      {
         Long roleId = roleToolForm.getRoleId();
         /**�鿴�ý�ɫ�Ƿ��Ѿ����ù�Ȩ��*/
         /**��ʹ��hibernate ���Ը� 2011-12-28
          * Ӱ�����RoleTool*/
         if(StrutsRoleToolDelegate.getRoleMenuNum(roleId)>0)
         {
             /**������ù�Ȩ����ɾ�������Ϣ*/
        	 /**��ʹ��hibernate ���Ը� 2011-12-28
        	  * Ӱ�����RoleTool*/
             boolean deleteResult = StrutsRoleToolDelegate.deleteFromRoleId(roleId);
             /**ɾ��ʧ������תԭҳ��*/
             if(deleteResult==false)
             {
                 messages.add(FitechResource.getMessage(locale,resources,"save.fail","role.popedom.info"));     
                 request.setAttribute(Config.MESSAGES,messages);
                 return mapping.findForward("role_data_access");
             }
         }
         
         /**��ʹ��hibernate ���Ը� 2011-12-28
          * Ӱ�����RoleTool Role*/
         boolean insertResult = StrutsRoleToolDelegate.insert(roleToolForm);
         if(insertResult==false)
         {
             messages.add(FitechResource.getMessage(locale,resources,"save.fail","role.popedom.info"));     
             request.setAttribute(Config.MESSAGES,messages);
             return mapping.findForward("role_data_access");    
         }
         else
             messages.add(FitechResource.getMessage(locale,resources,"update.success","role.popedom.info"));  
      }
      if(messages.getMessages() != null && messages.getMessages().size() > 0)
          request.setAttribute(Config.MESSAGES,messages);
      return new ActionForward("/popedom_mgr/viewRole.do");
   }
}
