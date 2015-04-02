package com.fitech.gznx.action;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.MessageResources;
import org.apache.struts.util.RequestUtils;

import com.cbrc.smis.common.Config;
import com.cbrc.smis.form.MCellFormuForm;
import com.cbrc.smis.util.FitechMessages;
import com.cbrc.smis.util.FitechResource;

import com.cbrc.smis.util.FitechUtil;
import com.fitech.gznx.service.StrutsAFCellFormuDelegate;
import com.fitech.gznx.util.FitechTemplate;


public class SaveBJGXAction extends Action {
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
			HttpServletResponse response) throws IOException, ServletException
	{

		Locale locale = getLocale(request);
		MessageResources resources = getResources(request);

		FitechMessages messages = new FitechMessages();

		MCellFormuForm mCellForumForm = (MCellFormuForm) form;
		RequestUtils.populate(mCellForumForm, request);

		HttpSession session = request.getSession();
		com.cbrc.smis.security.Operator operator = (com.cbrc.smis.security.Operator) session
				.getAttribute(Config.OPERATOR_SESSION_ATTRIBUTE_NAME);
		String orgId = "";
		if (operator != null)
		{
			orgId = operator.getOrgId();
		}

		/** ����ѡ�б�־ **/
		String reportFlg = "0";
		
		if (session.getAttribute(Config.REPORT_SESSION_FLG) != null){
			reportFlg = (String) session.getAttribute(Config.REPORT_SESSION_FLG);
			mCellForumForm.setReportFlg(reportFlg);
		}
		String expression = mCellForumForm.getExpression();
		// if(request.getParameter("expression")!=null)
		// expression=(String)request.getParameter("expression");
		// System.out.println(mCellForumForm.getExpression());
		boolean flag = false;

		try
		{
			FitechTemplate template = new FitechTemplate();

			if (mCellForumForm.getReportStyle().compareTo(Config.REPORT_STYLE_DD) == 0)
			{ // ��Ե�ʽ
				flag = template.validateAF(mCellForumForm, expression, locale, resources);
			}
			else if (mCellForumForm.getReportStyle().compareTo(Config.REPORT_STYLE_QD) == 0)
			{ // �嵥ʽ
				flag = template.validateEsp(mCellForumForm, expression, locale, resources);
			}
			
			if (flag == true)
			{ // ���ʽУ��

				List cells = template.getMCellToFormuList();
				if (cells != null && cells.size() > 0)
				{
					flag = StrutsAFCellFormuDelegate.savePatch(cells);
					// System.out.println("***��⣡��"+flag);
				}

				if (flag == true)
				{ // ���ڱ���趨�ɹ�

					messages.add(FitechResource.getMessage(locale, resources, "bjgx.set.success"));
					/** �����ڱ���ϵ���ʽͬ��������* */
					// synchrocnizedGather(messages,locale,resources,mCellForumForm);
				}
				else
				{ // ���ڱ���趨ʧ��
					messages.add(FitechResource.getMessage(locale, resources, "bjgx.set.failed"));
				}
			}
			else
			{ // ��ȡ��ϵ���ʽУ�鲻ͨ����ԭ��
				messages.add(template.getError());
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
			messages.add(FitechResource.getMessage(locale, resources, "errors.system"));
			return new ActionForward("/viewAFTemplateDetail.do?templateId="+mCellForumForm.getChildRepId()+"&versionId="+mCellForumForm.getVersionId()+"&bak2=2");
		}

		if (messages != null && messages.getSize() > 0)
			request.setAttribute(Config.MESSAGES, messages);

		//String param = mapping.getParameter();
		FitechUtil.removeAttribute(mapping, request);
		String param = request.getParameter("flag");
		if (flag == true)
		{ // �����ɹ������뱨�ͷ�Χ�趨ҳ��
			String url = "";

			if (param.toUpperCase().equals("ADD") == true)
			{ // ����������ɣ���ת����һ����
				String path = mapping.findForward("jiaoYanAction").getPath();
				if (mCellForumForm.getChildRepId() != null)
				{
					url += (url.equals("") ? "" : "&") + "childRepId=" + mCellForumForm.getChildRepId();
					request.setAttribute("childRepId", mCellForumForm.getChildRepId());
				}
				if (mCellForumForm.getVersionId() != null)
				{
					url += (url.equals("") ? "" : "&") + "versionId=" + mCellForumForm.getVersionId();
					request.setAttribute("versionId", mCellForumForm.getVersionId());
				}
				// // System.out.println("[SaveBJGXAction]ReportName:" +
				// mCellForumForm.getReportName());
				if (mCellForumForm.getReportName() != null)
				{
					url += (url.equals("") ? "" : "&") + "reportName=" + mCellForumForm.getReportName();
					request.setAttribute("ReportName", mCellForumForm.getReportName());
				}
				if (mCellForumForm.getReportStyle() != null)
				{
					url += (url.equals("") ? "" : "&") + "reportStyle=" + mCellForumForm.getReportStyle();
					request.setAttribute("ReportStyle", mCellForumForm.getReportStyle());
				}
				if (orgId != null)
				{
					url += (url.equals("") ? "" : "&") + "orgId=" + orgId;
					request.setAttribute("orgId", orgId);
				}
				url +="&type=edit&next=yes"; 
				
				// System.out.println("savebjgx" + path + "?" + url);
				return new ActionForward(path + "?" + url);
			}
			else
			{ // ά��׷�Ӳ�����ɣ���ת����PDF���б���Ϣ
				// String path=mapping.findForward("view").getPath();
				
				return new ActionForward("/viewAFTemplateDetail.do?templateId="+mCellForumForm.getChildRepId()+"&versionId="+mCellForumForm.getVersionId()+"&bak2=2");
			}
		}
		else
		{ // ����ʧ�ܣ�����ԭҳ��
			request.setAttribute("ObjForm", mCellForumForm);
			if (param.toUpperCase().equals("ADD") == true)
			{
				request.setAttribute("ReportName", mCellForumForm.getReportName());
				return mapping.getInputForward();
			}
			else
			{
//				return new ActionForward(mapping.getInputForward().getPath() + "?curPage="
//						+ (request.getParameter("curPage") == null ? "" : (String) request.getParameter("curPage")));
				return new ActionForward("/gznx/editBJGXInit.do?childRepId="+mCellForumForm.getChildRepId()+"&versionId="+mCellForumForm.getVersionId()+"&reportName="+mCellForumForm.getReportName());
			}
		}
	}

	
	
}
