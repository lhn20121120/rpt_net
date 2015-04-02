/*
 * 创建日期 2005-8-23
 *
 */
package com.gather.servlets;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author AhFun.Net
 * 显示导入文件中的附件文件
 */
public class ShowAffixFile extends HttpServlet {
    /**
     * doPost方法
     * 
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @return void
     */	
    public void doGet(HttpServletRequest request,HttpServletResponse response) 
    throws IOException ,ServletException{
        
        String strPath = request.getParameter("affixPath");
     
        FileInputStream fis = null;
        
        
        strPath = new String(strPath.getBytes("ISO8859_1"),"GB2312");//12 
        File file = null;
        
        if(!strPath.equals("")) {
            
            file = new File(strPath);
            
            try {
                fis = new FileInputStream(file);
            } catch (FileNotFoundException e1) {
                e1.printStackTrace();
            }
            
        }
              
        OutputStream out=null;
        InputStream in=null;
            
        if(fis!=null){
            in=fis;
        }else{
            response.reset();
			response.setContentType("text/html;charset=GB2312");
			PrintWriter w=response.getWriter();
			w.println("<font color=\"blue\">文件读取失败，请重试!</font>");
			w.close();
        }
        
        response.setContentType("application/octet-stream");
        
        try {
            out=response.getOutputStream();
            byte[] buf=new byte[1024*10];
            while(in.read(buf)!=-1)
                out.write(buf);
        } catch (IOException e) {

            e.printStackTrace();
            response.reset();
			response.setContentType("text/html;charset=GB2312");
			PrintWriter w=response.getWriter();
			w.println("<font color=\"blue\">文件读取失败，请重试!</font>");
			w.close();
        }
        in.close();
        out.flush();
        out.close();
        
    }
    
    /**
     * doPost方法
     * 
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @return void
     */
    public void doPost(HttpServletRequest request,HttpServletResponse response) 
    throws IOException ,ServletException{
        doGet(request,response);  	
    }
}
