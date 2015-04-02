package com.fitech.gznx.action;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
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

import com.cbrc.smis.action.ViewMChildReportAction;
import com.cbrc.smis.adapter.StrutsMRepRangeDelegate;
import com.cbrc.smis.common.ApartPage;
import com.cbrc.smis.common.Config;
import com.cbrc.smis.other.Aditing;
import com.cbrc.smis.security.Operator;
import com.cbrc.smis.util.FitechException;
import com.cbrc.smis.util.FitechMessages;
import com.fitech.gznx.common.DateUtil;
import com.fitech.gznx.common.PageListInfo;
import com.fitech.gznx.common.StringUtil;
import com.fitech.gznx.form.AFReportForm;
import com.fitech.gznx.service.AFReportProductDelegate;
import com.fitech.gznx.service.AFTemplateTypeDelegate;

public class AFReportProductAction extends Action {
    private static FitechException log = new FitechException(ViewMChildReportAction.class);

    /** 
     * 已使用hibernate 卞以刚 2011-12-21
     * 影响对象：ViewMReport AfTemplate ReportIn AfViewReport AfOrg AfReport
     * Method execute
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return ActionForward
     */
    public ActionForward execute(
        ActionMapping mapping,
        ActionForm form,
        HttpServletRequest request,
        HttpServletResponse response)  throws IOException, ServletException{
        
    	
        MessageResources resources = getResources(request);
        FitechMessages messages = new FitechMessages();
        //取得request范围内的请求参数，并存放在reportForm内
        AFReportForm reportInForm = (AFReportForm)form;
        RequestUtils.populate(reportInForm, request);
        HttpSession session = request.getSession();
        int recordCount =0; //记录总数		

		   
		//List对象的初始化
		List resList=null;
		String ss = (String)request.getAttribute("checkTask");
		if(ss != null&&!ss.equals("")){
			resList = null;
			recordCount = 0 ;
			String dd = "";
			if(reportInForm.getDate() == null || reportInForm.getDate().equals("")){
				dd = (String) session.getAttribute(Config.USER_LOGIN_DATE);
			}else{
				dd = reportInForm.getDate();
			}
			messages.add(dd+resources.getMessage(ss));
			request.setAttribute(Config.MESSAGES,messages);
			return mapping.findForward("index");	 
		}

		ApartPage aPage=new ApartPage();
		int curPage = 1;
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
		
		Operator operator = null; 
		if(session.getAttribute(Config.OPERATOR_SESSION_ATTRIBUTE_NAME)!=null)
			operator = (Operator)session.getAttribute(Config.OPERATOR_SESSION_ATTRIBUTE_NAME);          
		/** 报表选中标志 **/
		String reportFlg = "0";
		
		System.out.println(session.getAttribute(Config.REPORT_SESSION_FLG));
		if (session.getAttribute(Config.REPORT_SESSION_FLG) != null){
			reportFlg = (String) session.getAttribute(Config.REPORT_SESSION_FLG);
		}

		if(StringUtil.isEmpty(reportInForm.getDate())){
			String yestoday = (String) session.getAttribute(Config.USER_LOGIN_DATE);
			reportInForm.setDate(yestoday);
		}
		reportInForm.setIsReport(Integer.valueOf(com.fitech.gznx.common.Config.TEMPLATE_REPORT));
		
		if(reportInForm.getOrgId() == null)
			reportInForm.setOrgId(operator.getOrgId());
		PageListInfo pageListInfo = null;
		try{
			if(reportFlg.equals("1")){
				/**已使用hibernate 卞以刚 2011-12-21
				 * 影响对象：ViewMReport AfTemplate ReportIn**/
				pageListInfo = AFReportProductDelegate.selectYJHReportList(reportInForm,operator,curPage);
			} else{
				/**已使用hibernate 卞以刚 2011-12-21
				 * 影响对象：AfViewReport AfOrg AfReport**/
				pageListInfo = AFReportProductDelegate.selectNOTYJHReportList(reportInForm,operator,curPage,reportFlg);
			}
			if(pageListInfo!=null && pageListInfo.getList()!=null)
				resList = pageListInfo.getList();
			recordCount = (int) pageListInfo.getRowCount();
		}catch(Exception ex){
			log.printStackTrace(ex);
			messages.add(resources.getMessage("select.dataReport.failed"));		
		}
  
         //把ApartPage对象存放在request范围内
		aPage.setTerm(this.getTerm(reportInForm));		 	
		aPage.setCount(recordCount);
		request.setAttribute(Config.APART_PAGE_OBJECT,aPage);
		request.setAttribute("orgId",reportInForm.getOrgId());
		request.setAttribute("reportName", reportInForm.getRepName());
		request.setAttribute("ObjForm", reportInForm);
//		if(!StringUtil.isEmpty(reportInForm.getBak1()) && StringUtil.isEmpty(reportInForm.getTemplateTypeName())){
//	       	 String templateName = AFTemplateTypeDelegate.getTemplateTypeName(reportInForm.getBak1(), reportFlg);
//	       	reportInForm.setTemplateTypeName(templateName);
//	        }
		if(messages.getMessages() != null && messages.getMessages().size() > 0)		
			request.setAttribute(Config.MESSAGES,messages);
		  
		if(resList!=null && resList.size()>0){
			request.setAttribute(Config.RECORDS,resList);   
		}
		return mapping.findForward("index");	 
    }
    
    
    public String getTerm(AFReportForm reportInForm){
    	
		String term="";  
		String orgId = reportInForm.getOrgId();
		String setDate = reportInForm.getDate();	
		String repName= reportInForm.getRepName();
		String templateType = reportInForm.getTemplateType();
		
		
		if(setDate!=null&&!setDate.equals("")){
			term += (term.indexOf("?")>=0 ? "&" : "?");			
			term += "date="+setDate.toString();    
		}
		if(repName!=null&&!repName.equals("")){
			term += (term.indexOf("?")>=0 ? "&" : "?");			
			term += "repName="+repName.toString();    
		}
		
		if(templateType!=null&&!templateType.equals("")){
			term += (term.indexOf("?")>=0 ? "&" : "?");			
			term += "templateType="+templateType.toString();    
		}
		/**加入报送频度条件*/
		if(reportInForm.getRepFreqId() != null){
			term += (term.indexOf("?")>=0 ? "&" : "?");
			term += "repFreqId=" + reportInForm.getRepFreqId();
		}
		
		
		if(orgId!=null&&!orgId.equals("")){		
			term += (term.indexOf("?")>=0 ? "&" : "?");			
			term += "orgId="+orgId;    
		}
		String orgName = reportInForm.getOrgName();
		if(orgName!=null&&!orgName.equals("")){		
			term += (term.indexOf("?")>=0 ? "&" : "?");			
			term += "orgName="+orgName;    
		}
		
		if(term.indexOf("?")>=0)		
			term = term.substring(term.indexOf("?")+1);
		
		return term;
	}
}
