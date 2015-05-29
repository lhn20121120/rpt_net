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
import com.cbrc.auth.adapter.StrutsMUserToGrpDelegate;
import com.cbrc.auth.form.MUserGrpForm;
import com.cbrc.smis.common.Config;
import com.cbrc.smis.util.FitechException;
import com.cbrc.smis.util.FitechMessages;
import com.cbrc.smis.util.FitechResource;

/**
 *  删除用户组信息
 * @author 姚捷
 *
 * @struts.action
 *    path="/struts/deleteMUserGrp"
 *
 * @struts.action-forward
 *    name="all"
 *    path="/struts/getAll.do"
 *    redirect="false"
 *

 */
public final class DeleteMUserGrpAction extends Action {
    private static FitechException log = new FitechException(DeleteMUserGrpAction.class);
   /**
    * 已使用Hibernate 卞以刚 2011-12-28
    * 影响对象：MPurBanklevel MPurOrg MUserGrp MUserToGrp
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
       
       /**取得request范围内的请求参数，并存放在内*/
       MUserGrpForm mUserGrpForm = new MUserGrpForm(); 
       RequestUtils.populate(mUserGrpForm, request);
       
       /**已使用hibernate 卞以刚 2011-12-28
        * 影响对象：MUserToGrp*/
       int userGrpUserNum = StrutsMUserToGrpDelegate.getUserNumFromUserGrpId(mUserGrpForm.getUserGrpId());
       
       /**查看当前是否有用户属于该用户组*/
       
       if(userGrpUserNum >0)/**如果有用户属于该用户组,则不允许删除*/ 
          // messages.add(FitechResource.getMessage(locale,resources,"dept.delete.fail","user.num",String.valueOf(userGrpUserNum)));
    	   messages.add("不允许删除该用户组!已有"+String.valueOf(userGrpUserNum)+"个用户属于该用户组,请先删除这"+String.valueOf(userGrpUserNum)+"个用户.");
       else/**如果没有用户属于该用户组则可以删除*/
       {
           try 
           {
        	   
        	   /**已使用Hibernate 卞以刚 2011-12-28
        	    * 影响对象：MPurBanklevel MPurOrg MUserGrp*/
               boolean result = StrutsMUserGrpDelegate.remove(mUserGrpForm);
               if(result==false)/**删除失败*/
                    messages.add(FitechResource.getMessage(locale,resources,"delete.fail","userGrp.info"));
               else/**删除成功*/
                   messages.add("删除成功！");  
           } 
           catch (Exception e) 
           {
               log.printStackTrace(e);
               messages.add(FitechResource.getMessage(locale,resources,"delete.fail","userGrp.info"));
           }         
       }
       if(messages.getMessages() != null && messages.getMessages().size() != 0)
           request.setAttribute(Config.MESSAGES,messages);
       return new ActionForward("/popedom_mgr/viewMUserGrp.do");
   }

}
