package com.fitech.net.action;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.MessageResources;
import org.apache.struts.util.RequestUtils;

import com.cbrc.smis.adapter.StrutsMRepRangeDelegate;
import com.cbrc.smis.common.ApartPage;
import com.cbrc.smis.common.Config;
import com.cbrc.smis.dao.DBConn;
import com.cbrc.smis.form.ReportInForm;
import com.cbrc.smis.security.Operator;
import com.cbrc.smis.util.FitechException;
import com.cbrc.smis.util.FitechMessages;
import com.fitech.gznx.action.SystemSchemaBaseAction;
import com.fitech.gznx.po.AfOrg;
import com.fitech.gznx.po.AfTemplate;
import com.fitech.gznx.service.AFOrgDelegate;

public class ViewDataReportAction extends SystemSchemaBaseAction {

	private static FitechException log=new FitechException(ViewDataReportAction.class);
	   /**
	    * 已使用hibernate 卞以刚 2011-12-21
	    * @param result 查询返回标志,如果成功返回true,否则返回false
	    * @param ReportInForm 
	    * @param request 
	    * @exception Exception 有异常捕捉并抛出
	    */
	public ActionForward execute(ActionMapping mapping,ActionForm form,
			   	HttpServletRequest request,HttpServletResponse response)throws IOException, ServletException {
//		this.test();
		FitechMessages messages = new FitechMessages();
		MessageResources resources = getResources(request);		   

		// 是否有Request
		ReportInForm reportInForm = (ReportInForm)form ;
		RequestUtils.populate(reportInForm, request);
		HttpSession session = request.getSession();
		int recordCount =0; //记录总数		
		int offset=0; //偏移量
		int limit=0;  //每页显示的记录条数
		   
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
			return mapping.findForward("view");	 
		}

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
			
        /***
          * 取得当前用户的权限信息
          */   
		
		Operator operator = null; 
		if(session.getAttribute(Config.OPERATOR_SESSION_ATTRIBUTE_NAME)!=null)  
			operator = (Operator)session.getAttribute(Config.OPERATOR_SESSION_ATTRIBUTE_NAME);          
		if(reportInForm.getOrgName()!= null &&!reportInForm.getOrgName().equals("")){
			
			reportInForm.setOrgId(AFOrgDelegate.getIdByName(reportInForm.getOrgName()));
		}

		if(reportInForm.getDate() == null || reportInForm.getDate().equals("")){
			String yestoday = (String) session.getAttribute(Config.USER_LOGIN_DATE);
			//Calendar calendar = Calendar.getInstance();
			//calendar.add(Calendar.MONTH, -1); yql修改
			reportInForm.setYear(new Integer(yestoday.substring(0,4)));		   
			reportInForm.setTerm(new Integer(yestoday.substring(5, 7)));
			reportInForm.setDay(new Integer(yestoday.substring(8,10)));
			reportInForm.setDate(yestoday.substring(0, 10));
//			if (reportInForm.getTerm().intValue()<10)
//				reportInForm.setDate(yestoday.substring(0, 7));
//			else
//				reportInForm.setDate(calendar.get(Calendar.YEAR)+"-"+(calendar.get(Calendar.MONTH)+1));

		}else{
			reportInForm.setYear(new Integer(reportInForm.getDate().substring(0, 4)));
			reportInForm.setTerm(new Integer(reportInForm.getDate().substring(5, 7)));
		}
		
		/******************任务管理查询(begin)********************/
		String workTaskTemp = request.getParameter("workTaskTemp");
		StringBuffer wtb = new StringBuffer("");
		String hsqlOld = "";
		if(Config.SYSTEM_SCHEMA_FLAG==1 && workTaskTemp!=null && !workTaskTemp.equals("")){
			
			session.setAttribute(Config.REPORT_SESSION_FLG, "1");
			String workTaskTerm = request.getParameter("workTaskTerm");
			String[] workTaskTerms = workTaskTerm.split("-");
			reportInForm.setYear(new Integer(workTaskTerms[0]));
			reportInForm.setTerm(new Integer(workTaskTerms[1]));
			reportInForm.setOrgId(request.getParameter("workTaskOrgId"));
			reportInForm.setDate(workTaskTerm);
			hsqlOld = operator.getChildRepReportPopedom();
			String[] workTaskTemps = workTaskTemp.split(",");
			for(int i=0;i<workTaskTemps.length;i++)
				wtb.append(",'" + reportInForm.getOrgId() + workTaskTemps[i] + "'");
			operator.setChildRepReportPopedom(wtb.substring(1));
			
			String url  = request.getParameter("url");
			if(url==null||url.equals("")){
				url = request.getContextPath()+"/viewDataReport.do?workTaskTerm="+workTaskTerm+"&workTaskOrgId="+request.getParameter("workTaskOrgId")+"&workTaskTemp="+workTaskTemp;
//				System.out.println(url);
			}
			request.setAttribute("url",url);
		}
		if(Config.SYSTEM_SCHEMA_FLAG==0){
			String url  = request.getParameter("url");
			if(url==null||url.equals("")){
				url = request.getContextPath()+"/viewDataReport.do";
//				System.out.println(url);
			}
			request.setAttribute("url",url);
		}
		/******************任务管理查询(end)********************/
		
