package com.fitech.net.action;

import java.io.File;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.MessageResources;
import org.apache.struts.util.RequestUtils;

import com.cbrc.smis.adapter.StrutsReportInDelegate;
import com.cbrc.smis.common.Config;
import com.cbrc.smis.excel.CreateExcel;
import com.cbrc.smis.form.ReportInForm;
import com.cbrc.smis.util.FitechMessages;
import com.cbrc.smis.util.FitechUtil;
import com.fitech.net.common.CommMethod;

/**
 * 在线修改
 * 
 * @author Yao(修改)
 * 
 */
public class CheckFailedReportAction extends Action
{
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException
	{
		boolean result = false;

		FitechMessages messages = new FitechMessages();
		MessageResources resources = getResources(request);

		ReportInForm reportForm = (ReportInForm) form;
		RequestUtils.populate(reportForm, request);

		Integer repInId = reportForm.getRepInId();
		String curPage = reportForm.getCurPage();
		try
		{
			ReportInForm record = StrutsReportInDelegate.getReportIn(repInId);
			if (record != null)
			{
				// 取得该报表相关基本信息
				String orgId = record.getOrgId();
				String childRepId = record.getChildRepId();
				String versionId = record.getVersionId();
				Integer year = record.getYear();
				Integer term = record.getTerm();
				Integer curId = record.getCurId();
				Integer dataRangeId = record.getDataRangeId();

				if (record != null)
				{
					// 将数据库中的数据写进Excel
					CreateExcel createExcel = new CreateExcel(repInId);
					createExcel.createExcel(year.toString(), term.toString());

					String excelFilePath = com.fitech.net.config.Config.getCollectExcelFolder() + File.separator + year
							+ "_" + term + File.separator + orgId.trim() + File.separator + childRepId + "_"
							+ versionId + "_" + dataRangeId + "_" + curId +"_"+orgId.trim()+ ".xls";

					/** 把文件复制到临时文件夹中* */
					String newFileName = String.valueOf(System.currentTimeMillis()) + ".xls";
					FitechUtil.copyFile(excelFilePath, Config.TEMP_DIR + newFileName);

					String requestParam = "year=" + year + "&term=" + term + "&curPage=" + curPage + "&orgId=" + orgId;

					request.setAttribute("ReportURL", Config.TEMP_DIR_WEB_PATH + newFileName);
					request.setAttribute("FileName", newFileName);
					request.setAttribute("RepInId", repInId);
					request.setAttribute("RequestParam", requestParam);
					request.setAttribute("ExcelPath", "tmp");
					CommMethod.buidPageInfo(request);
					result = true;
				}
			}
		}
		catch (Exception e)
		{
			// TODO 自动生成 catch 块
			e.printStackTrace();
			result = false;
		}

		if (result)
			return mapping.findForward("success");
		else
		{
			messages.add(resources.getMessage("系统忙，请稍后再试...！"));
			request.setAttribute(Config.MESSAGES, messages);
			return new ActionForward("viewDataReport.do");
		}
	}
}
