/*
 * Created on 2005-12-21
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.cbrc.smis.servlet;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.sql.Blob;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.cbrc.smis.adapter.StrutsInfoFilesDelegate;
import com.cbrc.smis.form.InfoFilesForm;
import com.cbrc.smis.util.FitechException;
import com.cbrc.smis.util.FitechUtil;

/**
 * @author 曹发根
 * 
 * 下载文件
 */
public class DownLoadInfoFiles extends HttpServlet {
	private FitechException log = new FitechException(DownLoadInfoFiles.class);

	/**
	 * ServletContext
	 */
	private ServletContext context = null;

	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		context = config.getServletContext();

	}
	/**
	 * 已使用hibernate 卞以刚 2011-12-21 
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response) {
		String infoFilesId = request.getParameter("infoFilesId").trim();
        try 
        {
			if (infoFilesId != null && !infoFilesId.equals("")) {
				
				InfoFilesForm infoFileForm = StrutsInfoFilesDelegate
						.find2(new Integer(infoFilesId));
				
				if (infoFileForm != null) 
                {
					response.reset();
					response.setContentType(infoFileForm.getInfoFileType()
							+ ";name=\"" + infoFileForm.getInfoFileName()
							+ "\"");
					// System.out.println(":::"+infoFileForm.getInfoFileType()+infoFileForm.getInfoFileName());
					response.addHeader("Content-Disposition",
							"attachment; filename=\""
									+ FitechUtil.toUtf8String(infoFileForm.getInfoFileName()) + "\"");
					response.setHeader("Accept-ranges", "bytes");
					
					Blob blob = infoFileForm.getBlob();
					if(blob != null){
						int len;
						byte[] buffer = new byte[100];
						InputStream inStream = blob.getBinaryStream();
						while((len = inStream.read(buffer)) > 0){
							response.getOutputStream().write(buffer,0,len);
						}
						inStream.close();
					}
				}else{
                    ErrorOutPut(response);
				}
			}else{
                ErrorOutPut(response);
			}
		}catch (Exception e){
            e.printStackTrace();
            ErrorOutPut(response);
		}

	}
    /**
     * 错误输出
     * @param response
     */
	private void ErrorOutPut(HttpServletResponse response)
    {
        response.reset();
        response.setContentType("text/html;charset=GB2312");
        PrintWriter out = null;
        try 
        {
            out = response.getWriter();
        } 
        catch (IOException e1) 
        {
            e1.printStackTrace();
        }
        out.println("<font color=\"blue\">文件不存在!</font>");
        out.close();     
    }
	/**
	 * 系统运行终止
	 * 
	 * @return void
	 */
	public void destroy() {

	}

}