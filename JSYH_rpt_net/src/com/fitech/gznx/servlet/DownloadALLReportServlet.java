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
		/** ����ѡ�б�־ **/
		String reportFlg = "0";
		
		
		if (session.getAttribute(Config.REPORT_SESSION_FLG) != null){
			reportFlg = (String) session.getAttribute(Config.REPORT_SESSION_FLG);
		}
		
		// ������ʱ�ļ���ſռ�
		
		String pathName = "templateExcel";
				
		String srcFileName = Config.WEBROOTPATH  + "temp" + Config.FILESEPARATOR + pathName;
		
		String zipFileName = pathName + ".zip";

		//���
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
    		
    		//���excel�ϳ�һ��excel
    		Boolean toaexcel = Boolean.valueOf(request.getParameter("toaexcel"));
     		// ���type������downAll �Ͳ�������ȫ��
			if(!type.equals("downAll")){
	        	if(repNames != null && !repNames.equals("")){
	        		String[] repNameArr = repNames.split(",");
	        		if(repNameArr != null && repNameArr.length > 0){

	        			/**��ѡ����*/
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
            	// ����ᱨ��
            	if(reportFlg.equals(com.fitech.gznx.common.Config.CBRC_REPORT)){
            		list = AFReportProductDelegate.selectYJHReportList(reportInForm,operator);
            	} else {
            		list = AFReportProductDelegate.selectNOTYJHReportList(reportInForm,operator,reportFlg);
            	}
            	// ȡ��ģ��������������
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
			
			// ����excel�ļ�
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
    				//���ɴ�ű�����ļ���
            		if(!map.containsKey(strorgId)) {
	        			map.put(strorgId,orgReportPath);
	        			File orgReportFileFolder = new File(orgReportPath);
	        			orgReportFileFolder.mkdir();
	        		}
	        		else
	        			orgReportPath = (String)map.get(strorgId);
            		//ƴ�ӵ���Excel��ȫ·����Ϣ
    				String xlsName=templateid+"_"+versionod+"_"+strorgId+"_"+curid+"_"+freqId+"_"+objDate.replace("-", "") + ".xls";
    				String excelFileName = orgReportPath + File.separator +xlsName;//����Excel�ļ�ȫ·��
            		String excelProductReportFileName = Config.WEBROOTPATH +"reportFiles" + File.separator +xlsName;//ProductReport.java���ɵ�Excel�ļ�
            		//LuYueFei:2014-01-05
        			//��Ϊ���ɱ���ʱ��Ҫ����������Ǭ���������һ��>>����������⣬����ڶ���>>�����û�ʹ�õ�Excel�ļ�
        			//�Ż���ʽ������һ�����ɵ�Excel�ļ�ֱ�Ӽ����ļ�������Ŀ¼�У����ٲ���Ҫ�ĵڶ��μ��㣬��������ٶ�
            		File productReportExcel=new File(excelProductReportFileName);
            		if(productReportExcel.exists()){//�жϵ�һ�����ɵ�Excel�ļ��Ƿ���ڣ�������ڣ���ֱ�Ӽ���
            			productReportExcel.renameTo(new File(excelFileName));
            			File file = (File) reportMap.get("file");
            			AFReportDealDelegate.resoveExcelFormaule(excelFileName,file.getPath(),templateid);//����excelʱ���Ϲ�ʽ
            		}else{//������������ٴ�����
    				File file = (File) reportMap.get("file");
    				inStream = new FileInputStream(file);
    				ReportDefine rd = (ReportDefine)ReportUtils.read(inStream);
    				Context cxt = new Context();  //��������������㻷��
    				cxt.setParamValue("OrgID", strorgId);
    				cxt.setParamValue("ReptDate", objDate.replace("-", ""));
//    				String curid = String.valueOf(reportMap.get("curId"));//LuYueFeiע��:2014-01-05
//    				String freqId = String.valueOf(reportMap.get("freqID"));//LuYueFeiע��:2014-01-05
    				
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
    				
    				Engine engine = new Engine(rd, cxt);  //���챨������
    				
    				IReport iReport = engine.calc();  //���㱨��
//    				String excelFileName = orgReportPath + File.separator + //LuYueFeiע��:2014-01-05
//    									   templateid+"_"+versionod+"_"+strorgId+"_"+curid+"_"+freqId+"_"+objDate.replace("-", "") + ".xls";//LuYueFeiע��:2014-01-05
    				
    				ReportUtils.exportToExcel(excelFileName, iReport, false);
    				//����excelʱ���Ϲ�ʽ
    				/***
					 * 2012/12/31���޸ģ�Ϊ����ȷ����BIII����IF��ʽ
					 * �޸�֮�󣬴˷����ὫIF��ʽ�еĹ�ʽ�==���滻Ϊ��=��
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
			
			
			/***�����excel�ϳ�һ��excel���sheet********************************/
			if(toaexcel){
				DownloadALLReportServlet servlet=new DownloadALLReportServlet();
				String toDay = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
				String name = servlet.CopyExcelSheetToAnotherExcelSheet(orgReportPath, orgReportPath, toDay.replace("-", ""));
				srcFileName = name;
			}
			
			long time2=System.currentTimeMillis();System.out.println("����ʱ��Ϊ��"+(time2-time1)+"����");
			
			boolean bool = false;
        	dldtZip.gzip(srcFileName,bool);

        	long time3=System.currentTimeMillis();System.out.println("ѹ��ʱ��Ϊ��"+(time3-time2)+"����");
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
        	
			
			//ɾ����ʱ�ļ�
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
	 * �������
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
		out.println("<font color=\"blue\">û�п������ص������ļ�!</font>");
		out.close();
	}
	
	/**
	 * �������
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
		out.println("<font color=\"blue\">" +info +"û�п������ص������ļ�!</font>");
		out.close();
	}

	
	/***
	 * �����excel�ϳ�һ��excel���sheet
	 * @param fromPath
	 * @param toPath
	 */
	private String CopyExcelSheetToAnotherExcelSheet(String fromPath,String toPath,String excelName) throws Exception{
		//�����µ�excel
		HSSFWorkbook wbCreat = new HSSFWorkbook();
		File fromFile = new File(fromPath);
		for(File fromExcel : fromFile.listFiles()){
			//�����е�excel
			String strExcelPath = fromPath + Config.FILESEPARATOR + fromExcel.getName();
			InputStream in = new FileInputStream(strExcelPath);
			HSSFWorkbook wb = new HSSFWorkbook(in);
			Map<String, String> colorMap = new HashMap<String, String>();
			for(int i=0;i<wb.getNumberOfSheets();i++){
				//ԭ����excel��sheet
				HSSFSheet sheet = wb.getSheetAt(i);
				HSSFSheet sheetCreate = wbCreat.createSheet(wb.getSheetName(i));
				
				
				//����ԭ�е�sheet �ϲ���Ԫ���½���sheet
				MergerRegion(sheetCreate, sheet);
				
				int firstRow = sheet.getFirstRowNum();
				int lastRow = sheet.getLastRowNum();
				for(int j=firstRow;j<=lastRow;j++){
					//�����½�excel Sheet����
					HSSFRow rowCreat = sheetCreate.createRow(j);
					//ȡ��ԭ��sheet����
					HSSFRow row = sheet.getRow(j);
					row.setHeight(rowCreat.getHeight());
					row.setHeightInPoints(rowCreat.getHeightInPoints());
					//row.setZeroHeight(rowCreat.getZeroHeight());
					//��Ԫ��
					int firstCell = row.getFirstCellNum();
					int lastCell = row.getLastCellNum();
					for(int k=firstCell;k<=lastCell;k++){
						//System.out.println(row.getCell((short)k));
						//�µĵ�Ԫ��
						HSSFCell cellCreat = rowCreat.createCell((short)k);
						//�ɵ�excel��Ԫ��
						HSSFCell cell = row.getCell((short)k);
						//�����µ�excel��Ԫ����ʽ
						HSSFCellStyle cellStyleCreat = wbCreat.createCellStyle();
						HSSFFont fontCreat = wbCreat.createFont();;
						HSSFFont font = wb.getFontAt((short)i);
						
						
						String strVal = "";
						if(cell!=null){	
							//��ȡԭ���ĵ�Ԫ����ʽ
							HSSFCellStyle styleCell = cell.getCellStyle();
						
					
							//����ǰ��ɫ
							cellStyleCreat.setFillPattern(styleCell.getFillPattern());
							if(styleCell.getFillForegroundColor()==(short)17)
								cellStyleCreat.setFillForegroundColor(HSSFColor.SKY_BLUE.index);
							if(styleCell.getFillForegroundColor()==(short)16)
								cellStyleCreat.setFillForegroundColor(HSSFColor.GREY_40_PERCENT.index);
							//cellStyleCreat.setFillForegroundColor(HSSFColor.SKY_BLUE.index);
							//���õ�Ԫ�������ʽΪԭ��Ԫ�������ʽ
					
							
							//���ñ���ɫ
							
					
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
							//System.out.println("ԭ��������Ϊ��"+sam);
							colorMap.put(String.valueOf(sam), String.valueOf(sam));
							
							/**end*/
							BeanUtils.copyProperties(fontCreat, font);
							
							//styleCell.
							//��������
							cellStyleCreat.setFont(font);
							
							//������ʽ
							cellCreat.setCellStyle(cellStyleCreat);
							
							//���õ�Ԫ�����
							//��ȡ��Ԫ���ֵ
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
     * ����ԭ��sheet�ĺϲ���Ԫ���´�����sheet
     * 
     * @param sheetCreat
     *            �´���sheet
     * @param sheet
     *            ԭ�е�sheet
     */
	private static void MergerRegion(HSSFSheet sheetCreat, HSSFSheet sheet) {
        int sheetMergerCount = sheet.getNumMergedRegions();
        for (int i = 0; i < sheetMergerCount; i++) {
            Region mergedRegionAt = sheet.getMergedRegionAt(i);
            sheetCreat.addMergedRegion(mergedRegionAt);
        }

    }
	
	
	 /**
     * ȥ���ַ����ڲ��ո�
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
