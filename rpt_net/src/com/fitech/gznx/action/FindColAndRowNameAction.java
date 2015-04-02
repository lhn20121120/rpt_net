package com.fitech.gznx.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.fitech.gznx.service.StrutsAFCellDelegate;

public class FindColAndRowNameAction extends Action{

	@Override
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		//模板ID
		String templateId = request.getParameter("templateId");
		//版本ID
		String versionId = request.getParameter("versionId");
		//单元格名称
		String cellName = request.getParameter("cellName");
		
		/**根据模板ID，版本ID和单元格名称查询出横向和*/
		String colNameAndRowName = StrutsAFCellDelegate.getColNameAndRowNamebyId(templateId, versionId, cellName);
		
		request.setAttribute("colNameAndRowName", colNameAndRowName);
		
		return mapping.findForward("index");
	}
	
}
