package com.fitech.export.servlet;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

import com.cbrc.smis.common.Config;
import com.cbrc.smis.common.DownLoadDataToZip;
import com.cbrc.smis.security.Operator;
import com.cbrc.smis.util.FitechUtil;
import com.fitech.export.service.ExportQDReportDelegate;
import com.fitech.gznx.common.FileUtil;
import com.fitech.gznx.form.AFReportForm;
import com.fitech.gznx.po.AfTemplate;
import com.fitech.gznx.service.AFReportDelegate;
import com.fitech.gznx.service.AFTemplateDelegate;
import com.runqian.report4.model.ReportDefine;
import com.runqian.report4.usermodel.Context;
import com.runqian.report4.usermodel.Engine;
import com.runqian.report4.usermodel.IReport;
import com.runqian.report4.util.ReportUtils;
import com.runqian.report4.view.excel.ExcelReport;

public class DownloadExcelQDServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	/**
	 * ServletContext
	 */
	private ServletContext context = null;

	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		context = config.getServletContext();
	}

	/***
	 * 有jdbc技术 需要修改 卞以刚 2011-12-22 此处修改oracle to_char函数为sqlserver用法 待测试
	 * 影响表：VIEW_AF_REPORT af_report VIEW_ORG_REP 影响对象：AfTemplate AfReport
	 * MActuRep 卞以刚 2011-12-27
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response) {
		try {
			Operator operator = null;
			HttpSession session = request.getSession();
			if (session.getAttribute(Config.OPERATOR_SESSION_ATTRIBUTE_NAME) != null)
				operator = (Operator) session
						.getAttribute(Config.OPERATOR_SESSION_ATTRIBUTE_NAME);
			/** 报表选中标志 **/
			String reportFlg = "3";
			String srcFileName = Config.TEMP_DIR + File.separator
					+ operator.getOrgId();
			String zipFileName = "REPORTS.zip";

			DownLoadDataToZip dldtZip = DownLoadDataToZip.newInstance();
			if (dldtZip.isTemplateZipExist(srcFileName)) {
				dldtZip.deleteFolder(new File(srcFileName));
				File zipFile = new File(srcFileName + ".zip");
				if (zipFile.exists())
					zipFile.delete();
			}

			File outfile = new File(srcFileName);

			if (outfile.exists())
				dldtZip.deleteFolder(outfile);
			outfile.mkdir();

			HashMap map = new HashMap();

			String repInIdString = request.getParameter("repInIds") != null ? request
					.getParameter("repInIds") : "";
			String dataragId = request.getParameter("dataRagId") != null ? request
					.getParameter("dataRagId") : "";
			// 全部导出
			if (repInIdString == null || repInIdString.equals("")) {
			} else {
				// 批量导出
				String[] repInIds = repInIdString
						.split(Config.SPLIT_SYMBOL_COMMA);

				for (int i = 0; i < repInIds.length; i++) {
					String repInId = repInIds[i];
					AFReportForm reportInForm = AFReportDelegate
							.getReportIn(repInId);
					String orgId = reportInForm.getOrgId();
					// String orgId = repInIds[i].split(":")[1];
					if (repInId.equals("0")) {
					} else {

						String orgReportPath = srcFileName + File.separator
								+ orgId;

						if (!map.containsKey(orgId)) {
							map.put(orgId, orgReportPath);
							File orgReportFileFolder = new File(orgReportPath);
							orgReportFileFolder.mkdir();
						} else
							orgReportPath = (String) map.get(orgId);
						String templateId = "";
						String versionId = "";
						String dataRangeId = dataragId;
						String curId = "";

						/**
						 * 已使用hibernate 卞以刚 2011-12-22 影响对象：AfReport
						 **/
						templateId = reportInForm.getTemplateId();
						versionId = reportInForm.getVersionId();
						curId = String.valueOf(reportInForm.getCurId());
						/**
						 * 已使用hibernate 卞以刚 2011-12-21 影响对象：AfTemplate
						 **/
						AfTemplate aftemplate = AFTemplateDelegate.getTemplate(
								templateId, versionId);
						if (aftemplate.getReportStyle() != null
								&& com.fitech.gznx.common.Config.REPORT_QD
										.equals(String.valueOf(aftemplate
												.getReportStyle()))) {
							try {
								String excelFilePath = Config.RAQ_TEMPLATE_PATH
										+ "templatefile"
										+ File.separator
										+ templateId.replace("HB", "")
										+ "-"
										+ versionId.substring(0,versionId.length() - 1) + "-"
										+ dataRangeId+ ".xls";
								String outFilePath = orgReportPath
										+ File.separator + templateId + "-"
										+ versionId + "-" + dataRangeId + ".xls";
								File excelFile = new File(excelFilePath);
								if (!excelFile.exists()) {
									System.out.println(excelFile+"标准 excel模版不存在！");
									String raqFileName = Config.RAQ_TEMPLATE_PATH
											+ "templateFiles" + File.separator
											+ "printRaq" + File.separator + "qdfile"
											+ File.separator + templateId + "_"
											+ versionId + ".raq";
									ReportDefine rd = (ReportDefine) ReportUtils
											.read(raqFileName);
									Context cxt = new Context(); // 构建报表引擎计算环境
									cxt.setParamValue("RepID", repInId);
									Engine engine = new Engine(rd, cxt); // 构造报表引擎
									IReport iReport = engine.calc(); // 运算报表
									// ReportUtils.exportToExcel(excelFileName,
									// iReport, true);
									ExcelReport eReport = new ExcelReport();
									eReport.export(iReport);
									eReport.saveTo(outFilePath);
								} else {//获取报表数据
									List<Object[]> cellList = ExportQDReportDelegate
											.getCellList(reportInForm
													.getTemplateId(),
													reportInForm.getRepId());
									Long startRow = ExportQDReportDelegate
											.getStartRow(templateId, versionId);
									createExcel(cellList, excelFilePath,
											outFilePath, startRow);
								}
							} catch (Throwable e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							// }
						} else {
						}
					}
				}
			}

			boolean bool = false;
			dldtZip.gzip(srcFileName, bool);

			response.reset();
			response.setContentType("application/x-zip-compressed;name=\""
					+ zipFileName + "\"");
			response.addHeader("Content-Disposition", "attachment; filename=\""
					+ FitechUtil.toUtf8String(zipFileName) + "\"");
			response.setHeader("Accept-ranges", "bytes");

			FileInputStream inStream = new FileInputStream(srcFileName + ".zip");

			int len;
			byte[] buffer = new byte[100];

			while ((len = inStream.read(buffer)) > 0) {
				response.getOutputStream().write(buffer, 0, len);
			}
			inStream.close();

			// 删除临时文件
			FileUtil.deleteFile(outfile);
			FileUtil.deleteFile(srcFileName + ".zip");
		} catch (Exception e) {
			e.printStackTrace();
			ErrorOutPut(response);
		}

	}
