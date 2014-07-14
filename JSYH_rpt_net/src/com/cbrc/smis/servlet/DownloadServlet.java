package com.cbrc.smis.servlet;
/**
 *<p>标题：下载Servlet 
 *filePath 文件路径 deleteFile 部位空则删除文件 fileName 文件名<p>
 *<p>描述：<p>
 *<p>日期：<p>
 *<p>作者：<p>
 */
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.cbrc.smis.common.Config;
import com.cbrc.smis.util.FitechUtil;

public class DownloadServlet extends HttpServlet
{
    public DownloadServlet()
    {
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response)
        throws IOException, ServletException
    {
    	String filePath = request.getParameter("filePath");
        String deleteFile = request.getParameter("deleteFile");
        String fileName = request.getParameter("fileName");

        File file = new File(filePath);
        if(!file.exists())
            throw new FileNotFoundException();
        response.reset();
        response.setContentType("application/octet-stream;charset=gb2312"); 
		response.addHeader("Content-Disposition","attachment; filename="+ URLEncoder.encode(fileName,"gb2312"));
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
        if(deleteFile != null)
            file.delete();
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response)
        throws IOException, ServletException
    {
        doPost(request, response);
    }
}
