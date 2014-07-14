package com.fitech.gznx.servlet;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.hssf.util.Region;





import com.cbrc.smis.common.Config;
import com.cbrc.smis.common.DownLoadDataToZip;
import com.cbrc.smis.other.Aditing;
import com.cbrc.smis.security.Operator;
import com.cbrc.smis.util.FitechUtil;
import com.fitech.gznx.common.DateUtil;
import com.fitech.gznx.common.FileUtil;
import com.fitech.gznx.common.StringUtil;
import com.fitech.gznx.form.AFReportForm;
import com.fitech.gznx.po.AfTemplate;
import com.fitech.gznx.service.AFReportDealDelegate;
import com.fitech.gznx.service.AFReportProductDelegate;
import com.fitech.gznx.service.AFTemplateDelegate;
import com.runqian.report4.model.ReportDefine;
import com.runqian.report4.usermodel.Context;
import com.runqian.report4.usermodel.Engine;
import com.runqian.report4.usermodel.IReport;
import com.runqian.report4.util.ReportUtils;

public class DownloadALLReportServlet extends HttpServlet {
	
	/**
	 * ServletContext
	 */
	private ServletContext context = null;

	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		context = config.getServletContext();
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response) {
long time1=System.currentTimeMillis();
		HttpSession session = request.getSession();
		Operator operator = null;
		if(session.getAttribute(Config.OPERATOR_SESSION_ATTRIBUTE_NAME)!=null)
			operator = (Operator)session.getAttribute(Config.OPERATOR_SESSION_ATTRIBUTE_NAME);
		/** 报表选中标志 **/
		String reportFlg = "0";
		
		
		if (session.getAttribute(Config.REPORT_SESSION_FLG) != null){
			reportFlg = (String) session.getAttribute(Config.REPORT_SESSION_FLG);
		}
		
		// 创建临时文件存放空间
		
		String pathName = "templateExcel";
				
		String srcFileName = Config.WEBROOTPATH  + "temp" + Config.FILESEPARATOR + pathName;
		
		String zipFileName = pathName + ".zip";

		//打包
		DownLoadDataToZip dldtZip = DownLoadDataToZip.newInstance();
		if (dldtZip.isTemplateZipExist(srcFileName)) {
			dldtZip.deleteFolder(new File(srcFileName));
			File zipFile = new File(srcFileName + ".zip");
			if (zipFile.exists())
				zipFile.delete();
		}

		File outfile = new File(srcFileName);

		if (outfile.exists())
			dldtZip.deleteFolder(outfile);
		outfile.mkdir();
		
		List fileList = null;

		try{
			String repNames = request.getParameter("repNames") != null ? request.getParameter("repNames") : null;	
			String type = request.getParameter("type") != null ? request.getParameter("type") : "";	
			
			String date = request.getParameter("date") != null ? request.getParameter("date") : "";			
			
    		String orgId = request.getParameter("orgId") != null ? request.getParameter("orgId") : "";
    		String repFreqId = request.getParameter("repFreqId") != null ? request.getParameter("repFreqId") : "";
    		
    		//多个excel合成一个excel
    		Boolean toaexcel = Boolean.valueOf(request.getParameter("toaexcel"));
     		// 如果type不等于downAll 就不是下载全部
			if(!type.equals("downAll")){
	        	if(repNames != null && !repNames.equals("")){
	        		String[] repNameArr = repNames.split(",");
	        		if(repNameArr != null && repNameArr.length > 0){

	        			/**复选下载*/
	        			fileList = new ArrayList();        				
        				for(int i=0;i<repNameArr.length;i++){
        					String[] repInfo = repNameArr[i].split("_");
        					Map report = new HashMap();
        					try{
        						report.put("templateid", repInfo[0]);
        						report.put("versionod", repInfo[1]);
        						if(StringUtil.isEmpty(repInfo[2])){
        							report.put("dataRgId", "1");
        						} else{
        							report.put("dataRgId", repInfo[2]);
        						}
        						
        						report.put("freqID", repInfo[3]);
        						report.put("curId", repInfo[4]);
        						report.put("orgId", repInfo[5]);

        					}catch(Exception e){
        						e.printStackTrace();
        					}
        					String raqFileName = Config.RAQ_TEMPLATE_PATH +"templateFiles" + File.separator + "Raq"+ 
        					File.separator+repInfo[0]+"_"+repInfo[1]  + ".raq";
            				File file = new File(raqFileName);
            				if(!file.exists()){            					
            					continue;                  					
            				}
            				report.put("file", file);
            				fileList.add(report);
            			} 
	        		}
	        	}
			}else{
				String repName = request.getParameter("repName");
				if( repName != null){
//					repName = new String(repName.getBytes("ISO-8859-1"),"GB2312");
				}
				String templateType = request.getParameter("templateType") != null ? request.getParameter("templateType") : "";	
				AFReportForm reportInForm = new AFReportForm();
				reportInForm.setOrgId(orgId);            	
				reportInForm.setDate(date);
				reportInForm.setRepName(repName);
				reportInForm.setTemplateType(templateType);
				reportInForm.setRepFreqId(repFreqId);
            	List list = null;
            	// 银监会报表
            	if(reportFlg.equals(com.fitech.gznx.common.Config.CBRC_REPORT)){
            		list = AFReportProductDelegate.selectYJHReportList(reportInForm,operator);
            	} else {
            		list = AFReportProductDelegate.selectNOTYJHReportList(reportInForm,operator,reportFlg);
            	}
            	// 取得模板生成条件参数
            	if(list != null && list.size() > 0){
            		fileList = new ArrayList();
            		for(int i=0;i<list.size();i++){
            			Aditing aditing = (Aditing)list.get(i);
            			Map report = new HashMap();
    					try{
    						report.put("templateid", aditing.getChildRepId());
    						report.put("versionod", aditing.getVersionId());
    						report.put("dataRgId", aditing.getDataRangeId());
    						report.put("freqID", aditing.getActuFreqID());
    						report.put("curId", aditing.getCurId());
    						report.put("orgId", aditing.getOrgId());

    					}catch(Exception e){
    						e.printStackTrace();
    					}
            			String raqFileName = Config.RAQ_TEMPLATE_PATH +"templateFiles" + File.separator + "Raq" 
            			+ File.separator +  aditing.getChildRepId() + "_" + aditing.getVersionId()  + ".raq";
            			File file = new File(raqFileName);
            			if(!file.exists()){                			
            				continue;            				
            			}
            			
            			report.put("file", file);
        				fileList.add(report);
            		}
            	}
            	
			}
			if(fileList == null || fileList.size() <= 0){ 
				ErrorOutPut(response,repNames);
				return;				
        	}
			HashMap map = new HashMap();
			
			String orgReportPath = srcFileName + File.separator + orgId;    
			
			// 生成excel文件
			for(int i=0;i<fileList.size();i++){
    			Map reportMap = (Map)fileList.get(i); 
    			InputStream inStream = null;
    			try{
    				String objDate = DateUtil.getFreqDateLast(date,Integer.valueOf(String.valueOf(reportMap.get("freqID"))));
    				
    				String templateid = (String) reportMap.get("templateid");
    				String versionod = (String) reportMap.get("versionod");
    				
    				String strorgId = (String) reportMap.get("orgId");
    				String curid = String.valueOf(reportMap.get("curId"));
    				String freqId = String.valueOf(reportMap.get("freqID"));
    				//生成存放报表的文件夹
            		if(!map.containsKey(strorgId)) {
	        			map.put(strorgId,orgReportPath);
	        			File orgReportFileFolder = new File(orgReportPath);
	        			orgReportFileFolder.mkdir();
	        		}
	        		else
	        			orgReportPath = (String)map.get(strorgId);
            		//拼接导出Excel的全路径信息
    				String xlsName=templateid+"_"+versionod+"_"+strorgId+"_"+curid+"_"+freqId+"_"+objDate.replace("-", "") + ".xls";
    				String excelFileName = orgReportPath + File.separator +xlsName;//下载Excel文件全路径
            		String excelProductReportFileName = Config.WEBROOTPATH +"reportFiles" + File.separator +xlsName;//ProductReport.java生成的Excel文件
            		//LuYueFei:2014-01-05
        			//因为生成报表时，要计算两次润乾报表，计算第一次>>保存数据入库，计算第二次>>生成用户使用的Excel文件
        			//优化方式：将第一次生成的Excel文件直接剪切文件至下载目录中，减少不必要的第二次计算，提高下载速度
            		File productReportExcel=new File(excelProductReportFileName);
            		if(productReportExcel.exists()){//判断第一次生成的Excel文件是否存在，如果存在，则直接剪切
            			productReportExcel.renameTo(new File(excelFileName));
            			File file = (File) reportMap.get("file");
            			AFReportDealDelegate.resoveExcelFormaule(excelFileName,file.getPath(),templateid);//生成excel时加上公式
            		}else{//如果不存在则再次生成
    				File file = (File) reportMap.get("file");
    				inStream = new FileInputStream(file);
    				ReportDefine rd = (ReportDefine)ReportUtils.read(inStream);
    				Context cxt = new Context();  //构建报表引擎计算环境
    				cxt.setParamValue("OrgID", strorgId);
    				cxt.setParamValue("ReptDate", objDate.replace("-", ""));
//    				String curid = String.valueOf(reportMap.get("curId"));//LuYueFei注释:2014-01-05
//    				String freqId = String.valueOf(reportMap.get("freqID"));//LuYueFei注释:2014-01-05
    				
    				cxt.setParamValue("CCY", curid);

    				cxt.setParamValue("RangeID", reportMap.get("dataRgId"));
    				cxt.setParamValue("Freq", Integer.valueOf(freqId).intValue());
    				AfTemplate aftemplate = AFTemplateDelegate.getTemplate(templateid,versionod);
    				if(aftemplate.getReportStyle() != null && 
    						com.fitech.gznx.common.Config.REPORT_QD.equals(String.valueOf(aftemplate.getReportStyle()))){
    					reportMap.put("QDFlg",  com.fitech.gznx.common.Config.PROFLG_SENCEN_QD);
    				}
    				
    				        		
//            		if(!map.containsKey(strorgId)) {
//            			map.put(strorgId,orgReportPath);
//            			File orgReportFileFolder = new File(orgReportPath);
//            			orgReportFileFolder.mkdir();
//            		}
//            		else
//            			orgReportPath = (String)map.get(strorgId);
    				
    				Engine engine = new Engine(rd, cxt);  //构造报表引擎
    				
    				IReport iReport = engine.calc();  //运算报表
//    				String excelFileName = orgReportPath + File.separator + //LuYueFei注释:2014-01-05
//    									   templateid+"_"+versionod+"_"+strorgId+"_"+curid+"_"+freqId+"_"+objDate.replace("-", "") + ".xls";//LuYueFei注释:2014-01-05
    				
    				ReportUtils.exportToExcel(excelFileName, iReport, false);
    				//生成excel时加上公式
    				/***
					 * 2012/12/31日修改，为能正确解析BIII报表IF公式
					 * 修改之后，此方法会将IF公式中的公式项“==”替换为“=”
					 */
    				AFReportDealDelegate.resoveExcelFormaule(excelFileName,file.getPath(),templateid);
//    				reportMap.put("times",  objDate.replace("-", ""));
//    				reportMap.put("orgid", strorgId);
//    				reportMap.put("file", excelFileName);
//    				
//    				if(!reportFlg.equals(com.fitech.gznx.common.Config.CBRC_REPORT) && aftemplate.getIsReport()!=null &&
//    						aftemplate.getIsReport().intValue()==1){
//    					AFReportProductDelegate.insertNXReport(reportMap,operator,reportFlg);					
//    				}
            	}
    			} catch(Exception e){
    				e.printStackTrace();
    			} catch (Throwable e1) {
    				e1.printStackTrace();
    			} finally {
    				if(inStream!=null){
    					inStream.close();
    				}
    			}
			}
			
			
			/***将多个excel合成一个excel多个sheet********************************/
			if(toaexcel){
				DownloadALLReportServlet servlet=new DownloadALLReportServlet();
				String toDay = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
				String name = servlet.CopyExcelSheetToAnotherExcelSheet(orgReportPath, orgReportPath, toDay.replace("-", ""));
				srcFileName = name;
			}
			
			long time2=System.currentTimeMillis();System.out.println("剪切时间为："+(time2-time1)+"毫秒");
			
			boolean bool = false;
        	dldtZip.gzip(srcFileName,bool);

        	long time3=System.currentTimeMillis();System.out.println("压缩时间为："+(time3-time2)+"毫秒");
			response.reset();
			response.setContentType("application/x-zip-compressed;name=\""
					+ zipFileName + "\"");
			response.addHeader("Content-Disposition",
					"attachment; filename=\""
							+ FitechUtil.toUtf8String(zipFileName) + "\"");
			response.setHeader("Accept-ranges", "bytes");

			FileInputStream inStream = new FileInputStream(srcFileName
					+ ".zip");

			int len;
			byte[] buffer = new byte[100];

			while ((len = inStream.read(buffer)) > 0) {
				response.getOutputStream().write(buffer, 0, len);
			}
			inStream.close();
			
//    		response.reset();
//    		response.setContentType("text/html;charset=GB2312");
//    		PrintWriter out = null;
//    		try {
//    			out = response.getWriter();
//    		} catch (IOException e1) {
//    			e1.printStackTrace();
//    		}
//
//    		out.println("window.location=<%=request.getContextPath()%>/afReportProduct.do;");
//
//    		out.close();
        	
			
			//删除临时文件
			FileUtil.deleteFile(outfile);
			FileUtil.deleteFile(srcFileName + ".zip");

		} catch (Exception e) {
			e.printStackTrace();
			ErrorOutPut(response);
		}
	}

	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		this.doGet(request, response);
	}

	/**
	 * 错误输出
	 * 
	 * @param response
	 */
	private void ErrorOutPut(HttpServletResponse response) {
		response.reset();
		response.setContentType("text/html;charset=GB2312");
		PrintWriter out = null;
		try {
			out = response.getWriter();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		out.println("<font color=\"blue\">没有可以下载的数据文件!</font>");
		out.close();
	}
	
	/**
	 * 错误输出
	 * 
	 * @param response
	 */
	private void ErrorOutPut(HttpServletResponse response,String info) {
		response.reset();
		response.setContentType("text/html;charset=GB2312");
		PrintWriter out = null;
		try {
			out = response.getWriter();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		out.println("<font color=\"blue\">" +info +"没有可以下载的数据文件!</font>");
		out.close();
	}

	
	/***
	 * 将多个excel合成一个excel多个sheet
	 * @param fromPath
	 * @param toPath
	 */
	private String CopyExcelSheetToAnotherExcelSheet(String fromPath,String toPath,String excelName) throws Exception{
		//创建新的excel
		HSSFWorkbook wbCreat = new HSSFWorkbook();
		File fromFile = new File(fromPath);
		for(File fromExcel : fromFile.listFiles()){
			//打开已有的excel
			String strExcelPath = fromPath + Config.FILESEPARATOR + fromExcel.getName();
			InputStream in = new FileInputStream(strExcelPath);
			HSSFWorkbook wb = new HSSFWorkbook(in);
			Map<String, String> colorMap = new HashMap<String, String>();
			for(int i=0;i<wb.getNumberOfSheets();i++){
				//原来的excel的sheet
				HSSFSheet sheet = wb.getSheetAt(i);
				HSSFSheet sheetCreate = wbCreat.createSheet(wb.getSheetName(i));
				
				
				//复制原有的sheet 合并单元格到新建的sheet
				MergerRegion(sheetCreate, sheet);
				
				int firstRow = sheet.getFirstRowNum();
				int lastRow = sheet.getLastRowNum();
				for(int j=firstRow;j<=lastRow;j++){
					//创建新建excel Sheet的行
					HSSFRow rowCreat = sheetCreate.createRow(j);
					//取得原有sheet的行
					HSSFRow row = sheet.getRow(j);
					row.setHeight(rowCreat.getHeight());
					row.setHeightInPoints(rowCreat.getHeightInPoints());
					//row.setZeroHeight(rowCreat.getZeroHeight());
					//单元格
					int firstCell = row.getFirstCellNum();
					int lastCell = row.getLastCellNum();
					for(int k=firstCell;k<=lastCell;k++){
						//System.out.println(row.getCell((short)k));
						//新的单元格
						HSSFCell cellCreat = rowCreat.createCell((short)k);
						//旧的excel单元格
						HSSFCell cell = row.getCell((short)k);
						//创建新的excel单元格样式
						HSSFCellStyle cellStyleCreat = wbCreat.createCellStyle();
						HSSFFont fontCreat = wbCreat.createFont();;
						HSSFFont font = wb.getFontAt((short)i);
						
						
						String strVal = "";
						if(cell!=null){	
							//获取原来的单元格样式
							HSSFCellStyle styleCell = cell.getCellStyle();
						
					
							//设置前景色
							cellStyleCreat.setFillPattern(styleCell.getFillPattern());
							if(styleCell.getFillForegroundColor()==(short)17)
								cellStyleCreat.setFillForegroundColor(HSSFColor.SKY_BLUE.index);
							if(styleCell.getFillForegroundColor()==(short)16)
								cellStyleCreat.setFillForegroundColor(HSSFColor.GREY_40_PERCENT.index);
							//cellStyleCreat.setFillForegroundColor(HSSFColor.SKY_BLUE.index);
							//设置单元格填充样式为原单元格填充样式
					
							
							//设置背景色
							
					
							cellStyleCreat.setAlignment(styleCell.getAlignment());
							cellStyleCreat.setBorderBottom(styleCell.getBorderBottom());
							cellStyleCreat.setBorderLeft(styleCell.getBorderLeft());
							cellStyleCreat.setAlignment(styleCell.getAlignment());
							cellStyleCreat.setBorderRight(styleCell.getBorderRight());
							cellStyleCreat.setBorderTop(styleCell.getBorderTop());
							cellStyleCreat.setBottomBorderColor(styleCell.getBottomBorderColor());
							cellStyleCreat.setDataFormat(styleCell.getDataFormat());
							cellStyleCreat.setLeftBorderColor(styleCell.getLeftBorderColor());
							cellStyleCreat.setRightBorderColor(styleCell.getRightBorderColor());
							cellStyleCreat.setTopBorderColor(styleCell.getTopBorderColor());
							cellStyleCreat.setVerticalAlignment(styleCell.getVerticalAlignment());
							cellStyleCreat.setWrapText(styleCell.getWrapText());
							
							boolean sam = styleCell.getWrapText();
							//System.out.println("原来的字体为："+sam);
							colorMap.put(String.valueOf(sam), String.valueOf(sam));
							
							/**end*/
							BeanUtils.copyProperties(fontCreat, font);
							
							//styleCell.
							//设置字体
							cellStyleCreat.setFont(font);
							
							//设置样式
							cellCreat.setCellStyle(cellStyleCreat);
							
							//设置单元格编码
							//获取单元格的值
							strVal = getValue(row.getCell((short)k));
							
							rowCreat.getCell((short)k).setEncoding(HSSFCell.ENCODING_UTF_16);
							//row.getCell((short)k).setCellStyle(rowCreat.getCell((short)k).getCellStyle());
						}
						//rowCreat.getCell((short)k).setCellStyle(cellStyle);
						cellCreat.setCellValue(strVal);
					}
					
				}
				Set<Entry<String, String>> en = colorMap.entrySet();
				for(Entry<String, String> e :en){
					System.out.println(e.getKey()+":"+e.getValue());
				}
				
			}
		}
		String namePath = toPath +File.separator+"excel";
		File file = new File(namePath);
		if(!file.canWrite())
			file.mkdirs();
		FileOutputStream fileOut = new FileOutputStream(namePath + File.separator  + excelName + ".xls");
	    wbCreat.write(fileOut);
	    fileOut.close();
	    return namePath;
	}
	
	/**
     * 复制原有sheet的合并单元格到新创建的sheet
     * 
     * @param sheetCreat
     *            新创建sheet
     * @param sheet
     *            原有的sheet
     */
	private static void MergerRegion(HSSFSheet sheetCreat, HSSFSheet sheet) {
        int sheetMergerCount = sheet.getNumMergedRegions();
        for (int i = 0; i < sheetMergerCount; i++) {
            Region mergedRegionAt = sheet.getMergedRegionAt(i);
            sheetCreat.addMergedRegion(mergedRegionAt);
        }

    }
	
	
	 /**
     * 去除字符串内部空格
     */
    public static String removeInternalBlank(String s) {
        // System.out.println("bb:" + s);
        Pattern p = Pattern.compile("\\s*|\t|\r|\n");
        Matcher m = p.matcher(s);
        char str[] = s.toCharArray();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < str.length; i++) {
            if (str[i] == ' ') {
                sb.append(' ');
            } else {
                break;
            }
        }
        String after = m.replaceAll("");
        return sb.toString() + after;
    }

    private String getValue(HSSFCell hssfCell) {
		//if(hssfCell.getCellType() == hssfCell.c)
		//hssfCell.
		
		if (hssfCell.getCellType() == HSSFCell.CELL_TYPE_BOOLEAN) {
			return String.valueOf(hssfCell.getBooleanCellValue());
		} else if (hssfCell.getCellType() == HSSFCell.CELL_TYPE_NUMERIC) {
			Date date = null;
			//try {
				//date = HSSFDateUtil.getJavaDate(hssfCell.getNumericCellValue());
				//SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				//String dateString = sdf.format(date);
				//return dateString;
			//} catch (Exception e) {
				return String.valueOf(hssfCell.getNumericCellValue());
			//}
			
		}else {
			return String.valueOf(hssfCell.getStringCellValue());
		}
    }
}
