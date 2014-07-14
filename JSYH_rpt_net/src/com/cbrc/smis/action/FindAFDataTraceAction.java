package com.cbrc.smis.action;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.cbrc.org.form.AFDataTraceForm;
import com.cbrc.smis.entity.AFDataTrace;
import com.cbrc.smis.service.IAFDataTraceService;
import com.cbrc.smis.service.impl.AFDataTraceServiceImpl;

public class FindAFDataTraceAction extends Action{

	@Override
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		AFDataTraceForm traceForm = (AFDataTraceForm)form;
		String repInId = traceForm.getRepInId();
		String cellName = traceForm.getCellName();//单元格名称
		
		List<AFDataTraceForm> traceList = null;
		
		if(repInId==null || repInId.equals("")
				|| cellName ==null || cellName.equals(""))
			return mapping.findForward("index");
		
		IAFDataTraceService traceService = new AFDataTraceServiceImpl();
		try {
			traceList = traceService.findListByTemplateIDandVersionId(repInId, cellName);
			traceForm.setDataList(traceList);
		} catch (Exception e) {
			e.printStackTrace();
			return mapping.findForward("index");
		}
		request.setAttribute("traceList", traceList);
		return mapping.findForward("index");
	}
	
}
