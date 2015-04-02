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
import com.cbrc.org.hibernate.MFinaOrg;

public class ConductShowMFinaOrgAction extends Action {

	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		HttpSession session = request.getSession();

		String[] relations = request.getParameterValues("cb");
		
		
	//	this.saveRelations(session, relations);

		String name = request.getParameter("tijiao");

		if (name.equals("首  页"))

			this.conductFirstPage(session);

		if (name.equals("前一页"))

			this.conductLastPage(session);

		if (name.equals("后一页"))

			this.conductNextPage(session);

		if (name.equals("尾  页"))

			this.conductEndPage(session);
		
		if(name.equals("确认设置")){
			
			boolean  flog = this.saveRelations(session,relations);
			
			if(flog)
				
				session.setAttribute("isSetSucess","y");
			
			
			
			else
				
				session.setAttribute("isSetSucess","n");
			
		  return	mapping.findForward("zhuanfaJsp");
			
		}

		return mapping.findForward("showMFinaOrgsJsp");
	}

	/**
	 * 将页面提交上来的多选框入库,但不更新里面的金融机构列表
	 * 
	 * @param session
	 * @param relations
	 * @throws Exception
	 */
	public boolean saveRelations(HttpSession session, String[] relations)
			throws Exception {
		
		boolean isSetSucess ;

		DeputationForm df = (DeputationForm) session.getAttribute("df");

		String orgId = df.getMToRepOrg().getOrgId();

		List mfos = df.getMFinaOrgs();
		
		if(relations==null){
			
			this.setAllNull(mfos);
			
		    isSetSucess = StrutsMFinaOrgDelegate.setRelationToMToRepOrg(df);
			
			return isSetSucess;
		}

		MFinaOrg mfo = null;

		int size = mfos.size();

		int length = relations.length;

		int j = 0;

		int m;

		for (int i = 0; i < size; i++) {

			mfo = (MFinaOrg) mfos.get(i);

			if (j < length) {

				m = Integer.parseInt(relations[j]);

				if (m == i) {

					mfo.setOrgId(orgId);

					j++;
				} else {

					mfo.setOrgId("xxx");
				}
			} else {

				mfo.setOrgId("xxx");
			}

		}

	   isSetSucess = StrutsMFinaOrgDelegate.setRelationToMToRepOrg(df);
	   
	   return  isSetSucess;

	}

	public void setAllNull(List mfos) throws Exception {

		int size = mfos.size();

		MFinaOrg mf = null;

		for (int i = 0; i < size; i++) {

			mf = (MFinaOrg) mfos.get(i);

			mf.setOrgId("xxx");
		}
	}

	public void conductNextPage(HttpSession session) throws Exception {

		PageStateForm psfOnMF = (PageStateForm) session.getAttribute("psf");

		DeputationForm df = (DeputationForm) session.getAttribute("df");

		int pageVisiting = psfOnMF.getPageVisiting(); // 正在访问的页面号

		int recordCountByPage = psfOnMF.getRecordCountByPage();  

		int begin = recordCountByPage * (pageVisiting + 1);

		int end = recordCountByPage;

		StrutsMFinaOrgDelegate.setMFinaOrg(df, begin, end);  //通过计算出的起始记录来从数据库中拿出一页

		pageVisiting++; // 从这里开始处理页面状态

		psfOnMF.setPageVisiting(pageVisiting);

		psfOnMF.setFirstPage(false);

		if (pageVisiting == (psfOnMF.getPageCount()-1))

			psfOnMF.setEndPage(true);

		else

			psfOnMF.setEndPage(false);

		psfOnMF.setExistsListPage(true);

		if (pageVisiting == (psfOnMF.getPageCount()-1))

			psfOnMF.setExistsNextPage(false);

		else

			psfOnMF.setExistsNextPage(true);

		session.setAttribute("psf", psfOnMF);
	}

	public void conductLastPage(HttpSession session) throws Exception {

		PageStateForm psfOnMF = (PageStateForm) session.getAttribute("psfOnMF");

		DeputationForm df = (DeputationForm) session.getAttribute("df");

		int pageVisiting = psfOnMF.getPageVisiting(); // 正在访问的页面号

		int recordCountByPage = psfOnMF.getRecordCountByPage();

		int begin = recordCountByPage * (pageVisiting - 1);

		int end = recordCountByPage;

		StrutsMFinaOrgDelegate.setMFinaOrg(df, begin, end);

		session.setAttribute("df", df);

		pageVisiting--; // 从这里开始处理页面状态

		psfOnMF.setPageVisiting(pageVisiting);

		if (pageVisiting == 0)

			psfOnMF.setFirstPage(true);

		else

			psfOnMF.setFirstPage(false);

		psfOnMF.setEndPage(false);

		if (pageVisiting == 0)

			psfOnMF.setExistsListPage(false);

		else

			psfOnMF.setExistsListPage(true);

		psfOnMF.setExistsNextPage(true);

		session.setAttribute("psf", psfOnMF);
	}

	public void conductFirstPage(HttpSession session) throws Exception {

		PageStateForm psfOnMF = (PageStateForm) session.getAttribute("psfOnMF");

		DeputationForm df = (DeputationForm) session.getAttribute("df");

		com.cbrc.org.adapter.StrutsMFinaOrgDelegate.setMFinaOrg(df, 0, psfOnMF
				.getRecordCountByPage());

		psfOnMF.setPageVisiting(0);

		psfOnMF.setFirstPage(true);

		if (psfOnMF.getPageCount() == 1)

			psfOnMF.setEndPage(true);

		else

			psfOnMF.setEndPage(false);

		psfOnMF.setExistsListPage(false);

		if (psfOnMF.getPageCount() > 1)

			psfOnMF.setExistsNextPage(true);

		else

			psfOnMF.setExistsNextPage(false);

		session.setAttribute("psf", psfOnMF);

	}

	public void conductEndPage(HttpSession session) throws Exception {

		PageStateForm psfOnMF = (PageStateForm) session.getAttribute("psfOnMF");

		DeputationForm df = (DeputationForm) session.getAttribute("df");

		int begin = psfOnMF.getRecordCountByPage()
				* (psfOnMF.getPageCount() - 1);

		int end = psfOnMF.getRecordCountByPage();

		com.cbrc.org.adapter.StrutsMFinaOrgDelegate.setMFinaOrg(df, begin, end);

		psfOnMF.setPageVisiting(psfOnMF.getPageCount() - 1);

		if (psfOnMF.getPageCount()==1)

			psfOnMF.setFirstPage(true);

		else

			psfOnMF.setFirstPage(false);

		psfOnMF.setEndPage(true);

		if (psfOnMF.getPageCount() > 1)

			psfOnMF.setExistsListPage(true);

		else

			psfOnMF.setExistsListPage(false);

		psfOnMF.setExistsNextPage(false);

		session.setAttribute("psf", psfOnMF);
	}

}
