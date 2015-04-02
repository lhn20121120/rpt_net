package com.gather.down;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;

import com.gather.adapter.StrutsReportDataDelegate;
import com.gather.common.Config;
import com.gather.common.ZipUtils;
import com.gather.servlets.AutoSynchronized;


/** 
 * @author James
 */
public class PackUtils {
	/**
	 * @author James
	 * 
	 * @param request HttpServletRequest 当前request对象
	 * @param files File[] 需打包文件数组
	 * @param zipName String 打包形成的文件名
	 * @return url String 打包的zip文件url路径
	 * @function 打包pdf、form_model_web.txt和view_tree_node.txt文件成zip压缩包，存在download目录
	 */
	public String zipURL(HttpServletRequest request, File[] files, String zipName) {
		// 文件绝对路径，不包含文件名
		String absPath = AutoSynchronized.SMMIS_PATH + Config.FILESEPARATOR 
		                                             + Config.DOWNLOAD_DIR 
		                                             + Config.FILESEPARATOR;		
		File zipFile = null;
		
		try {
			zipFile = ZipUtils.zipFiles(files, absPath, zipName);
			if (zipFile == null || zipFile.length() == 0) {
				// System.out.println("===== zipFile is null");
				return null;
			}		
		} catch (IOException ioe) {
			ioe.printStackTrace();
			return null;
		}
					
		// System.out.println("+++++++++ zipFile's length = " + zipFile.length());

		// 将生成的打包文件写入模板下载目录，允许覆盖
		try {
			InputStream in = new FileInputStream(zipFile);			
			FileOutputStream out = new FileOutputStream(absPath + zipName, true); // zipFile存在性已经判断过，不再判断
			FileDescriptor fd = out.getFD();

			int ch = 0;
			long count = 0;
			/**
			 * TODO: 光判断-1会死循环，暂时加个上限。以后再找原因
			 */
			while((ch=in.read()) != -1 && count < (zipFile.length()/7) + 1){
				out.write(ch);
				out.flush();
				
				count++;
			}
			fd.sync();
			
			in.close();
			out.close();			
			
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		
		// zip文件的URL
		String url = "http://" + request.getServerName() + ":" + request.getServerPort()
		           + "/" + Config.OUTER_SYSTEM_NAME + "/" + Config.DOWNLOAD_DIR
		           + "/" + zipName;

		return url;
	}
	
	/**
	 * @author James
	 *  
	 * @param childRepId String 子报表ID
	 * @param versionId String 版本号 
	 * @return pdfFile File PDF模板文件
	 * @function 根据机构ID、子报表ID和版本号获取PDF模板文件
	 */
	public File getPDF(String childRepId, String versionId) {
		File pdfFile = new File("");
		
		Blob pdfBlob = null;
		pdfBlob = StrutsReportDataDelegate.getPdf(childRepId, versionId);
		InputStream in = null;		
		
		if (pdfBlob != null) {
			try{
				in = pdfBlob.getBinaryStream();
			}
			catch (SQLException sqle) {
				sqle.printStackTrace();
			}
		}
		
		try{
			FileOutputStream out = new FileOutputStream(pdfFile);
			byte[] buffer = new byte[1024];
			while (in.read(buffer) != -1)
				out.write(buffer);
			in.close();
			out.flush();
			out.close();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
		// System.out.println("++++++++++++++ pdf size=" + pdfFile.length());
		
		return pdfFile;
	}
	
	
}
