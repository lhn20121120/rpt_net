/*
 * �������� 2005-7-21
 *
 * TODO Ҫ���Ĵ����ɵ��ļ���ģ�壬��ת��
 * ���� �� ��ѡ�� �� Java �� ������ʽ �� ����ģ��
 */
package com.gather.servlets;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
/**
 * @author rds
 * @date 2005-07-21
 * 
 * дһ��XML�ĵ���Request
 */
public class WriteXML extends HttpServlet {
  /**
   * doGet����
   * 
   * @return void
   */
  public void doGet(HttpServletRequest request,HttpServletResponse response) throws IOException,ServletException{
  	response.setContentType("text/xml;charset=utf-8");
  	OutputStream out=response.getOutputStream();
  	//writeXML(out);
  	createXML(out);
  }
  
  /**
   * doPost����
   * 
   * @return void
   */
  public void doPost(HttpServletRequest request,HttpServletResponse response) throws IOException,ServletException{
  	doGet(request,response);
  }
  
  /**
   * дXML�ĵ�
   * 
   */
  private void writeXML(OutputStream out){
  	File file=null;
  	
  	try{
  		file=new File("C:\\eclipse\\workspace\\SMMIS\\WebRoot\\build.xml");
  		SAXReader reader=new SAXReader();
  		Document document=reader.read(file);
  		 		
  		OutputFormat format=OutputFormat.createPrettyPrint();
  		XMLWriter write=new XMLWriter(out,format);
  		write.write(document);
  		write.close();
  	}catch(DocumentException de){
  		de.printStackTrace();
  	}catch(IOException ioe){
  		ioe.printStackTrace();
  	}
  }
  
  /**
   * ����XML�ĵ�
   * 
   */
  private void createXML(OutputStream out){
  	File file=null;
  	
  	try{
  		Document document=DocumentHelper.createDocument();
  		Element root=document.addElement("Resources");
  		root.addElement("Resource").setText("http://cbrcssoa.cbrc.gov.cn:80/SMMIS");
  		root.addElement("Resource").setText("http://cbrcssoa.cbrc.gov.cn:80/SMMIS/*");
  		OutputFormat format=OutputFormat.createPrettyPrint();
  		XMLWriter write=new XMLWriter(out,format);
  		write.write(document);
  		write.close();
  	}catch(IOException ioe){
  		ioe.printStackTrace();
  	}
  }
}
