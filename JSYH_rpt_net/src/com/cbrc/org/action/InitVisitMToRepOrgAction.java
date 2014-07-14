package com.cbrc.org.action;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.cbrc.org.form.PageStateForm;
import com.cbrc.smis.common.ConfigOncb;

public class InitVisitMToRepOrgAction extends Action {

	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		HttpSession session = request.getSession();

		int mToRepOrgCount = com.cbrc.org.adapter.StrutsMToRepOrgDelegate
				.getMToRepOrgCount(); // �õ���������������

		int recordCountByPage = ConfigOncb.recordCountByPage; // �õ�ÿҳ����ʾ�ļ�¼��

		int m = mToRepOrgCount / recordCountByPage;

		int y = mToRepOrgCount % recordCountByPage;

		if (y != 0)

			m++;

		int pageCount = m; // ���㲢�õ���ҳ��

		boolean existsNextPage = true;

		boolean endPage = false;

		if (m == 1) {

			existsNextPage = false;

			endPage = true;

		}

		PageStateForm psf = new PageStateForm();

		psf.setRecordCount(mToRepOrgCount); // �����������ܼ�¼��

		psf.setRecordCountByPage(recordCountByPage);// ÿҳ��ʾ�ļ�¼��

		psf.setPageCount(m); // ��ҳ��

		psf.setPageVisiting(0); // ��ʼ�����ڷ��ʵ�ҳ��Ϊ0

		psf.setExistsListPage(false);

		psf.setExistsNextPage(existsNextPage);

		psf.setFirstPage(true);

		psf.setEndPage(endPage);

		session.setAttribute("psf", psf);

		List dfs = com.cbrc.org.adapter.StrutsMToRepOrgDelegate
				.getDeputationForms(0, recordCountByPage);

		session.setAttribute("dfs", dfs);

		return mapping.findForward("showMToRepOrgsJsp");
	}

}
