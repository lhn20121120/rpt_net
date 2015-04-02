package com.cbrc.auth.action;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.RequestUtils;

import com.cbrc.auth.adapter.StrutsOperatorDelegate;
import com.cbrc.auth.form.OperatorForm;
import com.cbrc.smis.common.ApartPage;
import com.cbrc.smis.common.Config;
import com.cbrc.smis.util.FitechException;
import com.cbrc.smis.util.FitechMessages;

/**
 * 用户密码重置
 * @author jcm
 * @2008-02-18
 */
public class ResetPwdAction extends Action{

	private static FitechException log = new FitechException(ResetPwdAction.class); 
	
	/***
	 * 已使用hibernate 卞以刚 2011-12-28
	 * 影响对象：Operator
	 */
	public ActionForward execute(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response
	)
		throws IOException, ServletException {

		FitechMessages messages = new FitechMessages();
		
		/**取得request范围内的请求参数*/
        OperatorForm operatorForm = new OperatorForm(); 
        RequestUtils.populate(operatorForm, request);
        OperatorForm operator_Form = null;
           	
		String curPage = request.getParameter("curPage");
		String term = mapping.findForward("view").getPath();
		
        try{
        	/**已使用hibernate 卞以刚 2011-12-28
        	 * 影响对象：Operator*/
        	operator_Form = StrutsOperatorDelegate.getUserDetail(operatorForm.getUserId());
        	if(operator_Form != null){
        		operator_Form.setPassword("123456");
        		
        		/**已使用hibernate 卞以刚 2011-12-23
        		 * 影响对象：Operator*/
        		boolean result;
        		if(Config.PORTAL)
        			result = StrutsOperatorDelegate.update(operator_Form,true);
        		else
        			result = StrutsOperatorDelegate.update(operator_Form,false);
        		if(result)
        			messages.add("用户"+operator_Form.getUserName()+"登录密码重置成功，默认为123456！");
        		else{
        			messages.add("用户"+operator_Form.getUserName()+"登录密码重置失败！"); 
                    request.setAttribute(Config.MESSAGES,messages);
                    return mapping.findForward("view");
        		}
        	}else{
        		messages.add("用户"+operator_Form.getUserName()+"登录密码重置失败！"); 
                request.setAttribute(Config.MESSAGES,messages);
                return mapping.findForward("view");
        	}
        	term += "?curPage=" + curPage;
        	term += "&userName=" + operatorForm.getUserName();
        }catch(Exception ex){
        	log.printStackTrace(ex);
        	messages.add("用户"+operator_Form.getUserName()+"登录密码重置失败！"); 
            request.setAttribute(Config.MESSAGES,messages);
            return mapping.findForward("view");
        }
        
        if(messages.getMessages() != null && messages.getMessages().size() > 0)
            request.setAttribute(Config.MESSAGES,messages);
        
		return mapping.findForward("view");
	}
}
