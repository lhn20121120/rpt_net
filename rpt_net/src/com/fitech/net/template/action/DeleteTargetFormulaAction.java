package com.fitech.net.template.action;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.MessageResources;

import com.cbrc.smis.common.Config;
import com.cbrc.smis.util.FitechException;
import com.cbrc.smis.util.FitechMessages;
import com.cbrc.smis.util.FitechUtil;
import com.fitech.net.adapter.StrutsFormulaDelegate;
import com.fitech.net.form.EtlIndexForm;
/**
 * ������ɾ��action��ʵ��
 *
 * @author James
 *
 */
public final class DeleteTargetFormulaAction extends Action {
	private static FitechException log = new FitechException(DeleteTargetFormulaAction.class);
   /**
    * @"view"  mapping Action
    * @return request request����
    * @return  response respponse����
    * @exception IOException  �Ƿ���IO�쳣��������׽���׳�
    * @exception ServletException �Ƿ���Servlet�쳣��������׽���׳�
    */
   public ActionForward execute(
      ActionMapping mapping,
      ActionForm form,
      HttpServletRequest request,
      HttpServletResponse response
   )
   throws IOException, ServletException{
	   
	   MessageResources resources=this.getResources(request);
	   FitechMessages messages=new FitechMessages();

      EtlIndexForm etlIndexForm = (EtlIndexForm)form;
		try 
        {
			boolean result = StrutsFormulaDelegate.removeEtlIndex(etlIndexForm);
			if (result == true)
				messages.add("ɾ��ȡ����ʽָ��ɹ�!");	
            else
            	messages.add("ɾ��ȡ����ʽָ��ʧ��!");	
		} 
        catch (Exception e){
			log.printStackTrace(e);
			messages.add("ɾ��ȡ����ʽָ��ʧ��!");	
		}

        if(messages.getMessages() != null && messages.getMessages().size() != 0)
               request.setAttribute(Config.MESSAGES,messages);
     FitechUtil.removeAttribute(mapping,request);
        return mapping.findForward("delete");
 }

}
