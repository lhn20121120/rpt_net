package com.fitech.net.template.action;

import java.io.IOException;
import java.util.ArrayList;
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
import com.cbrc.smis.other.Aditing;
import com.cbrc.smis.security.Operator;
import com.cbrc.smis.util.FitechException;
import com.cbrc.smis.util.FitechMessages;

public class ViewOnlineSJBSAction  extends Action {
	private static FitechException log = new FitechException(ViewOnlineSJBSAction.class);

	/** 
     * Method execute
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return ActionForward
     */
	
	public ActionForward execute(ActionMapping mapping,ActionForm form,
						HttpServletRequest request,HttpServletResponse response)  throws IOException, ServletException{
	        
		MessageResources resources = getResources(request);
		FitechMessages messages = new FitechMessages();
        //取得request范围内的请求参数，并存放在ReportInForm内
        ReportInForm reportInForm = (ReportInForm) form;
        RequestUtils.populate(reportInForm, request);
	    
        String year = request.getParameter("year");
        String term = request.getParameter("term");
        try{
        	if(year != null && !year.equals(""))
        		reportInForm.setYear(new Integer(year));
        	else
        		reportInForm.setYear(null);
        }catch(Exception ex){
        	reportInForm.setYear(null);
        	ex.printStackTrace();
        }
        try{
        	if(term != null && !term.equals(""))
        		reportInForm.setTerm(new Integer(term));
        	else
        		reportInForm.setTerm(null);
        }catch(Exception ex){
        	reportInForm.setTerm(null);
        	ex.printStackTrace();
        }
        request.getParameter("year");
        HttpSession session=request.getSession();
        /**获取当前用户*/
        Operator operator=(Operator)session.getAttribute(Config.OPERATOR_SESSION_ATTRIBUTE_NAME);

        if(operator != null && operator.getOrgId() != null)
        	reportInForm.setOrgId(operator.getOrgId());
    
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
		
        try{
        	List ybList = StrutsReportInDelegate.getOnLineYBRecords(reportInForm,operator);
        	
        	if(ybList != null && ybList.size() > 0){
        		recordCount = ybList.size();
        	}
        	if(recordCount > 0){
        		list = new ArrayList();
        		int arraySize = limit + offset;
        		if(ybList.size() < (limit + offset)) arraySize = ybList.size();
        		for(int i=offset;i<arraySize;i++){
        			Aditing aditing = (Aditing)ybList.get(i);
        			list.add(aditing);
        		}
        	}
        }catch(Exception ex){
       	 log.printStackTrace(ex);
            messages.add(resources.getMessage("select.onlineReporg.failed"));  
        }
        
        //移除request或session范围内的属性
        //FitechUtil.removeAttribute(mapping,request);
        //把ApartPage对象存放在request范围内
        
        aPage.setTerm(this.getTerm(reportInForm));
	 	aPage.setCount(recordCount);
	 	request.setAttribute(Config.APART_PAGE_OBJECT,aPage);
        
        if(messages.getMessages() != null && messages.getMessages().size() > 0)
          request.setAttribute(Config.MESSAGES,messages);
        if(list!=null && list.size()!=0)
          request.setAttribute("records",list);
        
        return mapping.findForward("init");
	}  
	
	public String getTerm(ReportInForm reportInForm){
		String term="";
		String repName = reportInForm.getRepName();
		String year = reportInForm.getYear() != null ? reportInForm.getYear().toString() : "";
		String month = reportInForm.getTerm() != null ? reportInForm.getTerm().toString() : "";
		
		if(repName != null && !repName.equals("")){
			term += (term.indexOf("") >= 0 ? "" : "&");
			term += "repName=" + repName;
		}
		if(year != null && !year.equals("")) 
			term+=(term.indexOf("?")>=0?"&":"?") + "year=" + year;
		
		if(month != null && !month.equals("")){
			term += (term.indexOf("?")>0 ? "&" : "?");
			term += "term=" + month;
		}
		if(term.indexOf("?")>=0)
			term = term.substring(term.indexOf("?")+1);
		return term;
	}        
}



