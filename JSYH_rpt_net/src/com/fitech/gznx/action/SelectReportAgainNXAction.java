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

import com.cbrc.smis.common.ApartPage;
import com.cbrc.smis.common.Config;
import com.cbrc.smis.security.Operator;
import com.cbrc.smis.util.FitechException;
import com.cbrc.smis.util.FitechMessages;
import com.fitech.gznx.common.StringUtil;
import com.fitech.gznx.form.AFReportForm;
import com.fitech.gznx.service.AFReportAgainDelegate;
import com.fitech.gznx.service.AFTemplateTypeDelegate;

/**
 * 已使用hibernate 卞以刚 2011-12-27
 * 影响对象：AfTemplate AfReportAgain  AfTemplate
 * 报表重报查询
 * @author YeE
 *
 */
public class SelectReportAgainNXAction extends Action {
	
	private static FitechException log = new FitechException(SelectReportAgainNXAction.class);  
	   
	/**
	 * Performs action.
	 * @param mapping Action mapping.
	 * @param form Action form.
	 * @param request HTTP request.
	 * @param response HTTP response.
	 * @exception IOException if an input/output error occurs
	 * @exception ServletException if a servlet exception occurs
	 */
	public ActionForward execute(
      ActionMapping mapping,
      ActionForm form,
      HttpServletRequest request,
      HttpServletResponse response   
	)throws IOException, ServletException {
	 
		MessageResources resources = getResources(request);	   
		FitechMessages messages = new FitechMessages();		
		AFReportForm reportInForm = (AFReportForm) form;		
		RequestUtils.populate(reportInForm, request);
		
//		if(reportInForm.getYear()!=null && reportInForm.getYear().intValue()==0) reportInForm.setYear(null);
//	    if(reportInForm.getTerm()!=null && reportInForm.getTerm().intValue()==0) reportInForm.setTerm(null);

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
		
		//获得当前用户信息
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
			/**已使用Hibernate 卞以刚 2011-12-27
			 * 影响对象：AfTemplate AfReportAgain**/
	   		recordCount = AFReportAgainDelegate.getCount(reportInForm,operator);
	   		//显示分页后的记录
	   		if(recordCount > 0)
	   			/**已使用hibernate 卞以刚 2011-12-27
	   			 * 影响对象：AfReportAgain AfTemplate**/
		   	    list = AFReportAgainDelegate.getRecord(reportInForm,offset,limit,operator);  
		}catch (Exception e){
			log.printStackTrace(e);
			messages.add(resources.getMessage("forse_report_again.select.fail"));		
		}
		//把ApartPage对象存放在request范围内
	 	aPage.setTerm(this.getTerm(reportInForm));
	 	aPage.setCount(recordCount);
	 	request.setAttribute(Config.APART_PAGE_OBJECT,aPage);
//	 	if(!StringUtil.isEmpty(reportInForm.getBak1()) && StringUtil.isEmpty(reportInForm.getTemplateTypeName())){
//	       	 String templateName = AFTemplateTypeDelegate.getTemplateTypeName(reportInForm.getBak1(), reportFlg);
//	       	reportInForm.setTemplateTypeName(templateName);
//	        }
	 	if(messages.getMessages() != null && messages.getMessages().size() > 0)
	 		request.setAttribute(Config.MESSAGES,messages);
	 	if(list!=null && list.size()!=0)
	 		request.setAttribute(Config.RECORDS,list);
	 	//将查询条件存放在request范围内
	 	request.setAttribute("form",reportInForm);
	 	
	 	return mapping.findForward("view");   
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
		/**加入模板类型条件*/
		if(reportInForm.getBak1() != null){
			term += (term.indexOf("?")>=0 ? "&" : "?");
			term += "bak1=" + reportInForm.getBak1();
		}
		/**加入报表批次条件 add by 王明明*/
		if(reportInForm.getSupplementFlag() != null){
			term += (term.indexOf("?")>=0 ? "&" : "?");
			term += "supplementFlag=" + reportInForm.getSupplementFlag();
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
