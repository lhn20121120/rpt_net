package com.fitech.gznx.action;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Timer;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.apache.struts.util.MessageResources;
import org.apache.struts.util.RequestUtils;

import com.cbrc.smis.common.ApartPage;
import com.cbrc.smis.common.Config;
import com.cbrc.smis.util.DayReportTask;
import com.cbrc.smis.util.FitechException;
import com.cbrc.smis.util.FitechMessages;
import com.fitech.gznx.form.DayTaskForm;
import com.fitech.gznx.service.DayReportDelegate;
import com.fitech.gznx.util.DayDateUtil;

/**
 * �ձ�����������Action
 * 
 * @author Nick
 * 
 */
public final class DayTaskAction extends DispatchAction {

	private static FitechException log = new FitechException(DayTaskAction.class);

	// �ձ����ѯ��ҳ
	public ActionForward view(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

		MessageResources resources = getResources(request);
		FitechMessages messages = new FitechMessages();
		// ȡ��request��Χ�ڵ�����������������dayTaskForm��
		DayTaskForm dayTaskForm = (DayTaskForm) form;
		RequestUtils.populate(dayTaskForm, request);

		int recordCount = 0; // ��¼����
		int offset = 0; // ƫ����
		int limit = 0; // ÿҳ��ʾ�ļ�¼����
		List list = null;

		ApartPage aPage = new ApartPage();
		String strCurPage = request.getParameter("curPage");
		if (strCurPage != null) {
			if (!strCurPage.equals(""))
				aPage.setCurPage(new Integer(strCurPage).intValue());
		} else
			aPage.setCurPage(1);
		// ����ƫ����
		offset = (aPage.getCurPage() - 1) * Config.PER_PAGE_ROWS;
		limit = Config.PER_PAGE_ROWS;

		try {
			// �趨������ѯ����
			if (dayTaskForm.getQueryStartTaskDate() == null || dayTaskForm.getQueryStartTaskDate().equals("") || dayTaskForm.getQueryEndTaskDate() == null || dayTaskForm.getQueryEndTaskDate().equals("")) {
				Date date = new Date();
				dayTaskForm.setQueryStartTaskDate(DayDateUtil.firstDayOfMonth(date));// �趨��ʼʱ��Ϊ����1��
				dayTaskForm.setQueryEndTaskDate(DayDateUtil.formatDateToYYYYMMDD(date));// �趨����ʱ��Ϊ��ǰʱ��
			}

			// ȡ�ü�¼����
			recordCount = DayReportDelegate.getRecordCount(dayTaskForm);
			// ��ʾ��ҳ��ļ�¼
			if (recordCount > 0)

				list = DayReportDelegate.select(dayTaskForm, offset, limit);
		} catch (Exception e) {
			log.printStackTrace(e);
			messages.add(resources.getMessage("log.select.fail"));
		}
		// �Ƴ�request��session��Χ�ڵ�����
		// FitechUtil.removeAttribute(mapping, request);
		// ��ApartPage��������request��Χ��
		aPage.setTerm(this.getTerm(dayTaskForm));
		aPage.setCount(recordCount);
		request.setAttribute(Config.APART_PAGE_OBJECT, aPage);

		if (messages.getMessages() != null && messages.getMessages().size() > 0)
			request.setAttribute(Config.MESSAGES, messages);
		if (list != null && list.size() != 0)
			request.setAttribute(Config.RECORDS, list);

		return mapping.findForward("view");
	}

	// �ձ�����������
	public ActionForward reRun(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

		MessageResources resources = getResources(request);
		FitechMessages messages = new FitechMessages();
		// ȡ��request��Χ�ڵ�����������������dayTaskForm��
		DayTaskForm dayTaskForm = (DayTaskForm) form;
		RequestUtils.populate(dayTaskForm, request);

		try {
			// ��������������ܲ���
			Timer timer = new Timer();
			DayReportTask task = new DayReportTask();
			// �趨��������
			task.setTaskDate(dayTaskForm.getTaskDate());
			// ������һ�������Ժ�ִ�У�����ִֻ��һ��
			timer.schedule(task, 1);

			messages.add("����ʼ���ܣ����Ժ�ȷ�����ܽ��(�����ǰ����������ִ�У���ϵͳĬ�ϲ��������ܲ���)");
		} catch (Exception e) {
			log.printStackTrace(e);
			messages.add("�������ܲ���ʧ��!");
		}

		if (messages.getMessages() != null && messages.getMessages().size() > 0)
			request.setAttribute(Config.MESSAGES, messages);

		return view(mapping, form, request, response);
	}

	/**
	 * ȡ�ò�ѯ����url
	 */
	public String getTerm(DayTaskForm dayTaskForm) {
		String term = "";

		/** ������ʼʱ�� */
		String queryStartTaskDate = dayTaskForm.getQueryStartTaskDate();
		/** ��������ʱ�� */
		String queryEndTaskDate = dayTaskForm.getQueryEndTaskDate();
		/** ����ID */
		String queryTemplateId = dayTaskForm.getQueryTemplateId();
		/** ����汾 */
		String queryVersionId = dayTaskForm.getQueryVersionId();
		/** �������� */
		String queryTemplateName = dayTaskForm.getQueryTemplateName();
		/** �������ɱ�־λ��0Ϊδִ�У�1Ϊ����ִ�У�2Ϊִ�гɹ���-1Ϊִ��ʧ�� */
		Integer queryFlag = dayTaskForm.getQueryFlag();

		if (queryStartTaskDate != null && !queryStartTaskDate.trim().equals("")) {
			term += (term.indexOf("?") >= 0 ? "&" : "?");
			term += "queryStartTaskDate=" + queryStartTaskDate;
		}
		if (queryEndTaskDate != null && !queryEndTaskDate.trim().equals("")) {
			term += (term.indexOf("?") >= 0 ? "&" : "?");
			term += "queryEndTaskDate=" + queryEndTaskDate;
		}
		if (queryTemplateId != null && !queryTemplateId.trim().equals("")) {
			term += (term.indexOf("?") >= 0 ? "&" : "?");
			term += "queryTemplateId=" + queryTemplateId;
		}
		if (queryVersionId != null && !queryVersionId.trim().equals("")) {
			term += (term.indexOf("?") >= 0 ? "&" : "?");
			term += "queryVersionId=" + queryVersionId;
		}
		if (queryTemplateName != null && !queryTemplateName.trim().equals("")) {
			term += (term.indexOf("?") >= 0 ? "&" : "?");
			term += "queryTemplateName=" + queryTemplateName;
		}
		if (queryFlag != null) {
			term += (term.indexOf("?") >= 0 ? "&" : "?");
			term += "queryFlag=" + queryFlag;
		}

		if (term.indexOf("?") >= 0)
			term = term.substring(term.indexOf("?") + 1);
		return term;
	}
}
