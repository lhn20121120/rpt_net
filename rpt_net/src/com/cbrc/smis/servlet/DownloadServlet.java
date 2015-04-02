package com.cbrc.smis.servlet;
/**
 *<p>标题：下载Servlet 
 *filePath 文件路径 deleteFile 部位空则删除文件 fileName 文件名<p>
 *<p>描述：<p>
 *<p>日期：<p>
 *<p>作者：<p>
 */
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.List;
import java.util.zip.CRC32;
import java.util.zip.CheckedOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.cbrc.smis.common.Config;
import com.cbrc.smis.util.FitechUtil;
import com.fitech.gznx.common.StringUtil;
import com.fitech.gznx.form.AFPBOCReportForm;

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
        String type = request.getParameter("type");
        
        File file = null;
        
        if(!StringUtil.isEmpty(filePath))
        	file = new File(filePath);
        
        /**
         * 全部下载---------------------------------------开始
         */
        if(!StringUtil.isEmpty(type)){
        	List<AFPBOCReportForm> recordsList = (List<AFPBOCReportForm>)request.getSession().getAttribute(Config.RECORDS);
        	List<AFPBOCReportForm> adList = (List<AFPBOCReportForm>)request.getSession().getAttribute("adList");
        	
        	fileName = "templateFile.zip";
        	String zipPath = Config.TEMP_DIR + File.separator+fileName;
        	
        	File zipFile = new File(zipPath);
        	FileOutputStream fileOutputStream = new FileOutputStream(zipFile);
    		CheckedOutputStream checkedOutputStream = new CheckedOutputStream(fileOutputStream, new CRC32());
    		
    		ZipOutputStream zipOutputStream = new ZipOutputStream(checkedOutputStream);
    		
        	//String[] file = fileName.split("$");
        	for(AFPBOCReportForm af : recordsList){//报文文件
        		String path = af.getFilePath();
        		String fName = af.getFileName();
        		
        		zipFile(path,fName,zipOutputStream);
        	}
        	
        	//说明文件
        	for(AFPBOCReportForm af : adList){//说明文件
        		String path = af.getFilePath();
        		String fName = af.getFileName();
        		
        		zipFile(path,fName,zipOutputStream);
        	}
        	
        	zipOutputStream.close();
        	file = zipFile;
        }
        /**
         * 全部下载---------------------------------------结束
         */

        
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
    
    private void zipFile(String path,String fileName,ZipOutputStream zipOutputStream){

		BufferedInputStream input = null;
		File dirFile = new File(path);
		try {
			input = new BufferedInputStream(new FileInputStream(dirFile));
			ZipEntry zipEntry = new ZipEntry(fileName);
			zipOutputStream.putNextEntry(zipEntry);
			
			int count = 0;
			byte[] b = new byte[1024];
			
			while((count = input.read(b,0,1024))!=-1){
				zipOutputStream.write(b, 0, count);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally{
			try {
				if(input!=null){
					input.close();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
    }
}
