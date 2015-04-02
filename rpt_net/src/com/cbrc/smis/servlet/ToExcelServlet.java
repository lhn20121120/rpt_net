package com.cbrc.smis.servlet;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.cbrc.smis.common.Config;
import com.cbrc.smis.excel.CreateExcel;
import com.cbrc.smis.excel.DB2Excel;
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
		/**
		 * Config 类太多分不清了
		 */
		String filePath=Config.WEBROOTPATH+com.fitech.net.config.Config.WEBROOT_TEMP+Config.FILESEPARATOR+System.currentTimeMillis();
		
		if(db2Excel.createExcel(filePath)){
	
			//response.sendRedirect(request.getContextPath()+"/servlets/DownloadServlet?filePath="+filePath+"&deleteFile=1&fileName="+db2Excel.getFileName());
			File file = new File(filePath);
			response.setContentType("application/octet-stream;charset=gb2312"); 
			response.addHeader("Content-Disposition","attachment; filename="+ URLEncoder.encode(db2Excel.getFileName(),"gb2312"));
			response.setHeader("Accept-ranges", "bytes");
								
		   OutputStream os = response.getOutputStream();
	        byte b[] = new byte[500];
	        
	        long fileLength = file.length();
	        String length = String.valueOf(fileLength);
	        response.setHeader("Content-length", length);
	        FileInputStream in = new FileInputStream(file);
	        for(int n = 0; (n = in.read(b)) != -1;)
	            os.write(b, 0, n);

	        in.close();
	        os.close();
	       // if(deleteFile != null)
	            file.delete();
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