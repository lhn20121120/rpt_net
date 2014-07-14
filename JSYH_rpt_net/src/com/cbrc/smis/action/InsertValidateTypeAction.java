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

import com.cbrc.smis.common.Config;
import com.cbrc.smis.form.ValidateTypeForm;
import com.cbrc.smis.security.Operator;
import com.cbrc.smis.util.FitechException;
import com.cbrc.smis.util.FitechLog;
import com.cbrc.smis.util.FitechMessages;
import com.cbrc.smis.util.FitechResource;

/**
 * 校验关系插入一条记录的Action对象
 *
 * @author 唐磊
 *
 * @struts.action
 *    path="/struts/insertValidateType"
 *    name="ValidateTypeForm"
 *    scope="request"
 *    validate="false"
 *
 * @struts.action-forward
 *    name="view"
 *    path="/struts/viewValidateType.do"
 *    redirect="false"
 */
public final class InsertValidateTypeAction extends Action {
	private static FitechException log = new FitechException(InsertValidateTypeAction.class);
   
	/**
	 *插入数据到数据库
	 * @exception IOException  是否有IO异常，如有则捕捉并抛出
	 * @exception ServletException 是否有Servlet异常，如有则捕捉并抛出
	 */
	public ActionForward execute(
      ActionMapping mapping,
      ActionForm form,
      HttpServletRequest request,
      HttpServletResponse response   
	)throws IOException, ServletException {
			   
		Locale locale=getLocale(request);	   
		MessageResources resources=getResources(request);
		FitechMessages messages=new FitechMessages();
		boolean insertResult=false;	   	   
		ValidateTypeForm validateTypeForm = (ValidateTypeForm)form;
      	   
		try{       
			if (validateTypeForm!=null){
				HttpSession session = request.getSession();
				Operator operator = null; 		    
				if(session.getAttribute(Config.OPERATOR_SESSION_ATTRIBUTE_NAME)!=null)		    
					operator = (Operator)session.getAttribute(Config.OPERATOR_SESSION_ATTRIBUTE_NAME);
				
				insertResult = com.cbrc.smis.adapter.StrutsValidateTypeDelegate.create(validateTypeForm);

				if (insertResult==true){            	
					String msg =FitechResource.getMessage(locale, resources,                    
							"save.success", "validateType.msg",                            
							validateTypeForm.getValidateTypeName());                  
					FitechLog.writeLog(Config.LOG_OPERATION,operator.getUserName(),msg);                    
                    messages.add(FitechResource.getMessage(locale,resources,"save.success","validateType.info"));            	
				}else{
                	String msg =FitechResource.getMessage(locale, resources,
                            "save.failed", "validateType.msg",
                            validateTypeForm.getValidateTypeName());
                    FitechLog.writeLog(Config.LOG_OPERATION,operator.getUserName(),msg);                    
                    messages.add(FitechResource.getMessage(locale,resources,"save.failed","validateType.info"));
            	}             
			}         
		}catch (Exception e){        
			insertResult=false;            
			messages.add(FitechResource.getMessage(locale,resources,"save.failed","validateType.info"));            
			log.printStackTrace( e);    
		}
		
		if(messages!=null && messages.getSize()>0)         
			request.setAttribute(Config.MESSAGES,messages);
                  
		//新增失败        
		if(insertResult==false){        
			return mapping.findForward("insert");          
		}
		
		String path="/config/ViewCurVerifyType.do?validateTypeName=";
		
//		Integer validateTypeId = validateTypeForm.getValidateTypeId();    	
//		String validateTypeName = validateTypeForm.getValidateTypeName();
//		
//		if(validateTypeId!=null){    	
//			path += (path.indexOf("?")>=0 ? "&" : "?");    		
//			path += "validateTypeId="+validateTypeId.toString();       	  
//		}    	
//		if(validateTypeName!=null && !validateTypeName.equals("")){    	
//			path += (path.indexOf("?")>=0 ? "&" : "?");    		
//			path += "validateTypeName="+validateTypeName.toString();       	   
//		}		
		return new ActionForward(path);     
	}   
}
