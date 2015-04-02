package com.cbrc.auth.action;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.LabelValueBean;
import org.apache.struts.util.MessageResources;

import com.cbrc.auth.adapter.StrutsProductUserDelegate;
import com.cbrc.auth.form.ProductUserForm;
import com.cbrc.auth.hibernate.ProductUser;
import com.cbrc.smis.common.Config;
import com.cbrc.smis.util.FitechException;
import com.cbrc.smis.util.FitechMessages;
import com.cbrc.smis.util.FitechResource;
import com.cbrc.smis.util.FitechUtil;

/**
 * Displays a productUser. We'll first try to look for a productUser
 * object on the request attribute (which should be set if an insert, 
 * update or select action forwarded to us). If this attribute is not set,
 * we're probably called directly from a page, and we'll look up
 * the person by its id which should be passed as a request parameter.
 *
 * @author gujie
 *
 * @struts.action
 *    path="/struts/viewProductUser"
 *
 * @struts.action-forward
 *    name="view"
 *    path="/struts/viewProductUser.jsp"
 *    redirect="false"
 *

 */
public final class ViewProductUserAction extends Action {
	
	private static FitechException log = new FitechException(ViewProductUserAction.class);  
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
             HttpServletResponse response)
    throws IOException, ServletException 
    {
	   MessageResources resources = getResources(request);
       FitechMessages messages = new FitechMessages();
       Locale locale = request.getLocale();
       
       //取得request范围内的请求参数，并存放在logInForm内
        
       List list=null;
       List productUserList=null;
       ProductUser currUser = null;
       try 
        {
    	   /**所有系统用户集合*/
           list = StrutsProductUserDelegate.findAll();
          // // System.out.println("List size == "+list.size());
           if(list!=null && list.size()!=0)
           {           	 
               productUserList = new ArrayList();
               for(int i=0;i<list.size();i++)
               {
                  ProductUserForm productUserForm =(ProductUserForm)list.get(i);
                  productUserList.add(new LabelValueBean(productUserForm.getProductUserName(),productUserForm.getProductUserId().toString()));
               }
           }
             
           /**当前系统用户*/ 
           currUser = StrutsProductUserDelegate.getCurrentSysUser();
        }
        catch (Exception e) 
        {
            log.printStackTrace(e);
            messages.add(FitechResource.getMessage(locale,resources,"select.fail","dept.info"));      
        }
        //移除request或session范围内的属性
        FitechUtil.removeAttribute(mapping,request);
        
        if(productUserList!=null && productUserList.size()!=0)
            request.setAttribute("productUserList",productUserList);

        if(currUser!=null)
            request.setAttribute("currUser",currUser);
        
        if(messages.getMessages() != null && messages.getMessages().size() > 0)
          request.setAttribute(Config.MESSAGES,messages);
  
        return mapping.findForward("system_user_setting");
   }   

}
