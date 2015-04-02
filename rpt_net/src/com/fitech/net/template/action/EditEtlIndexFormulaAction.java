package com.fitech.net.template.action;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.cbrc.smis.common.Config;
import com.cbrc.smis.util.FitechException;
import com.cbrc.smis.util.FitechUtil;
import com.fitech.net.adapter.StrutsFormulaDelegate;
import com.fitech.net.common.ETLXmlToXml;
import com.fitech.net.form.EtlIndexForm;


/**
 * 取数公式指标编辑功能的Action
 * @author wh
 *
 */
public final class EditEtlIndexFormulaAction extends Action {
	private static FitechException log = new FitechException(EditEtlIndexFormulaAction.class);

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

      EtlIndexForm etlIndexForm = (EtlIndexForm)form;
     
     //将request对象传回
  //    RequestUtils.populate(etlIndexForm, request);
      
      String path="";
      String curPage="";
      if(request.getParameter("curPage")!=null) curPage=(String)request.getParameter("curPage");
      
      try {
    	  StrutsFormulaDelegate.selectOneEtlIndex(etlIndexForm);
    	 
    	  if (etlIndexForm != null){
    	  	  path = mapping.findForward("edit")!=null?mapping.findForward("edit").getPath():"";
    	  }else{
    		  path=path.equals("") ? "/template/view/TargetFormula.jsp" : path;
    	  }
    	  request.setAttribute(Config.CUR_PAGE_OBJECT,curPage);    	  
      }catch (Exception e) {
    	  log.printStackTrace( e);
      }  
      FitechUtil.removeAttribute(mapping,request);
      request.setAttribute("indexName",etlIndexForm.getIndexName());
      request.setAttribute("IdrFormula",etlIndexForm.getFormual().trim());
      request.setAttribute("desc",etlIndexForm.getDesc());
      ETLXmlToXml.CreateETLXml(ETLXmlToXml.ParserXML());
      return new ActionForward(path);
   }
}
   
  
   

