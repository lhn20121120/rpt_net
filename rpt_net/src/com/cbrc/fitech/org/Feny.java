package com.cbrc.fitech.org;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.cbrc.org.form.MOrgForm;
public class Feny extends Action {

	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		   HttpSession session=request.getSession();
		int totalRows; // 记录总行数
	  
	    List suolist=new ArrayList();
	    List suosuolist=new ArrayList();
	    suolist=(List)session.getAttribute("subList");
		totalRows = suolist.size();
		Pager pager = PagerHelper.getPager(request, totalRows);
	  
		int uplimit = ((pager.getStartRow() + pager.getPageSize()) >suolist.size()) ? suolist.size() : pager.getStartRow() + pager.getPageSize();		
		for (int i = pager.getStartRow(); i < uplimit; i++) {
			suosuolist.add((MOrgForm)suolist.get(i));
		}
	    
		if(suolist !=null && suolist.size()!=0)
			request.setAttribute("Records", suosuolist);
		    request.setAttribute("PAGER", pager);	    
		 return mapping.findForward("show");
	    
	}
}
