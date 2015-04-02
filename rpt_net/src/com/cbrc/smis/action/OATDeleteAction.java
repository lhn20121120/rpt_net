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

/**
 * 删除频度类别
 *
 * @author gujie 
 * @date 2006-02-06
 * 
 *
 *
 * @struts.action-forward
 *    name="all"
 *    path="/struts/getAll.do"
 *    redirect="false"
 *

 */
public final class OATDeleteAction extends Action {
	   private static FitechException log = new FitechException(OATDeleteAction.class);
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
	   
       MessageResources resources = getResources(request);
       FitechMessages messages = new FitechMessages();
       Locale locale = request.getLocale();
       
       /**从request里面取出参数，放在里面*/
       OrgActuTypeForm orgActuTypeForm = new OrgActuTypeForm();
       RequestUtils.populate(orgActuTypeForm,request);
       
       try{   
    	   // System.out.println("XXXXXXXXXXXXXXXX");
    	   boolean result = StrutsOATDelegate.remove(orgActuTypeForm);
    	   if(result==false)/**删除失败*/
               messages.add(FitechResource.getMessage(locale,resources,"delete.failed","OAT.info"));
           else/**删除成功*/
               messages.add(FitechResource.getMessage(locale,resources,"delete.success","OAT.info"));
       }
       catch(Exception e){
    	   log.printStackTrace(e);
    	   messages.add(FitechResource.getMessage(locale,resources,"delete.failed","OAT.info"));
    	   
       }
       if(messages.getMessages() != null && messages.getMessages().size() != 0)
       request.setAttribute(Config.MESSAGES,messages);
       return new ActionForward("/template/oat/OATInit.do");
   }
  }


