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
import com.fitech.net.bean.Figure;
import com.fitech.net.figure.Config;
import com.fitech.net.form.ActuTargetResultForm;

public final class ViewPreStandAction extends Action {
	private static FitechException log=new FitechException(ViewPreStandAction.class);

   /**
    * Performs action.
    * @param mapping Action mapping.
    * @param form Action form.
    * @param request HTTP request.
    * @param response HTTP response.
    * @exception IOException  �Ƿ���IO�쳣��������׽���׳�
    * @exception ServletException �Ƿ���Servlet�쳣��������׽���׳�
    */
   public ActionForward execute(
      ActionMapping mapping,
      ActionForm form,
      HttpServletRequest request,
      HttpServletResponse response
   )
      throws IOException, ServletException {

	  ActuTargetResultForm  actuTargetResultForm = (ActuTargetResultForm)form;
       
      //��request���󴫻�
      RequestUtils.populate(actuTargetResultForm, request);
//      actuTargetResultForm.setPreStandardValue(request.getParameter("preStandardValue"));
      String path="";
      String curPage="";
      if(request.getParameter("curPage")!=null) curPage=(String)request.getParameter("curPage");
      try
      {
    	
    	  StrutsActuTargetResultDelegate.searchDetail(actuTargetResultForm,com.fitech.net.config.Config.Target_Pre_Standard);
          //Figure bean 
    	  Figure fg=new Figure();
    	  fg.setTargetDefineName(actuTargetResultForm.getTargetDefineName().trim()+Config.SPLIT_TITLE+com.fitech.net.config.Config.Target_Pre_Standard);    	  
    	  fg.setCurrentValue(Float.parseFloat(actuTargetResultForm.getPreStandardValue())+"");
    	  fg.setAllWarnMessage(actuTargetResultForm.getAllWarnMessage());
    	  request.setAttribute("figure",fg);   	  
    	  //��ʾ�ɰٷֱ���ʽ    	  
    	  actuTargetResultForm.setPreStandardValue(actuTargetResultForm.getPreStandardValue()+"%");
      }
      catch(Exception e)
      {
    	  log.printStackTrace(e);
    	  
      }
      
    	   
       return mapping.findForward("view");
   }
}