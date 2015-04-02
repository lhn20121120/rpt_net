package com.cbrc.smis.action;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.cbrc.smis.adapter.StrutsMChildReportDelegate;
import com.cbrc.smis.common.ApartPageState;
import com.cbrc.smis.common.Config;
import com.cbrc.smis.entity.MChildReportQueryTerm;
import com.cbrc.smis.form.MChildReportQueryTermForm;

public final class QueryTemplateAction extends Action {

	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String apartType = request.getParameter("apartType");

		if (apartType == null)

			this.conductQuery(request, form); // 如果是NULL说明是查询操作

		else

			this.conductApart(request, apartType.trim()); // 如果不是NULL说明是分页操作

		return (mapping.findForward("showTemplateJsp"));

	}

	/**
	 * 当用户在客户端输入查询参数时调用该方法进行处理
	 * 
	 * @param request
	 * @param form
	 * @throws Exception
	 */
	public void conductQuery(HttpServletRequest request, ActionForm form)
			throws Exception {

		MChildReportQueryTermForm mrqtForm = (MChildReportQueryTermForm) form;

		HttpSession session = (HttpSession) request.getSession();

		MChildReportQueryTerm mrqt = (MChildReportQueryTerm) session
				.getAttribute("mrqt"); // 记录查询条件的实体

		if (mrqt != null)

			session.removeAttribute("mrqt"); // 如果存在就先删除

		ApartPageState aps = (ApartPageState) session.getAttribute("aps"); // 记录查询页面状态的实体

		if (aps != null)

			session.removeAttribute("aps"); // 如果存在就先删除

		List ts = (List) request.getAttribute("ts"); // 一页的模板记录

		if (ts != null)

			request.removeAttribute("ts"); // 如果模板记录存在就先删除

		mrqt = this.getMChildReportQueryTerm(mrqtForm); // 调用this.getMChildReportQueryTerm方法得到MChildReportQueryTerm类型的实体类

		session.setAttribute("mrqt", mrqt); // 将存储查询条件的实体类存入session中

		int recordCount = StrutsMChildReportDelegate.getMChildReportCount(mrqt); // 得到记录的数量

		aps = new ApartPageState(recordCount, Config.PER_PAGE_ROWS); // NEW出一个分页类

		ts = StrutsMChildReportDelegate.queryMChildReports(mrqt,
				aps.getBegin(), aps.getLimit()); // 得到返回的模板记录集

		session.setAttribute("aps", aps);

		request.setAttribute("ts", ts);

	}

	/**
	 * 该方法用于处理分页操作
	 * 
	 * @param request
	 * @param apartType
	 *            type:操作类型字符串(首页,上一页,下一页,尾页)
	 * @throws Exception
	 */
	public void conductApart(HttpServletRequest request, String apartType)
			throws Exception {

		HttpSession session = request.getSession();

		MChildReportQueryTerm mrqt = (MChildReportQueryTerm) session
				.getAttribute("mrqt"); // 记录查询条件的实体

		ApartPageState aps = (ApartPageState) session.getAttribute("aps"); // 记录页面状态的实体

		if ((aps != null) && (mrqt != null)) {

			aps.conduct(apartType);

			List ts = StrutsMChildReportDelegate.queryMChildReports(mrqt, aps
					.getBegin(), aps.getLimit()); // 得到返回的模板记录集

			request.setAttribute("ts", ts);

		}

	}

	/**
	 * 该方法将MChildReportQueryTermForm转化为MChildReportQueryTerm实体类
	 * 
	 * @param form
	 *            type: MChildReportQueryTermForm
	 * @return type: MChildReportQueryTerm
	 * @throws Exception
	 */
	public MChildReportQueryTerm getMChildReportQueryTerm(
			MChildReportQueryTermForm form) throws Exception {

		MChildReportQueryTerm mrqt = new MChildReportQueryTerm(form
				.getReportName(), form.getReportVersion(), form.getOrderType());

		return mrqt;
	}
}
