package com.fitech.gznx.action;

import java.io.IOException;
import java.util.ArrayList;
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

import com.cbrc.smis.adapter.StrutsMRepFreqDelegate;
import com.cbrc.smis.common.Config;
import com.cbrc.smis.form.MActuRepForm;
import com.cbrc.smis.form.MRepFreqForm;
import com.cbrc.smis.util.FitechMessages;
import com.cbrc.smis.util.FitechResource;
import com.fitech.gznx.po.AfTemplate;
import com.fitech.gznx.service.StrutsAFActuRepDelegate;
import com.fitech.net.adapter.StrutsExcelData;

public class EditAFActuRepAction extends Action {
	   /**
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
		  /** 报表选中标志 **/
			String reportFlg = "0";
			
			if (session.getAttribute(Config.REPORT_SESSION_FLG) != null){
				reportFlg = (String) session.getAttribute(Config.REPORT_SESSION_FLG);
			}
		  FitechMessages messages = new FitechMessages();
		  
	      MActuRepForm mActuRepForm = (MActuRepForm)form;
	      RequestUtils.populate(mActuRepForm, request);
	      
	      String curPage="";
	      if(request.getParameter("curPage")!=null) curPage=(String)request.getParameter("curPage");
	    	  
	      boolean flag=true;
	      List reqList = null;
	      try {
	    	  //修改。。不要OAT字段
	    	// if(mActuRepForm.getOATId()==null) mActuRepForm.setOATId(StrutsOATDelegate.getFirstOATId());
	    		 
	         List list=StrutsAFActuRepDelegate.select(mActuRepForm.getChildRepId(),
	        		 mActuRepForm.getVersionId());
	         
	         reqList = new ArrayList();
	         List results = null;
	         if(reportFlg.equals(com.fitech.gznx.common.Config.PBOC_REPORT)){
	        	 results = StrutsMRepFreqDelegate.findRHAllFeq();
	         } else{
	        	 results = StrutsMRepFreqDelegate.findAllFeq();
	         }	          
	         for(int i=0;i<results.size();i++){
	        	 MRepFreqForm form1 = (MRepFreqForm) results.get(i);
	        	 boolean reqflag = false;
	        	 int index = 0;
				for(int j=0;j<list.size();j++){
					MActuRepForm form2 =(MActuRepForm) list.get(j);
					if(form1.getRepFreqId().intValue() == form2.getRepFreqId().intValue()){
						reqflag = true;
						index = j;
						break;
					}
				}
	        	 
	        	 MActuRepForm areqForm = new MActuRepForm();
	
	        	 areqForm.setDataRangeId(form1.getRepFreqId());
	        	 areqForm.setRepFreqName(form1.getRepFreqName());
	        	 if(reqflag){
	        		 areqForm.setRepFreqId(form1.getRepFreqId());
	        		 areqForm.setDelayTime(((MActuRepForm) list.get(index)).getDelayTime());
	        		 areqForm.setNormalTime(((MActuRepForm) list.get(index)).getNormalTime());
	        	 }
	        	 reqList.add(areqForm);
	         }
	         if(reqList!=null && reqList.size()!=0)
	             request.setAttribute(Config.RECORDS,reqList);
	         request.setAttribute("ObjForm",mActuRepForm);
	         
	         AfTemplate af = StrutsExcelData.getTemplateSimple(mActuRepForm.getVersionId(), mActuRepForm.getChildRepId());
	         String reportName=af!=null?af.getTemplateName():mActuRepForm.getReportName();
	         request.setAttribute("ReportName",reportName);

	      } catch (Exception e) {
	    	  messages.add(FitechResource.getMessage(locale,resources,"bspl.mod.init"));
	      }

	      if(messages!=null && messages.getSize()>0) request.setAttribute(Config.MESSAGES,messages);
	      
	      request.setAttribute("curPage",curPage);
	      
	      if(flag==true){
	    	  
	    	  return mapping.getInputForward();
	   	  }else{
	    	  return mapping.findForward("back");
	      }
	   }
	   

}
