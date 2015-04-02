package com.cbrc.smis.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.cbrc.org.form.AFDataTraceForm;
import com.cbrc.smis.service.IAFDataTraceService;
import com.cbrc.smis.service.impl.AFDataTraceServiceImpl;

public class FindOriAFDataTraceAction extends Action {

	@Override
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		AFDataTraceForm traceForm = (AFDataTraceForm)form;
		String repInId = String.valueOf(traceForm.getRepInId());
		String cellName = traceForm.getCellName();//单元格名称
		
		String oriData = "";
		IAFDataTraceService traceService = new AFDataTraceServiceImpl();
		oriData = traceService.findOriDataByTemplateIDAndVersionId(repInId, cellName);
		request.setAttribute("oriData", oriData);
		
		return mapping.findForward("success");
	}
	
}
