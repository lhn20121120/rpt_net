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

	public final class OATViewUpdateAction extends Action {
		   private static FitechException log = new FitechException(OATViewUpdateAction.class);
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
	       
	       /**ȡ��request��Χ�ڵ�������������������*/
	       OrgActuTypeForm oatActionForm = new OrgActuTypeForm();
	        RequestUtils.populate(oatActionForm,request);
	      
	        OrgActuTypeForm detail = null;
	   
	        try 
	         {
	             detail = StrutsOATDelegate.getOATDetail(oatActionForm.getOATId());
	         }
	         catch (Exception e) 
	         {
	             log.printStackTrace(e);
	             messages.add(FitechResource.getMessage(locale,resources,"select.fail","OAT.info"));      
	         }
	         
	         //�Ƴ�request��session��Χ�ڵ�����
	         FitechUtil.removeAttribute(mapping,request);
	         
	         if(messages.getMessages() != null && messages.getMessages().size() > 0)
	           request.setAttribute(Config.MESSAGES,messages);
	         if(detail!=null)
	               request.setAttribute("Detail",detail);
	          
	         return mapping.findForward("OAT_Update");
	}
	}

