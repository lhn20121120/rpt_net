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
 * 机构级别删除action的实现
 *
 * @author jcm
 *
 */
public final class DeleteOrgLayerAction extends Action {
	private static FitechException log = new FitechException(DeleteOrgLayerAction.class);
   /**
    * @"view"  mapping Action
    * @return request request请求
    * @return  response respponse请求
    * @exception IOException  是否有IO异常，如有则捕捉并抛出
    * @exception ServletException 是否有Servlet异常，如有则捕捉并抛出
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
      
      // 将request保存进orgLayerForm 
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
