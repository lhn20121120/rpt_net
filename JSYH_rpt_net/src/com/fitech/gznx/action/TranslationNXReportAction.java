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
 * <p>Description ��������ת�ϱ�����Action</p>
 * <p>Copyright: Copyright (c) 2007</p>
 * <p>Company:</p>
 * 
 * @author ����
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
	 *            HttpServletResponse IOEception ���쳣��׽���׳� SeverletException
	 */
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {

		FitechMessages messages = new FitechMessages();
		AFReportForm reportInForm = (AFReportForm) form;
		RequestUtils.populate(reportInForm, request);

		/** ȡ�õ�ǰ�û���Ȩ����Ϣ */
		HttpSession session = request.getSession();
		Operator operator = null;
		if (session.getAttribute(Config.OPERATOR_SESSION_ATTRIBUTE_NAME) != null)
			operator = (Operator) session
					.getAttribute(Config.OPERATOR_SESSION_ATTRIBUTE_NAME);

		Integer repInId = Integer.valueOf(request.getParameter("repInId"));

		// ���ݻ���������Ƿ��ж�Ӧ�ı��ڵ��ѻ��ܵ������ж��Ƿ�ת�ϱ�����
		if (repInId == null) {
			
			messages.add("û�л������ݣ�����ת�ϱ����ݣ�");
			
		} else {
			
			// �����������ϱ����ݵļ�¼
			AfReport _reportIn = AFReportDelegate.findHasTranslation(repInId);
			
			if (_reportIn != null && Config.CHECK_FLAG_PASS.equals(
					_reportIn.getCheckFlag().shortValue())) {
				
				messages.add("���ڱ�������ˣ�����ת�ϱ����ݣ�");
				
			} else if (_reportIn != null && Config.CHECK_FLAG_UNCHECK.equals(
					_reportIn.getCheckFlag().shortValue())) {
				
				messages.add("���ڱ������ϱ�������ת�ϱ�����");
				
			} else {
				
				//�����ת�ϱ���report��¼
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
//					messages.add("��������ת�ϱ����ݳɹ���");
//				}
				
				Integer templateType = null;
				if (session.getAttribute(com.cbrc.smis.common.Config.REPORT_SESSION_FLG) != null)
					templateType =  Integer.valueOf(session.getAttribute(
							com.cbrc.smis.common.Config.REPORT_SESSION_FLG).toString());
				
				//ת�ϱ�����
				AFDataDelegate afd = new AFDataDelegate();
				String result = afd.transCollectDataToReport(_reportIn, reportIn, templateType);
				
				messages.add(result);
			}
		}

		String term = mapping.findForward("view").getPath();
		
		/** ���뱨�������� */
		if (reportInForm.getTemplateId() != null
				&& !reportInForm.getTemplateId().equals("")) {
			term += (term.indexOf("?") >= 0 ? "&" : "?");
			term += "templateId=" + reportInForm.getTemplateId();
		}
		/** ���뱨���������� */
		if (reportInForm.getRepName() != null
				&& !reportInForm.getRepName().equals("")) {
			term += (term.indexOf("?") >= 0 ? "&" : "?");
			/** ����WebLogic����Ҫ����ת�룬ֱ����Ϊ�������� */
			term += "repName=" + reportInForm.getRepName();
			/** ����WebSphere����Ҫ�Ƚ���ת�룬����Ϊ�������� */
			// term += "repName=" + new
			// String(reportInForm.getRepName().getBytes("gb2312"),
			// "iso-8859-1");
		}
		/** ����ģ���������� */
		if (reportInForm.getBak1() != null) {
			term += (term.indexOf("?") >= 0 ? "&" : "?");
			term += "bak1=" + reportInForm.getBak1();
		}
		/** ���뱨��������� */
		if (reportInForm.getDate() != null) {
			term += (term.indexOf("?") >= 0 ? "&" : "?");
			term += "date=" + reportInForm.getDate();
		}
		/** ���뱨���������� */
//		if (reportInForm.getTerm() != null) {
//			term += (term.indexOf("?") >= 0 ? "&" : "?");
//			term += "term=" + reportInForm.getTerm();
//		}
		
		request.setAttribute(Config.MESSAGE, messages);
		return new ActionForward(term);
	}
}
