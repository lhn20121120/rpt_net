package com.cbrc.smis.servlet;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.cbrc.smis.common.Config;
import com.cbrc.smis.excel.CreateExcel;
import com.cbrc.smis.excel.DB2Excel;
import com.cbrc.smis.security.Operator;
import com.fitech.gznx.common.FileUtil;
public class ToExcelServlet extends HttpServlet {
	/**
	 * DoGet方法
	 * 
	 * @param request HttpServletRequest
	 * @param response HttpServletResponse
	 * @return void
	 */
	public void doGet(HttpServletRequest request,HttpServletResponse response) throws ServletException,IOException{
		Integer repInId=null;
		
		if(request.getParameter("repInId")!=null) repInId=Integer.valueOf(request.getParameter("repInId"));
				
//		CreateExcel ce = new CreateExcel(repInId);
//		HSSFWorkbook book = ce.createDataReport();
//		String filePath=Config.WEBROOTPATH+com.fitech.net.config.Config.WEBROOT_TEMP+Config.FILESEPARATOR+System.currentTimeMillis();
//
//		String excelFilePath=filePath+ ".xls";
//		File file = new File(excelFilePath);
//		FileOutputStream fos = new FileOutputStream(file);
//		book.write(fos);  
//		fos.close();
		
		
		
//		DB2Excel db2Excel=new DB2Excel(repInId);
//		/**
//		 * Config 类太多分不清了
//		 */
//		String filePath=Config.WEBROOTPATH+com.fitech.net.config.Config.WEBROOT_TEMP+Config.FILESEPARATOR+System.currentTimeMillis();
//		
//		if(file.exists()){
//	
//			response.sendRedirect(request.getContextPath()+"/servlets/DownloadServlet?filePath="+excelFilePath+"&deleteFile=1&fileName="+file.getName());
//		};
//		
 
		if(request.getParameter("repInId")!=null) repInId=Integer.valueOf(request.getParameter("repInId"));
				
		DB2Excel db2Excel=new DB2Excel(repInId);
//		String filePath=Config.WEBROOTPATH+com.fitech.net.config.Config.WEBROOT_TEMP+Config.FILESEPARATOR+System.currentTimeMillis();//使用下面的文件路径，避免下载的临时文件删除不了
		Operator operator = null; 
		if(request.getSession().getAttribute(Config.OPERATOR_SESSION_ATTRIBUTE_NAME)!=null)
			operator = (Operator)request.getSession().getAttribute(Config.OPERATOR_SESSION_ATTRIBUTE_NAME);
		String filePath=new StringBuffer(Config.WEBROOTPATH).append(com.fitech.net.config.Config.WEBROOT_TEMP).append(Config.FILESEPARATOR)
							.append(operator.getOperatorId()).append(".xls").toString();
		FileUtil.deleteFile(new File(filePath));//删除已经存在的文件
		if(db2Excel.createExcel(filePath)){
	
			response.sendRedirect(request.getContextPath()+"/servlets/DownloadServlet?filePath="+filePath+"&deleteFile=1&fileName="+db2Excel.getFileName());
		};
		
	}
	
	/**
	 * DoPost方法
	 * 
	 * @param request HttpServletRequest
	 * @param response HttpServletResponse
	 * @return void
	 */
	public void doPost(HttpServletRequest request,HttpServletResponse response) throws ServletException,IOException{
		doGet(request,response);
	}
}