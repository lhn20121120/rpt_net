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
import com.cbrc.smis.util.FitechTemplate;
import com.cbrc.smis.util.FitechUtil;
import com.fitech.gznx.form.ReportCheckForm;
import com.fitech.gznx.service.StrutsAFCellFormuDelegate;

 
public class AddSaveCheckAction extends Action {
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
		/** ����ѡ�б�־ **/
		String reportFlg = "0";
		if (session.getAttribute(Config.REPORT_SESSION_FLG) != null){
			reportFlg = (String) session.getAttribute(Config.REPORT_SESSION_FLG);
		}
		String orgId = "";
		if (operator != null)
		{
			orgId = operator.getOrgId();
		}
		String childRepId=request.getParameter("childRepId");
		String versionId = mCellForumForm.getVersionId();
		String _cellFormu = request.getParameter("_cellFormu");
		Integer type = Integer.valueOf(request.getParameter("_formuType"));
		String  cellFormuView =request.getParameter("cellFormuView");
		request.setAttribute("cellFormuView",cellFormuView);
		// if(request.getParameter("expression")!=null)
		// expression=(String)request.getParameter("expression");
		// System.out.println(mCellForumForm.getExpression());
		boolean flag = false;
		
		try{
		
			FitechTemplate template = new FitechTemplate();

			if (mCellForumForm.getReportStyle().compareTo(Config.REPORT_STYLE_DD) == 0)
			{ // ��Ե�ʽ
				flag = template.validateAFF(mCellForumForm, locale, resources,reportFlg,versionId,type,childRepId,_cellFormu);
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
			
			
		}catch (Exception e)
		{
			e.printStackTrace();
			messages.add(FitechResource.getMessage(locale, resources, "errors.system"));
		}

		if (messages != null && messages.getSize() > 0)
			request.setAttribute(Config.MESSAGES, messages);

		String param = mapping.getParameter();
		FitechUtil.removeAttribute(mapping, request);

		if (flag == true)
		{
		
		ReportCheckForm reportcheck = new ReportCheckForm();
		reportcheck.setTemplateId(mCellForumForm.getChildRepId());
		reportcheck.setVersionId(mCellForumForm.getVersionId());
		
		request.setAttribute("form", reportcheck);
		
		return mapping.findForward("check");
		}
		else
		{ 	ReportCheckForm reportcheck = new ReportCheckForm();
			reportcheck.setTemplateId(mCellForumForm.getChildRepId());
			reportcheck.setVersionId(mCellForumForm.getVersionId());
			request.setAttribute("form", reportcheck);
			return mapping.findForward("check");
		}
	}

	

}
