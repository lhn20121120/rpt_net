package com.cbrc.smis.action;

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

import com.cbrc.smis.adapter.StrutsOATDelegate;
import com.cbrc.smis.common.Config;
import com.cbrc.smis.form.OrgActuTypeForm;
import com.cbrc.smis.util.FitechException;
import com.cbrc.smis.util.FitechMessages;
import com.cbrc.smis.util.FitechResource;
import com.cbrc.smis.util.FitechUtil;

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
public final class OATUpdateAction extends Action {
    private static FitechException log = new FitechException(OATUpdateAction.class); 
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
        OrgActuTypeForm orgActuTypeForm = new OrgActuTypeForm();
        RequestUtils.populate(orgActuTypeForm,request);
        
        boolean result = false;
        
        try 
         {
             result = StrutsOATDelegate.update(orgActuTypeForm); 
             if(result == false)
             {
                 messages.add(FitechResource.getMessage(locale,resources,"update.failed","OAT.info"));     
                 request.setAttribute(Config.MESSAGES,messages);
                 return mapping.findForward("OAT_Update");
             }
             else
                 messages.add(FitechResource.getMessage(locale,resources,"update.success","OAT.info"));  
         }
         catch (Exception e) 
         {
             log.printStackTrace(e);
             messages.add(FitechResource.getMessage(locale,resources,"update.failed","OAT.info"));     
             request.setAttribute(Config.MESSAGES,messages);
             return mapping.findForward("OAT_Update");   
         }
         
         //移除request或session范围内的属性
         FitechUtil.removeAttribute(mapping,request);
            
         if(messages.getMessages() != null && messages.getMessages().size() > 0)
           request.setAttribute(Config.MESSAGES,messages);
         return mapping.findForward("view");
    }
 }
