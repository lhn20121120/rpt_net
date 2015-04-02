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
import com.fitech.net.common.StringTool;
import com.cbrc.smis.util.FitechException;
import com.fitech.net.adapter.StrutsTargetDefineWarnDelegate;
import com.fitech.net.form.TargetDefineWarnForm;

public final class EditWarnInitAction extends Action {
	private static FitechException log=new FitechException(EditWarnInitAction.class);

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

      TargetDefineWarnForm  targetDefineWarnForm = (TargetDefineWarnForm)form;
     
     //将request对象传回
      RequestUtils.populate(targetDefineWarnForm, request);
      //处理参数传递产生的乱码问题
      targetDefineWarnForm.setTargetDefineName(request.getParameter("targetDefineName"));
      
      String path="";
      String curPage="";
      if(request.getParameter("curPage")!=null) curPage=(String)request.getParameter("curPage");
      try
      {
    	  
    	  List list=StrutsTargetDefineWarnDelegate.findAll(targetDefineWarnForm,com.fitech.net.config.Config.Target_Warn);
    	 
    	  if(list!=null && list.size()>0)
    	  {
    		  
    		  StringBuffer js=new StringBuffer("var arrAC=new Array();\nvar i=0;\n");
    		  TargetDefineWarnForm acForm=null;
				for(int j=0;j<list.size();j++){
					acForm=(TargetDefineWarnForm)list.get(j);
					js.append("arrAC[i++]=new AbnormityChange(\"" + 
							acForm.getUpLimit() + "\",\"" +
							acForm.getDownLimit() + "\",\"" +
							acForm.getColor().trim() + "\"," +
							"3"+","+
							"54" + "," +
							"45" + ");\n");
				}
				if(!js.toString().equals("")) request.setAttribute("JS",js);
				
    	  }
    	  
    	  request.setAttribute("ObjForm",targetDefineWarnForm);
    	  request.setAttribute(Config.CUR_PAGE_OBJECT,curPage);
      }
      catch(Exception e)
      {
    	  log.printStackTrace(e);
    	  
      }
      if(targetDefineWarnForm!=null)
      return mapping.findForward("edit");
      else return mapping.findForward("view");
   }
}