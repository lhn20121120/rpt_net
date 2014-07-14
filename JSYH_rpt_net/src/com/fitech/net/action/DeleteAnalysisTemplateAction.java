package com.fitech.net.action;
 
import java.io.IOException;
import java.util.Locale;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.MessageResources;
import org.apache.struts.util.RequestUtils;

import com.cbrc.smis.common.Config;
import com.cbrc.smis.form.MCurUnitForm;
import com.cbrc.smis.security.Operator;
import com.cbrc.smis.util.FitechException;
import com.cbrc.smis.util.FitechLog;
import com.cbrc.smis.util.FitechMessages;
import com.cbrc.smis.util.FitechResource;
import com.fitech.net.form.AAnalysisTPForm;
/**
 * 删除action的实现
 *
 * @author wh
 *
 */
public final class DeleteAnalysisTemplateAction extends Action {
	private static FitechException log = new FitechException(DeleteAnalysisTemplateAction.class);
   /**
    * @"view"  mapping Action
    * @return request request请求
    * @return  response respponse请求
    * @exception IOException  是否有IO异常，如有捕捉并抛出
    * @exception ServletException 是否有Servlet异常，如有则捕捉并抛出
    */
   public ActionForward execute(
      ActionMapping mapping,
      ActionForm form,
      HttpServletRequest request,
      HttpServletResponse response
   )
   throws IOException, ServletException{
	   Locale locale=getLocale(request);
	   MessageResources resources=this.getResources(request);
	   FitechMessages messages=new FitechMessages();

	   AAnalysisTPForm aAnalysisTPForm = (AAnalysisTPForm)form ;
	   RequestUtils.populate(aAnalysisTPForm, request);
      
      try{
			HttpSession session = request.getSession();
    		Operator operator = null; 
    		if(session.getAttribute(Config.OPERATOR_SESSION_ATTRIBUTE_NAME)!=null)
    			operator = (Operator)session.getAttribute(Config.OPERATOR_SESSION_ATTRIBUTE_NAME);
    		
			boolean result = com.cbrc.smis.adapter.StrutsMCurUnitDelegate.remove(aAnalysisTPForm);
			/**删除成功*/
            if (result == true) {
            	/** 写入日志 */
            	 
            	
                messages.add("删除成功");
			}
            /**删除失败*/
            else{ 
            	/** 写入日志 */
            	 
            	
                messages.add("删除失败");
            }
		} 
        catch (Exception e){
			log.printStackTrace(e);
			messages.add("删除失败");
        }
   
        if(messages.getMessages() != null && messages.getMessages().size() != 0)
               request.setAttribute(Config.MESSAGES,messages);
    
        return mapping.findForward("view");	 
   }
}
