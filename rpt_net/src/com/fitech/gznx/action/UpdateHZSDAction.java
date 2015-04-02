package com.fitech.gznx.action;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.MessageResources;

import com.cbrc.smis.adapter.StrutsMChildReportDelegate;
import com.cbrc.smis.common.Config;
import com.cbrc.smis.form.UpdateBSFWForm;
import com.cbrc.smis.security.Operator;
import com.cbrc.smis.util.FitechMessages;
import com.fitech.gznx.po.AfTemplate;
import com.fitech.gznx.service.StrutsTemplateOrgCollRelationDelegate;
import com.fitech.gznx.service.StrutsTemplateOrgRelationDelegate;
import com.fitech.net.adapter.StrutsExcelData;

public class UpdateHZSDAction extends Action {
	/**
	 * Performs action.
	 * 
	 * @param mapping
	 *            Action mapping.
	 * @param form
	 *            Action form.
	 * @param request
	 *            HTTP request.
	 * @param response
	 *            HTTP response.
	 * @exception IOException
	 *                if an input/output error occurs
	 * @exception ServletException
	 *                if a servlet exception occurs
	 */
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {
		boolean flag = false;
		Locale locale = getLocale(request);
		MessageResources resources = this.getResources(request);
		FitechMessages messages = new FitechMessages();
		
		UpdateBSFWForm bsfwForm = (UpdateBSFWForm) form;
		if (bsfwForm == null)
			return mapping.findForward("updateOK");

		HttpSession session = request.getSession();
		
		Operator operator = (Operator) session
				.getAttribute(Config.OPERATOR_SESSION_ATTRIBUTE_NAME);
		String curOrgId = "";
		if (operator != null) {
			curOrgId = operator.getOrgId();
		}
		String orgId = request.getParameter("orgId");
		if (orgId == null||orgId.trim().equals(""))
			orgId = curOrgId;

		String orgIds = request.getParameter("orgIds");
		ArrayList list = new ArrayList();
		if (orgIds != null) {
			String[] arrOrgIds = orgIds.split(",");
			for (int i = 0; i < arrOrgIds.length; i++) {
				list.add(arrOrgIds[i]);
			}
		}
		String path = "";
		String url = "";
		String curPage = null;
		curPage = request.getParameter("curPage");
		if (StringUtils.isEmpty(curPage))
			curPage = "1";
		String childRepId = null;
		String versionId = null;
		try {
			childRepId = request.getParameter("childRepId");
			versionId = request.getParameter("versionId");
			StrutsTemplateOrgCollRelationDelegate.removeLowerOrg(childRepId, versionId, ((Operator) session
					.getAttribute(Config.OPERATOR_SESSION_ATTRIBUTE_NAME)).getOrgId());
			StrutsTemplateOrgCollRelationDelegate.updateHZSD(childRepId, versionId, list);
			StrutsTemplateOrgRelationDelegate.updateBSFW(childRepId, versionId);
			messages.add("�����趨�ɹ���");

			if (bsfwForm.getChildRepId() != null && bsfwForm.getVersionId() != null)
				bsfwForm.setReportName(StrutsMChildReportDelegate.getname(bsfwForm.getChildRepId(), versionId));
			request.setAttribute("ObjForm", bsfwForm);

		} catch (Exception ex) {
			ex.printStackTrace();
			messages.add("�����趨ʧ��!");
			if (path.equals(""))
				return new ActionForward("/viewAFTemplateDetail.do?bak2=2&templateId='" + childRepId + "'&versionId="
						+ versionId);
		}
		if (messages != null && messages.getSize() > 0)
			request.setAttribute(Config.MESSAGES, messages);
		//�ж��� �鿴�����޸ĵı�־��
		String opration = request.getParameter("opration");
		AfTemplate af = StrutsExcelData.getTemplateSimple(versionId, childRepId);
		String reportName=af!=null?af.getTemplateName():request.getParameter("reportName");
		if(null!=opration&&opration.equals("next"))
			return new ActionForward("/gznx/hzformula.do" + 
				"?childRepId=" + childRepId + 
				"&versionId=" + versionId + 				
				"&reportName="+reportName+
				"&templateId="+childRepId+
				"&orgId="+orgId+
				"&opration=next");
//			return new ActionForward("/gznx/preSetBSSD.do" + 
//				"?childRepId=" + childRepId + 
//				"&versionId=" + versionId + 
//				"&reportName="+reportName+
//				"&orgId="+orgId+
//				"&opration=next");
		else
			return new ActionForward("/viewAFTemplateDetail.do?bak2=2&templateId=" + childRepId + "&versionId=" + versionId);
	}
}
