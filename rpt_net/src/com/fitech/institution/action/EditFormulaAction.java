package com.fitech.institution.action;

import java.io.IOException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.MessageResources;

import com.cbrc.smis.common.ApartPage;
import com.cbrc.smis.common.Config;
import com.cbrc.smis.util.FitechException;
import com.cbrc.smis.util.FitechMessages;
import com.cbrc.smis.util.FitechUtil;
import com.fitech.gznx.po.AfTemplate;
import com.fitech.gznx.po.AfValidateformula;
import com.fitech.institution.adapter.AFValidateFormulaDelegate;
import com.fitech.institution.adapter.AfValidateFormulaStandardDelegate;
import com.fitech.institution.form.ViewFormulaForm;
import com.fitech.institution.po.AfValidateFormulaStandard;
import com.fitech.net.adapter.StrutsExcelData;

public class EditFormulaAction extends Action {
	private static FitechException log = new FitechException(
			EditFormulaAction.class);

	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		MessageResources resources = getResources(request);
		FitechMessages messages = new FitechMessages();

		// 取得request范围内的请求参数，并存放在logInForm内
		ViewFormulaForm viewFormulaForm = (ViewFormulaForm) form;

		int recordCount = 0; // 记录总数

		List<AfValidateformula> zidiyiList = null;
		List<AfValidateFormulaStandard> standardList = new ArrayList<AfValidateFormulaStandard>();
		ApartPage aPage = new ApartPage();

		String templateId = viewFormulaForm.getTemplateId();
		String versionId = viewFormulaForm.getVersionId();

		AfTemplate af = StrutsExcelData
				.getTemplateSimple(versionId, templateId);
		String reportName = request.getParameter("reportName");
		reportName = af != null ? af.getTemplateName() : request
				.getParameter("reportName");
		if (reportName != null) {
			request.setAttribute("reportName",
					new String(URLDecoder.decode(reportName, "UTF-8")));
		}
		try

		{
			// 显示分页后的记录
			/**
			 * 已使用hibernate 卞以刚 2011-12-27 影响对象：AfValidateformula
			 */
			zidiyiList = AFValidateFormulaDelegate.findZdyById(templateId,
					versionId);
			standardList = AfValidateFormulaStandardDelegate.findAllById(
					templateId, versionId);
		} catch (Exception e) {
			log.printStackTrace(e);
			messages.add(resources.getMessage("log.select.fail"));
		}
		// 移除request或session范围内的属性
		FitechUtil.removeAttribute(mapping, request);
		// 把ApartPage对象存放在request范围内
		aPage.setTerm(this.getTerm(viewFormulaForm, reportName));

		request.setAttribute(Config.APART_PAGE_OBJECT, aPage);

		request.setAttribute("reportName", reportName);
		request.setAttribute("templateId", templateId);
		request.setAttribute("versionId", versionId);
		if (messages.getMessages() != null && messages.getMessages().size() > 0)
			request.setAttribute(Config.MESSAGES, messages);
		if (zidiyiList != null && zidiyiList.size() != 0)
			request.setAttribute("zidingyiList", zidiyiList);
		if (standardList != null && standardList.size() != 0)
			request.setAttribute("standardList", standardList);

		return mapping.findForward("edit");
	}

	/**
	 * 取得查询条件url
	 * 
	 * @param logInForm
	 * @return
	 */
	public String getTerm(ViewFormulaForm viewFormulaForm, String reportName) {
		String term = "";

		String childRepId = viewFormulaForm.getTemplateId();
		String versionId = viewFormulaForm.getVersionId();
		if (childRepId != null && !childRepId.equals("")) {
			term += (term.indexOf("?") >= 0 ? "&" : "?");
			term += "childRepId=" + childRepId;
		}
		if (versionId != null && !versionId.equals("")) {
			term += (term.indexOf("?") >= 0 ? "&" : "?");
			term += "versionId=" + versionId;
		}
		if (reportName != null && !reportName.equals("")) {
			term += (term.indexOf("?") >= 0 ? "&" : "?");
			term += "reportName=" + reportName;
		}
		if (term.indexOf("?") >= 0)
			term = term.substring(term.indexOf("?") + 1);
		// System.out.println("term"+term);
		return term;

	}
}
