/*
 * Created on 2006-5-18
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.fitech.net.common;

import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Administrator
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class ReadExcelServlet extends HttpServlet {

	/**
	 * �����û�����������󣬸��ݲ�ͬ�Ĺ���������ò�ͬ�Ĺ��ܷ�����
	 *
	 * @author: wunaigang
	 *
	 * @exception ServletException, IOException
	 * @change: 
	 */		
	 public void service(
			HttpServletRequest request,
			HttpServletResponse response)
			throws ServletException, IOException {
	 	/*
	 	 * ����ͷ��Ϣ
	 	 */
	 	response.setContentType("application/vnd.ms-excel");
	 	String fileName = request.getParameter("fileName");
	    InputStream bis = null;
	    BufferedOutputStream bos = null;
	    ServletOutputStream os = null;
	    try {//filename
	         os = response.getOutputStream();
	         bis =  new FileInputStream(fileName);

			 //ѭ��ȡ�����е����� 
			 byte[] b = new byte[100]; 
			 int len; 
			 while((len=bis.read(b)) >0){
			   os.write(b,0,len); 
			 } 
			os.flush();
			bis.close();   
			os.close();       
	    } catch(final IOException e) {
	    	error(response);
	    } finally {
	        if (bis != null)
	            bis.close();
	        if (bos != null)
	            bos.close();
	    }

	 }
	 
	 
	/**
	 * ������
	 * 
	 * @param response HttpServletResponse
	 * @return void
	 * @exception IOException,ServletException
	 */
	private void error(HttpServletResponse response) throws IOException,ServletException{
		response.setContentType("text/html;charset=gb2312");
		PrintWriter out=response.getWriter();
		out.println("��ȡ��������ʧ��!");
	}	 
	
}
