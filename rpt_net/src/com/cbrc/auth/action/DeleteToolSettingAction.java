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
import com.cbrc.auth.adapter.StrutsToolSettingDelegate;
import com.cbrc.auth.form.ToolSettingForm;
import com.cbrc.smis.common.Config;
import com.cbrc.smis.util.FitechException;
import com.cbrc.smis.util.FitechMessages;
import com.cbrc.smis.util.FitechResource;

/**
 * 删除菜单
 *
 * @author gujie 
 *
 * @struts.action
 *    path="/struts/deleteToolSetting"
 *
 * @struts.action-forward
 *    name="all"
 *    path="/struts/getAll.do"
 *    redirect="false"
 *

 */
public final class DeleteToolSettingAction extends Action {
	   private static FitechException log = new FitechException(DeleteToolSettingAction.class);
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
	   
       MessageResources resources = getResources(request);
       FitechMessages messages = new FitechMessages();
       Locale locale = request.getLocale();
       
       /**从request里面取出参数，放在里面*/
       ToolSettingForm toolSettingForm = new ToolSettingForm();
       RequestUtils.populate(toolSettingForm,request);
       
       try{
          /**取得该菜单是否有角色设置过，如果设置过，则不允许删除*/
           int roleNum = StrutsRoleToolDelegate.getMenuRoleNum(toolSettingForm.getMenuId());
           if(roleNum>0)
           {
               messages.add(FitechResource.getMessage(locale,resources,"toolSetting.delete.fail","user.num",String.valueOf(roleNum)));
               request.setAttribute(Config.MESSAGES,messages);
               return new ActionForward("/popedom_mgr/viewToolSetting.do");
           }
          
    	   boolean result = StrutsToolSettingDelegate.remove(toolSettingForm);
    	   if(result==false)/**删除失败*/
               messages.add(FitechResource.getMessage(locale,resources,"delete.failed","toolSetting.info"));
           else/**删除成功*/
               messages.add(FitechResource.getMessage(locale,resources,"delete.success","toolSetting.info"));
       }
       catch(Exception e){
    	   log.printStackTrace(e);
    	   messages.add(FitechResource.getMessage(locale,resources,"delete.failed","toolSetting.info"));
    	   
       }
       if(messages.getMessages() != null && messages.getMessages().size() != 0)
       request.setAttribute(Config.MESSAGES,messages);
       return new ActionForward("/popedom_mgr/viewToolSetting.do");
   }
  }
