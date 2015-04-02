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

import com.cbrc.auth.adapter.StrutsOperatorDelegate;
import com.cbrc.auth.adapter.StrutsUserRoleDelegate;
import com.cbrc.auth.form.OperatorForm;
import com.cbrc.smis.common.Config;
import com.cbrc.smis.security.Operator;
import com.cbrc.smis.security.OperatorHandler;
import com.cbrc.smis.util.FitechException;
import com.cbrc.smis.util.FitechMessages;
import com.cbrc.smis.util.FitechResource;

public class SearchUserInfoAction extends Action{

	private static FitechException log = new FitechException(SearchUserInfoAction.class); 
	
	public ActionForward execute(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response
	)
		throws IOException, ServletException {

		MessageResources resources=getResources(request);
		FitechMessages messages = new FitechMessages();
		Locale locale = request.getLocale();
		
		/**取得request范围内的请求参数，并存放在内*/
  //      OperatorForm operatorForm = new OperatorForm(); 
   //     RequestUtils.populate(operatorForm, request);
        Long role=null;
        String roleId=(String)request.getParameter("roleId");
        if(roleId==null){
        	role=null;
        }else{
        	try{
        		role=new Long(roleId);
        	}catch(Exception e){
        		log.printStackTrace(e);
        	}
       }
  //      Operator operator = new Operator();
        
        try{
        	//取得用户信息
        	OperatorForm operator_Form=StrutsUserRoleDelegate.getUserId(role);
     	 
             OperatorForm resultForm = StrutsOperatorDelegate.userNewLoginValidate(operator_Form);
             /**如果不存在，则登录错误*/
             if(resultForm==null)
             {
                 messages.add(FitechResource.getMessage(locale,resources,"userLogin.false"));  
                 request.setAttribute(Config.MESSAGES,messages);
                 return mapping.findForward("login");
             }
             /**登录成功则保存用户信息在Session内*/
             else
             {
                 //// System.out.println("=====================UserID =====" + resultForm.getUserId());
                 OperatorHandler operatorHandler = new OperatorHandler();
                 Operator operator = operatorHandler.saveUserInfo(resultForm);
                 
                
                 
                 HttpSession session = request.getSession();
                 
                 if(session.getAttribute(Config.OPERATOR_SESSION_ATTRIBUTE_NAME)!=null)
                     session.removeAttribute(Config.OPERATOR_SESSION_ATTRIBUTE_NAME);
                 session.setAttribute(Config.OPERATOR_SESSION_ATTRIBUTE_NAME,operator);
                 if(session.getAttribute("AllRole")!=null)session.setAttribute("AllRole",null);
             }   	
        }catch(Exception ex){
        	log.printStackTrace(ex);
            messages.add(FitechResource.getMessage(locale,resources,"operator.update.failed")); 
            request.setAttribute(Config.MESSAGES,messages);
            return mapping.findForward("success");
        }
        if(messages.getMessages() != null && messages.getMessages().size() > 0)
            request.setAttribute(Config.MESSAGES,messages);

		return mapping.findForward("success");
	}
		        
		        
}
