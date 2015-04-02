package com.fitech.net.action;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.RequestUtils;

import com.cbrc.smis.util.FitechException;
import com.fitech.net.adapter.StrutsActuTargetResultDelegate;
import com.fitech.net.form.ActuTargetResultForm;

public final class OptionWarnDetailAction extends Action {
	private static FitechException log=new FitechException(OptionWarnDetailAction.class);

   /**
    * Performs action.
    * @param mapping Action mapping.
    * @param form Action form.
    * @param request HTTP request.
    * @param response HTTP response.
    * @exception IOException  是否有IO异常，如有则捕捉并抛出
    * @exception ServletException 是否有Servlet异常，如有则捕捉并抛出
    */
   public ActionForward execute(
      ActionMapping mapping,
      ActionForm form,
      HttpServletRequest request,
      HttpServletResponse response
   )
      throws IOException, ServletException {

	 ActuTargetResultForm  actuTargetResultForm = (ActuTargetResultForm)form;
     String tid=request.getParameter("id");
     String DeId=request.getParameter("targetDefineId");
     String repFreId=request.getParameter("repFreId");
     String dataRangeId=request.getParameter("dataRangeId"); 
     //将request对象传回
     RequestUtils.populate(actuTargetResultForm, request);
     
     request.setAttribute("actuTargetResultForm",actuTargetResultForm);
     
      String path="";
      String curPage="";
     if(request.getParameter("curPage")!=null) curPage=(String)request.getParameter("curPage");
      try
      {
    	  StrutsActuTargetResultDelegate.searchDetail(actuTargetResultForm,com.fitech.net.config.Config.Target_Warn);
     	  //显示成百分比形式
    	  actuTargetResultForm.setAllWarnMessage(actuTargetResultForm.getTargetResult().floatValue()+"%");   
	
      }
      catch(Exception e)
      {
    	  log.printStackTrace(e);    	  
      }  
      
      request.setAttribute("repFreId",repFreId);
	  request.setAttribute("dataRangeId",dataRangeId);
      request.setAttribute("tid",tid);
	  request.setAttribute("DeId",DeId);
	  request.setAttribute("orgId",request.getParameter("orgId"));
	  request.setAttribute("curPage",curPage);
       return mapping.findForward("option");
   }
}