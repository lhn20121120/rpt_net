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

import com.cbrc.auth.adapter.StrutsDepartmentDelegate;
import com.cbrc.auth.adapter.StrutsOperatorDelegate;
import com.cbrc.auth.form.DepartmentForm;
import com.cbrc.smis.common.Config;
import com.cbrc.smis.util.FitechException;
import com.cbrc.smis.util.FitechMessages;
import com.cbrc.smis.util.FitechResource;

/**
 * ������Ϣɾ��
 *
 * @author Ҧ��
 *
 * @struts.action
 *    path="/struts/deleteDepartment"
 *
 * @struts.action-forward
 *    name="all"
 *    path="/struts/getAll.do"
 *    redirect="false"
 *

 */
public final class DeleteDepartmentAction extends Action {
    private static FitechException log = new FitechException(DeleteDepartmentAction.class);
   /**
    * ��ʹ��hibernate ���Ը� 2011-12-28
    * Ӱ����� Operator Department
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
   throws IOException, ServletException 
   {
       MessageResources resources=getResources(request);
       FitechMessages messages = new FitechMessages();
       Locale locale = request.getLocale();
       
       /**ȡ��request��Χ�ڵ�������������������*/
       DepartmentForm departmentForm = new DepartmentForm(); 
       RequestUtils.populate(departmentForm, request);
       
       /**��ʹ��Hibernate ���Ը� 2011-12-28
        * Ӱ�����Operator*/
       int deptUserNum = StrutsOperatorDelegate.getUserNumFromDeptId(departmentForm.getDepartmentId());
       
       /**�鿴��ǰ�Ƿ����û����ڸû���*/
       
       if(deptUserNum >0)/**������û����ڸû���,������ɾ��*/ 
           messages.add(FitechResource.getMessage(locale,resources,"dept.delete.fail","user.num",String.valueOf(deptUserNum)));
       else/**���û���û����ڸû��������ɾ��*/
       {
           try 
           {
        	   /**��ʹ��hibernate ���Ը� 2011-12-28
        	    * Ӱ�����Department*/
               boolean result = StrutsDepartmentDelegate.remove(departmentForm);
               if(result==false)/**ɾ��ʧ��*/
                    messages.add(FitechResource.getMessage(locale,resources,"delete.failed","dept.info"));
               else/**ɾ���ɹ�*/
                   messages.add(FitechResource.getMessage(locale,resources,"delete.success","dept.info"));  
           } 
           catch (Exception e) 
           {
               log.printStackTrace(e);
               messages.add(FitechResource.getMessage(locale,resources,"delete.failed","dept.info"));
           }         
       }
       if(messages.getMessages() != null && messages.getMessages().size() != 0)
           request.setAttribute(Config.MESSAGES,messages);
       return new ActionForward("/popedom_mgr/viewDepartment.do");
   }
}
