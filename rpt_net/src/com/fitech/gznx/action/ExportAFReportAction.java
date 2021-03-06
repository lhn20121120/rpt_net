package com.fitech.gznx.action;

import java.io.IOException;
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
import com.cbrc.smis.common.ApartPage;
import com.cbrc.smis.common.Config;
import com.cbrc.smis.security.Operator;
import com.cbrc.smis.util.FitechException;
import com.cbrc.smis.util.FitechMessages;
import com.fitech.gznx.common.DateUtil;
import com.fitech.gznx.common.PageListInfo;
import com.fitech.gznx.common.StringUtil;
import com.fitech.gznx.form.AFReportForm;
import com.fitech.gznx.service.AFReportProductDelegate;
import com.fitech.gznx.service.AFTemplateTypeDelegate;

public class ExportAFReportAction extends Action {
    private static FitechException log = new FitechException(ExportAFReportAction.class);

    /** 
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
        AFReportForm reportInForm = (AFReportForm)form ;
        RequestUtils.populate(reportInForm, request);
        String isHiddenSearch = null;         
        int recordCount =0; //记录总数		
        int offset = 0; // 偏移量
		int limit = 0; // 每页显示的记录条数
		   
		//List对象的初始化
		List resList=null;

		ApartPage aPage=new ApartPage();

		String strCurPage=request.getParameter("curPage");
		if(strCurPage!=null){
			if(!strCurPage.equals(""))
				aPage.setCurPage(new Integer(strCurPage).intValue());
		}else
			aPage.setCurPage(1);
		//计算偏移量
		offset=(aPage.getCurPage()-1)*Config.PER_PAGE_ROWS; 		
		limit = Config.PER_PAGE_ROWS;
			
        /**
         * 取得当前用户的权限信息
         */   
		HttpSession session = request.getSession();
		Operator operator = null; 
		if(session.getAttribute(Config.OPERATOR_SESSION_ATTRIBUTE_NAME)!=null)
			operator = (Operator)session.getAttribute(Config.OPERATOR_SESSION_ATTRIBUTE_NAME);          
		/** 报表选中标志 **/
		String reportFlg = "0";
		if (session.getAttribute(Config.REPORT_SESSION_FLG) != null){
			reportFlg = (String) session.getAttribute(Config.REPORT_SESSION_FLG);
		}
		/******************任务管理查询(begin)********************/
		String showmenu = request.getParameter("showmenu");
        if(showmenu==null)
        	showmenu="1";
        request.setAttribute("showmenu", showmenu);
		String workTaskTemp = request.getParameter("workTaskTemp");
		StringBuffer wtb = new StringBuffer("");
		String hsqlOld = "";
		int flag  = 0 ;
		if(Config.SYSTEM_SCHEMA_FLAG==1 && workTaskTemp!=null && !workTaskTemp.equals("")){
			isHiddenSearch="1";
			String workTaskTerm = request.getParameter("workTaskTerm");
			if(workTaskTerm!=null && !"".equals(workTaskTerm)){
				String[] workTaskTerms = workTaskTerm.split(",,");
				if(workTaskTerms!=null && workTaskTerms.length>=1)
					reportInForm.setDate(workTaskTerms[0]);
			}
			String busiLine = request.getParameter("report");
			if(busiLine.equals("yjtx")){
				session.setAttribute(Config.REPORT_SESSION_FLG, "1");
				reportFlg = "1";
			}else if(busiLine.equals("qttx")){
				session.setAttribute(Config.REPORT_SESSION_FLG, "3");
				reportFlg = "3";
			}else{
				session.setAttribute(Config.REPORT_SESSION_FLG, "2");
				reportFlg = "2";
			}
			reportInForm.setTemplateId(null);
			reportInForm.setOrgId(null);
			hsqlOld = operator.getChildRepReportPopedom();
			String workTaskOrgId = request.getParameter("workTaskOrgId");
//			String[] workTaskOrgIds = workTaskOrgId.split(",");
//			String[] workTaskTemps = workTaskTemp.split(",");
//			for(int i=0;i<workTaskTemps.length;i++){
//				wtb.append("'" + workTaskOrgIds[i] + workTaskTemps[i] + "',");
//			}
			if(workTaskOrgId!=null && !"".equals(workTaskOrgId)){
				String[] workTaskOrgIds = workTaskOrgId.split(",,");
				for(int i=0;i<workTaskOrgIds.length;i++){
					String[] strs = workTaskOrgIds[i].split("@@");
					if(strs!=null && strs.length>=1){
						String[] workTaskTemps = strs[1].split(",");
						for (int j = 0; j < workTaskTemps.length; j++) {
							wtb.append("'" + strs[0] + workTaskTemps[j] + "',");
						}
					}
				}
				
				String str = wtb.toString().substring(0, wtb.lastIndexOf(","));
				operator.setChildRepReportPopedom(str);
				flag  = 1 ;
			}
		}else if(Config.SYSTEM_SCHEMA_FLAG==1 && workTaskTemp==null){
			isHiddenSearch="no";
			request.setAttribute("ishiddenSearch", isHiddenSearch);
		}
		
