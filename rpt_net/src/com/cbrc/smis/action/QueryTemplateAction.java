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

			this.conductQuery(request, form); // �����NULL˵���ǲ�ѯ����

		else

			this.conductApart(request, apartType.trim()); // �������NULL˵���Ƿ�ҳ����

		return (mapping.findForward("showTemplateJsp"));

	}

	/**
	 * ���û��ڿͻ��������ѯ����ʱ���ø÷������д���
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
				.getAttribute("mrqt"); // ��¼��ѯ������ʵ��

		if (mrqt != null)

			session.removeAttribute("mrqt"); // ������ھ���ɾ��

		ApartPageState aps = (ApartPageState) session.getAttribute("aps"); // ��¼��ѯҳ��״̬��ʵ��

		if (aps != null)

			session.removeAttribute("aps"); // ������ھ���ɾ��

		List ts = (List) request.getAttribute("ts"); // һҳ��ģ���¼

		if (ts != null)

			request.removeAttribute("ts"); // ���ģ���¼���ھ���ɾ��

		mrqt = this.getMChildReportQueryTerm(mrqtForm); // ����this.getMChildReportQueryTerm�����õ�MChildReportQueryTerm���͵�ʵ����

		session.setAttribute("mrqt", mrqt); // ���洢��ѯ������ʵ�������session��

		int recordCount = StrutsMChildReportDelegate.getMChildReportCount(mrqt); // �õ���¼������

		aps = new ApartPageState(recordCount, Config.PER_PAGE_ROWS); // NEW��һ����ҳ��

		ts = StrutsMChildReportDelegate.queryMChildReports(mrqt,
				aps.getBegin(), aps.getLimit()); // �õ����ص�ģ���¼��

		session.setAttribute("aps", aps);

		request.setAttribute("ts", ts);

	}

	/**
	 * �÷������ڴ����ҳ����
	 * 
	 * @param request
	 * @param apartType
	 *            type:���������ַ���(��ҳ,��һҳ,��һҳ,βҳ)
	 * @throws Exception
	 */
	public void conductApart(HttpServletRequest request, String apartType)
			throws Exception {

		HttpSession session = request.getSession();

		MChildReportQueryTerm mrqt = (MChildReportQueryTerm) session
				.getAttribute("mrqt"); // ��¼��ѯ������ʵ��

		ApartPageState aps = (ApartPageState) session.getAttribute("aps"); // ��¼ҳ��״̬��ʵ��

		if ((aps != null) && (mrqt != null)) {

			aps.conduct(apartType);

			List ts = StrutsMChildReportDelegate.queryMChildReports(mrqt, aps
					.getBegin(), aps.getLimit()); // �õ����ص�ģ���¼��

			request.setAttribute("ts", ts);

		}

	}

	/**
	 * �÷�����MChildReportQueryTermFormת��ΪMChildReportQueryTermʵ����
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
