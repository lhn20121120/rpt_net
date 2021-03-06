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

import com.cbrc.smis.adapter.StrutsMCellFormuDelegate;
import com.cbrc.smis.common.Config;
import com.cbrc.smis.form.MCellFormuForm;
import com.cbrc.smis.util.FitechMessages;
import com.cbrc.smis.util.FitechResource;
import com.cbrc.smis.util.FitechTemplate;
import com.cbrc.smis.util.FitechUtil;
import com.fitech.gznx.form.ReportCheckForm;
import com.fitech.gznx.po.AfTemplate;
import com.fitech.gznx.service.AFTemplateDelegate;
import com.fitech.gznx.service.StrutsAFCellFormuDelegate;


public class UpdateSaveCheckAction extends Action {
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
		ReportCheckForm reportCheckForm = (ReportCheckForm) form;
		
		RequestUtils.populate(reportCheckForm, request);
		
		HttpSession session = request.getSession();
		com.cbrc.smis.security.Operator operator = (com.cbrc.smis.security.Operator) session
				.getAttribute(Config.OPERATOR_SESSION_ATTRIBUTE_NAME);
		/** 报表选中标志 **/
		String reportFlg = "0";
		if (session.getAttribute(Config.REPORT_SESSION_FLG) != null){
			reportFlg = (String) session.getAttribute(Config.REPORT_SESSION_FLG);
		}

		MCellFormuForm mCellForumForm = new MCellFormuForm();
		mCellForumForm.setChildRepId(reportCheckForm.getTemplateId());
		mCellForumForm.setVersionId(reportCheckForm.getVersionId());
		mCellForumForm.setCellFormuId(reportCheckForm.getFormulaId().intValue());
		mCellForumForm.setCellFormu(reportCheckForm.getFormulaValue());
		mCellForumForm.setCellFormuView(reportCheckForm.getFormulaName());
		mCellForumForm.setFormuType(new Integer(reportCheckForm.getValidateTypeId()));

		String childRepId=mCellForumForm.getChildRepId();
		String versionId = mCellForumForm.getVersionId();
		
		AfTemplate aftemplate = AFTemplateDelegate.getTemplate(childRepId, versionId);
		mCellForumForm.setReportStyle(aftemplate.getReportStyle().intValue());
		
		
		
		String  cellFormuView =reportCheckForm.getFormulaName();
		request.setAttribute("cellFormuView",cellFormuView);

		boolean flag = false;
		
		try{
			FitechTemplate template = new FitechTemplate();
			if (mCellForumForm.getReportStyle().compareTo(Config.REPORT_STYLE_DD) == 0)
			{ // 点对点式
				flag = template.validateAFU(mCellForumForm, locale, resources,reportFlg);
			}
			if (flag == true)
			{ // 表达式校验

				List cells = template.getMCellToFormuList();
				if (cells != null && cells.size() > 0)
				{
					if(reportFlg.equals("1")){
						flag = StrutsAFCellFormuDelegate.saveYJHPatch(cells);
					} else {
						flag = StrutsAFCellFormuDelegate.UpdatePatch(cells);
					}
					// System.out.println("***入库！＝"+flag);
				}

				if (flag == true)
				{ // 表内表间设定成功

					messages.add(FitechResource.getMessage(locale, resources, "bjgx.set.success"));
					/** 将表内表间关系表达式同步到外网* */
					// synchrocnizedGather(messages,locale,resources,mCellForumForm);
				}
				else
				{ // 表内表间设定失败
					messages.add(FitechResource.getMessage(locale, resources, "bjgx.set.failed"));
				}
			}
			else
			{ // 获取关系表达式校验不通过的原因
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
			request.setAttribute("templateId", childRepId);
			request.setAttribute("versionId", versionId);
			if(reportFlg.equals(com.fitech.gznx.common.Config.CBRC_REPORT)){
				return new ActionForward("/template/mod/editBJGXInit.do?childRepId=" + 
						childRepId + "&versionId=" + versionId );
			} else {
				return new ActionForward("/gznx/editBJGXInit.do?childRepId=" + 
						childRepId + "&versionId=" + versionId );
			}
		}
		else
		{ 
			request.setAttribute("templateId", childRepId);
			request.setAttribute("versionId", versionId);
			request.setAttribute("reportCheckForm", reportCheckForm);
			return mapping.findForward("nocheck");
		}
	}

	

}
