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

import com.cbrc.auth.adapter.StrutsOperatorDelegate;
import com.cbrc.auth.adapter.StrutsUserDelegate;
import com.cbrc.auth.form.OperatorForm;
import com.cbrc.smis.common.Config;
import com.cbrc.smis.security.Operator;
import com.cbrc.smis.util.FitechException;
import com.cbrc.smis.util.FitechMessages;
import com.cbrc.smis.util.FitechResource;
import com.fitech.fitosa.adapter.ImpReportData;
import com.fitech.fitosa.bean.UserInfoBean;
import com.cbrc.auth.util.Encoder;
/**
 *  添加用户信息
 *
 * @author 姚捷
 *
 * @struts.action
 *    path="/struts/insertOperator"
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
public final class InsertOperatorAction extends Action {
    private static FitechException log = new FitechException(InsertOperatorAction.class); 
    /**
     * 已使用hibernate  卞以刚 2011-12-28
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
        
        Operator operator = (Operator)request.getSession().getAttribute(Config.OPERATOR_SESSION_ATTRIBUTE_NAME);
        
        /**取得request范围内的请求参数，并存放在内*/
        OperatorForm operatorForm = new OperatorForm(); 
        RequestUtils.populate(operatorForm, request);
       
        boolean result = false;
        try{
            /**检查用户是否存在*/
        	/**已使用hibernate 卞以刚 2011-12-28
        	 * 影响对象：Operator*/
            boolean isExist = StrutsOperatorDelegate.isUserNameExist(operatorForm.getUserName().trim());
            if(isExist==false){
            	operatorForm.setSetOrgId(operator.getOrgId());
                /**插入记录*/
            	/**已使用hibernate 卞以刚 2011-12-28
            	 * 影响对象：Operator*/
            	if(Config.PORTAL)
            		result = StrutsOperatorDelegate.create(operatorForm,true); 
            	else
            		result = StrutsOperatorDelegate.create(operatorForm,false);
               
				/*
				 * 同步分析系统开始
				 */
                
                if(Config.ISADDFITOSA){
                	UserInfoBean bean = new UserInfoBean();
    				bean.setUserId(operatorForm.getUserName());
    				bean.setUserName(operatorForm.getFirstName()
    						+ (operatorForm.getLastName()==null?"":operatorForm.getLastName()));
    				bean.setOrgId(operatorForm.getOrgId());
    				if (operatorForm.getPassword() != null
    						&& !operatorForm.getPassword().trim().equals("")) {
    					bean.setPassword(encode1(operatorForm.getPassword()));
    				}
    				bean.setSex(operatorForm.getSex().equals("男") ? "0" : "1");
    				bean.setDpmtId(operatorForm.getDepartmentId().toString());
    				bean.setTelNo(operatorForm.getTelephoneNumber());
    				ImpReportData ird = new ImpReportData();
    				ird.setWebroot(Config.WEBROOTPATH);
    				ird.addUser(bean);
                }
				

				/*
				 * 同步分析系统结束
				 */
                
                if(result==true)
                    messages.add(FitechResource.getMessage(locale,resources,"operator.save.success"));
                else{ /**错误则返回原页面*/                
                    messages.add(FitechResource.getMessage(locale,resources,"save.failed","operator.info"));
                    request.setAttribute(Config.MESSAGES,messages);
                    return mapping.findForward("user_add");
                }                                
            }else{
                messages.add(FitechResource.getMessage(locale,resources,"operator.save.failed"));
                request.setAttribute(Config.MESSAGES,messages);
                return mapping.findForward("user_add");               
            }   
        }catch (Exception e){
            /**错误则返回原页面*/           
        	log.printStackTrace(e);           
        	messages.add(FitechResource.getMessage(locale,resources,"save.failed","operator.info"));            
        	request.setAttribute(Config.MESSAGES,messages);        
        	return mapping.findForward("user_add");
        }
        
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
