/*
 * 创建日期 2005-7-21
 *
 * TODO 要更改此生成的文件的模板，请转至
 * 窗口 － 首选项 － Java － 代码样式 － 代码模板
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
 * 写一个XML文档给Request
 */
public class WriteXML extends HttpServlet {
  /**
   * doGet方法
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
   * doPost方法
   * 
   * @return void
   */
  public void doPost(HttpServletRequest request,HttpServletResponse response) throws IOException,ServletException{
  	doGet(request,response);
  }
  
  /**
   * 写XML文档
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
   * 创建XML文档
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
