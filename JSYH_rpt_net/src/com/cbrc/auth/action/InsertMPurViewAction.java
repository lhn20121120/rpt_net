package com.cbrc.auth.action;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.cbrc.smis.util.FitechException;

/**无用
 * @author 姚捷
 *
 * @struts.action
 *    path="/struts/insertMPurView"
 *    name="MPurViewForm"
 *    scope="session"
 *    validate="false"
 *
 * @struts.action-forward
 *    name="view"
 *    path="/struts/viewMPurView.do"
 *    redirect="false"
 *

 */
public final class InsertMPurViewAction extends Action {
    private static FitechException log = new FitechException(InsertMPurViewAction.class); 
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
/*
       MessageResources resources=getResources(request);
       FitechMessages messages = new FitechMessages();
       Locale locale = request.getLocale();
       
       MPurViewForm mPurViewForm = (MPurViewForm) form;
       RequestUtils.populate(mPurViewForm,request);
             
       try 
       {
           Long userGrpId = mPurViewForm.getUserGrpId();
           String selectOrgIds = mPurViewForm.getSelectOrgIds();
           String selectRepIds = mPurViewForm.getSelectRepIds();
           
           *//**查询该用户组已经有的报表权限信息*//*
           boolean saveResult = StrutsMPurViewDelegate.insertUserGrpPopedom(userGrpId,selectOrgIds,selectRepIds);
           if(saveResult==false)
           {
               messages.add(FitechResource.getMessage(locale,resources,"save.failed","userGrp.popedom.info"));
               request.setAttribute(Config.MESSAGES,messages);
               return new ActionForward("/popedom_mgr/viewUserGrpRepPopedom.do");
            }
           else
               messages.add(FitechResource.getMessage(locale,resources,"save.success","userGrp.popedom.info"));  
       }
       catch (Exception e) 
       {
           log.printStackTrace(e);
           messages.add(FitechResource.getMessage(locale,resources,"save.failed","userGrp.popedom.info"));
           request.setAttribute(Config.MESSAGES,messages);
           return new ActionForward("/popedom_mgr/viewUserGrpRepPopedom.do");
       }
     
       if(messages.getMessages() != null && messages.getMessages().size() > 0)
           request.setAttribute(Config.MESSAGES,messages);*/
       return mapping.findForward("view");

   }
}
