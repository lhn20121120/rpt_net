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

import com.cbrc.smis.common.Config;
import com.cbrc.smis.util.FitechException;
import com.fitech.net.adapter.StrutsVParameterDelegate;
import com.fitech.net.form.VParameterForm;


/**
 * ������༭���ܵ�Action
 * @author James
 *
 */
public final class EditVParameterAction extends Action {
	private static FitechException log = new FitechException(EditVParameterAction.class);

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

      VParameterForm vParamForm = (VParameterForm)form;
     
     //��request���󴫻�
      RequestUtils.populate(vParamForm, request);
      
      String path="";
      String curPage="";
      if(request.getParameter("curPage")!=null) curPage=(String)request.getParameter("curPage");
      
      try {
    	  StrutsVParameterDelegate.selectOne(vParamForm);
    	 
    	  if (vParamForm != null){
    	  	  path = mapping.findForward("edit")!=null?mapping.findForward("edit").getPath():"";
    	  }else{
    		  path=path.equals("") ? "/config/ViewVParam.do" : path;
    	  }
    	  request.setAttribute(Config.CUR_PAGE_OBJECT,curPage);    	  
      }catch (Exception e) {
    	  log.printStackTrace( e);
      }     		
      return new ActionForward(path);
   }
}
   
  
   

