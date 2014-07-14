package com.fitech.gznx.servlet;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.cbrc.smis.adapter.StrutsMRepRangeDelegate;
import com.cbrc.smis.common.Config;
import com.cbrc.smis.common.DownLoadDataToZip;
import com.cbrc.smis.form.ReportInForm;
import com.cbrc.smis.other.Aditing;
import com.cbrc.smis.security.Operator;
import com.cbrc.smis.util.FitechUtil;
import com.fitech.gznx.common.DateUtil;
import com.fitech.gznx.common.FileUtil;
import com.fitech.gznx.form.AFReportForm;
import com.fitech.gznx.po.AfOrg;
import com.fitech.gznx.service.AFOrgDelegate;
import com.fitech.gznx.service.AFReportDelegate;
import com.fitech.net.adapter.StrutsOrgNetDelegate;
import com.fitech.net.hibernate.OrgNet;

/**
 * 2010-12-20 下载数据文件
 */
public class DownLoadReportServlet extends HttpServlet {

	/**
	 * 
	 */
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
	 * 已使用hibernate 卞以刚 2011-12-22
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response) {

		HttpSession session = request.getSession();
		Operator operator = null;

		if (session.getAttribute(Config.OPERATOR_SESSION_ATTRIBUTE_NAME) != null)
			operator = (Operator) session
					.getAttribute(Config.OPERATOR_SESSION_ATTRIBUTE_NAME);

		String orgId = "";

		List fileList = null;

		try {
			
			String type = request.getParameter("type") != null ? request.getParameter("type"): null;
			
			String repNames = request.getParameter("repNames") != null ? request.getParameter("repNames"): null;
			
			if ( repNames != null ) {
				// 需下载的报表list
				String[] repNameArr = repNames.split(",");
				// 报送时间
				String dates = request.getParameter("date") != null ? request.getParameter("date") : "";
				// 机构
				orgId = request.getParameter("orgId") != null ? request.getParameter("orgId") : "";
				
				
				if (!type.equals("downAll") && !repNames.equals("") 
						&& repNameArr != null && repNameArr.length > 0) {

					/** 单、复选下载 */
					fileList = new ArrayList();

					for (int i = 0; i < repNameArr.length; i++) {

						/** repNameArr格式：模板id+版本id+货币id+频度id+机构id）*/
						
						String[] repInfo = repNameArr[i].split(Config.SPLIT_SYMBOL_OUTLINE);
						Integer freqId = Integer.valueOf(repInfo[4]);
						
						
						//获得已生成的报表
						File file = new File(com.fitech.gznx.common.Config.REPORT_PATH
										+ repNameArr[i] + Config.SPLIT_SYMBOL_OUTLINE
										+ DateUtil.getFreqDateLast(dates, freqId).replaceAll("-", "") + ".xls");
						
						//如果没有该期已生成的报表，则下载空白模板
						if (!file.exists()) {
							file = new File(com.fitech.gznx.common.Config.TEMPLATE_PATH
											+ repInfo[0] + Config.SPLIT_SYMBOL_OUTLINE
											+ repInfo[1] + ".xls");
							if (!file.exists())
								continue;
						}
						
						fileList.add(file);
					}

				} else {
					/** 下载全部 */
					AFReportForm afReportForm = new AFReportForm();

					String bak1 = request.getParameter("bak1") != null ? request.getParameter("bak1") : null;
					
					repNames = new String (repNames.getBytes("ISO-8859-1"),"GB2312");
					
					afReportForm.setOrgId(orgId);
					afReportForm.setDate(dates);
					afReportForm.setRepName(repNames);
					afReportForm.setBak1(bak1);

//					if (session.getAttribute(Config.REPORT_SESSION_FLG) != null)
//						afReportForm.setTemplateType(session.getAttribute(Config.REPORT_SESSION_FLG).toString());
					afReportForm.setTemplateType(com.fitech.gznx.common.Config.OTHER_REPORT);
					afReportForm.setIsReport(Integer.valueOf(com.fitech.gznx.common.Config.TEMPLATE_ANALYSIS));
					
					/**已使用hibernate 卞以刚 2011-12-21*/
					List list = AFReportDelegate.selectNeedReportList(afReportForm, operator);
					
					String param = "";
					
					if (list != null && list.size() > 0) {
						fileList = new ArrayList();
						for (int i = 0; i < list.size(); i++) {
							Aditing aditing = (Aditing) list.get(i);
							param = aditing.getTemplateId() + Config.SPLIT_SYMBOL_OUTLINE 
										+ aditing.getVersionId() + Config.SPLIT_SYMBOL_OUTLINE
										+ aditing.getOrgId() + Config.SPLIT_SYMBOL_OUTLINE
										+ aditing.getCurId()+ Config.SPLIT_SYMBOL_OUTLINE
										+ aditing.getActuFreqID();
							//获得已生成的报表
							File file = new File(com.fitech.gznx.common.Config.REPORT_PATH
											+ param + Config.SPLIT_SYMBOL_OUTLINE
											+ DateUtil.getFreqDateLast(dates, aditing.getActuFreqID()).replace("-", "") + ".xls");
							
							//如果没有该期已生成的报表，则下载空白模板
							if (!file.exists()) {
								file = new File(com.fitech.gznx.common.Config.TEMPLATE_PATH
												+ aditing.getTemplateId() + Config.SPLIT_SYMBOL_OUTLINE
												+ aditing.getVersionId() + ".xls");
								if (!file.exists())
									continue;
							}
							
							fileList.add(file);
						}
					}
					
					
					
				}

				if (fileList == null || fileList.size() <= 0) {
					//ErrorOutPut(response);
					ErrorOutPut(response,repNames);
					return;
				}
				
				/**已使用hibernate 卞以刚 2011-12-21*/
				AfOrg afOrg = AFOrgDelegate.selectOne(orgId);
				
				String pathName = afOrg != null && afOrg.getOrgName() != null ? 
						afOrg.getOrgName() : "templateExcel";
						
				String srcFileName = Config.WEBROOTPATH  + "temp" + Config.FILESEPARATOR + pathName;
				
				String zipFileName = pathName + ".zip";

				//打包
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
				
				//删除临时文件
				FileUtil.deleteFile(outfile);
				FileUtil.deleteFile(srcFileName + ".zip");
				
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			ErrorOutPut(response);
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
	 * 错误输出
	 * 
	 * @param response
	 */
	private void ErrorOutPut(HttpServletResponse response,String info) {
		response.reset();
		response.setContentType("text/html;charset=GB2312");
		PrintWriter out = null;
		try {
			out = response.getWriter();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		out.println("<font color=\"blue\">" +info +"没有可以下载的数据文件!</font>");
		out.close();
	}

}