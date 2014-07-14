package com.cbrc.smis.servlet;

import java.io.BufferedReader;
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
import com.cbrc.smis.form.ReportDataForm;
/**
 * 读取数据库的PDF文件，并在IE中显示
 * 
 * @author IBMUSER
 * @serialData 2005-12-08
 */
public class ReadPDF extends HttpServlet {
	/**
	 * reset方法
	 * 
	 * @param request HttpServletRequest
	 * @param response HttpServletResponse
	 * @return void
	 * @exception IOException,ServletException
	 */
	public void doGet(HttpServletRequest request,HttpServletResponse response) throws IOException,ServletException{
		String childRepId=null;   //子报表ID
		String versionId=null;    //版本号
		
		if(request.getParameter("childRepId")!=null) childRepId=(String)request.getParameter("childRepId");
		if(request.getParameter("versionId")!=null) versionId=(String)request.getParameter("versionId");

		ServletOutputStream out=null;
	  	InputStream in=null;
	  	BufferedReader reader=null;
	  	
	  	try{
	  	  Blob pdf=null;
	  	  
	  	  ReportDataForm form=StrutsReportDataDelegate.getReportData(childRepId,versionId);
	  	  if(form!=null && form.getPdf()!=null) pdf=form.getPdf();
	  	  
	  	  if(pdf!=null){	
	  		  	  		
	  		  in=pdf.getBinaryStream();
	  		  /*
		  	  FitechUtil.writeFile(in,"pdf");
		  	  in.reset();*/
		  	  
		  	  response.setContentType("application/pdf");
		  	  response.setContentLength(in.available());
		  	  
		  	  /*reader=new BufferedReader(new InputStreamReader(in));
			  out=new BufferedWriter(new OutputStreamWriter(response.getOutputStream()));
			
			  String line="";
			  while((line=reader.readLine())!=null){
				  // System.out.println(line);
			    out.write(line);
			  }*/
			  
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
	  		  _out.println("读取PDf文件失败!");
	  	  }
	  	}catch(Exception e){
	  		e.printStackTrace();
	  	}
	}
	
	public void doPost(HttpServletRequest request,HttpServletResponse response) throws IOException,ServletException{
		doGet(request,response);
	}
}
