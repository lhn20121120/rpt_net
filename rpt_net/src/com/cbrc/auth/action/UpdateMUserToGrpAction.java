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
    * ��ʹ��Hibernate ���Ը� 2011-12-28
    * Ӱ�����MUserToGrp Operator MUserGrp
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
        /**�鿴���û��Ƿ��Ѿ������û���*/
        /**��ʹ��hibernate ���Ը� 2011-12-28
         * Ӱ�����MUserToGrp*/
        if(StrutsMUserToGrpDelegate.isUserGrpSetting(userId)==true)
        {
            /**������ù��û�����ɾ�������Ϣ*/
        	/**��ʹ��Hibernate ���Ը� 2011-12-28
        	 * Ӱ�����MUserToGrp*/
             deleteResult = StrutsMUserToGrpDelegate.deleteUserSetUserGrp(userId);
            /**ɾ��ʧ������תԭҳ��*/
            if(deleteResult==false)
            {
                messages.add(FitechResource.getMessage(locale,resources,"save.fail","operator.info"));     
                request.setAttribute(Config.MESSAGES,messages);
                return mapping.findForward("user_userGrp_setting");
            }
        }
        
        /**��ʹ��hibernate ���Ը� 2011-12-28
         * Ӱ�����Operator MUserGrp MUserToGrp*/
        //mod by wmm,�����û�ѡ����û���Ϊ��ʱ
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
     /**��ʹ��Hibernate ���Ը� 2011-12-28
	  * Ӱ�����Operator*/
     request.setAttribute("userName",StrutsOperatorDelegate.getUserName(mUserToGrpForm.getUserId()));
     return mapping.findForward("view");
  }
}
