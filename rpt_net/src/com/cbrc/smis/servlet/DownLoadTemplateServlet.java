package com.cbrc.smis.servlet;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.cbrc.smis.adapter.StrutsMRepRangeDelegate;
import com.cbrc.smis.adapter.StrutsReportInDelegate;
import com.cbrc.smis.common.Config;
import com.cbrc.smis.common.DownLoadDataToZip;
import com.cbrc.smis.excel.CreateExcel;
import com.cbrc.smis.form.MCurrForm;
import com.cbrc.smis.form.ReportInForm;
import com.cbrc.smis.hibernate.ReportIn;
import com.cbrc.smis.other.Aditing;
import com.cbrc.smis.security.Operator;
import com.cbrc.smis.util.FitechUtil;
import com.fitech.gznx.common.DateUtil;
import com.fitech.gznx.common.FileUtil;
import com.fitech.net.adapter.StrutsOrgNetDelegate;
import com.fitech.net.common.CommMethod;
import com.fitech.net.common.MCurrUtil;
import com.fitech.net.hibernate.OrgNet;

/**
 * 2006-11-11 下载数据文件
 */
public class DownLoadTemplateServlet extends HttpServlet {

	/**
	 * ServletContext
	 */
	private ServletContext context = null;

	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		context = config.getServletContext();

	}

	public void doGet(HttpServletRequest request, HttpServletResponse response) {
		HttpSession session = request.getSession();
		Operator operator = null;
		if (session.getAttribute(Config.OPERATOR_SESSION_ATTRIBUTE_NAME) != null)
			operator = (Operator) session
					.getAttribute(Config.OPERATOR_SESSION_ATTRIBUTE_NAME);
		String orgId = "";

		String templateOrgPath = Config.WEBROOTPATH
				+ com.fitech.net.config.Config.REPORT_NAME + File.separator
				+ com.fitech.net.config.Config.OBTAIN_RELEASE + File.separator;
		String templateSouPath = Config.RAQ_TEMPLATE_PATH
				+ com.fitech.net.config.Config.REPORT_NAME + File.separator
				+ com.fitech.net.config.Config.TEMPLATE_NAME + File.separator;
		List fileList = null;

		List currReportInFormList = MCurrUtil.newInstance()
				.getMCurrReportInForm();

		/** 考虑多币种数据文件下载* */
		if (currReportInFormList != null && currReportInFormList.size() > 0) {
			try {
				//需下载报表信息字符串
				String repNames = request.getParameter("repNames") != null ? 
						request.getParameter("repNames") : null;
				
				//下载类型
				String type = request.getParameter("type") != null ? 
						request.getParameter("type") : "";
				
				// 如果type不等于downAll 就不是下载全部
				if (!type.equals("downAll")) {
					if (repNames != null && !repNames.equals("")) {
						String[] repNameArr = repNames.split(",");
						String year = request.getParameter("year") != null ? request.getParameter("year") : "";
						String term = request.getParameter("term") != null ? request.getParameter("term") : "";
						
						if (term != null && term.length() == 2
								&& term.indexOf("0") == 0) {
							term = term.substring(1);
						}
						// orgId = request.getParameter("orgId") != null ?
						// request.getParameter("orgId") : "";

						if (repNameArr != null && repNameArr.length > 0) {
							/** 复选下载* */
							/** 先看有没有ETL数据，若没有下载空模板，若有则将ETL数据生成一份数据报表下载* */
							fileList = new ArrayList();
							
							for (int i = 0; i < repNameArr.length; i++) {

								ReportInForm currReportInForm = this.isExist(
										repNameArr[i].split("_")[0], currReportInFormList);
								String childRepId = repNameArr[i].split("_")[0];
								String versionId = repNameArr[i].split("_")[1];
								String dataRangeId = repNameArr[i].split("_")[2];
								orgId = repNameArr[i].split("_")[3];
								String actuFreqId = repNameArr[i].split("_")[4];

								boolean isExist = false;
								if (currReportInForm != null) {
									List currList = currReportInForm.getMCurr();
									if (currList != null && currList.size() > 0) {
										for (Iterator currIter = currList.iterator(); currIter.hasNext();) {
											MCurrForm mCurrForm = (MCurrForm) currIter.next();
											// 2007-11-29曹发根加

											/** 先看有没有ETL数据，若没有下载空模板，若有则将ETL数据生成一份数据报表下载* */
											File file = null;
											String excelFilePath = new StrutsMRepRangeDelegate()
													.getExcelFile(
															childRepId,
															versionId,
															orgId,
															new Integer(year),
															new Integer(term),
															new Integer(dataRangeId),
															mCurrForm.getCurId(),
															new Integer(-2));
											
											//10.02create 从生成报表功能中取已经生成的excel
											//获得已生成的报表
											file = new File(this.productReportPath(childRepId, versionId, orgId, 
													mCurrForm.getCurId().toString(), 
													Integer.valueOf(actuFreqId),
													year+"-"+ term +"-01"));
											
											if (excelFilePath != null && !excelFilePath.equals("")) {
												file = new File(excelFilePath);
											} 
											//10.02create 
											else if (file.exists()){
												//如果有则使用
											}
											else {
												file = new File(templateSouPath
														+ childRepId + "_"
														+ versionId + ".xls");
											}
											if (file != null && file.exists()) {
												fileList.add(file);
												isExist = true;
											}
										}
										if (isExist == false) {
											File file = new File(
													templateSouPath
															+ childRepId + "_"
															+ versionId
															+ ".xls");
											if (file.exists())
												fileList.add(file);
										}
									} else {
										File file = null;
										String excelFilePath = new StrutsMRepRangeDelegate()
												.getExcelFile(
														childRepId,
														versionId,
														orgId,
														new Integer(year),
														new Integer(term),
														new Integer(dataRangeId),
														new Integer(1),
														new Integer(-2));
										
										//10.02create 从生成报表功能中取已经生成的excel
										//获得已生成的报表
										file = new File(this.productReportPath(childRepId, versionId, orgId, 
												"1", 
												Integer.valueOf(actuFreqId),
												year+"-"+ term +"-01"));
										
										
										if (excelFilePath != null
												&& !excelFilePath.equals("")) {
											file = new File(excelFilePath);
										} 
										//10.02create 
										else if (file.exists()){
											//如果有则使用
										}
										else {
											file = new File(templateSouPath
													+ childRepId + "_"
													+ versionId + ".xls");
										}
										if (!file.exists())
											continue;
										fileList.add(file);
									}
								} else {
									File file = null;
									String excelFilePath = new StrutsMRepRangeDelegate()
											.getExcelFile(childRepId,
													versionId, orgId,
													new Integer(year),
													new Integer(term),
													new Integer(dataRangeId),
													new Integer(1),
													new Integer(-2));
									
									//10.02create 从生成报表功能中取已经生成的excel
									//获得已生成的报表
									file = new File(this.productReportPath(childRepId, versionId, orgId, 
											"1", 
											Integer.valueOf(actuFreqId),
											year+"-"+ term +"-01"));
									
									if (excelFilePath != null
											&& !excelFilePath.equals("")) {
										file = new File(excelFilePath);
									}
									//10.02create 
									else if (file.exists()){
										//如果有则使用
									}
									
									else {
										file = new File(templateSouPath
												+ childRepId + "_" + versionId
												+ ".xls");
									}
									if (!file.exists())
										continue;
									fileList.add(file);
								}
							}
						}
					}
				} else {
					/** 下载全部* */
					ReportInForm reportInForm = new ReportInForm();
					Calendar calendar = Calendar.getInstance();
					String year = request.getParameter("year") != null ? request
							.getParameter("year")
							: null;
					String term = request.getParameter("term") != null ? request
							.getParameter("term")
							: null;

					if (term != null && term.length() == 2
							&& term.indexOf("0") == 0) {
						term = term.substring(1);
					}
					orgId = request.getParameter("orgId") != null ? request
							.getParameter("orgId") : null;
					reportInForm.setOrgId(orgId);
					reportInForm.setSetDate(term);
					reportInForm.setRepName(repNames);
					try {
						reportInForm.setYear(new Integer(year));
					} catch (Exception ex) {
						reportInForm.setYear(new Integer(calendar
								.get(Calendar.YEAR)));
					}
					List list = null;
					list = StrutsMRepRangeDelegate.selectYBSJListNew(
							reportInForm, operator);

					if (list != null && list.size() > 0) {
						fileList = new ArrayList();
						for (int i = 0; i < list.size(); i++) {
							Aditing aditing = (Aditing) list.get(i);
							ReportInForm currReportInForm = this.isExist(
									aditing.getChildRepId(),
									currReportInFormList);

							boolean isExist = false;
							if (currReportInForm != null) {
								List currList = currReportInForm.getMCurr();
								if (currList != null && currList.size() > 0) {
									for (Iterator currIter = currList
											.iterator(); currIter.hasNext();) {
										MCurrForm mCurrForm = (MCurrForm) currIter
												.next();
										// 2007-11-29曹发根加
										String childRepId = aditing
												.getChildRepId();
										String versionId = aditing
												.getVersionId();
										Integer dataRangeId = aditing
												.getDataRgId();
										orgId = aditing.getOrgId();
										File file = null;
										String excelFilePath = new StrutsMRepRangeDelegate()
												.getExcelFile(childRepId,
														versionId, orgId,
														new Integer(year),
														new Integer(term),
														dataRangeId, mCurrForm
																.getCurId(),
														new Integer(-2));
										
										//10.02create 从生成报表功能中取已经生成的excel
										//获得已生成的报表
										file = new File(this.productReportPath(childRepId, versionId, orgId, 
												"1", 
												aditing.getActuFreqID(),
												year+"-"+ term +"-01"));
										
										if (excelFilePath != null
												&& !excelFilePath.equals("")) {
											file = new File(excelFilePath);
										}
										//10.02create 
										else if (file.exists()){
											//如果有则使用
										}
										
										else {
											file = new File(templateSouPath
													+ childRepId + "_"
													+ versionId + ".xls");
										}
										if (file != null && file.exists()) {
											fileList.add(file);
											isExist = true;
										}
									}
									if (isExist == false) {
										File file = new File(templateSouPath
												+ aditing.getChildRepId() + "_"
												+ aditing.getVersionId()
												+ ".xls");
										if (file.exists())
											fileList.add(file);
									}
								} else {
									File file = null;
									String excelFilePath = new StrutsMRepRangeDelegate()
											.getExcelFile(aditing
													.getChildRepId(), aditing
													.getVersionId(), orgId,
													new Integer(year),
													new Integer(term), aditing
															.getDataRgId(),
													new Integer(1),
													new Integer(-2));
									
									//10.02create 从生成报表功能中取已经生成的excel
									//获得已生成的报表
									file = new File(this.productReportPath(aditing.getChildRepId(),
											aditing.getVersionId(), orgId, 
											"1", 
											aditing.getActuFreqID(),
											year+"-"+ term +"-01"));
									
									if (excelFilePath != null
											&& !excelFilePath.equals("")) {
										file = new File(excelFilePath);
									} 
									//10.02create 
									else if (file.exists()){
										//如果有则使用
									}
									
									else {
										file = new File(templateSouPath
												+ aditing.getChildRepId() + "_"
												+ aditing.getVersionId()
												+ ".xls");
									}
									if (!file.exists())
										continue;
									fileList.add(file);
								}
							} else {
								File file = null;
								String excelFilePath = new StrutsMRepRangeDelegate()
										.getExcelFile(aditing.getChildRepId(),
												aditing.getVersionId(), orgId,
												new Integer(year), new Integer(
														term), aditing
														.getDataRgId(),
												new Integer(1), new Integer(-2));
								
								//10.02create 从生成报表功能中取已经生成的excel
								//获得已生成的报表
								file = new File(this.productReportPath(aditing.getChildRepId(),
										aditing.getVersionId(), orgId, 
										"1", 
										aditing.getActuFreqID(),
										year+"-"+ term +"-01"));
								
								if (excelFilePath != null
										&& !excelFilePath.equals("")) {
									file = new File(excelFilePath);
								}
								//10.02create 
								else if (file.exists()){
									//如果有则使用
								}
								else {
									file = new File(templateSouPath
											+ aditing.getChildRepId() + "_"
											+ aditing.getVersionId() + ".xls");
								}
								if (!file.exists())
									continue;
								fileList.add(file);
							}
						}
					}
				}

				if (fileList == null || fileList.size() <= 0) {
					ErrorOutPut(response);
					return;
				}

				OrgNet orgNet = StrutsOrgNetDelegate.selectOne(orgId);
				String pathName = orgNet != null && orgNet.getOrgName() != null ? orgNet
						.getOrgName()
						: "templateExcel";
				String srcFileName = Config.WEBROOTPATH + "temp"
						+ Config.FILESEPARATOR + pathName;
				String zipFileName = pathName + ".zip";

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

				String filePath = srcFileName + File.separator;

				for (int i = 0; i < fileList.size(); i++) {
					File file = (File) fileList.get(i);

					try {
						File reportDataFile = new File(filePath
								+ file.getName());
						reportDataFile.createNewFile();
						// CreateExcel ce = new CreateExcel(filePath +
						// file.getName());
						// HSSFWorkbook book = ce.createDataReport();
						int len = 0;
						InputStream inStream = new FileInputStream(file);
						byte[] buffer = new byte[inStream.available()];

						FileOutputStream outStream = null;
						while ((len = inStream.read(buffer)) > 0) {
							outStream = new FileOutputStream(reportDataFile);
							outStream.write(buffer, 0, len);
						}
						outStream.close();
						inStream.close();
						// file.delete();
					} catch (IOException ex) {
						ex.printStackTrace();
					}
				}

				dldtZip.gzip(srcFileName);

				response.reset();
				response.setContentType("application/x-zip-compressed;name=\""
						+ zipFileName + "\"");
				response.addHeader("Content-Disposition",
						"attachment; filename=\""
								+ FitechUtil.toUtf8String(zipFileName) + "\"");
				response.setHeader("Accept-ranges", "bytes");

				FileInputStream inStream = new FileInputStream(srcFileName
						+ ".zip");

				int len;
				byte[] buffer = new byte[100];

				while ((len = inStream.read(buffer)) > 0) {
					response.getOutputStream().write(buffer, 0, len);
				}
				inStream.close();

				if (outfile.exists())
					dldtZip.deleteFolder(outfile);
				File zipFile = new File(srcFileName + ".zip");
				if (zipFile.exists())
					zipFile.delete();

			} catch (Exception e) {
				e.printStackTrace();
				ErrorOutPut(response);
			}

			/** 没有多币种的情况* */
		} else {
			try {
				String repNames = request.getParameter("repNames") != null ? request
						.getParameter("repNames")
						: null;
				if (repNames != null && !repNames.equals("")) {
					String[] repNameArr = repNames.split(",");
					String year = request.getParameter("year") != null ? request
							.getParameter("year")
							: "";
					String term = request.getParameter("term") != null ? request
							.getParameter("term")
							: "";
					if (term != null && term.length() == 2
							&& term.indexOf("0") == 0) {
						term = term.substring(1);
					}
					orgId = request.getParameter("orgId") != null ? request
							.getParameter("orgId") : "";

					if (repNameArr != null && repNameArr.length > 0) {
						/** 复选下载 */
						fileList = new ArrayList();
						for (int i = 0; i < repNameArr.length; i++) {
							File file = new File(templateOrgPath + year + "_"
									+ term + File.separator + orgId
									+ File.separator + repNameArr[i] + ".xls");
							if (!file.exists()) {
								file = new File(templateSouPath + repNameArr[i]
										+ ".xls");
								if (!file.exists())
									continue;
							}
							fileList.add(file);
						}
					}
				} else {
					/** 下载全部 */
					ReportInForm reportInForm = new ReportInForm();
					Calendar calendar = Calendar.getInstance();
					String year = request.getParameter("year") != null ? request
							.getParameter("year")
							: null;
					String term = request.getParameter("term") != null ? request
							.getParameter("term")
							: null;
					if (term != null && term.length() == 2
							&& term.indexOf("0") == 0) {
						term = term.substring(1);
					}
					orgId = request.getParameter("orgId") != null ? request
							.getParameter("orgId") : null;
					reportInForm.setOrgId(orgId);
					reportInForm.setSetDate(term);
					try {
						reportInForm.setYear(new Integer(year));
					} catch (Exception ex) {
						reportInForm.setYear(new Integer(calendar
								.get(Calendar.YEAR)));
					}
					List list = StrutsMRepRangeDelegate.selectYBSJWJ(
							reportInForm, operator);
					if (list != null && list.size() > 0) {
						fileList = new ArrayList();
						for (int i = 0; i < list.size(); i++) {
							Aditing aditing = (Aditing) list.get(i);
							File file = new File(templateOrgPath + year + "_"
									+ term + File.separator + orgId
									+ File.separator + aditing.getChildRepId()
									+ "_" + aditing.getVersionId() + ".xls");
							if (!file.exists()) {
								file = new File(templateSouPath
										+ aditing.getChildRepId() + "_"
										+ aditing.getVersionId() + ".xls");
								if (!file.exists())
									continue;
							}
							fileList.add(file);
						}
					}
				}

				if (fileList == null || fileList.size() <= 0) {
					ErrorOutPut(response);
					return;
				}

				OrgNet orgNet = StrutsOrgNetDelegate.selectOne(orgId);
				String pathName = orgNet != null && orgNet.getOrgName() != null ? orgNet
						.getOrgName()
						: "templateExcel";
				String srcFileName = Config.WEBROOTPATH + pathName;
				String zipFileName = pathName + ".zip";

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

				String filePath = srcFileName + File.separator;

				for (int i = 0; i < fileList.size(); i++) {
					File file = (File) fileList.get(i);

					try {
						File reportDataFile = new File(filePath
								+ file.getName());
						reportDataFile.createNewFile();

						int len = 0;
						InputStream inStream = new FileInputStream(file);
						byte[] buffer = new byte[inStream.available()];

						FileOutputStream outStream = null;
						while ((len = inStream.read(buffer)) > 0) {
							outStream = new FileOutputStream(reportDataFile);
							outStream.write(buffer, 0, len);
						}
						outStream.close();
						inStream.close();
					} catch (IOException ex) {
						ex.printStackTrace();
					}
				}

				dldtZip.gzip(srcFileName);

				response.reset();
				response.setContentType("application/x-zip-compressed;name=\""
						+ zipFileName + "\"");
				response.addHeader("Content-Disposition",
						"attachment; filename=\""
								+ FitechUtil.toUtf8String(zipFileName) + "\"");
				response.setHeader("Accept-ranges", "bytes");

				FileInputStream inStream = new FileInputStream(srcFileName
						+ ".zip");

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
		out.println("<font color=\"blue\">没有可以下载的数据文件!</font>");
		out.close();
	}

	/**
	 * 判断该报表是否为多币种报表
	 * 
	 * @param currReportInFormList
	 * @param childRepId
	 * @return
	 */
	private boolean isExist(List currReportInFormList, String childRepId) {
		boolean bool = false;

		if (currReportInFormList == null || currReportInFormList.size() <= 0)
			return bool;

		for (int i = 0; i < currReportInFormList.size(); i++) {
			ReportInForm currReportInForm = (ReportInForm) currReportInFormList
					.get(i);

			if (currReportInForm.getChildRepId().equals(childRepId)) {
				bool = true;
				break;
			}
		}
		return bool;
	}

	private ReportInForm isExist(String childRepId, List currReportInFormList) {

		if (currReportInFormList == null || currReportInFormList.size() <= 0)
			return null;

		for (int i = 0; i < currReportInFormList.size(); i++) {
			ReportInForm currReportInForm = (ReportInForm) currReportInFormList
					.get(i);

			if (currReportInForm.getChildRepId().equals(childRepId)) {
				return currReportInForm;
			}
		}
		return null;
	}

	private ReportInForm isExist(String[] repNameArr,
			ReportInForm currReportInForm) {

		if (repNameArr == null || repNameArr.length == 0
				|| currReportInForm == null
				|| currReportInForm.getChildRepId() == null)
			return null;

		for (int i = 0; i < repNameArr.length; i++) {
			String childRepId = repNameArr[i].split("_")[0];
			if (childRepId.equals(currReportInForm.getChildRepId())) {
				return currReportInForm;
			}
		}
		return null;
	}
	
	/**
	 * 通过传入参数加工成生成报表的路径
	 * @param childRepId 报表编号
	 * @param versionId 版本号
	 * @param orgId 机构号
	 * @param curId 币种
	 * @param actuFreqId 频度
	 * @param date 日期（y-m-d格式）
	 * @return String 路径
	 */
	private String productReportPath(String childRepId,String versionId,
			String orgId, String curId, Integer actuFreqId, String date){
		
		String path = "";
		
		if(date.length()!=10) 
			date = date.substring(0, 5) + "0" + date.substring(5, 9);
			
		path = childRepId + Config.SPLIT_SYMBOL_OUTLINE 
			+ versionId + Config.SPLIT_SYMBOL_OUTLINE
			+ orgId + Config.SPLIT_SYMBOL_OUTLINE
			+ curId+ Config.SPLIT_SYMBOL_OUTLINE
			+ actuFreqId;
		
		path = com.fitech.gznx.common.Config.REPORT_PATH
					+ path + Config.SPLIT_SYMBOL_OUTLINE
					+ DateUtil.getFreqDateLast(date, actuFreqId).replace("-", "") + ".xls";
		
		return path;
	}

}