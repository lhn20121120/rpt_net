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
	 * @param request HttpServletRequest ��ǰrequest����
	 * @param files File[] �����ļ�����
	 * @param zipName String ����γɵ��ļ���
	 * @return url String �����zip�ļ�url·��
	 * @function ���pdf��form_model_web.txt��view_tree_node.txt�ļ���zipѹ����������downloadĿ¼
	 */
	public String zipURL(HttpServletRequest request, File[] files, String zipName) {
		// �ļ�����·�����������ļ���
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

		// �����ɵĴ���ļ�д��ģ������Ŀ¼��������
		try {
			InputStream in = new FileInputStream(zipFile);			
			FileOutputStream out = new FileOutputStream(absPath + zipName, true); // zipFile�������Ѿ��жϹ��������ж�
			FileDescriptor fd = out.getFD();

			int ch = 0;
			long count = 0;
			/**
			 * TODO: ���ж�-1����ѭ������ʱ�Ӹ����ޡ��Ժ�����ԭ��
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
		
		// zip�ļ���URL
		String url = "http://" + request.getServerName() + ":" + request.getServerPort()
		           + "/" + Config.OUTER_SYSTEM_NAME + "/" + Config.DOWNLOAD_DIR
		           + "/" + zipName;

		return url;
	}
	
	/**
	 * @author James
	 *  
	 * @param childRepId String �ӱ���ID
	 * @param versionId String �汾�� 
	 * @return pdfFile File PDFģ���ļ�
	 * @function ���ݻ���ID���ӱ���ID�Ͱ汾�Ż�ȡPDFģ���ļ�
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
