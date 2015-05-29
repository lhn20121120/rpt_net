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
 *  ɾ���û�����Ϣ
 * @author Ҧ��
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
    * ��ʹ��Hibernate ���Ը� 2011-12-28
    * Ӱ�����MPurBanklevel MPurOrg MUserGrp MUserToGrp
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
       
       /**ȡ��request��Χ�ڵ�������������������*/
       MUserGrpForm mUserGrpForm = new MUserGrpForm(); 
       RequestUtils.populate(mUserGrpForm, request);
       
       /**��ʹ��hibernate ���Ը� 2011-12-28
        * Ӱ�����MUserToGrp*/
       int userGrpUserNum = StrutsMUserToGrpDelegate.getUserNumFromUserGrpId(mUserGrpForm.getUserGrpId());
       
       /**�鿴��ǰ�Ƿ����û����ڸ��û���*/
       
       if(userGrpUserNum >0)/**������û����ڸ��û���,������ɾ��*/ 
          // messages.add(FitechResource.getMessage(locale,resources,"dept.delete.fail","user.num",String.valueOf(userGrpUserNum)));
    	   messages.add("������ɾ�����û���!����"+String.valueOf(userGrpUserNum)+"���û����ڸ��û���,����ɾ����"+String.valueOf(userGrpUserNum)+"���û�.");
       else/**���û���û����ڸ��û��������ɾ��*/
       {
           try 
           {
        	   
        	   /**��ʹ��Hibernate ���Ը� 2011-12-28
        	    * Ӱ�����MPurBanklevel MPurOrg MUserGrp*/
               boolean result = StrutsMUserGrpDelegate.remove(mUserGrpForm);
               if(result==false)/**ɾ��ʧ��*/
                    messages.add(FitechResource.getMessage(locale,resources,"delete.fail","userGrp.info"));
               else/**ɾ���ɹ�*/
                   messages.add("ɾ���ɹ���");  
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
