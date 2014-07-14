package com.fitech.gznx.action;

import java.io.File;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.RequestUtils;

import com.cbrc.smis.excel.DB2Excel;
import com.cbrc.smis.form.ReportInForm;
import com.cbrc.smis.hibernate.ReportIn;
import com.cbrc.smis.security.Operator;
import com.cbrc.smis.util.FitechException;
import com.cbrc.smis.util.FitechMessages;
import com.fitech.gznx.form.AFReportForm;
import com.fitech.gznx.po.AfReport;
import com.fitech.gznx.service.AFDataDelegate;
import com.fitech.gznx.service.AFReportDelegate;
import com.fitech.net.adapter.StrutsCollectDelegate;
import com.fitech.net.adapter.StrutsReportInDelegate;
import com.fitech.net.config.Config;

/**
 * <p>Title: TranslationReportAction</p>
 * <p>Description 汇总数据转上报数据Action</p>
 * <p>Copyright: Copyright (c) 2007</p>
 * <p>Company:</p>
 * 
 * @author 龚明
 * @date 2009-09-14
 * @version 1.0
 * 
 */
public class TranslationNXReportAction extends Action {

	private FitechException log = new FitechException(TranslationNXReportAction.class);

	/**
	 * @param mapping
	 *            ActionMapping
	 * @param form
	 *            ActionForm
	 * @param request
	 *            HttpServletRequest
	 * @param response
	 *            HttpServletResponse IOEception 有异常捕捉并抛出 SeverletException
	 */
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {

		FitechMessages messages = new FitechMessages();
		AFReportForm reportInForm = (AFReportForm) form;
		RequestUtils.populate(reportInForm, request);

		/** 取得当前用户的权限信息 */
		HttpSession session = request.getSession();
		Operator operator = null;
		if (session.getAttribute(Config.OPERATOR_SESSION_ATTRIBUTE_NAME) != null)
			operator = (Operator) session
					.getAttribute(Config.OPERATOR_SESSION_ATTRIBUTE_NAME);

		Integer repInId = Integer.valueOf(request.getParameter("repInId"));

		// 根据汇总情况表是否有对应的本期的已汇总的数据判断是否转上报数据
		if (repInId == null) {
			
			messages.add("没有汇总数据，不能转上报数据！");
			
		} else {
			
			// 查找有无已上报数据的记录
			AfReport _reportIn = AFReportDelegate.findHasTranslation(repInId);
			
			if (_reportIn != null && Config.CHECK_FLAG_PASS.equals(
					_reportIn.getCheckFlag().shortValue())) {
				
				messages.add("该期报表已审核，不可转上报数据！");
				
			} else if (_reportIn != null && Config.CHECK_FLAG_UNCHECK.equals(
					_reportIn.getCheckFlag().shortValue())) {
				
				messages.add("该期报表已上报，不可转上报数据");
				
			} else {
				
				//查出需转上报的report记录
				AfReport reportIn = AFReportDelegate.findById(repInId.longValue());
				
//				// ExecuteCollect collect = new ExecuteCollect();
//				DB2Excel db2Excel = new DB2Excel(reportIn.getRepId().intValue());
//				
//				if (!new File(Config.getReleaseTemplatePath()
//						+ com.cbrc.smis.common.Config.FILESEPARATOR
//						+ reportIn.getYear() + "_" + reportIn.getTerm()
//						+ com.cbrc.smis.common.Config.FILESEPARATOR
//						+ operator.getOrgId()).exists()) {
//					new File(Config.getReleaseTemplatePath()
//							+ com.cbrc.smis.common.Config.FILESEPARATOR
//							+ reportIn.getYear() + "_" + reportIn.getTerm())
//							.mkdir();
//					new File(Config.getReleaseTemplatePath()
//							+ com.cbrc.smis.common.Config.FILESEPARATOR
//							+ reportIn.getYear() + "_" + reportIn.getTerm()
//							+ com.cbrc.smis.common.Config.FILESEPARATOR
//							+ operator.getOrgId()).mkdir();
//				}
//				
//				boolean createExcel = db2Excel.createExcel(Config
//						.getReleaseTemplatePath()
//						+ com.cbrc.smis.common.Config.FILESEPARATOR
//						+ reportIn.getYear()
//						+ "_"
//						+ reportIn.getTerm()
//						+ com.cbrc.smis.common.Config.FILESEPARATOR
//						+ operator.getOrgId()
//						+ com.cbrc.smis.common.Config.FILESEPARATOR
//						+ reportIn.getTemplateId()
//						+ "_"
//						+ reportIn.getVersionId() + ".xls");
//
//				if (createExcel
//						&& StrutsCollectDelegate.transCollectDatat(reportIn)) {
//					messages.add("汇总数据转上报数据成功！");
//				}
				
				Integer templateType = null;
				if (session.getAttribute(com.cbrc.smis.common.Config.REPORT_SESSION_FLG) != null)
					templateType =  Integer.valueOf(session.getAttribute(
							com.cbrc.smis.common.Config.REPORT_SESSION_FLG).toString());
				
				//转上报数据
				AFDataDelegate afd = new AFDataDelegate();
				String result = afd.transCollectDataToReport(_reportIn, reportIn, templateType);
				
				messages.add(result);
			}
		}

		String term = mapping.findForward("view").getPath();
		
		/** 加入报表编号条件 */
		if (reportInForm.getTemplateId() != null
				&& !reportInForm.getTemplateId().equals("")) {
			term += (term.indexOf("?") >= 0 ? "&" : "?");
			term += "templateId=" + reportInForm.getTemplateId();
		}
		/** 加入报表名称条件 */
		if (reportInForm.getRepName() != null
				&& !reportInForm.getRepName().equals("")) {
			term += (term.indexOf("?") >= 0 ? "&" : "?");
			/** 若是WebLogic则不需要进行转码，直接作为参数传递 */
			term += "repName=" + reportInForm.getRepName();
			/** 若是WebSphere则需要先进行转码，再作为参数传递 */
			// term += "repName=" + new
			// String(reportInForm.getRepName().getBytes("gb2312"),
			// "iso-8859-1");
		}
		/** 加入模板类型条件 */
		if (reportInForm.getBak1() != null) {
			term += (term.indexOf("?") >= 0 ? "&" : "?");
			term += "bak1=" + reportInForm.getBak1();
		}
		/** 加入报表年份条件 */
		if (reportInForm.getDate() != null) {
			term += (term.indexOf("?") >= 0 ? "&" : "?");
			term += "date=" + reportInForm.getDate();
		}
		/** 加入报表期数条件 */
//		if (reportInForm.getTerm() != null) {
//			term += (term.indexOf("?") >= 0 ? "&" : "?");
//			term += "term=" + reportInForm.getTerm();
//		}
		
		request.setAttribute(Config.MESSAGE, messages);
		return new ActionForward(term);
	}
}
