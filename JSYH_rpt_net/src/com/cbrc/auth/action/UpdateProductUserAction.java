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

import com.cbrc.auth.adapter.StrutsProductUserDelegate;
import com.cbrc.auth.form.ProductUserForm;
import com.cbrc.smis.common.Config;
import com.cbrc.smis.util.FitechException;
import com.cbrc.smis.util.FitechMessages;
import com.cbrc.smis.util.FitechResource;
import com.cbrc.smis.util.FitechUtil;

/**
 * 修改当前用户信息
 *
 * @author gujie
 *
 * @struts.action
 *    path="/struts/updateProductUser"
 *    name="productUserForm"
 *    scope="session"
 *    validate="false"
 *
 * @struts.action-forward
 *    name="view"
 *    path="/struts/viewProductUser.do"
 *    redirect="false"
 *

 */
public final class UpdateProductUserAction extends Action {
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
      HttpServletResponse response
   )
      throws IOException, ServletException {
	   MessageResources resources=getResources(request);
       FitechMessages messages = new FitechMessages();
       Locale locale = request.getLocale();
       
       ProductUserForm userForm = new ProductUserForm();
       RequestUtils.populate(userForm,request);
       
       boolean result = false;
       //更新数据库
       try 
        {
            result = StrutsProductUserDelegate.update(userForm); 
            if(result == false)
            {
                messages.add(FitechResource.getMessage(locale,resources,"update.fail","productUser.info"));     
                request.setAttribute(Config.MESSAGES,messages);
                return mapping.findForward("productUser_update");
            }
            else
                messages.add(FitechResource.getMessage(locale,resources,"update.success","productUser.info"));  
        }
        catch (Exception e) 
        {
            log.printStackTrace(e);
            messages.add(FitechResource.getMessage(locale,resources,"update.fail","productUser.info"));     
            request.setAttribute(Config.MESSAGES,messages);
            return mapping.findForward("productUser_update");   
        }
        
        //移除request或session范围内的属性
        FitechUtil.removeAttribute(mapping,request);
           
        if(messages.getMessages() != null && messages.getMessages().size() > 0)
          request.setAttribute(Config.MESSAGES,messages);
        return new ActionForward("/popedom_mgr/viewProductUser.do");
   }
}

