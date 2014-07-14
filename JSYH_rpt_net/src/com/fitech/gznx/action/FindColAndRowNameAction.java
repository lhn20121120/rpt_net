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
		//ģ��ID
		String templateId = request.getParameter("templateId");
		//�汾ID
		String versionId = request.getParameter("versionId");
		//��Ԫ������
		String cellName = request.getParameter("cellName");
		
		/**����ģ��ID���汾ID�͵�Ԫ�����Ʋ�ѯ�������*/
		String colNameAndRowName = StrutsAFCellDelegate.getColNameAndRowNamebyId(templateId, versionId, cellName);
		
		request.setAttribute("colNameAndRowName", colNameAndRowName);
		
		return mapping.findForward("index");
	}
	
}
