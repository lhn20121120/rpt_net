package com.cbrc.org.action;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.cbrc.org.adapter.StrutsMFinaOrgDelegate;
import com.cbrc.org.form.DeputationForm;
import com.cbrc.org.form.PageStateForm;
import com.cbrc.smis.common.ConfigOncb;

public class InitVisitMFinaOrgAction extends Action {

	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		HttpSession session = request.getSession();

		PageStateForm psfOnMF = new PageStateForm();

		List dfs = (List) session.getAttribute("dfs"); // dfs是一个DeputationForm列表

		String serialString = request.getParameter("serial");

		int serial = Integer.parseInt(serialString);

		DeputationForm df = (DeputationForm) dfs.get(serial); // 根据连接字符串转成的序号来找到dfs中的df

		StrutsMFinaOrgDelegate.setMFinaOrg(df, 0, ConfigOncb.recordCountByPage); // 将金融机构注入df中

		session.setAttribute("df", df);

		int recordCount = StrutsMFinaOrgDelegate.getMFinaOrgCount();

		int pageCount = recordCount / ConfigOncb.recordCountByPage;

		int y = recordCount % ConfigOncb.recordCountByPage;

		if (y != 0)

			pageCount++;

		psfOnMF.setRecordCount(recordCount);

		psfOnMF.setRecordCountByPage(ConfigOncb.recordCountByPage);

		psfOnMF.setPageCount(pageCount);

		psfOnMF.setPageVisiting(0);

		psfOnMF.setFirstPage(true);

		if (pageCount > 1)

			psfOnMF.setEndPage(false);

		else

			psfOnMF.setEndPage(true);

		psfOnMF.setExistsListPage(false);

		if (pageCount > 1)

			psfOnMF.setExistsNextPage(true);

		else

			psfOnMF.setExistsNextPage(false);

		session.setAttribute("psfOnMF", psfOnMF);

		return mapping.findForward("showMFinaOrgsJsp");
	}

}
