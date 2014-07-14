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

import com.cbrc.smis.adapter.StrutsLogInDelegate;
import com.cbrc.smis.common.ApartPage;
import com.cbrc.smis.common.Config;
import com.cbrc.smis.form.LogInForm;
import com.cbrc.smis.util.FitechException;
import com.cbrc.smis.util.FitechMessages;
import com.cbrc.smis.util.FitechUtil;


/** 
 * @author 姚捷
 * 模块名:日志管理  
 * 功能:日志查询并分页显示
 * Creation date: 11-29-2005
 * @struts.action path="/viewLogIn" name="ViewLogForm" input="/system_mgr/log_mgr.jsp" scope="request" validate="true"
 * @struts.action-forward name="log_mgr" path="/system_mgr/log_mgr.jsp"
 */
public final class ViewLogInAction extends Action {
	private static FitechException log = new FitechException(ViewLogInAction.class);  
	/**
	 * 已使用hibernate 卞以刚 2011-12-28
	 * 影响对象：LogIn
	 * com.cbrc.smis.adapter.StrutsLogInDelegate行
	 * 206,210,277，281将oracle语法to_date函数转化为sqlserver convert函数 待测试
	 * 已增加数据库判断
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
	   //取得request范围内的请求参数，并存放在logInForm内
		LogInForm logInForm = (LogInForm) form;
		RequestUtils.populate(logInForm, request);
	
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
		
		try 

		{
	   		//取得记录总数
			/**已使用hibernate 卞以刚 2011-12-28
			 * 影响对象：LogIn
			 *  com.cbrc.smis.adapter.StrutsLogInDelegate行277，281将oracle语法to_date函数转化为sqlserver convert函数 待测试
			 *  已增加数据库判断*/
	   		recordCount = StrutsLogInDelegate.getRecordCount(logInForm);
	   		//显示分页后的记录
	   		if(recordCount > 0)
	   			/**已使用hibernate 卞以刚 2011-12-28
	   			 * 影响对象：LogIn
	   			 * 206，210行将oracle语法to_date函数转化为sqlserver convert函数 待测试
	   			 * 已增加数据库判断*/
		   	    list = StrutsLogInDelegate.select(logInForm,offset,limit);  
		}
		catch (Exception e) 
		{
			log.printStackTrace(e);
			messages.add(resources.getMessage("log.select.fail"));		
		}
		//移除request或session范围内的属性
		FitechUtil.removeAttribute(mapping,request);
		//把ApartPage对象存放在request范围内
	 	aPage.setTerm(this.getTerm(logInForm));
	 	aPage.setCount(recordCount);
	 	request.setAttribute(Config.APART_PAGE_OBJECT,aPage);
		
	 	if(messages.getMessages() != null && messages.getMessages().size() > 0)
	   	  request.setAttribute(Config.MESSAGES,messages);
	 	if(list!=null && list.size()!=0)
		  request.setAttribute(Config.RECORDS,list);
	 	//将查询条件存放在request范围内
	 	request.setAttribute("form",logInForm);
	 	
	 	return mapping.findForward("log_mgr");
   }
   /**
    * 取得查询条件url  
    * @param logInForm
    * @return
    */
   public String getTerm(LogInForm logInForm)
   {
	   String term="";
	   
	   Integer logTypeId = logInForm.getLogTypeId();
	   String operation = logInForm.getOperation();
	   String userName = logInForm.getUserName();
	   String startDate = logInForm.getStartDate();
	   String endDate = logInForm.getEndDate();
	   
	   if(logTypeId!=null)
	   {
		   term += (term.indexOf("?")>=0 ? "&" : "?");
		   term += "logTypeId="+logTypeId.toString();   
	   }
	   if(operation!=null && !operation.equals(""))
	   {
		   term += (term.indexOf("?")>=0 ? "&" : "?");
		   term += "operation="+operation.toString();   
	   }
	   if(userName!=null && !userName.equals(""))
	   {
		   term += (term.indexOf("?")>=0 ? "&" : "?");
		   term += "userName="+userName.toString();   
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
