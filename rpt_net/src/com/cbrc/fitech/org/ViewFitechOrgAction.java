package com.cbrc.fitech.org;

/**
 * @author jhb
 */
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.RequestUtils;

import com.cbrc.org.form.MOrgForm;

public class ViewFitechOrgAction extends Action {
	public ActionForward execute(ActionMapping mapping, ActionForm actionForm,
			HttpServletRequest request, HttpServletResponse httpServletresponse)
			throws Exception {

		// ȡ��request��Χ�ڵ�����������������fitechorgform��
    	MOrgForm fitechorgForm = (MOrgForm) actionForm;
		RequestUtils.populate(fitechorgForm, request);
		//// System.out.println("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
		MOrgForm morgform = new MOrgForm();
		List Orglist = null;// ������е�org��list
		List ViewOrglist = new ArrayList();// ��ʾ��ҳ���list
		List showlist=new ArrayList();
		//SMMISMessages messages = new SMMISMessages();// �������ʾ��Ϣ
		HttpSession session=request.getSession();
		StrutsMOrgDelegate strutsMOrgDelegate = new StrutsMOrgDelegate();
		StrutsMOrgClDelegate StrutsMOrgClDelegate = new StrutsMOrgClDelegate();
		Orglist = strutsMOrgDelegate.getOrgs();
	   // // System.out.println("Orglist.size is"+Orglist.size());
		MOrgForm org = null;// ��ʱ��orgfrom���ڵõ������ķ�������
		if (Orglist != null && Orglist.size() > 0) {
			for (int i = 0; i < Orglist.size(); i++) {
				String orgclsname = null;
				org = new MOrgForm();
				org = (MOrgForm) Orglist.get(i);
				orgclsname = StrutsMOrgClDelegate.getOrgClsName(org.getOrgClsId());
				org.setOrgClsName(orgclsname);
				/*// System.out.println(org.getOrgName());
				// System.out.println(org.getOrgClsId());
				// System.out.println(org.getOrgClsName());
				// System.out.println(org.getOrgType());*/
				ViewOrglist.add(org);
			}
		}

		else {
		//	messages.add(0, "û����������!");
			//request.setAttribute("Messages", messages);
			return mapping.findForward("view");
		}
		int totalRows; // ��¼������
		session.setAttribute("subList", ViewOrglist);
		totalRows = ViewOrglist.size();
		Pager pager = PagerHelper.getPager(request, totalRows);
		int uplimit = ((pager.getStartRow() + pager.getPageSize()) >ViewOrglist.size()) ? ViewOrglist.size() : pager.getStartRow() + pager.getPageSize();
	
		for (int i = pager.getStartRow(); i < uplimit; i++) {
			showlist.add((MOrgForm)ViewOrglist.get(i));
		//	// System.out.println(((MOrgForm)showlist.get(i)).getOrgClsName());
		}
		//// System.out.println(showlist.size());
		
		request.setAttribute("Records", showlist);
		request.setAttribute("PAGER", pager);
		return mapping.findForward("view");
	}
}
