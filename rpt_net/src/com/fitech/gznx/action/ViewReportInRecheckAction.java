package com.fitech.gznx.action;

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
 * @报表查询的公共Action
 * @author 唐磊
 *
 */
public final class ViewReportInRecheckAction extends Action {
	private FitechException log=new FitechException(ViewReportInRecheckAction.class);
	     
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
		ReportInForm reportInForm = (ReportInForm)form ;	     
	    RequestUtils.populate(reportInForm, request);
	    
        HttpSession session = request.getSession();
		String yestoday = (String) session.getAttribute(Config.USER_LOGIN_DATE);
		 if(reportInForm.getDate() ==null || reportInForm.getDate().equals("")){
		    	reportInForm.setDate(yestoday.substring(0, 7));
		    	reportInForm.setYear(new Integer(yestoday.substring(0,4)));
		    	reportInForm.setTerm(new Integer(yestoday.substring(5,7)));
		    }else{
		    	reportInForm.setYear(new Integer(reportInForm.getDate().split("-")[0]));
		    	reportInForm.setTerm(new Integer(reportInForm.getDate().split("-")[1]));
		    }
	    if(reportInForm.getYear()!=null && reportInForm.getYear().intValue()==0) reportInForm.setYear(null);
	    if(reportInForm.getTerm()!=null && reportInForm.getTerm().intValue()==0) reportInForm.setTerm(null);
	    
		int recordCount = 0; // 记录总数
		int offset = 0; // 偏移量
		int limit = 0; // 每页显示的记录条数
		
		// List对象的初始化	   
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

        Operator operator = null; 
        if(session.getAttribute(Config.OPERATOR_SESSION_ATTRIBUTE_NAME)!=null)
            operator = (Operator)session.getAttribute(Config.OPERATOR_SESSION_ATTRIBUTE_NAME);
        
        try {			
        	
        	//091030 现该方法查出的是需复核的记录
	   		//取得记录总数
	   		recordCount = StrutsReportInDelegate.getRecordCountOfmanualRecheck(reportInForm,operator);	  
	   		//显示分页后的记录
	   		if(recordCount > 0)
		   	    resList = StrutsReportInDelegate.selectOfManualRecheck(reportInForm,offset,limit,operator); 	   		
        }catch (Exception e){
        	log.printStackTrace(e);
			messages.add(resources.getMessage("manualCheck.get.failed"));		
		}
        		
		//把ApartPage对象存放在request范围内
	 	aPage.setTerm(this.getTerm(reportInForm));
	 	aPage.setCount(recordCount);
	 	request.setAttribute(Config.APART_PAGE_OBJECT,aPage);
	 		 	 
	 	if(messages.getMessages() != null && messages.getMessages().size() > 0)		
	 		request.setAttribute(Config.MESSAGES,messages);
	 	 	 	
	 	request.setAttribute("SelectedFlag",reportInForm.getAllFlags());
	 	
	 	if(resList!=null && resList.size()>0){    	
	 		request.setAttribute("form",resList);     
	 	}     	 	
	 	return mapping.findForward("viewCheck");  
	}
   
	/**
	 * 设置查询传递参数
	 * 
	 * @param reportInForm
	 * @return String 查询的URL
	 */
	public String getTerm(ReportInForm reportInForm){	   
		String term="";
		
		/**加入报表编号条件*/
		if(reportInForm.getChildRepId() != null && !reportInForm.getChildRepId().equals("")){
			term += (term.indexOf("?")>=0 ? "&" : "?");
			term += "childRepId=" + reportInForm.getChildRepId();
		}
		/**加入报表名称条件*/
		if(reportInForm.getRepName() != null && !reportInForm.getRepName().equals("")){
			term += (term.indexOf("?")>=0 ? "&" : "?");
			term += "repName=" + reportInForm.getRepName();
		}
		/**加入模板类型条件*/
		if(reportInForm.getFrOrFzType() != null){
			term += (term.indexOf("?")>=0 ? "&" : "?");
			term += "frOrFzType=" + reportInForm.getFrOrFzType();
		}
		/**加入模板报送频度*/
		if(reportInForm.getRepFreqId() != null){
			term += (term.indexOf("?")>=0 ? "&" : "?");
			term += "repFreqId=" + reportInForm.getRepFreqId();
		}
		/**加入报表时间条件*/
		if(reportInForm.getDate() != null){
			term += (term.indexOf("?")>=0 ? "&" : "?");
			term += "date=" + reportInForm.getDate();
		}
		/**加入报表年份条件*/
		if(reportInForm.getYear() != null){
			term += (term.indexOf("?")>=0 ? "&" : "?");
			term += "year=" + reportInForm.getYear();
		}
		/**加入报表期数条件*/
		if(reportInForm.getTerm() != null){
			term += (term.indexOf("?")>=0 ? "&" : "?");
			term += "term=" + reportInForm.getTerm();
		}
		/**加入报送机构条件*/
		if(reportInForm.getOrgId() != null){
			term += (term.indexOf("?")>=0 ? "&" : "?");
			term += "orgId=" + reportInForm.getOrgId();
		}
		if(term.indexOf("?")>=0)		
			term = term.substring(term.indexOf("?")+1);
		
		return term;   
	} 
}