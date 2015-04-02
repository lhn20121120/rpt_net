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

public class AddVOrgRelAction extends Action {

	@Override
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		request.removeAttribute("message");
		VorgRelForm relForm = (VorgRelForm)form;
		vOrgRel rel = new vOrgRel();
		BeanUtils.copyProperties(rel, relForm);
		IVorgRelService relService = new VorgRelServiceImpl();
		vOrgRel relOri = relService.findVOrgRelByOrgId(rel.getId().getOrgId());
		if(relOri!=null){
			request.setAttribute("message", "<script>alert('已存在该机构映射，添加失败!')</script>");
			return mapping.findForward("error");
		}
			
		relService.saveVorgRel(rel);
		return mapping.findForward("success");
	}
	
}
