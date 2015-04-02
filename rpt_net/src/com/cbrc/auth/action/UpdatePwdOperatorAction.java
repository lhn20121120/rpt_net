package com.cbrc.auth.action;

import java.io.IOException;
import java.security.MessageDigest;
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

import com.cbrc.auth.adapter.StrutsOperatorDelegate;
import com.cbrc.auth.form.OperatorForm;
import com.cbrc.auth.util.Encoder;
import com.cbrc.smis.common.Config;
import com.cbrc.smis.security.Operator;
import com.cbrc.smis.util.FitechException;
import com.cbrc.smis.util.FitechMessages;
import com.cbrc.smis.util.FitechResource;
import com.fitech.fitosa.adapter.ImpReportData;
import com.fitech.fitosa.bean.UserInfoBean;

public class UpdatePwdOperatorAction extends Action{

	private static FitechException log = new FitechException(InsertOperatorAction.class); 
	
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

		MessageResources resources=getResources(request);
		FitechMessages messages = new FitechMessages();
		Locale locale = request.getLocale();
		
		/**取得request范围内的请求参数，并存放在内*/
        OperatorForm operatorForm = new OperatorForm(); 
        RequestUtils.populate(operatorForm, request);
        
		HttpSession session=request.getSession();
        Operator operator = new Operator();
        if(session.getAttribute(Config.OPERATOR_SESSION_ATTRIBUTE_NAME)!=null)
            operator = (Operator)session.getAttribute(Config.OPERATOR_SESSION_ATTRIBUTE_NAME);
        
        try{
        	/**已使用hibernate 卞以刚 2011-12-28
        	 * 影响对象：Operator*/
        	OperatorForm operator_Form = StrutsOperatorDelegate.getUserDetail(operator.getOperatorId());
        	
        	if(operator_Form.getPassword().equals(Encoder.getMD5_Base64(operatorForm.getPasswordOld()))){
        		operator_Form.setPassword(operatorForm.getPassword());
        		operator_Form.setPassword1(operatorForm.getPassword1());
        		
				/*
				 * 同步分析系统开始
				 */
        		
        		if(Config.ISADDFITOSA){
        			UserInfoBean bean = new UserInfoBean();
    				bean.setUserId(operator_Form.getUserName());
    				bean.setUserName(operator_Form.getFirstName()
    						+ (operator_Form.getLastName()==null?"":operator_Form.getLastName()));
    				bean.setOrgId(operator_Form.getOrgId());
    				if (operator_Form.getPassword() != null
    						&& !operator_Form.getPassword().trim().equals("")) {
    					String pwd =encode1(operator_Form.getPassword());
    					bean.setPassword(pwd);
    				}
    				bean.setSex(operator_Form.getSex().equals("男") ? "0" : "1");
    				bean.setDpmtId(operator_Form.getDepartmentId().toString());
    				bean.setTelNo(operator_Form.getTelephoneNumber());
    				ImpReportData ird = new ImpReportData();
    				ird.setWebroot(Config.WEBROOTPATH);
    				ird.updateUser(bean);
        		}
				

				/*
				 * 同步分析系统结束
				 */
        		
        		/**已使用hibernate 卞以刚 2011-12-23
        		 * 影响对象：Operator*/
        		boolean result;
        		if(Config.PORTAL)
        			result = StrutsOperatorDelegate.update(operator_Form,true);
        		else
        			result = StrutsOperatorDelegate.update(operator_Form,false);
        		if(result)
        			messages.add(FitechResource.getMessage(locale,resources,"operator.update.success"));
        		else{
        			messages.add(FitechResource.getMessage(locale,resources,"operator.password.error")); 
                    request.setAttribute(Config.MESSAGES,messages);
                    return mapping.findForward("update_pwd");
        		}       		
        	}else{
        		messages.add(FitechResource.getMessage(locale,resources,"operator.password.error")); 
                request.setAttribute(Config.MESSAGES,messages);
                return mapping.findForward("update_pwd");
        	}
        }catch(Exception ex){
        	log.printStackTrace(ex);
            messages.add(FitechResource.getMessage(locale,resources,"operator.update.failed")); 
            request.setAttribute(Config.MESSAGES,messages);
            return mapping.findForward("update_pwd");
        }
        
        if(messages.getMessages() != null && messages.getMessages().size() > 0)
            request.setAttribute(Config.MESSAGES,messages);
        
		return mapping.findForward("update_pwd");
	}
    /**
     * 数据加密
     * @param input
     * @return
     */
    private static String encode1(String input) {

    	StringBuffer buf = new StringBuffer();
 	    try
 	    {
 	      MessageDigest md5 = MessageDigest.getInstance("MD5");
 	      md5.update(input.getBytes());
 	      byte bytes[] = md5.digest();
 	      for(int i = 0; i < bytes.length; i++)
 	      {
 	        String s = Integer.toHexString(bytes[i] & 0xff);
 	        if(s.length()==1){
 	          buf.append("0");
 	        }
 	        buf.append(s);
 	      }

 	    }
 	    catch(Exception ex)
 	    {
 	    }
 	    return buf.toString();
    }  
		        
}
