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

import com.cbrc.auth.adapter.StrutsMUserToGrpDelegate;
import com.cbrc.auth.adapter.StrutsOperatorDelegate;
import com.cbrc.auth.form.MUserToGrpForm;
import com.cbrc.smis.common.Config;
import com.cbrc.smis.util.FitechMessages;
import com.cbrc.smis.util.FitechResource;

/**
 * Updates a MUserToGrp.
 *
 * @author <a href="http://boss.bekk.no/boss/middlegen/">Middlegen</a>
 *
 * @struts.action
 *    path="/struts/updateMUserToGrp"
 *    name="MUserToGrpForm"
 *    scope="session"
 *    validate="false"
 *
 * @struts.action-forward
 *    name="view"
 *    path="/struts/viewMUserToGrp.do"
 *    redirect="false"
 *

 */
public final class UpdateMUserToGrpAction extends Action {

   /**
    * 已使用Hibernate 卞以刚 2011-12-28
    * 影响对象：MUserToGrp Operator MUserGrp
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
      
      String curPage=request.getParameter("curPage");
      MUserToGrpForm mUserToGrpForm = (MUserToGrpForm)form;
      RequestUtils.populate(mUserToGrpForm,request);
      boolean deleteResult=true;
     if(mUserToGrpForm!=null)
     {
        Long userId = mUserToGrpForm.getUserId();
        /**查看该用户是否已经设置用户组*/
        /**已使用hibernate 卞以刚 2011-12-28
         * 影响对象：MUserToGrp*/
        if(StrutsMUserToGrpDelegate.isUserGrpSetting(userId)==true)
        {
            /**如果设置过用户组则删除相关信息*/
        	/**已使用Hibernate 卞以刚 2011-12-28
        	 * 影响对象：MUserToGrp*/
             deleteResult = StrutsMUserToGrpDelegate.deleteUserSetUserGrp(userId);
            /**删除失败则跳转原页面*/
            if(deleteResult==false)
            {
                messages.add(FitechResource.getMessage(locale,resources,"save.fail","operator.info"));     
                request.setAttribute(Config.MESSAGES,messages);
                return mapping.findForward("user_userGrp_setting");
            }
        }
        
        /**已使用hibernate 卞以刚 2011-12-28
         * 影响对象：Operator MUserGrp MUserToGrp*/
        //mod by wmm,当给用户选择的用户组为空时
        if(mUserToGrpForm.getSelectedUserGrpIds()!=null&&!"".equals(mUserToGrpForm.getSelectedUserGrpIds())){
        	
        	boolean insertResult = StrutsMUserToGrpDelegate.insert(mUserToGrpForm);
        	if(insertResult==false)
        	{
        		messages.add(FitechResource.getMessage(locale,resources,"save.fail","operator.info"));     
        		request.setAttribute(Config.MESSAGES,messages);
        		return mapping.findForward("user_userGrp_setting");    
        	}
        	else
        		messages.add(FitechResource.getMessage(locale,resources,"save.success","operator.info"));  
        }else{
        	if(deleteResult==true){
        		messages.add(FitechResource.getMessage(locale,resources,"save.success","operator.info"));  
        	}
        }
        }
     if(messages.getMessages() != null && messages.getMessages().size() > 0)
         request.setAttribute(Config.MESSAGES,messages);

     request.setAttribute("curPage",curPage);
     /**已使用Hibernate 卞以刚 2011-12-28
	  * 影响对象：Operator*/
     request.setAttribute("userName",StrutsOperatorDelegate.getUserName(mUserToGrpForm.getUserId()));
     return mapping.findForward("view");
  }
}
