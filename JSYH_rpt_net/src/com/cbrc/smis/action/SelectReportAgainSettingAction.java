package com.cbrc.smis.action;

import java.io.IOException;
import java.util.List;

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

import com.cbrc.smis.adapter.StrutsReportInDelegate;
import com.cbrc.smis.common.ApartPage;
import com.cbrc.smis.common.Config;
import com.cbrc.smis.form.ReportInForm;
import com.cbrc.smis.security.Operator;
import com.cbrc.smis.util.FitechException;
import com.cbrc.smis.util.FitechMessages;


/** 
 * @author 曹发根
 * 模块名:报表重报设定查询  
 */
public final class SelectReportAgainSettingAction extends Action {
	private static FitechException log = new FitechException(SelectReportAgainSettingAction.class);  
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
	   MessageResources resources=getResources(request);
	   FitechMessages messages = new FitechMessages();
		ReportInForm reportInForm = (ReportInForm) form;
		RequestUtils.populate(reportInForm, request);
		int recordCount =0; //记录总数
		int offset=0; //偏移量
		int limit=0;  //每页显示的记录条数
		List list = null;
		
		ApartPage aPage=new ApartPage();
	   	String strCurPage=request.getParameter("curPage");
		if(strCurPage!=null){
		    if(!strCurPage.equals(""))
		      aPage.setCurPage(new Integer(strCurPage).intValue());
		}
		else
			aPage.setCurPage(1);
		//计算偏移量
		offset=(aPage.getCurPage()-1)*Config.PER_PAGE_ROWS; 
		limit = Config.PER_PAGE_ROWS;	
        
         /***
         * 取得当前用户的权限信息
         * @author 姚捷
         */
        HttpSession session = request.getSession();
        Operator operator = null; 
        if(session.getAttribute(Config.OPERATOR_SESSION_ATTRIBUTE_NAME)!=null)
            operator = (Operator)session.getAttribute(Config.OPERATOR_SESSION_ATTRIBUTE_NAME);
		
        try 

		{
	   		//取得记录总数
	   		recordCount = StrutsReportInDelegate.getReportAgainSettingCount(reportInForm,operator);
	   		//显示分页后的记录
	   		if(recordCount > 0)
		   	    list = StrutsReportInDelegate.getReportAgainSettingRecord(reportInForm,offset,limit,operator);  
		}
		catch (Exception e) 
		{
			log.printStackTrace(e);
			messages.add(resources.getMessage("report_again_setting.select.fail"));		
		}
		//把ApartPage对象存放在request范围内
	 	aPage.setTerm(this.getTerm(reportInForm));
	 	aPage.setCount(recordCount);
	 	request.setAttribute(Config.APART_PAGE_OBJECT,aPage);
		
	 	if(messages.getMessages() != null && messages.getMessages().size() > 0)
	   	  request.setAttribute(Config.MESSAGES,messages);
	 	if(list!=null && list.size()!=0)
		  request.setAttribute(Config.RECORDS,list);
	 	//将查询条件存放在request范围内
	 	request.setAttribute("form",reportInForm);
	 	
	 	return mapping.getInputForward();
   }
   /**
    * 取得查询条件url  
    * @param logInForm
    * @return
    */
   public String getTerm(ReportInForm reportInForm)
   {
	   String term="";
	   
	   String repName  = reportInForm.getRepName();
	   String orgName = reportInForm.getOrgName();
	   String childRepId = reportInForm.getChildRepId();
	   String startDate =reportInForm.getStartDate();
	   String endDate = reportInForm.getEndDate();
	   
	   if(repName!=null)
	   {
		   term += (term.indexOf("?")>=0 ? "&" : "?");
		   term += "repName="+repName.toString();   
	   }
	   if(orgName!=null && !orgName.equals(""))
	   {
		   term += (term.indexOf("?")>=0 ? "&" : "?");
		   term += "orgName="+orgName.toString();   
	   }
	   if(childRepId!=null && !childRepId.equals(""))
	   {
		   term += (term.indexOf("?")>=0 ? "&" : "?");
		   term += "childRepId="+childRepId.toString();   
	   }
	   if(startDate!=null && !startDate.equals(""))
	   {
		   term += (term.indexOf("?")>=0 ? "&" : "?");
		   term += "startDate="+startDate.toString();   
	   }
	   if(endDate!=null && !endDate.equals(""))
	   {
		   term += (term.indexOf("?")>=0 ? "&" : "?");
		   term += "endDate="+endDate.toString();   
	   }
	   if(term.indexOf("?")>=0)
		   term = term.substring(term.indexOf("?")+1);

	   return term;
	   
   }
}
