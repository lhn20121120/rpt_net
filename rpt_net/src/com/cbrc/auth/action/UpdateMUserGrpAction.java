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

import com.cbrc.auth.adapter.StrutsMUserGrpDelegate;
import com.cbrc.auth.form.MUserGrpForm;
import com.cbrc.smis.common.Config;
import com.cbrc.smis.util.FitechException;
import com.cbrc.smis.util.FitechMessages;
import com.cbrc.smis.util.FitechResource;
import com.cbrc.smis.util.FitechUtil;

/**
 * 修改用户组信息
 *
 * @author 姚捷
 *
 * @struts.action
 *    path="/struts/updateMUserGrp"
 *    name="MUserGrpForm"
 *    scope="session"
 *    validate="false"
 *
 * @struts.action-forward
 *    name="view"
 *    path="/struts/viewMUserGrp.do"
 *    redirect="false"
 */
public final class UpdateMUserGrpAction extends Action {
    private static FitechException log = new FitechException(UpdateMUserGrpAction.class);
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
       
       MUserGrpForm mUserGrpForm = new MUserGrpForm();
       RequestUtils.populate(mUserGrpForm,request);             
       boolean result = false;
       
       try 
        { 
       	    if(StrutsMUserGrpDelegate.isExist(mUserGrpForm) == false){
	            result = StrutsMUserGrpDelegate.update(mUserGrpForm); 
	       

	            if(result == false){
	                messages.add(FitechResource.getMessage(locale,resources,"update.failed","userGrp.info"));     
	                request.setAttribute(Config.MESSAGES,messages);
	                request.setAttribute("userGrpNm",mUserGrpForm.getoldUserGrpName());
	                return new ActionForward("/popedom_mgr/user_group_mgr/user_group_update.jsp?userGrpNm="+mUserGrpForm.getoldUserGrpName());
	            }
	            else
	                messages.add(FitechResource.getMessage(locale,resources,"update.success","userGrp.info"));
       	    }else{
     	    	
       	    	messages.add(resources.getMessage("save.musergrp.failed.Exist"));	
				request.setAttribute(Config.MESSAGES,messages);
				return mapping.findForward("user_group_update");
       	    }
        }
        catch (Exception e) 
        {
 
            log.printStackTrace(e);
            messages.add(FitechResource.getMessage(locale,resources,"update.failed","userGrp.info"));     
            request.setAttribute(Config.MESSAGES,messages);
            return mapping.findForward("user_group_update");   
        }
 
        //移除request或session范围内的属性
        FitechUtil.removeAttribute(mapping,request);
           
        if(messages.getMessages() != null && messages.getMessages().size() > 0)
          request.setAttribute(Config.MESSAGES,messages);
        return new ActionForward("/popedom_mgr/viewMUserGrp.do");
   }
}
