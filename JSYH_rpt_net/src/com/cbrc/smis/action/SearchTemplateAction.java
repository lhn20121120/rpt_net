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
import com.cbrc.smis.form.ReportInInfoForm;
import com.cbrc.smis.security.Operator;
import com.cbrc.smis.util.FitechException;
import com.cbrc.smis.util.FitechMessages;



/**
 * 用于查询报表
 * @author masclnj
 *
 */
public final class SearchTemplateAction extends Action {
	
	private static FitechException log=new FitechException(SearchTemplateAction.class);
	   /**
	    * @param result 查询返回标志,如果成功返回true,否则返回false
	    * @param ReportInInfoForm 
	    * @param request 
	    * @exception Exception 有异常捕捉并抛出
	    */
	   public ActionForward execute(
	      ActionMapping mapping,
	      ActionForm form,
	      HttpServletRequest request,
	      HttpServletResponse response
	   )
	      throws IOException, ServletException {
		   
		   FitechMessages messages = new FitechMessages();
		   MessageResources resources = getResources(request);		   
		  
		   
		   // 是否有Request
		   ReportInInfoForm reportInInfoForm = (ReportInInfoForm)form ;
		     
		   RequestUtils.populate(reportInInfoForm, request);
		   if(reportInInfoForm.getYear()!=null && reportInInfoForm.getYear().intValue()==0) 
			   reportInInfoForm.setYear(null);
		   if(reportInInfoForm.getTerm()!=null && reportInInfoForm.getTerm().intValue()==0)
			   reportInInfoForm.setTerm(null);
		   
		   int recordCount =0; //记录总数
		   int offset=0; //偏移量
		   int limit=0;  //每页显示的记录条数
		   
		   //List对象的初始化
		   List resList=null;
		   
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
           
            
            try{
//            	取得记录总数
		   		recordCount = StrutsReportInDelegate.getRecordCountOfmanual2(reportInInfoForm,operator);
		   		if(recordCount > 0)
			   	    resList = StrutsReportInDelegate.selectOfManual2(reportInInfoForm,offset,limit,operator);		   		
		   		}
            catch(Exception ex){
           	 	log.printStackTrace(ex);
                messages.add(resources.getMessage("log.select.fail"));  
            }
            
            
//          把ApartPage对象存放在request范围内
		 	aPage.setTerm(this.getTerm(reportInInfoForm));
		 	
		 	aPage.setCount(recordCount);
		 	request.setAttribute(Config.APART_PAGE_OBJECT,aPage);
		 	
		 	 if(messages.getMessages() != null && messages.getMessages().size() > 0)
			   	  request.setAttribute(Config.MESSAGES,messages);
		 	 
		 	 
//		 	如果StrutsMReportInDelegate类中返回的reslist对象不为空并且对象的大小大于0，
		     //则返回一个包含reslist集合的request对象
		     if(resList!=null && resList.size()>0){
		    	 request.setAttribute(Config.RECORDS,resList);
		     }
		     
		     request.setAttribute("SelectedFlag",reportInInfoForm.getAllFlags());	     
		     request.setAttribute("form",reportInInfoForm);
	     
	     return mapping.findForward("viewTemSearch");	     
	  }
	      // First see if there is a vo as request attribute.
	  public String getTerm(ReportInInfoForm reportInInfoForm){
		   String term="";
		   String repName = reportInInfoForm.getRepName();		   
		   String year = String.valueOf(reportInInfoForm.getYear());
		   String terms = String.valueOf(reportInInfoForm.getTerm());
		   
		   if(repName!=null&&!repName.equals(""))
		   {
			   term += (term.indexOf("?")>=0 ? "&" : "?");
			   term += "repName="+repName.toString();   
		   }
		   
		   if(year!=null&&!year.equals(""))
		   {
			   term += (term.indexOf("?")>=0 ? "&" : "?");
			   term += "year="+year.toString();   
		   }
		   
		   if(terms!=null&&!terms.equals(""))
		   {
			   term += (term.indexOf("?")>=0 ? "&" : "?");
			   term += "term="+terms.toString();   
		   }
		   
		   if(term.indexOf("?")>=0)
			   term = term.substring(term.indexOf("?")+1);
		      // System.out.println("term"+term);
		   return term;
	   }	
}