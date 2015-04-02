//Created by MyEclipse Struts
// XSL source (default): platform:/plugin/com.genuitec.eclipse.cross.easystruts.eclipse_4.0.1/xslt/JavaClass.xsl

package com.cbrc.auth.action;

import java.io.IOException;
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
import com.cbrc.auth.form.OperatorForm;
import com.cbrc.smis.common.Config;
import com.cbrc.smis.util.FitechException;
import com.cbrc.smis.util.FitechMessages;
import com.cbrc.smis.util.FitechResource;
import com.cbrc.smis.util.FitechUtil;

/** 
 *用户详细信息
 *@author 姚捷
 * 
 * XDoclet definition:
 * @struts.action path="/viewUserDetail" name="operatorForm" scope="request" validate="true"
 * @struts.action-forward name="user_detail" path="/popedom_mgr/user_mgr/user_detail.jsp"
 */
public class ViewUserDetailAction extends Action {
    private static FitechException log = new FitechException(ViewUserDetailAction.class);  
    /**
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
      
        OperatorForm detail = null;
         
      
         try 
         {
             detail = StrutsOperatorDelegate.getUserDetail(operatorForm.getUserId());
         }
         catch (Exception e) 
         {
             log.printStackTrace(e);
             messages.add(FitechResource.getMessage(locale,resources,"select.fail","operator.info"));      
         }
         
         //移除request或session范围内的属性
         FitechUtil.removeAttribute(mapping,request);
         
         if(messages.getMessages() != null && messages.getMessages().size() > 0)
           request.setAttribute(Config.MESSAGES,messages);
         if(detail!=null)
               request.setAttribute("Detail",detail);
          
         return mapping.findForward("user_detail");
    }
}
