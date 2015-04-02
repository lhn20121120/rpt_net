
package com.cbrc.smis.servlet;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.cbrc.smis.adapter.StrutsReportInDelegate;
import com.cbrc.smis.common.Config;
import com.cbrc.smis.common.DownLoadDataToZip;
import com.cbrc.smis.excel.CreateExcel;
import com.cbrc.smis.form.ReportInForm;
import com.cbrc.smis.security.Operator;
import com.cbrc.smis.util.FitechUtil;
import com.fitech.net.adapter.StrutsCollectTypeDelegate;
import com.fitech.net.form.CollectTypeForm;

/**
 * @author jcm
 * 下载以汇总方式汇总的报表
 */
public class DownLoadOtherCollectServlet extends HttpServlet {
	
	/**
	 * ServletContext
	 */
	private ServletContext context = null;

	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		context = config.getServletContext();
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response) {	
		HttpSession session = request.getSession();
		Operator operator = null; 
		if(session.getAttribute(Config.OPERATOR_SESSION_ATTRIBUTE_NAME)!=null)
			operator = (Operator)session.getAttribute(Config.OPERATOR_SESSION_ATTRIBUTE_NAME);
		
		CollectTypeForm collectTypeForm = null;
        try {        	
        	String srcFileName = Config.WEBROOTPATH + "otherCollectReport";
        	String zipFileName = "otherCollectReport.zip";
        	
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
        		
        		for(int i=0;i<repInIdsArr.length;i++){
    				ReportInForm reportInForm = StrutsReportInDelegate.getReportIn(new Integer(repInIdsArr[i]));
    				collectTypeForm = StrutsCollectTypeDelegate.selectCollectType(reportInForm.getPackage());
        			String collectName = collectTypeForm != null ? collectTypeForm.getCollectName() : "" + reportInForm.getPackage();
        			
    				CreateExcel ce = new CreateExcel(reportInForm.getRepInId());
        			HSSFWorkbook book = ce.createDataReport();
        			
        			File file = new File(filePath + reportInForm.getChildRepId() + "_" + reportInForm.getVersionId() + "_" + reportInForm.getDataRangeId() + "_" + reportInForm.getCurId() + "_" + collectName + ".xls");
        			FileOutputStream fos = new FileOutputStream(file);
        			book.write(fos);  
    			}        		
        	}else{
        		String year = request.getParameter("year") != null ? request.getParameter("year") : null;
            	String term = request.getParameter("term") != null ? request.getParameter("term") : null;
            	String repName = request.getParameter("repName") != null ? request.getParameter("repName") : null;
            	
            	ReportInForm reportInFormParam = new ReportInForm();
            	if(year != null && !year.trim().equals("")) reportInFormParam.setYear(new Integer(year));
            	if(term != null && !term.trim().equals("")) reportInFormParam.setTerm(new Integer(term));
            	if(repName != null && !repName.trim().equals("")) reportInFormParam.setRepName(repName);
            	if(operator != null && operator.getOrgId() != null) reportInFormParam.setOrgId(operator.getOrgId());
            	
            	List reportInFormList = StrutsReportInDelegate.getOtherCollectRecords(reportInFormParam);
            	
            	if(reportInFormList == null || reportInFormList.size() <= 0){
            		ErrorOutPut(response);
            		return;
            	}
            	
            	for(int i=0;i<reportInFormList.size();i++){
        			ReportInForm reportInForm = (ReportInForm)reportInFormList.get(i);
        			collectTypeForm = StrutsCollectTypeDelegate.selectCollectType(reportInForm.getPackage());
        			String collectName = collectTypeForm != null ? collectTypeForm.getCollectName() : "" + reportInForm.getPackage();
        			
        			CreateExcel ce = new CreateExcel(reportInForm.getRepInId());
        			HSSFWorkbook book = ce.createDataReport();
        			
        			File file = new File(filePath + reportInForm.getChildRepId() + "_" + reportInForm.getVersionId() + "_" + reportInForm.getDataRangeId() + "_" + reportInForm.getCurId() + "_" + collectName + ".xls");
        			FileOutputStream fos = new FileOutputStream(file);
        			book.write(fos);       			
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
				
			int len;
			byte[] buffer = new byte[100];
				
			while((len = inStream.read(buffer)) > 0){
				response.getOutputStream().write(buffer,0,len);
			}
			inStream.close();
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