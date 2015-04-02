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

public final class ConductShowMToRepOrgsAction extends Action {

	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		HttpSession session = request.getSession();

		String is = request.getParameter("is");

		if (is.equals("n"))

			this.conductNextPage(session);  

		if (is.equals("l"))

			this.conductLastPage(session);
		
		if(is.equals("f"))
			
			this.conductFirstPage(session);
		
		if(is.equals("e"))
			
			this.conductEndPage(session);

		return mapping.findForward("showMToRepOrgsJsp");
	}
    /**
     * @author cb
     * 用于处理显示下一页的代报机构
     * @param session
     * @throws Exception
     */
	public void conductNextPage(HttpSession session) throws Exception {

		PageStateForm psf = (PageStateForm) session.getAttribute("psf");

		List dfs = (List) session.getAttribute("dfs");

		dfs = null;

		int pageVisiting = psf.getPageVisiting(); // 正在访问的页面号

		int recordCountByPage = psf.getRecordCountByPage();

		int begin = recordCountByPage * (pageVisiting + 1);

		int end = recordCountByPage;

		dfs = com.cbrc.org.adapter.StrutsMToRepOrgDelegate.getDeputationForms(
				begin, end);

		session.setAttribute("dfs", dfs);

		pageVisiting++; // 从这里开始处理页面状态

		psf.setPageVisiting(pageVisiting);

		psf.setFirstPage(false);

		if (pageVisiting == (psf.getPageCount()-1))

			psf.setEndPage(true);

		else

			psf.setEndPage(false);

		psf.setExistsListPage(true);

		if (pageVisiting == (psf.getPageCount()-1))

			psf.setExistsNextPage(false);

		else

			psf.setExistsNextPage(true);

		session.setAttribute("psf", psf);
	}

	/**
	 * @author
	 * 用于处理显示前一页的代报机构
	 * @param session
	 * @throws Exception
	 */
	public void conductLastPage(HttpSession session) throws Exception {

		PageStateForm psf = (PageStateForm) session.getAttribute("psf");

		List dfs = (List) session.getAttribute("dfs");

		dfs = null;

		int pageVisiting = psf.getPageVisiting(); // 正在访问的页面号

		int recordCountByPage = psf.getRecordCountByPage();

		int begin = recordCountByPage * (pageVisiting - 1);

		int end = recordCountByPage;

		dfs = com.cbrc.org.adapter.StrutsMToRepOrgDelegate.getDeputationForms(
				begin, end);

		session.setAttribute("dfs", dfs);

		pageVisiting--; // 从这里开始处理页面状态

		psf.setPageVisiting(pageVisiting);

		if (pageVisiting == 0)

			psf.setFirstPage(true);

		else

			psf.setFirstPage(false);

		psf.setEndPage(false);

		if (pageVisiting == 0)

			psf.setExistsListPage(false);

		else

			psf.setExistsListPage(true);

		psf.setExistsNextPage(true);

		session.setAttribute("psf", psf);
	}
    /**
     * @author cb
     * 用于处理显示第一页的代报机构
     * @param session
     * @throws Exception
     */
	public void conductFirstPage(HttpSession session) throws Exception {

		PageStateForm psf = (PageStateForm) session.getAttribute("psf");

		List dfs = (List) session.getAttribute("dfs");

		dfs = null;

		dfs = com.cbrc.org.adapter.StrutsMToRepOrgDelegate.getDeputationForms(
				0, psf.getRecordCountByPage());

		session.setAttribute("dfs", dfs);

		psf.setPageVisiting(0);

		psf.setFirstPage(true);

		if (psf.getPageCount() == 1)

			psf.setEndPage(true);

		else

			psf.setEndPage(false);

		psf.setExistsListPage(false);

		if (psf.getPageCount() > 1)

			psf.setExistsNextPage(true);

		else

			psf.setExistsNextPage(false);

		session.setAttribute("psf", psf);

	}
	/**
	 * @author cb
	 * 用于处理显示最后一页的代报机构
	 * @param session
	 * @throws Exception
	 */
	public void conductEndPage(HttpSession session)throws Exception{
		
		PageStateForm psf = (PageStateForm) session.getAttribute("psf");

		List dfs = (List) session.getAttribute("dfs");

		dfs = null;

		int begin = psf.getRecordCountByPage() * (psf.getPageCount() - 1);

		int end = psf.getRecordCountByPage();

		dfs = com.cbrc.org.adapter.StrutsMToRepOrgDelegate.getDeputationForms(
				begin, end);

		session.setAttribute("dfs", dfs);

		psf.setPageVisiting(psf.getPageCount() -1);

		if (psf.getPageCount()==1)

			psf.setFirstPage(true);

		else

			psf.setFirstPage(false);

		psf.setEndPage(true);

		if (psf.getPageCount()>1)

			psf.setExistsListPage(true);

		else

			psf.setExistsListPage(false);

		psf.setExistsNextPage(false);

		session.setAttribute("psf", psf);
	}
}
