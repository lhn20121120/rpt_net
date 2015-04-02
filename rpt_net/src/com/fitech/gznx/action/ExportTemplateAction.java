package com.fitech.gznx.action;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.cbrc.smis.common.Config;
import com.cbrc.smis.common.DownLoadDataToZip;
import com.cbrc.smis.security.Operator;
import com.cbrc.smis.util.FitechUtil;
import com.fitech.gznx.common.FileUtil;
import com.fitech.gznx.po.AfTemplate;
import com.fitech.gznx.service.AFTemplateDelegate;

public class ExportTemplateAction extends HttpServlet {/**
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
		String sessionId=session.getId();
				
		Operator operator = null;
		
		/** 报表选中标志 **/
		String reportFlg = "0";
		if (session.getAttribute(Config.REPORT_SESSION_FLG) != null){
			reportFlg = (String) session.getAttribute(Config.REPORT_SESSION_FLG);
		}
		
		if (session.getAttribute(Config.OPERATOR_SESSION_ATTRIBUTE_NAME) != null)
			operator = (Operator) session
					.getAttribute(Config.OPERATOR_SESSION_ATTRIBUTE_NAME);

		String orgId = "";

		List fileList = null;
		
		Map<String, String> map=new HashMap<String, String>();
		
		

		try {
			
			String type = request.getParameter("type") != null ? request.getParameter("type"): null;
			
			String repNames =  request.getParameter("repNames");
			
			if ( repNames != null&&type.equals("checkbox")) {
				// 需下载的报表list
				String[] repNameArr = repNames.split(",");
				// 报送时间
				String dates = request.getParameter("date") != null ? request.getParameter("date") : "";
				// 机构
				orgId = request.getParameter("orgId") != null ? request.getParameter("orgId") : "";
				
				
				if (!repNames.equals("") && repNameArr != null && repNameArr.length > 0) {

					/** 单、复选下载 */
					fileList = new ArrayList();

					for (int i = 0; i < repNameArr.length; i++) {

						/** repNameArr格式：模板id+版本id+货币id+频度id+机构id）*/
						
						String[] repInfo = repNameArr[i].split(Config.SPLIT_SYMBOL_OUTLINE);
						
						
						//获得已生成的报表
						File file = new File(Config.RAQ_INIT_TEMP_PATH
										+ repInfo[0] + Config.SPLIT_SYMBOL_OUTLINE
										+ repInfo[1] + ".raq");
						
						
						if (!file.exists()) {
						
								continue;
						}
						map.put(file.getName(), repInfo[2]+repInfo[0]+repInfo[1]+".raq");
						fileList.add(file);
					}

				} 

				if (fileList == null || fileList.size() <= 0) {
					//ErrorOutPut(response);
					ErrorOutPut(response,repNames);
					return;
				}
				
				/**已使用hibernate 卞以刚 2011-12-21*/
				//AfOrg afOrg = AFOrgDelegate.selectOne(orgId);
				
				//String pathName = afOrg != null && afOrg.getOrgName() != null ? 
						//afOrg.getOrgName() : "templateExcel";
						
				String srcFileName = Config.WEBROOTPATH  + "temp" + Config.FILESEPARATOR + "templateRaq_"+sessionId;
				
				String DATE=new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
				
				String zipFileName = DATE +".zip";

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

				
				if(fileList.size() >1){
					for (int i = 0; i < fileList.size(); i++) {
						File file = (File) fileList.get(i);

						try {
							File reportDataFile = new File(filePath
									+ map.get(file.getName()));
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

					FileInputStream inStream = new FileInputStream(srcFileName+".zip"
							);
					OutputStream outStream=response.getOutputStream();

					int len;
					byte[] buffer = new byte[100];

					while ((len = inStream.read(buffer)) > 0) {
						outStream.write(buffer, 0, len);
					}
					inStream.close();
					outStream.close();
					
					//删除临时文件
					FileUtil.deleteFile(outfile);
					FileUtil.deleteFile(srcFileName + ".zip");
				}else {
					
					String absoluteFileName = ((File)fileList.get(0)).getAbsolutePath();
					String newFileName= map.get(((File)fileList.get(0)).getName());
					response.reset();
					response.setContentType("application/x-msdownload;name=\""
							+ newFileName + "\"");
					response.addHeader("Content-Disposition",
							"attachment; filename=\""
									+ FitechUtil.toUtf8String(newFileName));
					response.setHeader("Accept-ranges", "bytes");

					FileInputStream inStream = new FileInputStream(absoluteFileName);
					OutputStream outStream=response.getOutputStream();

					int len;
					byte[] buffer = new byte[100];

					while ((len = inStream.read(buffer)) > 0) {
						outStream.write(buffer, 0, len);
					}
					inStream.close();
					outStream.close();
				}
				
				
			}else if(type.equals("all")){
				fileList=new ArrayList();
				List list=AFTemplateDelegate.selectAllTemplate("", "", reportFlg);//获取所有模版
				this.compressToZip(list, fileList, response, repNames, sessionId);
				
				
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
	
	/**
	 * 打包为zip
	 * @param File
	 * @param List
	 */
	
	private  void compressToZip(List list,List fileList,HttpServletResponse response,String repNames,String sessionId) throws IOException{
		Map map=new HashMap();
		for (int i = 0; i < list.size(); i++) {
			
			AfTemplate afTemplate=(AfTemplate)list.get(i);
			
			File file = new File(Config.RAQ_INIT_TEMP_PATH
					+ afTemplate.getId().getTemplateId() + Config.SPLIT_SYMBOL_OUTLINE
					+ afTemplate.getId().getVersionId() + ".raq");
	
	
	if (!file.exists()) {
	
			continue;
	}
	map.put(file.getName(), afTemplate.getTemplateName()+afTemplate.getId().getTemplateId()+afTemplate.getId().getVersionId()+".raq");
	fileList.add(file);
		}
		if (fileList == null || fileList.size() <= 0) {
			//ErrorOutPut(response);
			ErrorOutPut(response,repNames);
			return;
		}
		
		String srcFileName = Config.WEBROOTPATH  + "temp" + Config.FILESEPARATOR + "templateRaq_"+sessionId;
		
		String DATE=new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
		
		String zipFileName = DATE+ ".zip";

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
							+ map.get(file.getName()));
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

			FileInputStream inStream = new FileInputStream(srcFileName+".zip"
					);

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
}
