package com.fitech.net.action;

import java.io.IOException;
import java.util.HashMap;

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
import com.fitech.net.bean.Figure;
import com.fitech.net.figure.Config;
import com.fitech.net.form.ActuTargetResultForm;

public final class ViewWarnDetailAction extends Action {
	private static FitechException log=new FitechException(ViewWarnDetailAction.class);

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
    
     String year=request.getParameter("year");
     String startMonth=request.getParameter("startMonth");
     String endMonth=request.getParameter("endMonth");
     
     
     int sm=1;
     int em=12;
   try{
	   if(startMonth!=null){
		   sm=Integer.parseInt(startMonth);
	   }
	   if(endMonth!=null){
		   em=Integer.parseInt(endMonth);
	   }
   }catch(Exception e1){
	   sm=1;
	   em=12;
	   e1.printStackTrace();
   }
     
     //将request对象传回
     RequestUtils.populate(actuTargetResultForm, request);

      String path="";
      String curPage="";
      if(request.getParameter("curPage")!=null) curPage=(String)request.getParameter("curPage");
      Figure fg=new Figure();
      try
      {
    	  StrutsActuTargetResultDelegate.searchDetail(actuTargetResultForm,com.fitech.net.config.Config.Target_Warn);
    	  //Figure Bean
    	 
    	  fg.setTargetDefineName(actuTargetResultForm.getTargetDefineName().trim()+Config.SPLIT_TITLE+com.fitech.net.config.Config.Target_Warn);    	  
    	  fg.setCurrentValue(actuTargetResultForm.getTargetResult().floatValue()+"");
    	  fg.setAllWarnMessage(actuTargetResultForm.getAllWarnMessage());
    	 // request.setAttribute("figure",fg);
    	  //显示成百分比形式
    	  actuTargetResultForm.setAllWarnMessage(actuTargetResultForm.getTargetResult().floatValue()+"%");    
    	  HashMap hm = StrutsActuTargetResultDelegate.searchTargetResult(actuTargetResultForm,year,sm,em);
    	  request.setAttribute("resultMap",hm);
    	  
      }
      catch(Exception e)
      {
    	  log.printStackTrace(e);  
      }   
          request.setAttribute("freq",actuTargetResultForm.getRepFreId());
    	  request.setAttribute("curPage",curPage); 
    	  request.setAttribute("figure",fg);
    	  
        return mapping.findForward("view");

   }
}