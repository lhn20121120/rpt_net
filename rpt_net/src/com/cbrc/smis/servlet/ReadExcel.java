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
 * ��ȡ���ݿ��Excel�ļ�������IE����ʾ(��ʱΪ����ʱ�ļ��ж�ȡ)
 * 
 * @author wng.wl
 * @serialData 2006-5-24
 */
public class ReadExcel extends HttpServlet {
	/**
	 * reset����
	 * 
	 * @param request HttpServletRequest
	 * @param response HttpServletResponse
	 * @return void
	 * @exception IOException,ServletException
	 */
	public void doGet(HttpServletRequest request,HttpServletResponse response) throws IOException,ServletException{
		String childRepId=null;   //�ӱ���ID
		String versionId=null;    //�汾��
		
		if(request.getParameter("childRepId")!=null) childRepId=(String)request.getParameter("childRepId");
		if(request.getParameter("versionId")!=null) versionId=(String)request.getParameter("versionId");

		ServletOutputStream out=null;
	  	InputStream in=null;
	  	BufferedReader reader=null;
	  	
	  	try{	  		
	  			Blob excel=null;
	  		
		  		ReportDataForm form=StrutsReportDataDelegate.getReportData(childRepId,versionId);
			  	if(form!=null && form.getPdf()!=null) excel=form.getPdf();
		  	
		  	  if(form!=null && form.getPdf()!=null)
		  	  {
		  		  
		  		  
		  		  in=excel.getBinaryStream();
		  		  
		  		  response.setContentType("application/vnd.ms-excel");
		  		  response.setContentLength(in.available());
		  		  
			  	  byte[] line=new byte[1024];
			  	  out=response.getOutputStream();
			  	  while(in.read(line)!=-1){
			  		  out.write(line);
		  	  }
	  		  
			  in.close();
			  out.flush();
			  out.close();
	  	}
	  	else
	  	{
	  		  response.setContentType("text/html;charset=gb2312");
	  		  PrintWriter _out=response.getWriter();
	  		  _out.println("��ȡExcel�ļ�ʧ��!");
	  	}
	  }
		catch(Exception e){
	  		e.printStackTrace();
	  	}
	}
	
	public void doPost(HttpServletRequest request,HttpServletResponse response) throws IOException,ServletException{
		doGet(request,response);
	}
}