		//System.out.println("ddddd "+reportInForm.getTemplateId());
		/******************任务管理查询(end)********************/

		if(StringUtil.isEmpty(reportInForm.getDate())){
			String yestoday = (String) session.getAttribute(Config.USER_LOGIN_DATE);
			reportInForm.setDate(yestoday);	
		}
		
		try{
			if(reportFlg.equals("1")){
				/**jdbc技术 疑是oracle语法(upper) 卞以刚 2011-12-22**/
				recordCount = AFReportProductDelegate.selectYJHExportReportCount(reportInForm,operator,flag);
				/**jdbc技术 疑是oracle语法(upper) 卞以刚 2011-12-22**/
				resList = AFReportProductDelegate.selectYJHExportReportList(reportInForm,operator,offset,limit ,flag);
			} else{
				/**jdbc技术 疑是oracle语法 需修改 卞以刚 2011-12-22**/
				recordCount = AFReportProductDelegate.selectNOTYJHExportReportCount(reportInForm,operator,reportFlg,flag);
				/**jdbc技术 oracle语法(rownum) 需修改 卞以刚 2011-12-22**/
				resList = AFReportProductDelegate.selectNOTYJHExportReportList(reportInForm,operator,offset,limit,reportFlg,flag);

			}
		}catch(Exception ex){
			log.printStackTrace(ex);
			messages.add(resources.getMessage("select.dataReport.failed"));		
		}
  
		/******************任务管理查询(begin)********************/
		if(Config.SYSTEM_SCHEMA_FLAG==1 && workTaskTemp!=null && !workTaskTemp.equals(""))
			operator.setChildRepReportPopedom(hsqlOld);
		/******************任务管理查询(end)********************/
         //把ApartPage对象存放在request范围内
		aPage.setTerm(this.getTerm(reportInForm));		 	
		aPage.setCount(recordCount);
		request.setAttribute(Config.APART_PAGE_OBJECT,aPage);
		request.setAttribute("orgId",reportInForm.getOrgId());
		request.setAttribute("date",reportInForm.getDate());

		request.setAttribute("reportName", reportInForm.getRepName());
//		if(!StringUtil.isEmpty(reportInForm.getBak1()) && StringUtil.isEmpty(reportInForm.getTemplateTypeName())){
//	       	 String templateName = AFTemplateTypeDelegate.getTemplateTypeName(reportInForm.getBak1(), reportFlg);
//	       	reportInForm.setTemplateTypeName(templateName);
//	    }
		if(messages.getMessages() != null && messages.getMessages().size() > 0)		
			request.setAttribute(Config.MESSAGES,messages);
		  
		if(resList!=null && resList.size()>0){
			request.setAttribute(Config.RECORDS,resList);   
		}
		return mapping.findForward("index");	 
    }
    public String getTerm(AFReportForm reportInForm){	   
		String term="";
		
		/**加入报表编号条件*/
		if(reportInForm.getTemplateId() != null && !reportInForm.getTemplateId().equals("")){
			term += (term.indexOf("?")>=0 ? "&" : "?");
			term += "templateId=" + reportInForm.getTemplateId();
		}
		/**加入报表名称条件*/
		if(reportInForm.getRepName() != null && !reportInForm.getRepName().equals("")){
			term += (term.indexOf("?")>=0 ? "&" : "?");
			term += "repName=" + reportInForm.getRepName();
		}
		/**加入模板类型条件*/
		if(reportInForm.getBak1() != null){
			term += (term.indexOf("?")>=0 ? "&" : "?");
			term += "bak1=" + reportInForm.getBak1();
		}
		/**加入报送频度条件*/
		if(reportInForm.getRepFreqId() != null){
			term += (term.indexOf("?")>=0 ? "&" : "?");
			term += "repFreqId=" + reportInForm.getRepFreqId();
		}

		/**加入报表期数条件*/
		if(reportInForm.getDate() != null){
			term += (term.indexOf("?")>=0 ? "&" : "?");
			term += "date=" + reportInForm.getDate();
		}	
		if(reportInForm.getOrgId() != null){
			term += (term.indexOf("?")>=0 ? "&" : "?");
			term += "orgId=" + reportInForm.getOrgId();
		}
		if(reportInForm.getSupplementFlag() != null){
			term += (term.indexOf("?")>=0 ? "&" : "?");
			term += "supplementFlag=" + reportInForm.getSupplementFlag();
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
