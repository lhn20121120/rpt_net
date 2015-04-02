package com.fitech.gznx.action;

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

import com.cbrc.smis.common.ApartPage;
import com.cbrc.smis.common.Config;
import com.cbrc.smis.other.Aditing;
import com.cbrc.smis.security.Operator;
import com.cbrc.smis.util.FitechException;
import com.cbrc.smis.util.FitechMessages;
import com.fitech.gznx.common.DateUtil;
import com.fitech.gznx.common.PageListInfo;
import com.fitech.gznx.form.AFReportForm;
import com.fitech.gznx.service.AFReportDelegate;

public class ViewGatherReportAction extends Action {
	
	private static FitechException log=new FitechException(ViewNXDataReportAction.class);
	
	/**
	 * 已使用hibernate 卞以刚 2011-12-22
	 * 影响对象：AfViewReport AfReport
	 * @param result 查询返回标志,如果成功返回true,否则返回false
	 * @param ReportInForm 
	 * @param request 
	 * @exception Exception 有异常捕捉并抛出
	 */
	public ActionForward execute(ActionMapping mapping,ActionForm form,
			   	HttpServletRequest request,HttpServletResponse response)throws IOException, ServletException {
		   
		FitechMessages messages = new FitechMessages();
		MessageResources resources = getResources(request);		   

		// 是否有Request
		AFReportForm afReportForm = (AFReportForm)form ;
		RequestUtils.populate(afReportForm, request);
		
		int recordCount =0; //记录总数
	   
		//List对象的初始化
		List resList=null;
		int curPage = 1;
		ApartPage aPage=new ApartPage();
		String strCurPage=request.getParameter("curPage");
		if(strCurPage!=null){
			if(!strCurPage.equals("")){
				curPage =  new Integer(strCurPage).intValue();
				aPage.setCurPage(new Integer(strCurPage).intValue());
			}
		}else
			aPage.setCurPage(1);
			
        /**
          * 取得当前用户的权限信息
          */   
		HttpSession session = request.getSession();
		Operator operator = null;
		if(session.getAttribute(Config.OPERATOR_SESSION_ATTRIBUTE_NAME)!=null)  
			operator = (Operator)session.getAttribute(Config.OPERATOR_SESSION_ATTRIBUTE_NAME);          
		
		if (afReportForm == null) 
			afReportForm = new AFReportForm();
		
		if (afReportForm.getDate() == null || afReportForm.getDate().equals("")) {
			//获得昨天日期
			String yestoday = (String) session.getAttribute(Config.USER_LOGIN_DATE);
			afReportForm.setDate(yestoday);
		}
		//取得模板类型
//		if(session.getAttribute(Config.REPORT_SESSION_FLG)!=null)
//			afReportForm.setTemplateType(session.getAttribute(Config.REPORT_SESSION_FLG).toString());
		//hbyh独立出来的功能块处理
		afReportForm.setTemplateType(com.fitech.gznx.common.Config.OTHER_REPORT);
		
		PageListInfo pageListInfo = null;
		try{
			
			/**已使用hibernate 卞以刚 2011-12-22
			 * 影响对象：AfViewReport AfReport**/
			pageListInfo = AFReportDelegate.selectNeedGatherReportRecord(afReportForm, operator,curPage);
			resList = pageListInfo.getList();
			recordCount = (int) pageListInfo.getRowCount();
		}catch(Exception ex){
			log.printStackTrace(ex);
			messages.add(resources.getMessage("select.dataReport.failed"));		
		}
  
         //把ApartPage对象存放在request范围内
		aPage.setTerm(this.getTerm(afReportForm));
		aPage.setCount(recordCount);
		request.setAttribute(Config.APART_PAGE_OBJECT,aPage);
		request.setAttribute("orgId",afReportForm.getOrgId());
		request.setAttribute("date",afReportForm.getDate());
		request.setAttribute("RequestParam", aPage.getTerm());
		
		if(request.getAttribute("messagenotnull") != null)
			messages = (FitechMessages)request.getAttribute("messagenotnull");

		if(messages.getMessages() != null && messages.getMessages().size() > 0)
			request.setAttribute(Config.MESSAGES,messages);
		  
		if(resList!=null && resList.size()>0){
			request.setAttribute(Config.RECORDS,resList);
		}
		return mapping.findForward("view");	     
	}
	
	/**
	 * 
	 * @param afReportForm
	 * @return
	 */
	public String getTerm(AFReportForm afReportForm){
		String term="";  
		String orgId = afReportForm.getOrgId();
		String date = String.valueOf(afReportForm.getDate());	
		String repFreqId = afReportForm.getRepFreqId();
		String repName = afReportForm.getRepName();
		
		if(orgId!=null&&!orgId.equals("")){
			term += (term.indexOf("?")>=0 ? "&" : "?");
			term += "orgId="+orgId;
		}
		if(repFreqId!=null&&!repFreqId.equals("")){
			term += (term.indexOf("?")>=0 ? "&" : "?");
			term += "repFreqId="+repFreqId;
		}
		if(repName!=null&&!repName.equals("")){
			term += (term.indexOf("?")>=0 ? "&" : "?");
			term += "repName="+repName;
		}
		if(date!=null&&!date.equals("")){		
			term += (term.indexOf("?")>=0 ? "&" : "?");
			term += "date="+date.toString();
		}
		
		String orgName = afReportForm.getOrgName();
		if(orgName!=null&&!orgName.equals("")){		
			term += (term.indexOf("?")>=0 ? "&" : "?");			
			term += "orgName="+orgName;    
		}
		
		if(term.indexOf("?")>=0)
			term = term.substring(term.indexOf("?")+1);
		
		return term;
	}	
}
