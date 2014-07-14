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
 * 部门信息删除
 *
 * @author 姚捷
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
    * 已使用hibernate 卞以刚 2011-12-28
    * 影响对象： Operator Department
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
       
       /**取得request范围内的请求参数，并存放在内*/
       DepartmentForm departmentForm = new DepartmentForm(); 
       RequestUtils.populate(departmentForm, request);
       
       /**已使用Hibernate 卞以刚 2011-12-28
        * 影响对象：Operator*/
       int deptUserNum = StrutsOperatorDelegate.getUserNumFromDeptId(departmentForm.getDepartmentId());
       
       /**查看当前是否有用户属于该机构*/
       
       if(deptUserNum >0)/**如果有用户属于该机构,则不允许删除*/ 
           messages.add(FitechResource.getMessage(locale,resources,"dept.delete.fail","user.num",String.valueOf(deptUserNum)));
       else/**如果没有用户属于该机构则可以删除*/
       {
           try 
           {
        	   /**已使用hibernate 卞以刚 2011-12-28
        	    * 影响对象：Department*/
               boolean result = StrutsDepartmentDelegate.remove(departmentForm);
               if(result==false)/**删除失败*/
                    messages.add(FitechResource.getMessage(locale,resources,"delete.failed","dept.info"));
               else/**删除成功*/
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
