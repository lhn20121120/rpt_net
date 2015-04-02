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
import com.cbrc.smis.form.MRepFreqForm;
import com.cbrc.smis.util.FitechException;


/**
 * 报表频度编辑功能的Action
 * @author 唐磊
 *
 * @struts.action
 *    path="/struts/editMRepFreq"
 *
 * @struts.action-forward
 *    name="form"
 *    path="/struts/formMRepFreq.jsp"
 *    redirect="false"
 *

 */
public final class EditMRepFreqAction extends Action {
	private static FitechException log=new FitechException(EditMRepFreqAction.class);

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

      MRepFreqForm mRepFreqForm = (MRepFreqForm)form;
     
     //将request对象传回
      RequestUtils.populate(mRepFreqForm, request);
      
      String path="";
      String curPage="";
      if(request.getParameter("curPage")!=null) curPage=(String)request.getParameter("curPage");
      
      try {
    	   com.cbrc.smis.adapter.StrutsMRepFreqDelegate.selectOne(mRepFreqForm);
    	 
    	  if (mRepFreqForm==null) 
    	  {
    		  // System.out.println("mRepFreqForm curName EMPTY!!!");
    	  }
    	
    	 path=mapping.findForward("edit")!=null?mapping.findForward("edit").getPath():"";
         request.setAttribute(Config.CUR_PAGE_OBJECT,curPage);    	  
      }catch (Exception e) {
    	  log.printStackTrace( e);
      }
     
      		path=path.equals("")?"/config/ViewCurRepFreqence.do":path;
      		
        	return new ActionForward(path);
       }
}
   
  
   

