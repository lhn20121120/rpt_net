package com.cbrc.smis.action;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.MessageResources;
import org.apache.struts.util.RequestUtils;

import com.cbrc.smis.common.Config;
import com.cbrc.smis.form.ReportInForm;
import com.cbrc.smis.util.FitechException;
import com.cbrc.smis.util.FitechMessages;
import com.cbrc.smis.util.FitechUtil;
import com.fitech.dataCollect.Util;

/**
 * 下载汇总数据Action
 * @author gen
 *
 */
public final class ViewCollectDownAction extends Action {
	private FitechException log=new FitechException(ViewCollectDownAction.class);
	
	
   /**
    * @param result 查询返回标志,如果成功返回true,否则返回false
    * @param ReportInForm 
    * @param request 
    * @exception Exception 有异常捕捉并抛出
    */
	public ActionForward execute(
      ActionMapping mapping,
      ActionForm form,
      HttpServletRequest request,
      HttpServletResponse response
	)throws IOException, ServletException {

		FitechMessages messages = new FitechMessages();
		MessageResources resources = getResources(request);
	   
		// 是否有Request
		ReportInForm reportInForm = (ReportInForm)form ;	   
		RequestUtils.populate(reportInForm, request);
			    


		List excelList=null;
		

		int mon=Integer.parseInt(reportInForm.getSetDate());
		
      
		try{
			excelList = Util.getExcels(reportInForm.getYear().toString(),reportInForm.getSetDate());
	   	}catch (Exception e){
			log.printStackTrace(e);
			messages.add("汇总数据浏览失败！");		
		}
		//移除request或session范围内的属性
		FitechUtil.removeAttribute(mapping,request);

	 	
	 	 if(messages.getMessages() != null && messages.getMessages().size() > 0)
		   	  request.setAttribute(Config.MESSAGES,messages);
	 	 
	 	 if(excelList!=null && excelList.size()>0)
	 		 request.setAttribute(Config.RECORDS,excelList);
	 	 else
	 		 request.setAttribute(Config.RECORDS,null);     
	 	 request.setAttribute("year",reportInForm.getYear());
	 	 request.setAttribute("mon",reportInForm.getSetDate());
	 	 
	 	 return mapping.findForward("view");
	}

   


	
}