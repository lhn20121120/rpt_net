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
import com.fitech.net.form.TargetNormalForm;


/**
 * ָ��ҵ���޸�
 * @author masclnj
 *
 */
public final class EditTargetNormalAction extends Action {
	private static FitechException log=new FitechException(EditTargetNormalAction.class);

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

	   TargetNormalForm targetNormal = (TargetNormalForm)form;
     
     //��request���󴫻�
      RequestUtils.populate(targetNormal, request);
      
      String path="";
      String curPage="";
      if(request.getParameter("curPage")!=null) curPage=(String)request.getParameter("curPage");
      
      try {
    	  com.fitech.net.adapter.StrutsTargetDelegate.selectOne(targetNormal);   	 
    	
    	 path=mapping.findForward("bad_edit")!=null?mapping.findForward("bad_edit").getPath():"";
         request.setAttribute(Config.CUR_PAGE_OBJECT,curPage);    	  
      }catch (Exception e) {
    	  log.printStackTrace( e);
      }
     
      		path=path.equals("")?"/target/viewTargetNormal.do":path;
      		
        	return new ActionForward(path);
       }
}
   
  
   

