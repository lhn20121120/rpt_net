package com.fitech.net.figure;


import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.imageio.ImageIO;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;



public class Generate extends HttpServlet 
{
//  File fimg=new File("fimg.jpeg");
  WarnFigure      pie=null;
  public void init(ServletConfig conf) throws ServletException
  {
    super.init(conf);
  }
  //Process the HTTP Get request
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
  {

	try
	{
    String    title =request.getParameter("title");
    String    allWarnMessageColor=request.getParameter("allWarnMessageColor");
    float    currentValue=Float.parseFloat(request.getParameter("currentValue"));
    if(title.split(Config.SPLIT_TITLE)[1].equals(com.fitech.net.config.Config.Target_Warn))
    	      title=title.split(Config.SPLIT_TITLE)[0]+"指标 指标值分配显示表";
    else
    	      title=title.split(Config.SPLIT_TITLE)[0]+"指标 比上期值分配显示表";
    
    pie=new WarnFigure(title,allWarnMessageColor,currentValue);
	}catch(Exception e)
	{
		e.printStackTrace();
	}
    
  
    //创建图形对象
    File f = new File("c.jpeg");
	JFreeChart chart2 = pie.initial();
	ChartUtilities.saveChartAsPNG(f,chart2, Config.WIDTH, Config.HEIGHT);
	BufferedImage img=ImageIO.read(f);
	
	Graphics2D g2d=img.createGraphics();	
	chart2.draw(g2d,new Rectangle2D.Double(0,0,Config.WIDTH,Config.HEIGHT));
//  可以手工画图	
//	g2d.drawString("地方高科技",10,10);
	
	img.flush();
	
	ImageIO.write(img,"png",f);
	
    sendImage(response,f);
  }
  
  protected void sendImage(HttpServletResponse res,File img) throws IOException
  {	   
	    try
	    {	   
	      OutputStream Out=res.getOutputStream();//取得响应输出流
	      byte[] buf=getBytesFromFile(img);	  
	      res.setContentLength(buf.length);//设置输出流长度
	      Out.write(buf);
	      Out.close();	    
	    }
	    catch(IOException e){
	      e.printStackTrace();
	    }

	  } 
  //Clean up resources
  public void destroy() {
  }
//Returns the contents of the file in a byte array.
  public static byte[] getBytesFromFile(File file) throws IOException
  {
      InputStream is = new FileInputStream(file);  
      // Get the size of the file
      long length = file.length();  
      // You cannot create an array using a long type.
      // It needs to be an int type.
      // Before converting to an int type, check
      // to ensure that file is not larger than Integer.MAX_VALUE.
      if (length > Integer.MAX_VALUE) {
          // File is too large
      }  
      // Create the byte array to hold the data
      byte[] bytes = new byte[(int)length];  
      // Read in the bytes
      int offset = 0;
      int numRead = 0;
      while (offset < bytes.length
             && (numRead=is.read(bytes, offset, bytes.length-offset)) >= 0) {
          offset += numRead;
      } 
      // Ensure all the bytes have been read in
      if (offset < bytes.length) {
          throw new IOException("Could not completely read file "+file.getName());
      } 
      // Close the input stream and return bytes
      is.close();
      return bytes;
  }

}