		try{
			/***
			 * --------------------------------
			 * 为保留查询条件，在此划分
			 * 查询机构ID(searchOrgId)
			 * 单击修改的机构ID(orgId)
			 * --------------------------------
			 */
			if(reportInForm.getSearchOrgId()==null){
				if(reportInForm.getOrgId() == null || reportInForm.getOrgId().equals("null")){
					reportInForm.setOrgId(operator.getOrgId());
				}
			}else{
				reportInForm.setOrgId(reportInForm.getSearchOrgId());
			}
			
			//20121217修改，在选择机构查询时保留机构名称信息
			if(reportInForm.getOrgId().equals(Config.DEFAULT_VALUE)){//全部机构
				reportInForm.setOrgName("全部机构");
			}else{
				AfOrg org = AFOrgDelegate.getOrgInfo(reportInForm.getOrgId());
				if(org==null || org.getOrgName()==null || org.getOrgName().equals(""))
					reportInForm.setOrgName("全部机构");
				else
					reportInForm.setOrgName(org.getOrgName());
			}
			
			StrutsMRepRangeDelegate delegate=new StrutsMRepRangeDelegate();
			/**已使用hibernate 卞以刚 2011-12-21**/
			recordCount=delegate.selectDBReportCount(reportInForm, operator);
			if(recordCount > 0){
				/**已使用hibernate 卞以刚 2011-12-21**/
				resList=delegate.selectDBReportRecord(reportInForm, operator, offset, limit);
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
		request.setAttribute("orgName", reportInForm.getOrgName());
		request.setAttribute(Config.APART_PAGE_OBJECT,aPage);
		request.setAttribute("orgId",reportInForm.getOrgId());

		if(request.getAttribute("messagenotnull") != null)
			messages = (FitechMessages)request.getAttribute("messagenotnull");

		if(messages.getMessages() != null && messages.getMessages().size() > 0)		
			request.setAttribute(Config.MESSAGES,messages);
		  
		if(resList!=null && resList.size()>0){
			request.setAttribute(Config.RECORDS,resList);   
		}
		// add by 王明明 将任务模式添加到request里
	 	request.setAttribute("system_schema_flag",this.getSchemaFlag());
	 	if(request.getParameter("nodeId")!=null){
	 		Config.NODE_ID = Integer.parseInt(request.getParameter("nodeId"));
	 	}
		return mapping.findForward("view");	     
	}
	  
	public String getTerm(ReportInForm reportInForm){
		String term="";  
//		String year = String.valueOf(reportInForm.getYear());
//		String setDate = String.valueOf(reportInForm.getTerm());	
		
		if(reportInForm.getOrgId()!=null&&!reportInForm.getOrgId().equals("")){
			term += (term.indexOf("?")>=0 ? "&" : "?");
			term += "orgId="+reportInForm.getOrgId();
		}
		
//		if(year!=null&&!year.equals("")){		
//			term += (term.indexOf("?")>=0 ? "&" : "?");			
//			term += "year="+year.toString();   		   
//		}	
//		
//		if(setDate!=null&&!setDate.equals("")){		
//			term += (term.indexOf("?")>=0 ? "&" : "?");			
//			term += "term="+setDate.toString();    
//		}
		if(reportInForm.getRepName()!=null&&!reportInForm.getRepName().equals("")){
			term += (term.indexOf("?")>=0 ? "&" : "?");
			term += "repName="+reportInForm.getRepName();
		}
		
		if(reportInForm.getDate()!=null&&!reportInForm.getDate().equals("")){
			term += (term.indexOf("?")>=0 ? "&" : "?");
			term += "date="+reportInForm.getDate();
		}
		
		if(term.indexOf("?")>=0)		
			term = term.substring(term.indexOf("?")+1);
		
		return term;
	}	
	public void test(){
		
		DBConn conn = null;
		try {
			File[] files = new File("").listFiles();
			conn = new DBConn();
			List<AfTemplate> list=conn.openSession().find("from AfTemplate");
			for(int i=0;i<list.size();i++){
				String templateId = list.get(i).getId().getTemplateId().trim();
				for(int j=0;j<files.length;j++){
					String name = files[i].getName();
					if(name.indexOf(templateId)==-1)
						System.out.println("============" + templateId);
				}
				
			}
		} catch (Exception he) {
			log.printStackTrace(he);

		} finally {
			if (conn != null)
				conn.closeSession();
		}
	}
}
