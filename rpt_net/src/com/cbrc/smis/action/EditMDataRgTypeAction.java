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
import com.cbrc.smis.form.MDataRgTypeForm;
import com.cbrc.smis.util.FitechException;


/**
 * 数据范围编辑功能的Action
 * @author 唐磊
 *
 * @struts.action
 *    path="/struts/editMDataRgType"
 *
 * @struts.action-forward
 *    name="form"
 *    path="/struts/formMDataRgType.jsp"
 *    redirect="false"
 *

 */
public final class EditMDataRgTypeAction extends Action {
	private static FitechException log=new FitechException(EditMDataRgTypeAction.class);

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

      MDataRgTypeForm mDataRgTypeForm = (MDataRgTypeForm)form;
     
     //将request对象传回
      RequestUtils.populate(mDataRgTypeForm, request);
      
      String path="";
      String curPage="";
      if(request.getParameter("curPage")!=null) curPage=(String)request.getParameter("curPage");
      
      try {
    	   com.cbrc.smis.adapter.StrutsMDataRgTypeDelegate.selectOne(mDataRgTypeForm);
    	 
    	  if (mDataRgTypeForm==null) 
    	  {
    		  // System.out.println("mDataRgTypeForm curName EMPTY!!!");
    	  }
    	
    	 path=mapping.findForward("edit")!=null?mapping.findForward("edit").getPath():"";
         request.setAttribute(Config.CUR_PAGE_OBJECT,curPage);    	  
      }catch (Exception e) {
    	  log.printStackTrace( e);
      }
     
      		path=path.equals("")?"/config/ViewDataRangeType.do":path;
      		
        	return new ActionForward(path);
       }
}
   
  
   

