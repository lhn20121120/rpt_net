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
import com.cbrc.smis.util.FitechUtil;
/**
 * 校验类型更新操作的 Action对象
 *
 * @author 唐磊
 *
 * @struts.action
 *    path="/struts/updateValidateType"
 *    name="ValidateTypeForm"
 *    scope="request"
 *    validate="false"
 *
 * @struts.action-forward
 *    name="view"
 *    path="/struts/viewValidateType.do"
 *    redirect="false"
 */
public final class UpdateValidateTypeAction extends Action {
	private static FitechException log = new FitechException(UpdateValidateTypeAction.class);
      
	/**
	 * @param mapping Action mapping.
	 * @param form Action form.
     * @param request HTTP请求
	 * @param response HTTP 请求
	 * @exception IOException是否有输入/输出的异常
	 * @exception ServletException是否有servlet的异常占用
	 */
	public ActionForward execute(
      ActionMapping mapping,
      ActionForm form,
      HttpServletRequest request,
      HttpServletResponse response
   
	)throws IOException, ServletException {

		Locale locale=getLocale(request);	   
		MessageResources resources=this.getResources(request);	   
		FitechMessages messages=new FitechMessages();
		ValidateTypeForm validateTypeForm = (ValidateTypeForm) form;
		
		String curPage="";
		if(request.getParameter("curPage")!=null) curPage=(String)request.getParameter("curPage");
		
		boolean updateResult = false;		
		try {
			HttpSession session = request.getSession();
			Operator operator = null; 		    
			if(session.getAttribute(Config.OPERATOR_SESSION_ATTRIBUTE_NAME)!=null)		    
				operator = (Operator)session.getAttribute(Config.OPERATOR_SESSION_ATTRIBUTE_NAME);
			
			if (validateTypeForm != null) {
				updateResult = com.cbrc.smis.adapter.StrutsValidateTypeDelegate.update(validateTypeForm);				
				if (updateResult == true){	
					//更新成功
					messages.add(FitechResource.getMessage(locale,resources,
							"update.success","validateType.info"));
					
					String msg =FitechResource.getMessage(locale, resources,
							"update.success", "validateType.msg",
							validateTypeForm.getValidateTypeName());
					FitechLog.writeLog(Config.LOG_OPERATION,operator.getUserName(),msg);					
				}else                
					messages.add(FitechResource.getMessage(locale,resources,
							"update.failed","validateType.info"));							
			}		
		} catch (Exception e) {		
			updateResult=false;
            messages.add(FitechResource.getMessage(locale,resources,
                    "update.failed","validateType.info"));
			log.printStackTrace(e);
		}

		FitechUtil.removeAttribute(mapping,request);
			
		//判断有无提示信息，如有将其存储在Request对象中，返回请求
		if(messages!=null && messages.getSize()>0)
			request.setAttribute(Config.MESSAGES,messages);
	
		String path="";
		if(updateResult==true){	//成功，返回货币单位列表页
			form = null;
			path = mapping.findForward("update").getPath() + 
				"?curPage=" + curPage + 
				"&validateTypeName=";
		}else{	//失败，返回提交页
			path = mapping.getInputForward().getPath();
		}
		
		path=path==null?mapping.findForward(Config.FORWARD_SYS_ERROR_PAGE).getPath():path;
		
		return new ActionForward(path);	
	}
}
