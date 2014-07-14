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
				.getMToRepOrgCount(); // 得到代报机构的总数

		int recordCountByPage = ConfigOncb.recordCountByPage; // 得到每页需显示的记录数

		int m = mToRepOrgCount / recordCountByPage;

		int y = mToRepOrgCount % recordCountByPage;

		if (y != 0)

			m++;

		int pageCount = m; // 计算并得到总页数

		boolean existsNextPage = true;

		boolean endPage = false;

		if (m == 1) {

			existsNextPage = false;

			endPage = true;

		}

		PageStateForm psf = new PageStateForm();

		psf.setRecordCount(mToRepOrgCount); // 代报机构的总记录数

		psf.setRecordCountByPage(recordCountByPage);// 每页显示的记录数

		psf.setPageCount(m); // 总页数

		psf.setPageVisiting(0); // 初始化正在访问的页号为0

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
