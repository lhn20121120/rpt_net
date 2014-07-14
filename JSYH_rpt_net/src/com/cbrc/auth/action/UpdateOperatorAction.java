package com.cbrc.auth.action;

import java.io.IOException;
import java.security.MessageDigest;
import java.util.Locale;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.MessageResources;
import org.apache.struts.util.RequestUtils;
import com.cbrc.auth.util.Encoder;
import com.cbrc.auth.adapter.StrutsOperatorDelegate;
import com.cbrc.auth.form.OperatorForm;
import com.cbrc.smis.common.Config;
import com.cbrc.smis.util.FitechException;
import com.cbrc.smis.util.FitechMessages;
import com.cbrc.smis.util.FitechResource;
import com.cbrc.smis.util.FitechUtil;
import com.fitech.fitosa.adapter.ImpReportData;
import com.fitech.fitosa.bean.UserInfoBean;

/**
 * Updates a operator.
 *
 * @author <a href="http://boss.bekk.no/boss/middlegen/">Middlegen</a>
 *
 * @struts.action
 *    path="/struts/updateOperator"
 *    name="operatorForm"
 *    scope="session"
 *    validate="false"
 *
 * @struts.action-forward
 *    name="view"
 *    path="/struts/viewOperator.do"
 *    redirect="false"
 *

 */
public final class UpdateOperatorAction extends Action {
    private static FitechException log = new FitechException(UpdateOperatorAction.class); 
    /**
     * 已使用hibernate 卞以刚 2011-12-23
     * 影响对象：Operator
     * Performs action.
     * @param mapping Action mapping.
     * @param form Action form.
     * @param request HTTP request.
     * @param response HTTP response.
     * @exception IOException if an input/output error occurs
     * @exception ServletException if a servlet exception occurs
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
        
        OperatorForm operatorForm = new OperatorForm();
        RequestUtils.populate(operatorForm,request);
        
        boolean result = false;
        
        try 
         {
        	/**已使用hibernate 卞以刚 2011-12-23
        	 * 影响对象：Operator*/
             result = StrutsOperatorDelegate.update(operatorForm); 
             
             /*
 			 * 同步分析系统开始
 			 
             if(Config.ISADDFITOSA){
            	 ImpReportData ird = new ImpReportData();
      			ird.setWebroot(Config.WEBROOTPATH);
      			UserInfoBean user = ird.findUser(operatorForm.getUserName());
      			if (user != null) {

      				user.setUserName(operatorForm.getFirstName()
      						+ (operatorForm.getLastName()==null?"":operatorForm.getLastName()));
      				user.setOrgId(operatorForm.getOrgId());
      				if (operatorForm.getPassword() != null
      						&& !operatorForm.getPassword().trim().equals("")) {
      					user.setPassword(encode1(operatorForm.getPassword()));
      				}
      				user.setSex(operatorForm.getSex().equals("男") ? "0" : "1");
      				user.setDpmtId(operatorForm.getDepartmentId().toString());
      				user.setTelNo(operatorForm.getTelephoneNumber());

      				ird.updateUser(user);
      			} else {

      				user = new UserInfoBean();
      				user.setUserId(operatorForm.getUserName());
      				user.setUserName(operatorForm.getFirstName()
      						+ (operatorForm.getLastName()==null?"":operatorForm.getLastName()));
      				user.setOrgId(operatorForm.getOrgId());
      				if (operatorForm.getPassword() != null
      						&& !operatorForm.getPassword().trim().equals("")) {
      					user.setPassword(encode1(operatorForm.getPassword()));
      				} else {
      					operatorForm = StrutsOperatorDelegate
      							.getUserDetail(operatorForm.getUserId());
      					user.setPassword(operatorForm.getPassword());
      				}
      				user.setSex(operatorForm.getSex().equals("男") ? "0" : "1");
      				user.setDpmtId(operatorForm.getDepartmentId().toString());
      				user.setTelNo(operatorForm.getTelephoneNumber());

      				ird.addUser(user);
      			}
             }
 			
			*/
 			/*
 			 * 同步分析系统结束
 			 */


             
             
             if(result == false)
             {
                 messages.add(FitechResource.getMessage(locale,resources,"update.failed","operator.info"));     
                 request.setAttribute(Config.MESSAGES,messages);
                 return mapping.findForward("user_update");
             }
             else
                 messages.add(FitechResource.getMessage(locale,resources,"update.success","operator.info"));  
         }
         catch (Exception e) 
         {
             log.printStackTrace(e);
             messages.add(FitechResource.getMessage(locale,resources,"update.failed","operator.info"));     
             request.setAttribute(Config.MESSAGES,messages);
             return mapping.findForward("user_update");   
         }
         
         //移除request或session范围内的属性
         FitechUtil.removeAttribute(mapping,request);
            
         if(messages.getMessages() != null && messages.getMessages().size() > 0)
           request.setAttribute(Config.MESSAGES,messages);
         return mapping.findForward("view");
    }
    
    /**
     * 数据加密
     * @param input
     * @return
     */
    public static String encode1(String input) {

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
