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
 * 参数表删除action的实现
 *
 * @author James
 *
 */
public final class DeleteTargetFormulaAction extends Action {
	private static FitechException log = new FitechException(DeleteTargetFormulaAction.class);
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

      EtlIndexForm etlIndexForm = (EtlIndexForm)form;
		try 
        {
			boolean result = StrutsFormulaDelegate.removeEtlIndex(etlIndexForm);
			if (result == true)
				messages.add("删除取数公式指标成功!");	
            else
            	messages.add("删除取数公式指标失败!");	
		} 
        catch (Exception e){
			log.printStackTrace(e);
			messages.add("删除取数公式指标失败!");	
		}

        if(messages.getMessages() != null && messages.getMessages().size() != 0)
               request.setAttribute(Config.MESSAGES,messages);
     FitechUtil.removeAttribute(mapping,request);
        return mapping.findForward("delete");
 }

}
