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
 * 日报表跑批处理Action
 * 
 * @author Nick
 * 
 */
public final class DayTaskAction extends DispatchAction {

	private static FitechException log = new FitechException(DayTaskAction.class);

	// 日报表查询首页
	public ActionForward view(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

		MessageResources resources = getResources(request);
		FitechMessages messages = new FitechMessages();
		// 取得request范围内的请求参数，并存放在dayTaskForm内
		DayTaskForm dayTaskForm = (DayTaskForm) form;
		RequestUtils.populate(dayTaskForm, request);

		int recordCount = 0; // 记录总数
		int offset = 0; // 偏移量
		int limit = 0; // 每页显示的记录条数
		List list = null;

		ApartPage aPage = new ApartPage();
		String strCurPage = request.getParameter("curPage");
		if (strCurPage != null) {
			if (!strCurPage.equals(""))
				aPage.setCurPage(new Integer(strCurPage).intValue());
		} else
			aPage.setCurPage(1);
		// 计算偏移量
		offset = (aPage.getCurPage() - 1) * Config.PER_PAGE_ROWS;
		limit = Config.PER_PAGE_ROWS;

		try {
			// 设定期数查询条件
			if (dayTaskForm.getQueryStartTaskDate() == null || dayTaskForm.getQueryStartTaskDate().equals("") || dayTaskForm.getQueryEndTaskDate() == null || dayTaskForm.getQueryEndTaskDate().equals("")) {
				Date date = new Date();
				dayTaskForm.setQueryStartTaskDate(DayDateUtil.firstDayOfMonth(date));// 设定开始时间为当月1号
				dayTaskForm.setQueryEndTaskDate(DayDateUtil.formatDateToYYYYMMDD(date));// 设定结束时间为当前时间
			}

			// 取得记录总数
			recordCount = DayReportDelegate.getRecordCount(dayTaskForm);
			// 显示分页后的记录
			if (recordCount > 0)

				list = DayReportDelegate.select(dayTaskForm, offset, limit);
		} catch (Exception e) {
			log.printStackTrace(e);
			messages.add(resources.getMessage("log.select.fail"));
		}
		// 移除request或session范围内的属性
		// FitechUtil.removeAttribute(mapping, request);
		// 把ApartPage对象存放在request范围内
		aPage.setTerm(this.getTerm(dayTaskForm));
		aPage.setCount(recordCount);
		request.setAttribute(Config.APART_PAGE_OBJECT, aPage);

		if (messages.getMessages() != null && messages.getMessages().size() > 0)
			request.setAttribute(Config.MESSAGES, messages);
		if (list != null && list.size() != 0)
			request.setAttribute(Config.RECORDS, list);

		return mapping.findForward("view");
	}

	// 日报表数据重跑
	public ActionForward reRun(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

		MessageResources resources = getResources(request);
		FitechMessages messages = new FitechMessages();
		// 取得request范围内的请求参数，并存放在dayTaskForm内
		DayTaskForm dayTaskForm = (DayTaskForm) form;
		RequestUtils.populate(dayTaskForm, request);

		try {
			// 调用任务进行重跑操作
			Timer timer = new Timer();
			DayReportTask task = new DayReportTask();
			// 设定重跑期数
			task.setTaskDate(dayTaskForm.getTaskDate());
			// 任务将在一毫秒中以后执行，并且只执行一次
			timer.schedule(task, 1);

			messages.add("任务开始重跑，请稍后确认重跑结果(如果当前有任务正在执行，则系统默认不进行重跑操作)");
		} catch (Exception e) {
			log.printStackTrace(e);
			messages.add("任务重跑操作失败!");
		}

		if (messages.getMessages() != null && messages.getMessages().size() > 0)
			request.setAttribute(Config.MESSAGES, messages);

		return view(mapping, form, request, response);
	}

	/**
	 * 取得查询条件url
	 */
	public String getTerm(DayTaskForm dayTaskForm) {
		String term = "";

		/** 期数开始时间 */
		String queryStartTaskDate = dayTaskForm.getQueryStartTaskDate();
		/** 期数结束时间 */
		String queryEndTaskDate = dayTaskForm.getQueryEndTaskDate();
		/** 报表ID */
		String queryTemplateId = dayTaskForm.getQueryTemplateId();
		/** 报表版本 */
		String queryVersionId = dayTaskForm.getQueryVersionId();
		/** 报表名称 */
		String queryTemplateName = dayTaskForm.getQueryTemplateName();
		/** 报表生成标志位，0为未执行，1为正在执行，2为执行成功，-1为执行失败 */
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
