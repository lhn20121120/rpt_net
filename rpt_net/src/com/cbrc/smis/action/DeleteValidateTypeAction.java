package com.cbrc.smis.action;

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
import com.cbrc.smis.form.ValidateTypeForm;
import com.cbrc.smis.security.Operator;
import com.cbrc.smis.util.FitechException;
import com.cbrc.smis.util.FitechLog;
import com.cbrc.smis.util.FitechMessages;
import com.cbrc.smis.util.FitechResource;
/**
 *校验关系删除action的实现
 *
 * @author唐磊
 *
 */
public final class DeleteValidateTypeAction extends Action {
	private static FitechException log = new FitechException(DeleteLogInAction.class);
	
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
   
	)throws IOException, ServletException{
	   
		Locale locale=getLocale(request);	   
		MessageResources resources=this.getResources(request);	   
		FitechMessages messages=new FitechMessages();
	         
		//mRepFreqForm ValidateTypeForm的初始化      
		ValidateTypeForm validateTypeForm = new ValidateTypeForm();
		// 将request保存进validateTypeForm       
		RequestUtils.populate(validateTypeForm, request);
		
		try{
			
			HttpSession session = request.getSession();
			Operator operator = null; 		    
			if(session.getAttribute(Config.OPERATOR_SESSION_ATTRIBUTE_NAME)!=null)		    
				operator = (Operator)session.getAttribute(Config.OPERATOR_SESSION_ATTRIBUTE_NAME);
			
			boolean result = com.cbrc.smis.adapter.StrutsValidateTypeDelegate
					.remove(validateTypeForm);
			if (result == true){
				String msg =FitechResource.getMessage(locale, resources,
                        "delete.success", "validateType.msg",
                        validateTypeForm.getValidateTypeName().toString());
                FitechLog.writeLog(Config.LOG_OPERATION,operator.getUserName(),msg);
                
                messages.add(FitechResource.getMessage(locale, resources,
                        "delete.success", "validateType.info"));
			}else{
            	String msg =FitechResource.getMessage(locale, resources,
                        "delete.failed", "validateType.msg",
                        validateTypeForm.getValidateTypeId().toString());
                FitechLog.writeLog(Config.LOG_OPERATION,operator.getUserName(),msg);
                
                messages.add(FitechResource.getMessage(locale, resources,
                        "delete.failed", "validateType.info"));
            }
		}catch (Exception e){
			log.printStackTrace(e);
            messages.add(FitechResource.getMessage(locale, resources,
                    "delete.failed", "validateType.info"));
		}
       
        if(messages.getMessages() != null && messages.getMessages().size() != 0)
            request.setAttribute(Config.MESSAGES,messages);
        String path="/config/ViewCurVerifyType.do";	   
	  
//        Integer validateTypeId = validateTypeForm.getValidateTypeId();
//        String validateTypeName = validateTypeForm.getValidateTypeName();
//	   	   
//        if(validateTypeId!=null){		
//        	path += (path.indexOf("?")>=0 ? "&" : "?");		   
//        	path += "validateTypeId="+validateTypeId.toString();   	  
//        }	   
//        if(validateTypeName!=null && !validateTypeName.equals("")){
//        	path += (path.indexOf("?")>=0 ? "&" : "?");		   
//        	path += "validateTypeName="+validateTypeName.toString();   	   	  
//        }	  	   
        
	   return new ActionForward(path); 
	}
}
