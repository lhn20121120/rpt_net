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
import com.fitech.net.adapter.StrutsOrgLayerDelegate;
import com.fitech.net.form.OrgLayerForm;


/**
 * 机构级别编辑功能的Action
 * @author jcm
 *
 */
public final class EditOrgLayerAction extends Action {
	private static FitechException log=new FitechException(EditOrgLayerAction.class);

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

      OrgLayerForm orgLayerForm = (OrgLayerForm)form;
     
     //将request对象传回
      RequestUtils.populate(orgLayerForm, request);
      
      String path="";
      String curPage="";
      if(request.getParameter("curPage")!=null) curPage=(String)request.getParameter("curPage");
      
      try {
    	  StrutsOrgLayerDelegate.selectOne(orgLayerForm);
    	 
    	  if (orgLayerForm != null){
    	  	  path = mapping.findForward("edit")!=null?mapping.findForward("edit").getPath():"";
    	  }else{
    		  path=path.equals("") ? "/viewOrgLayer.do" : path;
    	  }
    	  request.setAttribute(Config.CUR_PAGE_OBJECT,curPage);    	  
      }catch (Exception e) {
    	  log.printStackTrace( e);
      }     		
      return new ActionForward(path);
   }
}
   
  
   

