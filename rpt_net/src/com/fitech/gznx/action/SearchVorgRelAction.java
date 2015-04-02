package com.fitech.gznx.action;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.cbrc.org.entity.SysFlag;
import com.cbrc.smis.form.VorgRelForm;
import com.cbrc.smis.service.IVorgRelService;
import com.cbrc.smis.service.impl.VorgRelServiceImpl;
import com.fitech.gznx.po.AfOrg;
import com.fitech.gznx.po.vOrgRel;
import com.fitech.gznx.service.AFOrgDelegate;

public class SearchVorgRelAction extends Action {

	@Override
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String orgId = request.getParameter("orgId");
		String proram = request.getParameter("proram");
		if(proram!=null && !proram.equals(""))
			request.setAttribute("proram", proram);
		IVorgRelService orgRelService = new VorgRelServiceImpl();
		
		List<vOrgRel> orgRelList = null;
		List<SysFlag> flagList = null;
		vOrgRel orgRel = null;
		if(orgId!=null && !orgId.equals(""))
			orgRel = orgRelService.findVOrgRelByOrgId(orgId);
		if(orgRel==null){//������ ����ת������ҳ�棬�����µ�ӳ���ϵ
			orgRelList = orgRelService.findVorgRelAll();
		}else{//�������� ����ת���޸�ҳ�棬�޸ĸ�ӳ���ϵ
			orgRelList = orgRelService.searchPreRelList(orgRel.getId().getOrgId());
		}
		flagList = orgRelService.findAllSysFlag();
		
		request.setAttribute("flagList", flagList);
		request.setAttribute("orgId", orgId);
		request.setAttribute("vOrgRel", orgRel);
		request.setAttribute("orgRelList", orgRelList);
		return mapping.findForward("index");
	}
	
	
}
