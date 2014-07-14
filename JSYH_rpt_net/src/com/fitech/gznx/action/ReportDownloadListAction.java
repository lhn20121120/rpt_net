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

import com.cbrc.smis.adapter.StrutsMRepRangeDelegate;
import com.cbrc.smis.common.ApartPage;
import com.cbrc.smis.common.Config;
import com.cbrc.smis.form.ReportInForm;
import com.cbrc.smis.other.Aditing;
import com.cbrc.smis.security.Operator;
import com.cbrc.smis.util.FitechException;
import com.cbrc.smis.util.FitechMessages;
import com.fitech.gznx.common.DateUtil;
import com.fitech.gznx.common.PageListInfo;
import com.fitech.gznx.form.AFReportForm;
import com.fitech.gznx.service.AFReportDelegate;
import com.fitech.gznx.service.AFReportProductDelegate;

/**
 * 显示report的列表，以供下载
 */
public class ReportDownloadListAction extends Action {

	private static FitechException log = new FitechException(
			ReportDownloadListAction.class);

	/**
	 * 已使用hibernate 卞以刚 2011-12-22
	 * 影响对象：AfViewReport AfReport
	 * Method execute
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return ActionForward
	 */
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {

		MessageResources resources = getResources(request);
		FitechMessages messages = new FitechMessages();
		// 取得request范围内的请求参数，并存放在reportForm内
		AFReportForm afReportForm = (AFReportForm) form;
		RequestUtils.populate(afReportForm, request);

		int recordCount = 0; // 记录总数

		// List对象的初始化
		List resList = null;

		ApartPage aPage = new ApartPage();
		String strCurPage = request.getParameter("curPage");
		int curPage = 1;
		if (strCurPage != null) {
			if(!strCurPage.equals("")){
				curPage =  new Integer(strCurPage).intValue();
				aPage.setCurPage(new Integer(strCurPage).intValue());
			}
		} else
			aPage.setCurPage(1);

		/**
		 * 取得当前用户的权限信息
		 */
		HttpSession session = request.getSession();
		Operator operator = null;
		if (session.getAttribute(Config.OPERATOR_SESSION_ATTRIBUTE_NAME) != null)
			operator = (Operator) session
					.getAttribute(Config.OPERATOR_SESSION_ATTRIBUTE_NAME);

		if (afReportForm==null) afReportForm = new AFReportForm();

//		if (afReportForm.getOrgId() == null)
//			afReportForm.setOrgId(operator.getOrgId());
		
		if (afReportForm.getDate() == null || afReportForm.getDate().equals("")){
			String yestoday = (String) session.getAttribute(Config.USER_LOGIN_DATE);
			afReportForm.setDate(yestoday);
		}
		//取得模板类型
//		if(session.getAttribute(Config.REPORT_SESSION_FLG)!=null)
//			afReportForm.setTemplateType(session.getAttribute(
//					Config.REPORT_SESSION_FLG).toString());
		afReportForm.setTemplateType(com.fitech.gznx.common.Config.OTHER_REPORT);
		PageListInfo pageListInfo = null;
		try {

			//List list = AFReportDelegate.selectNeedReportList(afReportForm, operator);
			/**已使用hibernate 卞以刚 2011-12-22
			 * 影响对象：AfViewReport AfReport**/
			pageListInfo = AFReportProductDelegate.selectDownloadReportList(afReportForm,operator,curPage);
			resList = pageListInfo.getList();
			recordCount = (int) pageListInfo.getRowCount();
		
		} catch (Exception ex) {
			log.printStackTrace(ex);
			messages.add(resources.getMessage("select.dataReport.failed"));
		}

		// 把ApartPage对象存放在request范围内
		aPage.setTerm(this.getTerm(afReportForm));
		aPage.setCount(recordCount);
		request.setAttribute(Config.APART_PAGE_OBJECT, aPage);
		request.setAttribute("orgId", afReportForm.getOrgId());
		request.setAttribute("reportName", afReportForm.getRepName());
		request.setAttribute("date", afReportForm.getDate());
		
		if (messages.getMessages() != null && messages.getMessages().size() > 0)
			request.setAttribute(Config.MESSAGES, messages);

		if (resList != null && resList.size() > 0) {
			request.setAttribute(Config.RECORDS, resList);
		}
		return mapping.findForward("view");
	}

	public String getTerm(AFReportForm afReportForm) {

		String term = "";

		if (afReportForm.getDate() != null && ! afReportForm.getDate().equals("")) {
			term += (term.indexOf("?") >= 0 ? "&" : "?");
			term += "date=" + afReportForm.getDate();
		}

		if (afReportForm.getOrgId() != null && !afReportForm.getOrgId().equals("")) {
			term += (term.indexOf("?") >= 0 ? "&" : "?");
			term += "orgId=" + afReportForm.getOrgId();
		}

		if (afReportForm.getRepName() != null && !afReportForm.getRepName().equals("")) {
			term += (term.indexOf("?") >= 0 ? "&" : "?");
			term += "repName=" + afReportForm.getRepName();
		}
		
		if (afReportForm.getBak1() != null && !afReportForm.getBak1().equals("")) {
			term += (term.indexOf("?") >= 0 ? "&" : "?");
			term += "bak1=" + afReportForm.getBak1();
		}
		String orgName = afReportForm.getOrgName();
		if(orgName!=null&&!orgName.equals("")){		
			term += (term.indexOf("?")>=0 ? "&" : "?");			
			term += "orgName="+orgName;    
		}
		if (term.indexOf("?") >= 0)
			term = term.substring(term.indexOf("?") + 1);

		return term;
	}
}
