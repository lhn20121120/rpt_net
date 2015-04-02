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

import com.cbrc.auth.adapter.StrutsToolSettingDelegate;
import com.cbrc.auth.form.ToolSettingForm;
import com.cbrc.smis.common.Config;
import com.cbrc.smis.util.FitechException;
import com.cbrc.smis.util.FitechMessages;
import com.cbrc.smis.util.FitechResource;
import com.cbrc.smis.util.FitechUtil;

/**
 * 更新菜单信息
 *
 * @author gujie
 *
 * @struts.action
 *    path="/struts/updateToolSetting"
 *    name="toolSettingForm"
 *    scope="session"
 *    validate="false"
 *
 * @struts.action-forward
 *    name="view"
 *    path="/struts/viewToolSetting.do"
 *    redirect="false"
 *

 */
public final class UpdateToolSettingAction extends Action {
    private static FitechException log = new FitechException(UpdateRoleAction.class);
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
       
       ToolSettingForm toolSettingForm = new ToolSettingForm();
       RequestUtils.populate(toolSettingForm,request);
       
       boolean result = false;
       
       try 
        {
	       	if(StrutsToolSettingDelegate.isExist(toolSettingForm) == false){
	            result = StrutsToolSettingDelegate.update(toolSettingForm); 
	            if(result == false)
	            {
	                messages.add(FitechResource.getMessage(locale,resources,"update.fail","toolSetting.info"));     
	                request.setAttribute(Config.MESSAGES,messages);
	                return mapping.findForward("menu_update");
	            }
	            else
	                messages.add(FitechResource.getMessage(locale,resources,"update.success","toolSetting.info"));  
	        }else{
	        	messages.add(resources.getMessage("save.menu.failed.Exist"));	
				request.setAttribute(Config.MESSAGES,messages);
				return mapping.findForward("menu_update");
	        }
   		}catch (Exception e){
            log.printStackTrace(e);
            messages.add(FitechResource.getMessage(locale,resources,"update.fail","toolSetting.info"));     
            request.setAttribute(Config.MESSAGES,messages);
            return mapping.findForward("menu_update");   
        }
        
        //移除request或session范围内的属性
        FitechUtil.removeAttribute(mapping,request);
           
        if(messages.getMessages() != null && messages.getMessages().size() > 0)
          request.setAttribute(Config.MESSAGES,messages);
        return new ActionForward("/popedom_mgr/viewToolSetting.do");
   }
}
