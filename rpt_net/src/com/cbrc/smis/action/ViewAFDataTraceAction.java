package com.cbrc.smis.action;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.cbrc.org.form.AFDataTraceForm;
import com.cbrc.smis.adapter.StrutsMRepRangeDelegate;
import com.cbrc.smis.common.ApartPage;
import com.cbrc.smis.common.Config;
import com.cbrc.smis.form.ReportInForm;
import com.cbrc.smis.other.Aditing;
import com.cbrc.smis.security.Operator;
import com.cbrc.smis.service.IAFDataTraceService;
import com.cbrc.smis.service.impl.AFDataTraceServiceImpl;
import com.cbrc.smis.util.FitechException;
import com.fitech.gznx.common.PageListInfo;
import com.fitech.gznx.form.AFReportForm;
import com.fitech.gznx.service.AFReportDelegate;

public class ViewAFDataTraceAction extends Action{
	private static FitechException log=new FitechException(ViewAFDataTraceAction.class);
	@Override
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		/**获取查询参数-------开始*/
		String reportTerm = request.getParameter("reportTerm");
		String repName = request.getParameter("repName");
		String beginDate = request.getParameter("beginDate");
		String endDate = request.getParameter("endDate");
		String orgId = request.getParameter("orgId");
		String orgName = request.getParameter("orgName");
		String reportFlag = request.getParameter("reportFlag");
		if(reportFlag==null){
			HttpSession session = request.getSession();
			if (session.getAttribute(Config.REPORT_SESSION_FLG) != null){
				reportFlag = (String) session.getAttribute(Config.REPORT_SESSION_FLG);
			}
		}
		AFDataTraceForm traceForm = (AFDataTraceForm)form;
		if(traceForm==null)
			traceForm = new AFDataTraceForm();
		
		traceForm.setReportTerm(reportTerm);
		traceForm.setRepName(repName);
		traceForm.setBeginDate(beginDate);
		traceForm.setEndDate(endDate);
		traceForm.setOrgId(orgId);
		traceForm.setOrgName(orgName);
		/**获取查询参数-------结束*/
		
		IAFDataTraceService traceService = new AFDataTraceServiceImpl();//服务类
		List<AFDataTraceForm> fromList = null;
		int offset=0; //偏移量
        int limit=0;  //每页显示的记录条数
		String strCurPage=request.getParameter("curPage");
		ApartPage aPage=new ApartPage();
	    if(strCurPage!=null && !strCurPage.equals("")){
	    	aPage.setCurPage(new Integer(strCurPage).intValue());
	    }
	    else
	        aPage.setCurPage(1);
	        
	    //计算偏移量
	    offset=(aPage.getCurPage()-1)*Config.PER_PAGE_ROWS; 
	    limit = Config.PER_PAGE_ROWS;   
	    
	    
	    /***
         * 取得当前用户的权限信息
         */   
		HttpSession session = request.getSession();
		Operator operator = null; 
		if(session.getAttribute(Config.OPERATOR_SESSION_ATTRIBUTE_NAME)!=null)  
			operator = (Operator)session.getAttribute(Config.OPERATOR_SESSION_ATTRIBUTE_NAME);          

		int recordCount =0; //记录总数		
		PageListInfo pageListInfo = null;
		ReportInForm reportInForm = new ReportInForm();
		AFReportForm afReportForm=new AFReportForm();
		
		if(traceForm.getReportTerm() == null || traceForm.getReportTerm().equals("")){//报表期数
			String yestoday = (String) session.getAttribute(Config.USER_LOGIN_DATE);
			reportInForm.setYear(new Integer(yestoday.substring(0,4)));	
			reportInForm.setTerm(new Integer(yestoday.substring(5, 7)));
			reportInForm.setDay(new Integer(yestoday.substring(8,10)));
			reportInForm.setDate(yestoday.substring(0, 10));
			//人行和其他的
			afReportForm.setYear(yestoday.substring(0,4));	
			afReportForm.setTerm(yestoday.substring(5, 7));
			afReportForm.setDay(yestoday.substring(8,10));
			afReportForm.setDate(yestoday.substring(0, 10));
			traceForm.setReportTerm(yestoday.substring(0, 10));

		}else{
			reportInForm.setYear(new Integer(traceForm.getReportTerm().substring(0, 4)));
			reportInForm.setTerm(new Integer(traceForm.getReportTerm().substring(5, 7)));
			reportInForm.setDate(traceForm.getReportTerm());//放入报表期数
			
			afReportForm.setYear(traceForm.getReportTerm().substring(0, 4));
			afReportForm.setTerm(traceForm.getReportTerm().substring(5, 7));
			afReportForm.setDate(traceForm.getReportTerm());//放入报表期数
		}
		
