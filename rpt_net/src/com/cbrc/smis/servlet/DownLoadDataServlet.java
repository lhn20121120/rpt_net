/*
 * Created on 2005-12-21
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.cbrc.smis.servlet;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.sql.Blob;
import java.util.List;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.cbrc.smis.adapter.StrutsReportInDataDelegate;
import com.cbrc.smis.adapter.StrutsReportInDelegate;
import com.cbrc.smis.common.Config;
import com.cbrc.smis.common.DownLoadDataToZip;
import com.cbrc.smis.excel.CreateExcel;
import com.cbrc.smis.form.ReportInForm;
import com.cbrc.smis.hibernate.ReportInData;
import com.cbrc.smis.security.Operator;
import com.cbrc.smis.util.FitechUtil;

/**
 * @author jcm 
 * 
 * 生成数据文件
 */
public class DownLoadDataServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	/**
	 * ServletContext
	 */
	private ServletContext context = null;

	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		this.context = config.getServletContext();
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response) {
		HttpSession session = request.getSession();
		Operator operator = null; 
		//判断是否需要加密
		String encrypt=request.getParameter("encrypt");
		if(session.getAttribute(Config.OPERATOR_SESSION_ATTRIBUTE_NAME)!=null)
			operator = (Operator)session.getAttribute(Config.OPERATOR_SESSION_ATTRIBUTE_NAME);
		
        try {
        	String srcFileName = Config.TEMP_DIR+operator.getOrgId()+ "dataReport";
        	String zipFileName = "dataReport.zip";
        	
        	DownLoadDataToZip dldtZip = DownLoadDataToZip.newInstance();	
        	if(dldtZip.isTemplateZipExist(srcFileName)){
    			dldtZip.deleteFolder(new File(srcFileName));
    			File zipFile = new File(srcFileName + ".zip");
    			if(zipFile.exists()) zipFile.delete();
    		}
        	
        	File outfile = new File(srcFileName);
    		
    		if(outfile.exists())
    			dldtZip.deleteFolder(outfile);
    		outfile.mkdir();
    		
    		String filePath = srcFileName + File.separator;
    		    		
        	String repInIds = request.getParameter("repInIds") != null ? request.getParameter("repInIds") : null;
        	if(repInIds != null) {
        		String[] repInIdsArr = repInIds.split(",");
        		if(repInIdsArr == null || repInIdsArr.length <= 0){
        			ErrorOutPut(response);
            		return;
        		}
        		
        		if(Config.DATATYPE.equals(Config.EXCEL)){
        			for(int i=0;i<repInIdsArr.length;i++){
        				ReportInForm reportInForm = StrutsReportInDelegate.getReportIn(new Integer(repInIdsArr[i]));
        				CreateExcel ce = new CreateExcel(reportInForm.getRepInId());
            			HSSFWorkbook book = ce.createDataReport();
            			
            			String excelFilePath=filePath + reportInForm.getChildRepId() + "_" + reportInForm.getVersionId() + "_" + reportInForm.getDataRangeId() + "_" + reportInForm.getCurId() + ".xls";
            			File file = new File(excelFilePath);
            			FileOutputStream fos = new FileOutputStream(file);
            			book.write(fos);  
            			fos.close();
            			//该地方添加加密程序
            			if(encrypt!=null&&encrypt.equals(Config.ENCRYPT.toString())&&Config.ENCRYPT.equals(new Integer(1))){
            				try {
            					FitechUtil.encryptFile(excelFilePath, excelFilePath.replaceAll(".xls", ".fit"));
            					file.delete();
            				} catch (Exception e) {
            					e.printStackTrace();
            				} 
            				
            			}
        			}
        		}else if(Config.DATATYPE.equals(Config.XML)){
        			for(int i=0;i<repInIdsArr.length;i++){
            			ReportInForm reportInForm = StrutsReportInDelegate.getReportIn(new Integer(repInIdsArr[i]));
            			ReportInData reportInData = StrutsReportInDataDelegate.getReportInData(reportInForm.getRepInId());
            			if(reportInData == null) continue;
            			
            			try{
            				File reportDataFile = new File(filePath + reportInForm.getChildRepId() + "_" + reportInForm.getVersionId() + "_" + reportInForm.getCurId() + ".xml");
                			reportDataFile.createNewFile();
                			Blob blob = reportInData.getXml();
                			if(blob != null){
                				int len = 0;
                				InputStream inStream = blob.getBinaryStream();
                				byte[] buffer = new byte[inStream.available()];
                				
                				FileOutputStream outStream = null;
                				while((len = inStream.read(buffer)) > 0){
                					outStream = new FileOutputStream(reportDataFile);
                					outStream.write(buffer,0,len);
                				}
                				outStream.close();
                				inStream.close();
                			}
            			}catch(IOException ex){
            				ex.printStackTrace();
            			}
            		}
        		}
        	}else{
        		ReportInForm reportInFormParam = new ReportInForm();
        		reportInFormParam.setChildRepId(request.getParameter("childRepId") != null ? request.getParameter("childRepId") : null);
        		/**若是WebLogic则需要先进行转码*/
        		reportInFormParam.setRepName(request.getParameter("repName")!=null ? new String(request.getParameter("repName").getBytes("iso-8859-1"),"gb2312") : null);
        		/**若是WebSphere则不要进行转码*/
        		//reportInFormParam.setRepName(request.getParameter("repName") != null ? request.getParameter("repName") : null);
        		reportInFormParam.setYear(request.getParameter("year") != null ? new Integer(request.getParameter("year")) : null);
        		reportInFormParam.setTerm(request.getParameter("term") != null ? new Integer(request.getParameter("term")) : null);
        		reportInFormParam.setFrOrFzType(request.getParameter("frOrFzType") != null ? request.getParameter("frOrFzType") : null);
        		reportInFormParam.setRepFreqId(request.getParameter("repFreqId") != null ? new Integer(request.getParameter("repFreqId")) : null);

            	List reportInFormList = StrutsReportInDelegate.getDataRecords(reportInFormParam,operator);
            	if(reportInFormList == null || reportInFormList.size() <= 0){
            		ErrorOutPut(response);
            		return;
            	}
            	
            	if(Config.DATATYPE.equals(Config.EXCEL)){
            		
            		for(int i=0;i<reportInFormList.size();i++){
            			ReportInForm reportInForm = (ReportInForm)reportInFormList.get(i);
            			CreateExcel ce = new CreateExcel(reportInForm.getRepInId());
            			HSSFWorkbook book = ce.createDataReport();
            			
            			String excelFilePath=filePath + reportInForm.getChildRepId() + "_" + reportInForm.getVersionId() + "_" + reportInForm.getDataRangeId() + "_" + reportInForm.getCurId() + ".xls";
            			File file = new File(excelFilePath);
            			FileOutputStream fos = new FileOutputStream(file);
            			book.write(fos);  
            			fos.close();
            			//该地方添加加密程序
            			if(encrypt!=null&&encrypt.equals(Config.ENCRYPT.toString())&&Config.ENCRYPT.equals(new Integer(1))){           			
            		    	try {
            					FitechUtil.encryptFile(excelFilePath, excelFilePath.replaceAll(".xls", ".fit"));
            					file.delete();
            				} catch (Exception e) {
            					e.printStackTrace();
            				} 
            				
            			}
            		}        		
            	}else if(Config.DATATYPE.equals(Config.XML)){
            		
            		for(int i=0;i<reportInFormList.size();i++){
            			ReportInForm reportInForm = (ReportInForm)reportInFormList.get(i);
            			ReportInData reportInData = StrutsReportInDataDelegate.getReportInData(reportInForm.getRepInId());
            			if(reportInData == null) continue;
            			
            			try{
            				File reportDataFile = new File(filePath + reportInForm.getChildRepId() + "_" + reportInForm.getVersionId() + "_" + reportInForm.getCurId() + ".xml");
                			reportDataFile.createNewFile();
                			Blob blob = reportInData.getXml();
                			if(blob != null){
                				int len = 0;
                				InputStream inStream = blob.getBinaryStream();
                				byte[] buffer = new byte[inStream.available()];
                				
                				FileOutputStream outStream = null;
                				while((len = inStream.read(buffer)) > 0){
                					outStream = new FileOutputStream(reportDataFile);
                					outStream.write(buffer,0,len);
                				}
                				outStream.close();
                				inStream.close();
                			}
            			}catch(IOException ex){
            				ex.printStackTrace();
            			}
            		}
            	}
        	}
        	
        	dldtZip.gzip(srcFileName);
        	
			response.reset();
			response.setContentType("application/x-zip-compressed;name=\"" + zipFileName + "\""); 
			response.addHeader("Content-Disposition",
					"attachment; filename=\""
								+ FitechUtil.toUtf8String(zipFileName) + "\"");
			response.setHeader("Accept-ranges", "bytes");
								
			FileInputStream inStream = new FileInputStream(srcFileName + ".zip");
			OutputStream out = response.getOutputStream();
			
			int len;
			byte[] buffer = new byte[100];
				
			while((len = inStream.read(buffer)) > 0){
				out.write(buffer,0,len);
			}
			inStream.close();
			out.close();
		}catch (Exception e){
            e.printStackTrace();
            ErrorOutPut(response);
		}

	}
	
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		this.doGet(request,response);
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
        out.println("<font color=\"blue\">没有生成数据文件的报表!</font>");
        out.close();     
    }
}