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
import com.fitech.gznx.common.StringUtil;
import com.fitech.gznx.form.AFReportForm;
import com.fitech.gznx.service.AFReportDelegate;
import com.fitech.gznx.service.AFTemplateTypeDelegate;

	/**
	 * 已使用hibernate 卞以刚 2011-12-27
	 * 影响对象：AfReport AfTemplate
	 * @报表查询的公共Action
	 * @author 唐磊
	 *
	 */
	public final class ViewReportNXRecheckAction extends Action {
		
		private FitechException log = new FitechException(ViewReportNXRecheckAction.class);
		     
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
			AFReportForm reportInForm = (AFReportForm)form ;	     
		    RequestUtils.populate(reportInForm, request);
		    
//		    if(reportInForm.getYear()!=null && reportInForm.getYear().intValue()==0) reportInForm.setYear(null);
//		    if(reportInForm.getTerm()!=null && reportInForm.getTerm().intValue()==0) reportInForm.setTerm(null);
		    if(reportInForm.getSupplementFlag()==null||"".equals(reportInForm.getSupplementFlag()))
				reportInForm.setSupplementFlag("-999");
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
			        
	        HttpSession session = request.getSession();
	        Operator operator = null; 
	        if(session.getAttribute(Config.OPERATOR_SESSION_ATTRIBUTE_NAME)!=null)
	            operator = (Operator)session.getAttribute(Config.OPERATOR_SESSION_ATTRIBUTE_NAME);
	        /** 报表选中标志 **/
			String reportFlg = "";
			if (session.getAttribute(Config.REPORT_SESSION_FLG) != null){
				reportFlg = (String) session.getAttribute(Config.REPORT_SESSION_FLG);
			}
			if(StringUtil.isEmpty(reportInForm.getDate())){
				String yestoday = (String) session.getAttribute(Config.USER_LOGIN_DATE);
				reportInForm.setDate(yestoday);	
			}
			//取得模板类型
			reportInForm.setTemplateType(reportFlg);
	        
	        
	        try {
	        	
		   		//取得记录总数
	        	/***
	        	 * 已使用hibernate 卞以刚 2011-12-27
	        	 * 影响对象：AfReport AfTemplate
	        	 */
		   		recordCount = AFReportDelegate.getRecordCountOfmanual(reportInForm,
		   									operator,Config.CHECK_FLAG_UNCHECK.toString());	  
		   		//显示分页后的记录
		   		if(recordCount > 0)
		   			/**已使用hibernate 卞以刚 2011-12-27
		   			 * 影响对象：AfReport AfTemplate*/
			   	    resList = AFReportDelegate.selectOfManual(reportInForm,
			   	    			offset,limit,operator,Config.CHECK_FLAG_UNCHECK.toString()); 	   		
	        }catch (Exception e){
	        	log.printStackTrace(e);
				messages.add(resources.getMessage("manualCheck.get.failed"));
			}
	        
			//把ApartPage对象存放在request范围内
		 	aPage.setTerm(this.getTerm(reportInForm));
		 	aPage.setCount(recordCount);
		 	request.setAttribute(Config.APART_PAGE_OBJECT,aPage);
//		 	if(!StringUtil.isEmpty(reportInForm.getBak1()) && StringUtil.isEmpty(reportInForm.getTemplateTypeName())){
//		       	 String templateName = AFTemplateTypeDelegate.getTemplateTypeName(reportInForm.getBak1(), reportFlg);
//		       	reportInForm.setTemplateTypeName(templateName);
//		        }
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
//			/**加入模板类型条件*/
//			if(reportInForm.getFrOrFzType() != null){
//				term += (term.indexOf("?")>=0 ? "&" : "?");
//				term += "frOrFzType=" + reportInForm.getFrOrFzType();
//			}
			/**加入模板报送频度*/
			if(reportInForm.getRepFreqId() != null){
				term += (term.indexOf("?")>=0 ? "&" : "?");
				term += "repFreqId=" + reportInForm.getRepFreqId();
			}
			/**加入报表期数条件*/
			if(reportInForm.getDate() != null){
				term += (term.indexOf("?")>=0 ? "&" : "?");
				term += "date=" + reportInForm.getDate();
			}
			/**加入报表批次条件 add by 王明明*/
			if(reportInForm.getSupplementFlag() != null&&!"".equals(reportInForm.getSupplementFlag())){
				term += (term.indexOf("?")>=0 ? "&" : "?");
				term += "supplementFlag=" + reportInForm.getSupplementFlag();
			}
			/**加入模板类型条件*/
			if(reportInForm.getBak1() != null){
				term += (term.indexOf("?")>=0 ? "&" : "?");
				term += "bak1=" + reportInForm.getBak1();
			}
			/**加入报送机构条件*/
			if(reportInForm.getOrgId() != null){
				term += (term.indexOf("?")>=0 ? "&" : "?");
				term += "orgId=" + reportInForm.getOrgId();
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
