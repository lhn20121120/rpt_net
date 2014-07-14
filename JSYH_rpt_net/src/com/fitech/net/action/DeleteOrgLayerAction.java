package com.fitech.net.action;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.MessageResources;
import org.apache.struts.util.RequestUtils;

import com.cbrc.smis.common.Config;
import com.cbrc.smis.util.FitechException;
import com.cbrc.smis.util.FitechMessages;
import com.fitech.net.adapter.StrutsOrgLayerDelegate;
import com.fitech.net.form.OrgLayerForm;
/**
 * ��������ɾ��action��ʵ��
 *
 * @author jcm
 *
 */
public final class DeleteOrgLayerAction extends Action {
	private static FitechException log = new FitechException(DeleteOrgLayerAction.class);
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

      OrgLayerForm orgLayerForm = new OrgLayerForm();
      
      // ��request�����orgLayerForm 
      RequestUtils.populate(orgLayerForm, request);

		try 
        {
			boolean result = StrutsOrgLayerDelegate.remove(orgLayerForm);
			if (result == true)
				messages.add(resources.getMessage("delete.orgLayer.success"));	
            else
            	messages.add(resources.getMessage("delete.orgLayer.failed"));	
		} 
        catch (Exception e){
			log.printStackTrace(e);
			messages.add(resources.getMessage("delete.orgLayer.failed"));	
		}

        if(messages.getMessages() != null && messages.getMessages().size() != 0)
               request.setAttribute(Config.MESSAGES,messages);
        
        String path="/viewOrgLayer.do";

	   return new ActionForward(path);
 }

}
