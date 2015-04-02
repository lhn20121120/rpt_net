package com.cbrc.smis.servlet;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.sql.Blob;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.cbrc.smis.adapter.StrutsReportDataDelegate;
import com.cbrc.smis.common.Config;
import com.cbrc.smis.form.ReportDataForm;

public class SelfReadExcelServlet extends HttpServlet {
	/**
	 * DoGet方法
	 * 
	 * @param request HttpServletRequest
	 * @param response HttpServletResponse
	 * @return void
	 */
	public void doGet(HttpServletRequest request,HttpServletResponse response) throws IOException,ServletException{
		
		String childRepId = request.getParameter("childRepId") != null ? request.getParameter("childRepId") : "";
		String versionId = request.getParameter("versionId") != null ? request.getParameter("versionId") : "";

		ServletOutputStream out=null;
	  	InputStream in=null;
	  	
	  	try{
//	  	  Blob pdf=null;
	  	  String fileName = childRepId + versionId + ".xls";
//	  	  ReportDataForm form=StrutsReportDataDelegate.getReportData(childRepId,versionId);
//	  	  if(form!=null && form.getPdf()!=null) pdf=form.getPdf();
//	  	  if(pdf!=null){ 		
//	  		  in=pdf.getBinaryStream();
//	  		  response.setContentType("application/vnd.ms-excel");
//		  	  response.setContentLength(in.available());
//		  	
//		  	  response.setHeader("Content-disposition", "attachment;filename=" + fileName);
//		  	  byte[] line=new byte[1024];
//		  	  out=response.getOutputStream();
//		  	  while(in.read(line)!=-1){
//		  		  out.write(line);
//		  	  }
//			  in.close();
//			  out.flush();
//			  out.close();
//	  	  }else 
	  		  if(new File(
	 				Config.WEBROOTPATH + "report_mgr" + 
	 				Config.FILESEPARATOR + "excel" + 
	 				Config.FILESEPARATOR + fileName).exists()){
	  		
	  		 in=new FileInputStream(Config.WEBROOTPATH + "report_mgr" + 
	 				Config.FILESEPARATOR + "excel" + 
	 				Config.FILESEPARATOR + fileName);
	  		  response.setContentType("application/vnd.ms-excel");
//		  	  response.setContentLength(in.available());
	  		  response.setBufferSize(1024);
		  	
		  	  response.setHeader("Content-disposition", "attachment;filename=" + fileName);
		  	  byte[] line=new byte[1024];
		  	  out=response.getOutputStream();
		  	  while(in.read(line)!=-1){
		  		  out.write(line);
		  	  }
			  in.close();
			  out.flush();
			  out.close();
	  	  }else{
	  		  response.setContentType("text/html;charset=gb2312");
	  		  PrintWriter _out=response.getWriter();
	  		  _out.println("读取EXCEL文件失败!");
	  	  }
	  	}catch(Exception e){
	  		e.printStackTrace();
	  	}
	}
	
	public void doPost(HttpServletRequest request,HttpServletResponse response) throws IOException,ServletException{
		doGet(request,response);
	}
}