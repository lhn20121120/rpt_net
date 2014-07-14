package com.fitech.gznx.action;

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

public class DeleteVOrgRelAction extends Action {

	@Override
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		VorgRelForm relForm = (VorgRelForm)form;
		vOrgRel rel = new vOrgRel();
		BeanUtils.copyProperties(rel, relForm);
		
		IVorgRelService relService = new VorgRelServiceImpl();
		relService.deleteVorgRel(rel);
		
		return mapping.findForward("success");
	}
	
}
