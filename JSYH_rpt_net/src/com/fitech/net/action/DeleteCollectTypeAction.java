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
import com.fitech.net.adapter.StrutsCollectTypeDelegate;
import com.fitech.net.form.CollectTypeForm;
/**
 * ���ܷ�ʽɾ��action��ʵ��
 *
 * @author jcm
 *
 */
public final class DeleteCollectTypeAction extends Action {
	private static FitechException log = new FitechException(DeleteCollectTypeAction.class);
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
	   
	   String path="/collectType/viewCollectType.do?curPage=";
	   MessageResources resources=this.getResources(request);
	   FitechMessages messages=new FitechMessages();

	   CollectTypeForm collectTypeForm = new CollectTypeForm(); 
	   RequestUtils.populate(collectTypeForm, request);
      
	   String curPage = request.getParameter("curPage") != null ? request.getParameter("curPage") : null;
	   path = curPage != null ? path + curPage : path;
	   
	   try {		
		   boolean result = StrutsCollectTypeDelegate.remove(collectTypeForm);			
		   if (result == true)			
			   messages.add(resources.getMessage("delete.collectType.success"));	            
		   else           
			   messages.add(resources.getMessage("delete.collectType.failed"));		
		   
	   }catch (Exception e){		
		   log.printStackTrace(e);			
		   messages.add(resources.getMessage("delete.collectType.failed"));			
	   }
	   
	   if(messages.getMessages() != null && messages.getMessages().size() != 0)               
		   request.setAttribute(Config.MESSAGES,messages);

	   return new ActionForward(path);
   }
}