		//List对象的初始化
		List<Aditing> resList=null;
		try{
			if(reportInForm.getOrgId() == null){
				reportInForm.setOrgId(operator.getOrgId());
			
				afReportForm.setOrgId(operator.getOrgId());
			}else{
				reportInForm.setOrgId(orgId);
				
				afReportForm.setOrgId(orgId);
			}
			if("1".equals(reportFlag)){
				StrutsMRepRangeDelegate delegate=new StrutsMRepRangeDelegate();
				/**已使用hibernate 卞以刚 2011-12-21**/
				recordCount=delegate.selectDBReportCount(reportInForm, operator);
				if(recordCount > 0){
					/**已使用hibernate 卞以刚 2011-12-21**/
					resList=delegate.selectDBReportRecord(reportInForm, operator, offset, limit);
				}
			}else{
				pageListInfo = AFReportDelegate.selectNeedReportRecord(afReportForm, operator,1);
				recordCount = (int) pageListInfo.getRowCount();
				resList = pageListInfo.getList();
			}
			
		}catch(Exception ex){
			log.printStackTrace(ex);
		}
		if(resList!=null && resList.size()>0){
			List<String> repList = new ArrayList<String>();
			String repInIds = "(";
			for(int i=0;i<resList.size();i++){
				Aditing a = resList.get(i);
				if(a.getRepInId()!=null && !a.getRepInId().equals("")){
					repList.add(a.getRepInId().toString());		
				}
			}
			if(repList.size()==1)
				repInIds += "'"+repList.get(0)+"'";
			if(repList.size()>=2){
				for(int i=0;i<repList.size();i++){
					if(i!=0)
						repInIds += ",";
					repInIds +=	"'"+repList.get(i)+"'";
				}
			}
			repInIds += ")";
			if(!repInIds.equals("()"))
				traceForm.setRepInIds(repInIds);
		}
		if(true || traceForm.getRepInIds()!=null)//珠海银行发现该查询有问题，临时修改成直接执行操作
			fromList = traceService.findListByAFDataTrace(traceForm, limit, offset,reportFlag);
	    request.setAttribute("resList", resList);
	    request.setAttribute("afReportForm", traceForm);
		request.setAttribute("formList", fromList);
		request.setAttribute("orgId",reportInForm.getOrgId());
		request.setAttribute("orgName", orgName);
		request.setAttribute("orgId", orgId);
		
		 //把ApartPage对象存放在request范围内
		List recordList=traceService.findListByAFDataTrace(traceForm, reportFlag);
		int counts =recordList!=null?recordList.size():0;
		//把ApartPage对象存放在request范围内
		aPage.setTerm(this.getTerm(traceForm));		 	
		
		aPage.setCount(counts);
		request.setAttribute(Config.APART_PAGE_OBJECT,aPage);
		return mapping.findForward("index");
	}
	
	public String getTerm(AFDataTraceForm reportInForm){
		String term="";  
//		String year = String.valueOf(reportInForm.getYear());
//		String setDate = String.valueOf(reportInForm.getTerm());	
		
		if(reportInForm.getOrgId()!=null&&!reportInForm.getOrgId().equals("")){
			term += (term.indexOf("?")>=0 ? "&" : "?");
			term += "orgId="+reportInForm.getOrgId();
		}
		if(reportInForm.getOrgName()!=null&&!reportInForm.getOrgName().equals("")){
			term += (term.indexOf("?")>=0 ? "&" : "?");
			term += "orgName="+reportInForm.getOrgName();
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
		
		if(reportInForm.getBeginDate()!=null&&!reportInForm.getBeginDate().equals("")){
			term += (term.indexOf("?")>=0 ? "&" : "?");
			term += "beginDate="+reportInForm.getBeginDate();
		}
		if(reportInForm.getEndDate()!=null&&!reportInForm.getEndDate().equals("")){
			term += (term.indexOf("?")>=0 ? "&" : "?");
			term += "endDate="+reportInForm.getEndDate();
		}
		if(reportInForm.getReportTerm()!=null&&!reportInForm.getReportTerm().equals("")){
			term += (term.indexOf("?")>=0 ? "&" : "?");
			term += "reportTerm="+reportInForm.getReportTerm();
		}
		
		if(term.indexOf("?")>=0)		
			term = term.substring(term.indexOf("?")+1);
		
		return term;
	}	
	
}
