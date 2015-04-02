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

	public final class OATAddAction extends Action {
		   private static FitechException log = new FitechException(OATAddAction.class);
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
	       
	       /**取得request范围内的请求参数，并存放在内*/
	       OrgActuTypeForm oatActionForm = new OrgActuTypeForm(); 
	       RequestUtils.populate(oatActionForm, request);
	      
	       boolean result = false;
	       try 
	       {
	           /**插入记录*/
	           result = StrutsOATDelegate.create(oatActionForm);
	           if(result==true)
	               messages.add(FitechResource.getMessage(locale,resources,"save.success","OAT.info"));
	           else /**错误则返回原页面*/
	           {
	               messages.add(FitechResource.getMessage(locale,resources,"save.failed","OAT.info"));
	               request.setAttribute(Config.MESSAGES,messages);
	               return mapping.getInputForward();
	           }
	       }
	       catch (Exception e) 
	       {
	           /**错误则返回原页面*/
	          log.printStackTrace(e);
	          messages.add(FitechResource.getMessage(locale,resources,"save.failed","OAT.info")); 
	          request.setAttribute(Config.MESSAGES,messages);
	          return mapping.getInputForward();
	       }
	       
	       if(messages.getMessages() != null && messages.getMessages().size() > 0)
	           request.setAttribute(Config.MESSAGES,messages);
	       
	       return new ActionForward("/template/oat/OATInit.do");
	}
	}

