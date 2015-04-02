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

	import com.cbrc.smis.adapter.StrutsReportInDelegate;
	import com.cbrc.smis.common.ApartPage;
	import com.cbrc.smis.common.Config;
	import com.cbrc.smis.form.ReportInForm;
import com.cbrc.smis.other.Aditing;
	import com.cbrc.smis.security.Operator;
	import com.cbrc.smis.util.FitechException;
import com.cbrc.smis.util.FitechMessages;
import com.fitech.gznx.common.StringUtil;
import com.fitech.gznx.form.AFReportForm;
import com.fitech.gznx.po.AfReport;
import com.fitech.gznx.po.AfReportForceRep;
import com.fitech.gznx.service.AFReportDelegate;
import com.fitech.gznx.service.AFReportForceService;
import com.fitech.gznx.service.AFTemplateTypeDelegate;

	/**
	 * 已使用hibernate 卞以刚 2011-12-27
	 * 影响对象：AfReport AfTemplate
	 * @报表查询的公共Action
	 * @author 唐磊
	 *
	 */
	public final class ViewReportNXRecheckAction extends SystemSchemaBaseAction {
		
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
			AFReportForceService forceService = new AFReportForceService();
			FitechMessages messages = new FitechMessages();	   
			MessageResources resources = getResources(request);	   
			AFReportForm reportInForm = (AFReportForm)form ;	     
		    RequestUtils.populate(reportInForm, request);
		    request.setAttribute("schemaRepIds", request.getParameter("schemaRepIds"));
		   
		   System.out.println(request.getParameter("schemaRepIds"));
		    
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
	        
			/******************任务管理查询(begin)********************/
			String workTaskTemp = request.getParameter("workTaskTemp");
			String workTaskBusiLine = request.getParameter("workTaskBusiLine"); 
			String workTaskFreq = request.getParameter("workTaskFreq"); 
			StringBuffer wtb = new StringBuffer("");
			String hsqlOld = "";
			if(Config.SYSTEM_SCHEMA_FLAG==1 && workTaskTemp!=null && !workTaskTemp.equals("")){
				String nodeFlag = request.getParameter("nodeFlag");
				if(nodeFlag!=null)
					request.setAttribute("nodeFlag",nodeFlag);
				if("qttx".equals(workTaskBusiLine)){
					session.setAttribute(Config.REPORT_SESSION_FLG, "3");
					reportFlg = "3";
					
					reportInForm.setTemplateType("3");
					reportInForm.setRepFreqId(workTaskFreq);
					
				}
				if("rhtx".equals(workTaskBusiLine)){
					session.setAttribute(Config.REPORT_SESSION_FLG, "2");
					reportFlg = "2";
					reportInForm.setTemplateType("2");
					reportInForm.setRepFreqId(workTaskFreq);
				}
				reportInForm.setDate(request.getParameter("workTaskTerm"));
				reportInForm.setOrgId(request.getParameter("workTaskOrgId"));
				hsqlOld = operator.getChildRepCheckPodedom();
				String[] workTaskTemps = workTaskTemp.split(",");
				for(int i=0;i<workTaskTemps.length;i++)
					wtb.append(",'" + reportInForm.getOrgId() + workTaskTemps[i] + "'");
				operator.setChildRepCheckPodedom(wtb.substring(1));
			}
			/******************任务管理查询(end)********************/
	        
	        try {
	        	if(Config.SYSTEM_SCHEMA_FLAG==1){
		   			recordCount = AFReportDelegate.getRecordCountOfmanual(reportInForm,
								operator,Config.CHECK_FLAG_UNCHECK.toString()+ "," + Config.CHECK_FLAG_PASS.toString()+","+Config.CHECK_FLAG_FAILED+","+Config.CHECK_FLAG_AFTERJY+","+Config.CHECK_FLAG_UNREPORT+","+Config.CHECK_FLAG_AFTERSAVE);
		   			//显示分页后的记录
			   		if(recordCount > 0)
			   			/**已使用hibernate 卞以刚 2011-12-27
			   			 * 影响对象：AfReport AfTemplate*/
				   		 resList = AFReportDelegate.selectOfManual(reportInForm,
				   	    			offset,limit,operator,Config.CHECK_FLAG_UNCHECK.toString() + "," + Config.CHECK_FLAG_PASS.toString()+","+Config.CHECK_FLAG_FAILED+","+Config.CHECK_FLAG_AFTERJY+","+Config.CHECK_FLAG_UNREPORT+","+Config.CHECK_FLAG_AFTERSAVE);
	        	}else{
	        		recordCount = AFReportDelegate.getRecordCountOfmanual(reportInForm,
							operator,Config.CHECK_FLAG_UNCHECK.toString());
			   		//显示分页后的记录
			   		if(recordCount > 0)
			   			/**已使用hibernate 卞以刚 2011-12-27
			   			 * 影响对象：AfReport AfTemplate*/
				   		 resList = AFReportDelegate.selectOfManual(reportInForm,
				   	    			offset,limit,operator,Config.CHECK_FLAG_UNCHECK.toString());
	        	}
	        }catch (Exception e){
	        	log.printStackTrace(e);
				messages.add(resources.getMessage("manualCheck.get.failed"));
			}
	        
	        /******************任务管理查询(begin)********************/
			if(Config.SYSTEM_SCHEMA_FLAG==1 && workTaskTemp!=null && !workTaskTemp.equals(""))
				operator.setChildRepCheckPodedom(hsqlOld);
			/******************任务管理查询(end)********************/
	        
	        
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
		 	
		 	// add by 王明明 将任务模式添加到request里
		 	
		 	request.setAttribute("system_schema_flag",this.getSchemaFlag());
		 	/****************begin 判断报表是否强制上报过了***********/
		 	if(resList!=null && this.getSchemaFlag() == 1){
		 		List<AfReportForceRep> list =null;
			 	Aditing adit = null;
			 	for(int i = 0; i < resList.size(); i++){
			 		 list = new ArrayList<AfReportForceRep>();
			 		adit = (Aditing) resList.get(i);
			 		try {
			 			list = forceService.findAFReportForce(adit.getRepInId().longValue(),null);
			 			if(list.size()>0){
			 				adit.setIsFlag(1);
			 			}else{
			 				adit.setIsFlag(0);
			 			}
			 			resList.remove(i);
			 			resList.add(i, adit);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			 	}
		 	}
		 	/***************************end*************************/
		 	if(resList!=null && resList.size()>0){    	
		 		request.setAttribute("form",resList);     
		 	}
		 	String url = request.getParameter("url");
		 	if(request.getParameter("workTaskTerm")!=null&&!request.getParameter("workTaskTerm").equals("")){
		 		request.setAttribute("workTaskTerm", request.getParameter("workTaskTerm"));
		 	}
		 	if(request.getParameter("workTaskOrgId")!=null&&!request.getParameter("workTaskOrgId").equals("")){
		 		request.setAttribute("workTaskOrgId", request.getParameter("workTaskOrgId"));
		 	}
		 	if(request.getParameter("workTaskTemp")!=null&&!request.getParameter("workTaskTemp").equals("")){
		 		request.setAttribute("workTaskTemp", request.getParameter("workTaskTemp"));
		 	}
		 	if(url!=null&&!url.equals("")){
		 		request.setAttribute("url", url);
		 	}
		 	if(this.getSchemaFlag() == 1){
		 		return mapping.findForward("viewFlagCheck");
		 	}else{
		 		return mapping.findForward("viewCheck"); 
		 	}
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
