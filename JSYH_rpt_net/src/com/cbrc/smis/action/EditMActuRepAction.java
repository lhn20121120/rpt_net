package com.cbrc.smis.action;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.MessageResources;
import org.apache.struts.util.RequestUtils;

import com.cbrc.smis.adapter.StrutsMActuRepDelegate;
import com.cbrc.smis.adapter.StrutsMChildReportDelegate;
import com.cbrc.smis.common.Config;
import com.cbrc.smis.form.MActuRepForm;
import com.cbrc.smis.form.MChildReportForm;
import com.cbrc.smis.util.FitechMessages;
import com.cbrc.smis.util.FitechResource;

/**
 * 报表的报送频度与频率修改前的初始化操作类
 * 
 * @author rds
 * @serialData 2005-12-22 12:43
 */
public final class EditMActuRepAction extends Action {
   /**
    * 已使用hibernate 卞以刚 2011-12-22
    * Performs action.
    * @param mapping Action mapping.
    * @param form Action form.
    * @param request HTTP request.
    * @param response HTTP response.
    * @exception IOException if an input/output error occurs
    * @exception ServletException if a servlet exception occurs
    */
   public ActionForward execute(
      ActionMapping mapping,
      ActionForm form,
      HttpServletRequest request,
      HttpServletResponse response
   )
      throws IOException, ServletException {
	  Locale locale = getLocale(request);
	  MessageResources resources = getResources(request);
		
	  HttpSession session=request.getSession();
		
	  FitechMessages messages = new FitechMessages();
	  
      MActuRepForm mActuRepForm = (MActuRepForm)form;
      RequestUtils.populate(mActuRepForm, request);
      
      String curPage="";
      if(request.getParameter("curPage")!=null) curPage=(String)request.getParameter("curPage");
    	  
      boolean flag=false;
      
      try {
    	  //修改。。不要OAT字段
    	// if(mActuRepForm.getOATId()==null) mActuRepForm.setOATId(StrutsOATDelegate.getFirstOATId());
    		
    	 /**已使用hibernate 卞以刚 2011-12-22**/
         List list=StrutsMActuRepDelegate.getMActuRep(mActuRepForm.getChildRepId(),
        		 mActuRepForm.getVersionId(),
        		 //mActuRepForm.getOATId());
                        null);
         
         StringBuffer js=new StringBuffer("var arrMActuRep=new Array();\nvar i=0;\n");
         if(list!=null && list.size()>0){
        	 MActuRepForm _form=null;
        	 for(int i=0;i<list.size();i++){
        		 _form=(MActuRepForm)list.get(i);
        		 js.append("arrMActuRep[i++]=new MActuRep(" + _form.getDataRangeId() + "," +
        				 _form.getRepFreqId() + "," +
        				 (_form.getNormalTime()==null?"\"\"":String.valueOf(_form.getNormalTime())) + "," +
        				 (_form.getDelayTime()==null?"\"\"":String.valueOf(_form.getDelayTime())) +");\n");
        	 }
         }
         // System.out.println(js);
         if(!js.toString().equals("")){
        	 request.setAttribute("JS",js);
        	 flag=true;
         }
         
         /**已使用hibernate 卞以刚 2011-12-22**/
         MChildReportForm mChildReportForm=new StrutsMChildReportDelegate().getMChildReport(
    			 mActuRepForm.getChildRepId(),mActuRepForm.getVersionId()); 
         if(mChildReportForm!=null){
        	 request.setAttribute("ReportName",mChildReportForm.getReportName());
         }
      } catch (Exception e) {
    	  messages.add(FitechResource.getMessage(locale,resources,"bspl.mod.init"));
      }

      if(messages!=null && messages.getSize()>0) request.setAttribute(Config.MESSAGES,messages);
      
      request.setAttribute("curPage",curPage);
      
      if(flag==true){
    	  request.setAttribute("OATId",mActuRepForm.getOATId());
    	  return mapping.getInputForward();
   	  }else{
    	  return mapping.findForward("back");
      }
   }
   
   /**
    * Get the named value as a string from the request parameter, or from the
    * request obj if not found.
    * 
    * @param req The request object.
    * @param name The name of the parameter.
    *
    * @return The value of the parameter as a String.
    */
   private String getParameter(HttpServletRequest req, String name) {
      String value = req.getParameter(name);
      if (value == null) {
         value = req.getAttribute(name).toString();
      }
      return value;
   }
}
