package com.fitech.net.action;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.RequestUtils;

import com.cbrc.smis.common.Config;
import com.cbrc.smis.util.FitechException;
import com.fitech.net.adapter.StrutsTargetDefineDelegate;
import com.fitech.net.adapter.StrutsTargetDefineWarnDelegate;
import com.fitech.net.form.TargetDefineForm;
import com.fitech.net.form.TargetDefineWarnForm;

public final class ViewTargetDefineDetailAction extends Action {
	private static FitechException log=new FitechException(ViewTargetDefineDetailAction.class);

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

      TargetDefineForm  targetDefineForm = (TargetDefineForm)form;
     
     //将request对象传回
      RequestUtils.populate(targetDefineForm, request);
      String curPage="";
      if(request.getParameter("curPage")!=null) curPage=(String)request.getParameter("curPage");
      try
      {
    	  //取指标定义
    	  StrutsTargetDefineDelegate.selectone(targetDefineForm);
    	  TargetDefineWarnForm tform=new TargetDefineWarnForm ();
    	  tform.setTargetDefineId(targetDefineForm.getTargetDefineId());
    	  //取预警信息
    	  List listWarn=StrutsTargetDefineWarnDelegate.findAll(tform,com.fitech.net.config.Config.Target_Warn);
    	  //取比上期信息
    	  List listP=StrutsTargetDefineWarnDelegate.findAll(tform,com.fitech.net.config.Config.Target_Pre_Standard);
    	  
    	  request.setAttribute(Config.CUR_PAGE_OBJECT,curPage);
    	   
    	   
    	  if(listWarn!=null && listWarn.size()>0)
    		  request.setAttribute("Warn",listWarn);   	   
    	  if(listP!=null && listP.size()>0)   		
    		  request.setAttribute("PreStandard",listP);
      }catch(Exception e){
    	  log.printStackTrace(e);
      } 
      return mapping.findForward("view");
   }
}