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
import com.cbrc.smis.form.ValidateTypeForm;
import com.cbrc.smis.util.FitechException;


/**
 * 校验类型编辑功能的Action
 * @author 唐磊
 *
 * @struts.action
 *    path="/struts/editValidateType"
 *
 * @struts.action-forward
 *    name="form"
 *    path="/struts/formValidateType.jsp"
 *    redirect="false"
 *

 */
public final class EditValidateTypeAction extends Action {
	private static FitechException log=new FitechException(EditValidateTypeAction.class);

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

      ValidateTypeForm validateTypeForm = (ValidateTypeForm)form;
     
     //将request对象传回
      RequestUtils.populate(validateTypeForm, request);
      
      String path="";
      String curPage="";
      if(request.getParameter("curPage")!=null) curPage=(String)request.getParameter("curPage");
      
      try {
    	   com.cbrc.smis.adapter.StrutsValidateTypeDelegate.selectOne(validateTypeForm);
    	 
    	  if (validateTypeForm==null) 
    	  {
    		  // System.out.println("validateTypeForm validateTypeName  EMPTY!!!");
    	  }
    	
    	 path=mapping.findForward("edit")!=null?mapping.findForward("edit").getPath():"";
         request.setAttribute(Config.CUR_PAGE_OBJECT,curPage);    	  
      }catch (Exception e) {
    	  log.printStackTrace( e);
      }
     
      		path=path.equals("")?"/config/ViewCurVerifyType.do":path;
      		
        	return new ActionForward(path);
       }
}
   
  
   

