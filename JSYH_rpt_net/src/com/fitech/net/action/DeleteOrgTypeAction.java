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
import com.fitech.net.adapter.StrutsOrgTypeDelegate;
import com.fitech.net.form.OrgTypeForm;
/**
 * 机构级别删除action的实现
 *
 * @author jcm
 *
 */
public final class DeleteOrgTypeAction extends Action {
	private static FitechException log = new FitechException(DeleteOrgTypeAction.class);
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

      OrgTypeForm orgTypeForm = new OrgTypeForm();
      
      // 将request保存进orgTypeForm 
      RequestUtils.populate(orgTypeForm, request);

		try 
        {
			boolean result = StrutsOrgTypeDelegate.remove(orgTypeForm);
			if (result == true)
				messages.add(resources.getMessage("delete.orgType.success"));	
            else
            	messages.add(resources.getMessage("delete.orgType.failed"));	
		} 
        catch (Exception e){
			log.printStackTrace(e);
			messages.add(resources.getMessage("delete.orgType.failed"));	
		}

        if(messages.getMessages() != null && messages.getMessages().size() != 0)
               request.setAttribute(Config.MESSAGES,messages);
        
        String path="/viewOrgType.do";

	   return new ActionForward(path);
 }

}
