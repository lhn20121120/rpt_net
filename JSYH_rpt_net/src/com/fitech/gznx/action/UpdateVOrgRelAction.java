package com.fitech.gznx.action;
import java.net.URLDecoder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.cbrc.smis.form.VorgRelForm;
import com.cbrc.smis.service.IVorgRelService;
import com.cbrc.smis.service.impl.VorgRelServiceImpl;
import com.fitech.gznx.po.vOrgRel;

public class UpdateVOrgRelAction extends Action {

	@Override
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String orgNm = request.getParameter("orgNm");	
		String preOrgid = request.getParameter("preOrgid");
		if(preOrgid.equals("0"))
			preOrgid = null;
	
		VorgRelForm relForm = (VorgRelForm)form;
		relForm.setPreOrgid(preOrgid);
		relForm.setOrgNm(orgNm);
		vOrgRel orgRel = new vOrgRel();
		BeanUtils.copyProperties(orgRel, relForm);
		
		IVorgRelService relService = new VorgRelServiceImpl();
		relService.updateVorgRel(orgRel);
	
		return mapping.findForward("success");
	}
	
}
