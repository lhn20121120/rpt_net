package com.fitech.gznx.servlet;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.cbrc.smis.common.Config;
import com.cbrc.smis.common.DownLoadDataToZip;

import com.cbrc.smis.other.Aditing;
import com.cbrc.smis.security.Operator;
import com.cbrc.smis.util.FitechUtil;
import com.fitech.gznx.common.DateUtil;
import com.fitech.gznx.common.FileUtil;
import com.fitech.gznx.form.AFReportForm;
import com.fitech.gznx.po.AfTemplate;
import com.fitech.gznx.service.AFReportDelegate;
import com.fitech.gznx.service.AFReportProductDelegate;
import com.fitech.gznx.service.AFTemplateDelegate;
import com.fitech.gznx.service.StrutsViewCustom;
import com.fitech.gznx.util.CreateExcel;
import com.fitech.gznx.util.DateFromExcel;
import com.runqian.report4.model.ReportDefine;
import com.runqian.report4.usermodel.Context;
import com.runqian.report4.usermodel.Engine;
import com.runqian.report4.usermodel.IReport;
import com.runqian.report4.util.ReportUtils;
import com.runqian.report4.view.excel.ExcelReport;

public class CreateNXReportServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	/**
	 * ServletContext
	 */
	private ServletContext context = null;

	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		context = config.getServletContext();
	}
	
	/***
	 * 有jdbc技术 需要修改 卞以刚 2011-12-22
	 * 此处修改oracle to_char函数为sqlserver用法 待测试
     * 影响表：VIEW_AF_REPORT af_report VIEW_ORG_REP
     * 影响对象：AfTemplate AfReport MActuRep
     * 卞以刚 2011-12-27
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response) {
        try {        	
        	Operator operator = null; 
        	HttpSession session = request.getSession();
    		if(session.getAttribute(Config.OPERATOR_SESSION_ATTRIBUTE_NAME)!=null)
    			operator = (Operator)session.getAttribute(Config.OPERATOR_SESSION_ATTRIBUTE_NAME);
    		/** 报表选中标志 **/
    		String reportFlg = "0";
    		
    		if (session.getAttribute(Config.REPORT_SESSION_FLG) != null){
    			reportFlg = (String) session.getAttribute(Config.REPORT_SESSION_FLG);
    		}
    		String srcFileName=Config.TEMP_DIR+File.separator+operator.getOrgId();
    		String zipFileName = "REPORTS.zip";
    		
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
    		
    		HashMap map = new HashMap();
    		
    		String repInIdString = request.getParameter("repInIds") != null ? request.getParameter("repInIds") : "";
    		// 全部导出
    		if(repInIdString == null || repInIdString.equals("")){
    			AFReportForm reportInInfoForm = new AFReportForm();
    			
    			reportInInfoForm.setTemplateId(request.getParameter("templateId") != null ? request.getParameter("templateId") : null);
    			reportInInfoForm.setRepName(request.getParameter("repName") != null ? request.getParameter("repName") : null);        		
    			reportInInfoForm.setDate(request.getParameter("date") != null ? request.getParameter("date") : null);
    			
    			reportInInfoForm.setOrgId(request.getParameter("orgId") != null ? request.getParameter("orgId") : null);
    			
    			reportInInfoForm.setRepFreqId(request.getParameter("repFreqId") != null ? request.getParameter("repFreqId") : null);
    			reportInInfoForm.setBak1(request.getParameter("bak1") != null ? request.getParameter("bak1") : null);
    			
    			/**jdbc技术 需要修改 卞以刚 2011-12-22
    			 * 此处修改oracle to_char函数为sqlserver用法 待测试
    			 * 影响表：VIEW_AF_REPORT af_report VIEW_ORG_REP
    			 * 卞以刚 2011-12-27**/
    			List list = AFReportProductDelegate.searchReportRecord(reportInInfoForm,operator,reportFlg);
    			if(list != null && list.size() >0){
    				for(int i=0;i<list.size();i++){
    					Aditing reportInFormTemp = (Aditing)list.get(i);
    					
    					String orgId = reportInFormTemp.getOrgId();
    					

            			String repInId = String.valueOf(reportInFormTemp.getRepInId());
                		
                		if(repInId.equals("0")){
                			String orgReportPath = srcFileName + File.separator + orgId;
                    		
                    		if(!map.containsKey(orgId)) {
                    			map.put(orgId,orgReportPath);
                    			File orgReportFileFolder = new File(orgReportPath);
                    			orgReportFileFolder.mkdir();
                    		}
                    		else
                    			orgReportPath = (String)map.get(orgId);
                			String freqId = String.valueOf(reportInFormTemp.getActuFreqID());
                			String templateId = reportInFormTemp.getChildRepId();
                			String versionId = reportInFormTemp.getVersionId();
                			String curid = String.valueOf(reportInFormTemp.getCurId());
                			String date = request.getParameter("date");
                			String rdate = date.replaceAll("-", "");
                			String raqFileName = Config.RAQ_TEMPLATE_PATH +"templateFiles" + File.separator + "Raq" 
                			+ File.separator +  templateId + "_" + versionId  + ".raq";
            				
            				try {
            					ReportDefine rd = (ReportDefine)ReportUtils.read(raqFileName);
                				Context cxt = new Context();  //构建报表引擎计算环境
                				cxt.setParamValue("OrgID", orgId);
                				cxt.setParamValue("ReptDate", rdate);
                				String rangeID ="";
        		        		if(reportFlg.equals("1")){
        		        			/**已使用Hibernate 卞以刚 2011-12-22
        		        			 * 影响对象：MActuRep**/
        		        			rangeID = StrutsViewCustom.getdatarangeId(templateId,versionId,freqId);
        		        		}else{
        		        			rangeID = "1";
        		        		}

                				cxt.setParamValue("CCY", curid);
                				cxt.setParamValue("RangeID", rangeID);
                				cxt.setParamValue("Freq", Integer.valueOf(freqId).intValue());          
                				Engine engine = new Engine(rd, cxt);  //构造报表引擎            				
                				IReport iReport = engine.calc();  //运算报表
                				String excelFileName = orgReportPath +  File.separator + 
                				templateId+"_"+versionId+"_"+orgId+"_"+curid+"_"+freqId+"_"+rdate + ".xls";
        						ReportUtils.exportToExcel(excelFileName, iReport, false);
        					} catch (Throwable e) {
        						// TODO Auto-generated catch block
        						e.printStackTrace();
        					}
                			
                		}else{
                			
                    		String orgReportPath = srcFileName + File.separator + orgId;
                    		
                    		if(!map.containsKey(orgId)) {
                    			map.put(orgId,orgReportPath);
                    			File orgReportFileFolder = new File(orgReportPath);
                    			orgReportFileFolder.mkdir();
                    		}
                    		else
                    			orgReportPath = (String)map.get(orgId);
                    		String templateId = "";
                    		String versionId = "";
                    		String dataRangeId = "";
                    		String curId = "";
                    		
                			templateId = reportInFormTemp.getChildRepId();
                			versionId = reportInFormTemp.getVersionId();
                			dataRangeId = "1";
                			curId = String.valueOf(reportInFormTemp.getCurId());
                			
                			if(reportInFormTemp.getReportStyle() != null && 
            						com.fitech.gznx.common.Config.REPORT_QD.equals(String.valueOf(reportInFormTemp.getReportStyle()))){
                				
                				String raqFileName =  Config.RAQ_TEMPLATE_PATH +"templateFiles" + File.separator + "printRaq"+ 
        						File.separator+"qdfile"+ 
        						File.separator+templateId + "_" + versionId + ".raq";
                				try {
                					ReportDefine rd = (ReportDefine)ReportUtils.read(raqFileName);
                    				Context cxt = new Context();  //构建报表引擎计算环境
                    				cxt.setParamValue("RepID", repInId);
                
                    				Engine engine = new Engine(rd, cxt);  //构造报表引擎            				
                    				IReport iReport = engine.calc();  //运算报表
                    				String excelFileName = orgReportPath +  File.separator + 
                    				templateId + "_" + versionId + "_" + orgId + "_"+dataRangeId + "_"  + curId + ".xls";
            						//ReportUtils.exportToExcel(excelFileName, iReport, false);
                    				ExcelReport eReport = new ExcelReport(); 
            						eReport.export(iReport); 
            						eReport.saveTo(excelFileName);
            					} catch (Throwable e) {
            						
            						e.printStackTrace();
            					}
            				} else {
                				String raqFileName =  Config.RAQ_TEMPLATE_PATH +"templateFiles" 
		        					+ File.separator + "Raq" + File.separator
		        					+ reportInFormTemp.getChildRepId() + "_" + reportInFormTemp.getVersionId() + ".raq";
        				
	            				String objDate = DateUtil.getMonthLast(reportInFormTemp.getYear()+"-"
	        															+reportInFormTemp.getTerm() + "-01");
	        				
		        				String excelFileName = "";
		        				try {
		        					ReportDefine rd = (ReportDefine)ReportUtils.read(raqFileName);
		            				Context cxt = new Context();  //构建报表引擎计算环境
		            				cxt.setParamValue("RepID", reportInFormTemp.getRepInId());
		            				cxt.setParamValue("ReptDate", objDate.replace("-", ""));
		            				cxt.setParamValue("OrgID", reportInFormTemp.getOrgId());
		            				cxt.setParamValue("Freq", reportInFormTemp.getActuFreqID());
		            				
		            				Engine engine = new Engine(rd, cxt);  //构造报表引擎            				
		            				IReport iReport = engine.calc();  //运算报表
		            				excelFileName = orgReportPath +  File.separator + 
			            				reportInFormTemp.getChildRepId() + "_" + reportInFormTemp.getVersionId() + "_" +
			            				reportInFormTemp.getActuFreqID() + "_"  + reportInFormTemp.getCurId() + ".xls";
		    						ReportUtils.exportToExcel(excelFileName, iReport, false);
		    					} catch (Throwable e) {
		    						e.printStackTrace();
		    					}
								
		    					CreateExcel ce = new CreateExcel(Integer.valueOf(repInId));
		            			HSSFWorkbook book = ce.createDataReport(reportFlg,excelFileName);
		            			
		            			File file = new File(excelFileName);
		            			FileOutputStream fos = new FileOutputStream(file);
		            			book.write(fos);  
		            			fos.close();
            				}
                		}
                	
    				}
    			}
    			
    			
    			
    			
    		} else {
    			// 批量导出
    			String[] repInIds = repInIdString.split(Config.SPLIT_SYMBOL_COMMA);
            	
        		for(int i=0;i<repInIds.length;i++){
        			String repInId = repInIds[i].split(":")[0];
            		String orgId = repInIds[i].split(":")[1];
            		if(repInId.equals("0")){
            			String orgReportPath = srcFileName + File.separator + orgId;
                		
                		if(!map.containsKey(orgId)) {
                			map.put(orgId,orgReportPath);
                			File orgReportFileFolder = new File(orgReportPath);
                			orgReportFileFolder.mkdir();
                		}
                		else
                			orgReportPath = (String)map.get(orgId);
            			String freqId = repInIds[i].split(":")[2];
            			String templateId = repInIds[i].split(":")[3];
            			String versionId = repInIds[i].split(":")[4];
            			String curid = repInIds[i].split(":")[5];
            			String date = request.getParameter("date");
            			String rdate = date.replaceAll("-", "");
            			String raqFileName = Config.RAQ_TEMPLATE_PATH +"templateFiles" + File.separator + "Raq" 
            			+ File.separator +  templateId + "_" + versionId  + ".raq";
        				
        				try {
        					ReportDefine rd = (ReportDefine)ReportUtils.read(raqFileName);
            				Context cxt = new Context();  //构建报表引擎计算环境
            				cxt.setParamValue("OrgID", orgId);
            				cxt.setParamValue("ReptDate", rdate);
            				String rangeID ="";
    		        		if(reportFlg.equals("1")){
    		        			/**已使用hibernate  卞以刚 2011-12-21
    		        			 * 影响对象：MActuRep**/
    		        			rangeID = StrutsViewCustom.getdatarangeId(templateId,versionId,freqId);
    		        		}else{
    		        			rangeID = "1";
    		        		}

            				cxt.setParamValue("CCY", curid);
            				cxt.setParamValue("RangeID", rangeID);
            				cxt.setParamValue("Freq", Integer.valueOf(freqId).intValue());          
            				Engine engine = new Engine(rd, cxt);  //构造报表引擎            				
            				IReport iReport = engine.calc();  //运算报表
            				String excelFileName = orgReportPath +  File.separator + 
            				templateId+"_"+versionId+"_"+orgId+"_"+curid+"_"+freqId+"_"+rdate + ".xls";
    						ReportUtils.exportToExcel(excelFileName, iReport, false);
    					} catch (Throwable e) {
    						// TODO Auto-generated catch block
    						e.printStackTrace();
    					}
            			
            		}else{
            			
                		String orgReportPath = srcFileName + File.separator + orgId;
                		
                		if(!map.containsKey(orgId)) {
                			map.put(orgId,orgReportPath);
                			File orgReportFileFolder = new File(orgReportPath);
                			orgReportFileFolder.mkdir();
                		}
                		else
                			orgReportPath = (String)map.get(orgId);
                		String templateId = "";
                		String versionId = "";
                		String dataRangeId = "";
                		String curId = "";
                		
                		/**已使用hibernate 卞以刚 2011-12-22
                		 * 影响对象：AfReport**/
            			AFReportForm reportInForm = AFReportDelegate.getReportIn(repInId);
            			templateId = reportInForm.getTemplateId();
            			versionId = reportInForm.getVersionId();
            			dataRangeId = "1";
            			curId = String.valueOf(reportInForm.getCurId());
            			/**已使用hibernate 卞以刚 2011-12-21
            			 * 影响对象：AfTemplate**/
            			AfTemplate aftemplate = AFTemplateDelegate.getTemplate(templateId,versionId);
            			if(aftemplate.getReportStyle() != null && 
        						com.fitech.gznx.common.Config.REPORT_QD.equals(String.valueOf(aftemplate.getReportStyle()))){
            					String raqFileName =  Config.RAQ_TEMPLATE_PATH +"templateFiles" + File.separator + "printRaq"+ 
        						File.separator+"qdfile"+ File.separator+templateId + "_" + versionId + ".raq";
                				try {
                					ReportDefine rd = (ReportDefine)ReportUtils.read(raqFileName);
                    				Context cxt = new Context();  //构建报表引擎计算环境
                    				cxt.setParamValue("RepID", repInId);
                
                    				Engine engine = new Engine(rd, cxt);  //构造报表引擎            				
                    				IReport iReport = engine.calc();  //运算报表
                    				String excelFileName = orgReportPath +  File.separator + 
                    				templateId + "_" + versionId + "_" + orgId + "_"+ dataRangeId + "_"  + curId + ".xls";
//            						ReportUtils.exportToExcel(excelFileName, iReport, true);
            						ExcelReport eReport = new ExcelReport(); 
            						eReport.export(iReport); 
            						eReport.saveTo(excelFileName);
            					} catch (Throwable e) {
            						// TODO Auto-generated catch block
            						e.printStackTrace();
            					}
//            				}            				
        				} else {
            				String raqFileName =  Config.RAQ_TEMPLATE_PATH +"templateFiles" 
	        					+ File.separator + "Raq" + File.separator
	        					+ reportInForm.getTemplateId() + "_" + reportInForm.getVersionId() + ".raq";
				
	        				String objDate = DateUtil.getMonthLast(reportInForm.getYear()+"-"
	    															+reportInForm.getTerm() + "-01");
	    				
	        				String excelFileName = "";
	        				try {
	        					ReportDefine rd = (ReportDefine)ReportUtils.read(raqFileName);
	            				Context cxt = new Context();  //构建报表引擎计算环境
	            				cxt.setParamValue("RepID", reportInForm.getRepId());
	            				cxt.setParamValue("ReptDate", objDate.replace("-", ""));
	            				cxt.setParamValue("OrgID", reportInForm.getOrgId());
	            				cxt.setParamValue("Freq", Integer.valueOf(reportInForm.getRepFreqId()));
	            				
	            				Engine engine = new Engine(rd, cxt);  //构造报表引擎            				
	            				IReport iReport = engine.calc();  //运算报表
	            				excelFileName = orgReportPath +  File.separator + 
		            				reportInForm.getTemplateId() + "_" + reportInForm.getVersionId() + "_" +
		            				reportInForm.getRepFreqId() + "_"  + reportInForm.getCurId() + ".xls";
	    						ReportUtils.exportToExcel(excelFileName, iReport, false);
	    					} catch (Throwable e) {
	    						e.printStackTrace();
	    					}
							
	    					CreateExcel ce = new CreateExcel(Integer.valueOf(reportInForm.getRepId()));
	            			HSSFWorkbook book = ce.createDataReport(reportFlg,excelFileName);
	            			
	            			File file = new File(excelFileName);
	            			FileOutputStream fos = new FileOutputStream(file);
	            			book.write(fos);  
	            			fos.close();
        				}
            		}
            	}
    		}
        	
    		boolean bool = false;
        	dldtZip.gzip(srcFileName,bool);
        	
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
			
			//删除临时文件
			FileUtil.deleteFile(outfile);
			FileUtil.deleteFile(srcFileName + ".zip");
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
        out.println("<font color=\"blue\">没有需要导出的数据文件!</font>");
        out.close();     
    }
}
