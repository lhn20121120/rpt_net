
package com.cbrc.smis.servlet;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.cbrc.smis.adapter.StrutsReportInDelegate;
import com.cbrc.smis.adapter.StrutsReportInInfoDelegate;
import com.cbrc.smis.common.Config;
import com.cbrc.smis.common.DownLoadDataToZip;
import com.cbrc.smis.excel.CreateExcel;
import com.cbrc.smis.form.ReportInForm;
import com.cbrc.smis.form.ReportInInfoForm;
import com.cbrc.smis.security.Operator;
import com.cbrc.smis.util.FitechUtil;
import com.fitech.gznx.po.AfTemplate;
import com.fitech.gznx.service.AFTemplateDelegate;
import com.fitech.gznx.service.DayReportDelegate;
import com.fitech.gznx.util.POI2Util;
import com.runqian.report4.model.ReportDefine;
import com.runqian.report4.usermodel.Context;
import com.runqian.report4.usermodel.Engine;
import com.runqian.report4.usermodel.IReport;
import com.runqian.report4.util.ReportUtils;

/**
 * @author jcm
 * 导出子行报表数据
 */
public class CreateSubOrgReportServlet extends HttpServlet {

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
	 * 使用了hibernate技术 卞以刚 2011-12-22
	 * 影响对象：ReportIn AfTemplate
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response) {
        try {        	
        	Operator operator = null; 
    		if(request.getSession().getAttribute(Config.OPERATOR_SESSION_ATTRIBUTE_NAME)!=null)
    			operator = (Operator)request.getSession().getAttribute(Config.OPERATOR_SESSION_ATTRIBUTE_NAME);
    		String srcFileName=Config.TEMP_DIR+File.separator+"export"+operator.getOperatorId();
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
    		Map<String, String> orgShortNameMap = DayReportDelegate.getOrgShortNameMap();//初始化机构ID对应机构简称的Map对象
    		String repInIdString = request.getParameter("repInIds") != null ? request.getParameter("repInIds") : "";
    		
    		/***
    		 * 已使用hibernate 卞以刚 2011-12-27
    		 * 影响对象：ReportIn AfTemplate
    		 */
    		if(repInIdString == null || repInIdString.equals("")){
    			ReportInInfoForm reportInInfoForm = new ReportInInfoForm();
    			
    			reportInInfoForm.setChildRepId(request.getParameter("childRepId") != null ? request.getParameter("childRepId") : null);
    			reportInInfoForm.setRepName(request.getParameter("repName") != null ? request.getParameter("repName") : null);        		
    			reportInInfoForm.setYear(request.getParameter("year") != null ? new Integer(request.getParameter("year")) : null);
    			reportInInfoForm.setTerm(request.getParameter("term") != null ? new Integer(request.getParameter("term")) : null);
    			reportInInfoForm.setOrgId(request.getParameter("orgId") != null ? request.getParameter("orgId") : null);
    			reportInInfoForm.setFrOrFzType(request.getParameter("frOrFzType") != null ? request.getParameter("frOrFzType") : null);
    			reportInInfoForm.setRepFreqId(request.getParameter("repFreqId") != null ? new Integer(request.getParameter("repFreqId")) : null);
    			reportInInfoForm.setCheckFlag(request.getParameter("checkFlag") != null ? new Short(request.getParameter("checkFlag")) : null);
    			
    			/**无特殊oracle语法 不需修改 卞以刚 2011-12-22
    			 * 已使用hibernate
    			 * 影响对象：ReportIn**/
    			List list = StrutsReportInInfoDelegate.searchReportRecord(reportInInfoForm,operator);
    			if(list != null && list.size() >0){
    				for(int i=0;i<list.size();i++){
    					ReportInForm reportInFormTemp = (ReportInForm)list.get(i);
    					
//    					OrgNet orgNet = StrutsOrgNetDelegate.selectOne(reportInFormTemp.getOrgId());
    					String orgId = reportInFormTemp.getOrgId();
    					// orgShortNameMap.get(orgId) 添加机构简称
    					String orgReportPath = srcFileName + File.separator + orgId+ "_" + orgShortNameMap.get(orgId);    
    					
    					if(!map.containsKey(orgId)) {
                			map.put(orgId,orgReportPath);
                			File orgReportFileFolder = new File(orgReportPath);
                			orgReportFileFolder.mkdir();
                		}
                		else
                			orgReportPath = (String)map.get(orgId);
    					// 清单式报表
    					/**已使用hibernate 卞以刚 2011-12-22
    					 * 影响对象：AfTemplate**/
    					AfTemplate aftemplate = AFTemplateDelegate.getTemplate(reportInFormTemp.getChildRepId(),reportInFormTemp.getVersionId());
            			if(aftemplate.getReportStyle() != null && 
        						com.fitech.gznx.common.Config.REPORT_QD.equals(String.valueOf(aftemplate.getReportStyle()))){
            				String raqFileName =  Config.RAQ_TEMPLATE_PATH +"templateFiles" + File.separator + "printRaq"+ 
    						File.separator+"qdfile"+ File.separator+reportInFormTemp.getChildRepId() + "_" + reportInFormTemp.getVersionId() + ".raq";
            				
            				try {
            					ReportDefine rd = (ReportDefine)ReportUtils.read(raqFileName);
                				Context cxt = new Context();  //构建报表引擎计算环境
                				cxt.setParamValue("RepID", reportInFormTemp.getRepInId());
            
                				Engine engine = new Engine(rd, cxt);  //构造报表引擎            				
                				IReport iReport = engine.calc();  //运算报表
                				String excelFileName = orgReportPath +  File.separator + 
                				reportInFormTemp.getChildRepId() + "_" + reportInFormTemp.getVersionId() 
            					+ "_" + reportInFormTemp.getDataRangeId() + "_"  + reportInFormTemp.getCurId() + ".xls";
        						ReportUtils.exportToExcel(excelFileName, iReport, false);
        					} catch (Throwable e) {
        						e.printStackTrace();
        					}
        				} else {
        					CreateExcel ce = new CreateExcel(reportInFormTemp.getRepInId());
                			HSSFWorkbook book = ce.createDataReport();
                			// orgShortNameMap.get(orgId) 添加机构简称
                			File file = new File(orgReportPath + File.separator + orgShortNameMap.get(orgId) + "_" +  reportInFormTemp.getChildRepId() + "_" + reportInFormTemp.getVersionId() 
                					+ "_" + reportInFormTemp.getDataRangeId() + "_"  + reportInFormTemp.getCurId() + ".xls");    
                			FileOutputStream fos = new FileOutputStream(file);
                			book.write(fos);  
                			fos.close();
                			//打开导出的Excel，重新计算所有的公式区域并保存
                			POI2Util.excelFormulaEval(file);
        				}
    					
    				}
    			}else{
    				ErrorOutPut(response);
    				return;
    			}
    		/**已使用hibernate 卞以刚 2011-12-22
             * 影响对象：ReportIn*/
    		}else{
    			String[] repInIds = repInIdString.split(Config.SPLIT_SYMBOL_COMMA);
            	
        		for(int i=0;i<repInIds.length;i++){
        			String repInId = repInIds[i].split(":")[0];
            		String orgId = repInIds[i].split(":")[1];
            		
//            		OrgNet orgNet = StrutsOrgNetDelegate.selectOne(orgId);
            		String orgReportPath = srcFileName + File.separator + orgId + "_" + orgShortNameMap.get(orgId);  //新增银行简称--2013/10/25
            		
            		if(!map.containsKey(orgId)) {
            			map.put(orgId,orgReportPath);
            			File orgReportFileFolder = new File(orgReportPath);
            			orgReportFileFolder.mkdir();
            		}
            		else
            			orgReportPath = (String)map.get(orgId);
            		        		
            		ReportInForm reportInForm = StrutsReportInDelegate.getReportIn(new Integer(repInId));
            		
            		// 清单式报表
            		/**已使用hibernate 卞以刚 2011-12-22
            		 * 影响对象：ReportIn**/
					AfTemplate aftemplate = AFTemplateDelegate.getTemplate(reportInForm.getChildRepId(),reportInForm.getVersionId());
        			if(aftemplate.getReportStyle() != null && 
    						com.fitech.gznx.common.Config.REPORT_QD.equals(String.valueOf(aftemplate.getReportStyle()))){

        				String raqFileName =  Config.RAQ_TEMPLATE_PATH +"templateFiles" + File.separator + "printRaq"+ 
						File.separator+"qdfile"+ File.separator+reportInForm.getChildRepId() + "_" + reportInForm.getVersionId() + ".raq";
        				
        				try {
        					ReportDefine rd = (ReportDefine)ReportUtils.read(raqFileName);
            				Context cxt = new Context();  //构建报表引擎计算环境
            				cxt.setParamValue("RepID", reportInForm.getRepInId());
            				
            				Engine engine = new Engine(rd, cxt);  //构造报表引擎            				
            				IReport iReport = engine.calc();  //运算报表
            				String excelFileName = orgReportPath +  File.separator + 
            				reportInForm.getChildRepId() + "_"  + orgShortNameMap.get(orgId)+ "_" + reportInForm.getVersionId()  + "_" +reportInForm.getDataRangeId() + "_"  + reportInForm.getCurId() + ".xls";
    						ReportUtils.exportToExcel(excelFileName, iReport, false);
    					} catch (Throwable e) {
    						e.printStackTrace();
    					}
    				} else {
    					CreateExcel ce = new CreateExcel(reportInForm.getRepInId());
            			HSSFWorkbook book = ce.createDataReport();
            			// orgShortNameMap.get(orgId) 添加机构简称
            			File file = new File(orgReportPath + File.separator + orgShortNameMap.get(orgId)+ "_" + reportInForm.getChildRepId().replaceFirst("HB", "")  + "_" + reportInForm.getVersionId() + "_" + reportInForm.getDataRangeId() + "_"  + reportInForm.getCurId() + ".xls");
            			FileOutputStream fos = new FileOutputStream(file);
            			book.write(fos);  
            			fos.close();
            			//打开导出的Excel，重新计算所有的公式区域并保存
            			POI2Util.excelFormulaEval(file);
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
        out.println("<font color=\"blue\">没有需要导出的报送数据文件!</font>");
        out.close();     
    }
}