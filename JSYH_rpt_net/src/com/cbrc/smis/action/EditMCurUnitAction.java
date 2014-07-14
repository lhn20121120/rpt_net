package com.cbrc.smis.action;

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
import com.cbrc.smis.form.MCurUnitForm;
import com.cbrc.smis.util.FitechException;


/**
 * ���ҵ�λ�༭���ܵ�Action
 * @author ����
 *
 * @struts.action
 *    path="/struts/editMCurUnit"
 *
 * @struts.action-forward
 *    name="form"
 *    path="/struts/formMCurUnit.jsp"
 *    redirect="false"
 *

 */
public final class EditMCurUnitAction extends Action {
	private static FitechException log=new FitechException(EditMCurUnitAction.class);

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

      MCurUnitForm mCurUnitForm = (MCurUnitForm)form;
     
     //��request���󴫻�
      RequestUtils.populate(mCurUnitForm, request);
      
      String path="";
      String curPage="";
      if(request.getParameter("curPage")!=null) curPage=(String)request.getParameter("curPage");
      
      try {
    	   com.cbrc.smis.adapter.StrutsMCurUnitDelegate.selectOne(mCurUnitForm);
    	 
    	  if (mCurUnitForm==null) 
    	  {
    		  // System.out.println("mCurUnitForm curName EMPTY!!!");
    	  }
    	
    	 path=mapping.findForward("edit")!=null?mapping.findForward("edit").getPath():"";
         request.setAttribute(Config.CUR_PAGE_OBJECT,curPage);    	  
      }catch (Exception e) {
    	  log.printStackTrace( e);
      }
     
      		path=path.equals("")?"/config/ViewCurUnit.do":path;
      		
        	return new ActionForward(path);
       }
}
   
  
   

