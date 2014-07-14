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
import com.fitech.gznx.service.StrutsTemplateOrgOuterRelationDelegate;
import com.fitech.gznx.service.StrutsTemplateOrgRelationDelegate;

public class UpdateBSSDAction extends Action {
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
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {

		Locale locale = getLocale(request);
		MessageResources resources = this.getResources(request);
		FitechMessages messages = new FitechMessages();

		UpdateBSFWForm bsfwForm = (UpdateBSFWForm) form;
		if (bsfwForm == null)
			return mapping.findForward("updateOK");

		HttpSession session = request.getSession();
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

			StrutsTemplateOrgOuterRelationDelegate
					.removeLowerOrg(
							childRepId,
							versionId,
							((Operator) session
									.getAttribute(Config.OPERATOR_SESSION_ATTRIBUTE_NAME))
									.getOrgId());
			StrutsTemplateOrgOuterRelationDelegate.updateBSSD(childRepId,
					versionId, list);
			StrutsTemplateOrgRelationDelegate.updateBSFW(childRepId, versionId);
			messages.add("���͹�ϵ�趨�ɹ�");
			String repName = request.getParameter("ReportName");
			if (bsfwForm.getChildRepId() != null
					&& bsfwForm.getVersionId() != null)
				bsfwForm.setReportName(repName);

			request.setAttribute("ObjForm", bsfwForm);

			path = mapping.findForward("setBSPL").getPath();

			if (childRepId != null)
				url += (url.equals("") ? "" : "&") + "childRepId=" + childRepId;
			if (versionId != null)
				url += (url.equals("") ? "" : "&") + "versionId=" + versionId;
			if (bsfwForm.getReportName() != null)
				url += (url.equals("") ? "" : "&") + "reportName="
						+ bsfwForm.getReportName();
			if (bsfwForm.getReportStyle() != null)
				url += (url.equals("") ? "" : "&") + "reportStyle="
						+ bsfwForm.getReportStyle();
		} catch (Exception ex) {
			ex.printStackTrace();
			messages.add("���͹�ϵ�趨ʧ�ܣ�");
			if (path.equals(""))
				return new ActionForward(
						"/viewAFTemplateDetail.do?bak2=2&templateId='"
								+ childRepId + "'&versionId=" + versionId);
		}
		if (messages != null && messages.getSize() > 0)
			request.setAttribute(Config.MESSAGES, messages);
		String opration = request.getParameter("opration");
		//���ģ��ʱ���ҳ����ת
		if (opration!=null&&opration.equals("next"))
			return new ActionForward(path + (url.equals("") ? "" : "?" + url));
		return new ActionForward("/viewAFTemplateDetail.do?bak2=2&templateId="
				+ childRepId + "&versionId=" + versionId);
	}
}
