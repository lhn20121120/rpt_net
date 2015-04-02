package com.cbrc.smis.action;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.upload.FormFile;
import org.apache.struts.util.RequestUtils;

import com.cbrc.smis.adapter.StrutsReportInDelegate;
import com.cbrc.smis.form.ReportInForm;
import com.cbrc.smis.util.FitechException;
import com.cbrc.smis.util.ReportExcelHandler;
import com.fitech.net.config.Config;

/**
 * 在线报表保存校验
 * 
 * @author Yao
 * 
 */
public class SaveOnLineReportAction extends Action
{
	private FitechException log = new FitechException(SaveOnLineReportAction.class);

	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException
	{
		boolean result = false;
		Integer repInId = null;
		String excelFileName = null;
		String excelURL = null;
		String excelPath = null;
		try
		{
			ReportInForm reportInForm = (ReportInForm) form;
			RequestUtils.populate(reportInForm, request);
            
			
			String fileName = request.getParameter("fileName");
			
			File tempfile = new File(com.cbrc.smis.common.Config.TEMP_DIR + fileName);
			// 删除临时文件
			if (tempfile.exists())
			{
				tempfile.delete();
			}

			if (reportInForm != null && reportInForm.getRepInId() != null)
			{
				repInId = reportInForm.getRepInId();
				// 查询改报表的相应基本信息
				ReportInForm record = StrutsReportInDelegate.getReportIn(repInId);

				if (record != null)
				{
					String orgId = record.getOrgId();
					Integer year = record.getYear();
					Integer term = record.getTerm();
					String versionId = record.getVersionId();
					String childRepId = record.getChildRepId();
					Integer curId = record.getCurId();
					Integer dataRangeId = record.getDataRangeId();

					// 请求类型，校验 validate or 报送 or 修改
					FormFile formFile = reportInForm.getReportFile();

					// Excel文件保存目录
					String excelFilePath = Config.getCollectExcelFolder() + File.separator + year
							+ "_" + term + File.separator + orgId;

					File excelFile = new File(excelFilePath);
					if (!excelFile.exists())
						excelFile.mkdirs();

					excelFileName = childRepId + "_" + versionId + "_" + dataRangeId + "_" + curId + ".xls";
					excelFilePath = excelFilePath + File.separator + excelFileName;
					excelURL = com.cbrc.smis.common.Config.WEBROOTULR + Config.REPORT_NAME + "/" 
							+ Config.COLLECT_EXCEL + "/" + year + "_" + term + "/" + orgId + "/" + excelFileName;
					excelPath = Config.REPORT_NAME + "/" + Config.COLLECT_EXCEL + "/" + year + "_" + term + "/" + orgId;
				
					//拷贝文件至指定目录
					OutputStream bos = new FileOutputStream(excelFilePath);// 建立一个上传文件的输出流
					InputStream stream = formFile.getInputStream();// 把文件读入
					byte[] buffer = new byte[8192];
					int bytesRead = 0;

					while ((bytesRead = stream.read(buffer, 0, 8192)) != -1)
					{
						bos.write(buffer, 0, bytesRead);// 将文件写入服务器
					}
					bos.close();
					stream.close();
					
					/* 将Excel文件解析入库 */
					ReportExcelHandler excelHandler = new ReportExcelHandler(repInId, excelFilePath);
					result = excelHandler.copyExcelToDB(false);
					if (result)
						result = StrutsReportInDelegate.updateReportInCheckFlag(repInId,
								com.fitech.net.config.Config.CHECK_FLAG_AFTERSAVE);
				}
			}
		}
		catch (Exception ex)
		{
			log.printStackTrace(ex);
			result = false;
		}

		PrintWriter out = response.getWriter();
		
		response.setContentType("text/plain");
		response.setHeader("Cache-control", "no-cache");
		if (result == false)
			out.println("false");
		else
			out.println(excelFileName+","+excelURL+","+excelPath);
		out.close();
		return null;
	}
}