/**
 * 将数据写入标准Excel
 * @param cellList
 * @param excelFilePath
 * @param outFilePath
 * @param startRow
 */
	private void createExcel(List<Object[]> cellList, String excelFilePath,
			String outFilePath, Long startRow) {
		FileInputStream in = null;
		HSSFWorkbook workbook = null;

		try {
			in = new FileInputStream(excelFilePath);// 将excel文件转为输入流
			POIFSFileSystem fs = new POIFSFileSystem(in);// 构建POIFSFileSystem类对象，用输入流构建
			workbook = new HSSFWorkbook(fs);// 创建个workbook，根据POIFSFileSystem对象
			if (cellList != null && cellList.size() > 0) {
				Object[] objects = null;
				HSSFSheet sheet = workbook.getSheetAt(0);// 取得第一张sheet
				for (int i = 1; i < cellList.size(); i++) {
					objects = (Object[]) cellList.get(i);// 找到一行数据
					HSSFRow row = sheet.getRow(startRow.intValue() + i - 1);// 创建第二行
					if (Integer.parseInt(objects[1].toString()) >= startRow) {
						for (int j = 2; j < objects.length - 2; j++) {
							HSSFCell cell = row.getCell((short) (j - 2));
//							if (cell == null) {
//								System.out.println(j + ":::" + i);
//							}
//							System.out.println(objects[j]);
							cell.setCellValue(objects[j].toString());
						}
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				in.close();
			} catch (IOException e) {
				System.out.println(e.toString());
			}
		}

		FileOutputStream out = null;
		try {
			out = new FileOutputStream(outFilePath);
			workbook.write(out);
		} catch (IOException e) {
			System.out.println(e.toString());
		} finally {
			try {
				out.close();
			} catch (IOException e) {
				System.out.println(e.toString());
			}
		}

	}

	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		this.doGet(request, response);
	}

	/**
	 * 错误输出
	 * 
	 * @param response
	 */
	private void ErrorOutPut(HttpServletResponse response) {
		response.reset();
		response.setContentType("text/html;charset=GB2312");
		PrintWriter out = null;
		try {
			out = response.getWriter();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		out.println("<font color=\"blue\">没有需要导出的数据文件!</font>");
		out.close();
	}
}
