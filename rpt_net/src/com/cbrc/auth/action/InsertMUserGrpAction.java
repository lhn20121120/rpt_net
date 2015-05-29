package com.cbrc.auth.action;

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

import com.cbrc.auth.adapter.StrutsMUserGrpDelegate;
import com.cbrc.auth.form.MUserGrpForm;
import com.cbrc.smis.common.Config;
import com.cbrc.smis.security.Operator;
import com.cbrc.smis.util.FitechException;
import com.cbrc.smis.util.FitechMessages;
import com.cbrc.smis.util.FitechResource;

/**
 * 新增用户组信息
 *
 * @author 姚捷
 *
 * @struts.action
 *    path="/struts/insertMUserGrp"
 *    name="MUserGrpForm"
 *    scope="session"
 *    validate="false"
 *
 * @struts.action-forward
 *    name="view"
 *    path="/struts/viewMUserGrp.do"
 *    redirect="false"
 *

 */
public final class InsertMUserGrpAction extends Action {
    private static FitechException log = new FitechException(InsertMUserGrpAction.class);
   /**
    *  已使用hibernate 卞以刚 2011-12-28
     * 影响对象：MUserGrp
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
       
       /**取得request范围内的请求参数，并存放在内*/
       MUserGrpForm mUserGrpForm = new MUserGrpForm(); 
       RequestUtils.populate(mUserGrpForm, request);
      
       
       HttpSession session = request.getSession();
       Operator operator = null; 
       if(session.getAttribute(Config.OPERATOR_SESSION_ATTRIBUTE_NAME)!=null)
           operator = (Operator)session.getAttribute(Config.OPERATOR_SESSION_ATTRIBUTE_NAME);
              
       boolean result = false;
       try{
    	   if(StrutsMUserGrpDelegate.isExist(mUserGrpForm) == false){
    		   mUserGrpForm.setSetOrgId(operator.getOrgId());
       	   		
    		   /**插入记录*/	   
    		   /** 已使用hibernate 卞以刚 2011-12-28
    		    * 影响对象：MUserGrp*/
    		   result = StrutsMUserGrpDelegate.create(mUserGrpForm);	           
    		   if(result==true)	           
    			   messages.add("成功添加用户！请点击“权限分配”为该用户组分配权限！");	            
    		   else{ /**错误则返回原页面*/	                		                
    			   messages.add(FitechResource.getMessage(locale,resources,"save.failed","userGrp.info"));	               
    			   request.setAttribute(Config.MESSAGES,messages);	               
    			   return mapping.findForward("user_group_add");	            
    		   }       	   
    	   }else{	       
    		   messages.add("用户组名称已经存在！");					
    		   request.setAttribute(Config.MESSAGES,messages);				
    		   return mapping.findForward("user_group_add");       	   
    	   }          
       }catch (Exception e){           
    	   /**错误则返回原页面*/          
    	   log.printStackTrace(e);          
    	   messages.add(FitechResource.getMessage(locale,resources,"save.failed","userGrp.info"));           
    	   request.setAttribute(Config.MESSAGES,messages);          
    	   return mapping.findForward("user_group_add");
       }
       
       if(messages.getMessages() != null && messages.getMessages().size() > 0)
           request.setAttribute(Config.MESSAGES,messages);
       
       return new ActionForward("/popedom_mgr/viewMUserGrp.do");
   }
}
