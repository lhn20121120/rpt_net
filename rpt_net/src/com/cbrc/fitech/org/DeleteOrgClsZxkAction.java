package com.cbrc.fitech.org;

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

import com.cbrc.org.adapter.StrutsMOrgClDelegate;
import com.cbrc.org.adapter.StrutsMOrgDelegate;
import com.cbrc.org.form.MOrgClForm;
import com.cbrc.smis.common.Config;
import com.cbrc.smis.util.FitechException;
import com.cbrc.smis.util.FitechMessages;


/**
 * ���з�����Ϣɾ��
 *
 * @author zhangxinke
 *
 *
 */
public final class DeleteOrgClsZxkAction extends Action{
    private static FitechException log = new FitechException(DeleteOrgClsZxkAction.class);
    
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
   )throws IOException, ServletException 
   {
       MessageResources resources=getResources(request);
       FitechMessages messages = new FitechMessages();
       Locale locale = request.getLocale();
   
       /**ȡ��request��Χ�ڵ�������������������*/
       MOrgClForm mOrgClForm = new MOrgClForm(); 
       RequestUtils.populate(mOrgClForm, request); 
       
       // System.out.println(mOrgClForm.getOrgClsId());
       
      // int orgclsId = Integer.parseInt(mOrgClForm.getOrgClsId());
      
     int orghaveorgClId = StrutsMOrgDelegate.getMOrgFromorgClsId(mOrgClForm.getOrgClsId());
       
       /**�鿴��ǰ�Ƿ����û����ڸ�����*/
       
      if(orghaveorgClId>0)/**���Ҫɾ����orgclsId��MORG���е�orgclsId���,������ɾ��*/ 
       messages.add("����ɾ���÷������ͣ�");
      else/**��֮*/
       {
           try 
           {
           
               boolean result = StrutsMOrgClDelegate.remove2(mOrgClForm);
               if(result==false)/**ɾ��ʧ��*/
                    messages.add("ɾ�����з���ʧ�ܣ�");
               else/**ɾ���ɹ�*/
                   messages.add("ɾ�����з���ɹ���");  
           } 
           catch (Exception e) 
           {
               log.printStackTrace(e);
               messages.add("ɾ�����з���ʧ�ܣ�");
           }
       } 
      
       if(messages.getMessages() != null && messages.getMessages().size() != 0)
           request.setAttribute(Config.MESSAGES,messages);
       return new ActionForward("/orgcls/orgCls.do");
   }
    
   
  
   
   
}



