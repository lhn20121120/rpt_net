package com.cbrc.smis.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

import com.cbrc.smis.common.Config;
import com.cbrc.smis.db2xml.Db2XmlUtil;
import com.cbrc.smis.entity.Cell;
import com.cbrc.smis.excel.Utils;
import com.cbrc.smis.hibernate.ReportIn;
import com.cbrc.smis.proc.util.EngineException;
import com.fitech.gznx.common.StringUtil;
import com.fitech.gznx.po.AfOrg;
import com.fitech.gznx.po.AfReport;
import com.fitech.gznx.service.AFOrgDelegate;
import com.fitech.gznx.service.AFPbocReportDelegate;
import com.fitech.gznx.util.DateFromExcel;
import com.fitech.net.adapter.StrutsExcelData;
import com.runqian.report4.model.ReportDefine;
import com.runqian.report4.usermodel.Context;
import com.runqian.report4.usermodel.Engine;
import com.runqian.report4.usermodel.IByteMap;
import com.runqian.report4.usermodel.INormalCell;
import com.runqian.report4.usermodel.IReport;
import com.runqian.report4.util.ReportUtils;

public class UtilForExcelAndRaq {
	
	/**
	 * 调用润乾API生成相应的excel模板
	 * @param raqFilePath
	 * @param produceExcelPath
	 * @return
	 */
	public static boolean createExcelForRaq(String raqFilePath,String produceExcelPath,String templateId){
		boolean result = false;
		if(StringUtil.isEmpty(raqFilePath) ||
				StringUtil.isEmpty(produceExcelPath))
			return false;
		try {
			// 生成excel
			ReportDefine rd = (ReportDefine)ReportUtils.read(raqFilePath);
			Context cxt = new Context();  //构建报表引擎计算环境				
			Engine engine = new Engine(rd, cxt);  //构造报表引擎
			engine.calc();  //运算报表
			int rownum = rd.getRowCount();
			int colnum = rd.getColCount();
			for(int i=1;i<=rownum;i++){
				for(int j=1;j<=colnum;j++){					
					INormalCell cs = rd.getCell(i, (short) j);				
					IByteMap map = cs.getExpMap(false);
					if(map != null && !map.isEmpty() && map.getValue(0) != null){
						cs.setExpMap(null);
						//Nick:2013-07-15--如果为报表日期单元格，则设定此单元格的特殊格式值
						if(map.getValue(0).toString().indexOf("报表日期")>-1){
							cs.setValue("报表日期： ${report.year} 年${report.term} 月");
						}//长沙银行 填报机构在导出的excel中不存在
						if(map.getValue(0).toString().indexOf("填报机构")>-1){
							cs.setValue("填报机构：${report.OrgID}");
						}
					}
					//Nick:2013-07-15--如果为报表里面的单元格的值设定为0，则默认去除“0”字样，保证导出的数据格式正确
					String value=(cs.getValue()==null)?"":cs.getValue().toString();
					if(value!=null&&value.equals("0")){
						//cs.setValue(null);
					}
				}
			}
			IReport iReport = rd;  //运算报表
			//生成excel模板
			ReportUtils.exportToExcel(produceExcelPath, iReport, false); 
			
			result = writeFormulaToExcel(produceExcelPath,raqFilePath,templateId);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}
	/**
	 * 将公式写入excel
	 * @param excelpath
	 * @param rqpath
	 * @param templateId
	 * @return
	 * @throws Exception
	 */
	public static boolean writeFormulaToExcel(String excelpath,String rqpath,
			String templateId)throws Exception{
		boolean result = false;
		if(StringUtil.isEmpty(excelpath) || StringUtil.isEmpty(rqpath)
				|| StringUtil.isEmpty(templateId))
			return false;
		HSSFWorkbook book=null;
		File file=null;
		FileInputStream excel=null;
		HSSFCell hssfCell=null;
		HSSFRow hssfRow=null;
		try {
			file=new File(excelpath);
			excel=new FileInputStream(file);				
			book=new HSSFWorkbook(excel);
			book.setSheetName(0, templateId);
			HSSFSheet sheet=book.getSheetAt(0);
			ReportDefine rd = (ReportDefine)ReportUtils.read(rqpath);
			int rownum = rd.getRowCount();
			int colnum = rd.getColCount();
			for(int i=1;i<=rownum;i++){
				for(int j=1;j<=colnum;j++){					
					INormalCell cs = rd.getCell(i, (short) j);//润乾获取单元格			
					IByteMap map = cs.getExpMap(false);//
					if(map != null && !map.isEmpty()){
						//判断是否具有公式 和背景色是否为指定色
						if(map.getValue(0) != null && cs.getBackColor() == Config.FORMULA_BGCOLOR){
							
							String formulaValue = (String) map.getValue(0);//获取取数规则
							if(StringUtil.isEmpty(formulaValue)){
								continue;
							}
							hssfRow=sheet.getRow(i-1 );
							if(hssfRow==null){
								continue;
							}
							try{
								hssfCell=hssfRow.getCell((short)(j-1));
								if(hssfCell==null) continue;
								String valformul = checkFormul(formulaValue.trim());
								//处理表达式中Min函数和max函数
								if(valformul.toLowerCase().indexOf("min")>=0||valformul.toLowerCase().indexOf("max")>=0){
									valformul=valformul.replace("[", "");
									valformul=valformul.replace("]", "");
								}
								//处理表达式中IF 函数的 “==”excel中不能解析的问题 。
								valformul = valformul.replaceAll("==", "=");
								hssfCell.setCellFormula(valformul);//放入取数规则
							}catch(Exception e){
								e.printStackTrace();
							}
						}
					}
					//Nick:2013-07-15--设定Excel中的格式，目前设定为数值保留两位小数"0.00"，百分比设定为四位小数"0.0000%"
					String format = cs.getFormat();//取润乾模板中的格式字符串
					if(format!=null && format.length()>0){
						hssfRow=sheet.getRow(i-1);
						if(hssfRow==null) continue;
						hssfCell=hssfRow.getCell((short)(j-1));
						if(hssfCell==null) continue;
						
						org.apache.poi.hssf.usermodel.HSSFCellStyle cellStyle = hssfCell.getCellStyle();
						org.apache.poi.hssf.usermodel.HSSFCellStyle myCellStyle = book.createCellStyle();
						org.apache.commons.beanutils.BeanUtils.copyProperties(myCellStyle, cellStyle);
						
						if(format.indexOf("%")>-1){//设定百分比格式
							org.apache.poi.hssf.usermodel.HSSFDataFormat dataFormat= book.createDataFormat();
							myCellStyle.setDataFormat(dataFormat.getFormat("0.0000%"));
							hssfCell.setCellStyle(myCellStyle);
						}else{//设定为数字格式
							myCellStyle.setDataFormat(org.apache.poi.hssf.usermodel.HSSFDataFormat.getBuiltinFormat("0.00"));
							hssfCell.setCellStyle(myCellStyle);
						}
					}
				}
			}
			FileOutputStream fos = new FileOutputStream(file);
			book.write(fos);  
			fos.close();
			result = true;
		}catch(EngineException e1){
			e1.printStackTrace();
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			try {
				if(excel != null)
				excel.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return result;
	}
	
	/**
	 * 转化计算函数
	 * @param formulaValue
	 * @return
	 */
	public static String checkFormul(String formulaValue) {
		if(formulaValue.indexOf("{}")>=0 && (formulaValue.indexOf("SUM")>=0 || formulaValue.indexOf("sum")>=0)){
			
			String checkFormul = formulaValue.substring(4);
			int idex = checkFormul.indexOf("{");
			String cellrow = checkFormul.substring(0, idex);
			String[] formulA = subformul(cellrow);
			
			return "SUM(INDIRECT(\""+""+cellrow+":"+formulA[0]+"\"&ROW()-1))";
		}else{
			return formulaValue;
		}
	}
	
	public static String[] subformul(String formul) {
		String[] formarr = new String[2];
		int len = formul.length();
		for(int i=0;i<len;i++){
			if(Character.isDigit(formul.charAt(i))){
				formarr[0] =  formul.substring(0, i);
				formarr[1] =  formul.substring(i);
				
				return formarr;
			}
		}
		return null;
	}
	
	public static boolean copyDataToExcel(String srcExcelPath,List<Cell> cells,
			String templateId,String verisonId){
		boolean result = false;
		if(cells==null || cells.isEmpty() ||
				StringUtil.isEmpty(srcExcelPath))
			return false;
		if(!new File(srcExcelPath).exists())return false;
		Map percentCells=new Db2XmlUtil().getPercentCells(templateId,verisonId);
		HSSFWorkbook sourceWb = null;
		HSSFSheet sheet = null;
		FileInputStream inStream = null ;
		Number cellNumb = null;
		try {
			inStream= new FileInputStream(srcExcelPath);
			POIFSFileSystem srcPOIFile = new POIFSFileSystem(inStream);
			sourceWb = new HSSFWorkbook(srcPOIFile);
			if (sourceWb.getNumberOfSheets() > 0)
			{
				sheet = sourceWb.getSheetAt(0);
			}
			
			for (int i = 0; i < cells.size(); i++){
	               Cell cell = (Cell) cells.get(i);
	               if(StringUtil.isEmpty(cell.getCellValue()))continue;
	               short col = (short)convertColStringToNum(cell.getColId());
	               HSSFCell excelcell = sheet.getRow(cell.getRowId()-1).getCell(col);
	               
	               if(excelcell.getCellType()==HSSFCell.CELL_TYPE_FORMULA){
	   				//Excel中的公式区域不设定数值，后续步骤中让POI自动根据公式重新计算
//	            	   System.out.println("cellName:"+cell.getCellName());
//	            	   excelcell.setCellType(HSSFCell.CELL_TYPE_FORMULA);
//	            	   Double d = Double.valueOf(cell.getCellValue());
//	   					excelcell.setCellValue(d.doubleValue());
	               }else if(excelcell.getCellType()==HSSFCell.CELL_TYPE_NUMERIC){
	            	   try{
	   					Double d = Double.valueOf(cell.getCellValue());
	   					excelcell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
	   					excelcell.setCellValue(d.doubleValue());
	   				}catch(Exception ex){
	   					excelcell.setEncoding(HSSFCell.ENCODING_UTF_16);
	   					//hssfCell.setCellType(HSSFCell.CELL_TYPE_BLANK);
	   					excelcell.setCellValue(Utils.isPercentCell(percentCells,cell.getCellName())==true?
	   							Utils.round(cell.getCellValue(),2):
	   								(cell.getCellValue()==null?"":Utils.formatVal(cell.getCellValue())));
	   				}
	               }else{
	            	   try {
	   					cellNumb = parseNumber(cell.getCellValue());
	   					if(cellNumb == null){
	   						excelcell.setEncoding(HSSFCell.ENCODING_UTF_16);
	   						excelcell.setCellValue(cell.getCellValue());
	   					}else{
	   						if(excelcell.getCellType() !=HSSFCell.CELL_TYPE_STRING )
	   							excelcell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
	   						if(Utils.isPercentCell(percentCells, cell.getCellName()) == true)
	   							excelcell.setCellValue(Utils.round(cell.getCellValue(), 2));
	   						else
	   							excelcell.setCellValue(cellNumb.doubleValue());
	   					}
	   				} catch (Exception ex) {
	   					ex.printStackTrace();
	   				}
	               }
	        }
			FileOutputStream stream = new FileOutputStream(new File(srcExcelPath));
			
			sourceWb.write(stream);
			stream.flush();
			stream.close();
			result = true;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			try {
				if(inStream!=null)
					inStream.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return result;
	}
	
	public static Number parseNumber(String value){
		Number num = null;
		
		if(value == null || value.trim().equals("") || value.indexOf("-")!=-1 || value.indexOf("_")!=-1) return num;
		
		java.text.NumberFormat numberFormat = java.text.NumberFormat.getInstance();
		
		try{
			num = numberFormat.parse(value);
			if(!isNumeric(value))
            {
                num=null;
            }
		}catch(Exception e){
			num = null;
		}
		
		return num;
	}
	
	public static boolean isNumeric(String str){ 
        Pattern pattern = Pattern.compile("-?[0-9]+.?[0-9]+"); 
        Matcher isNum = pattern.matcher(str);
        if( !isNum.matches() ){
            return false; 
        } 
        return true; 
     }
	
	/**
	 * 将列号转换为数字
	 * 
	 * @param ref
	 * @return
	 * 
	 */
	public static int convertColStringToNum(String ref){
		int retval = 0;
		int pos = 0;
		for (int k = ref.length() - 1; k > -1; k--){
			char thechar = ref.charAt(k);
			if (pos == 0)
				retval += Character.getNumericValue(thechar) - 9;
			else
				retval += (Character.getNumericValue(thechar) - 9) * (pos * 26);
			pos++;
		}

		return retval - 1;
	}
	
	public static HSSFWorkbook writeExcelToData(Map cellMap)throws Exception{
		System.out.println("---------------writeExcelToData-------------------------------------");
		String path=(String)cellMap.get("path");
		String repId=(String)cellMap.get("repInId");
		String reportFlg=(String)cellMap.get("reportFlg");
		String templateId=(String)cellMap.get("templateId");
		String versionId=(String)cellMap.get("versionId");
		Map percentCells=new Db2XmlUtil().getPercentCells(templateId,versionId);
		String writer = DateFromExcel.WRITER;
		String checker = DateFromExcel.CHECKER;
		String principal = DateFromExcel.PRINCIPAL;
		String reptDate="报表日期";
		String orgName="填报机构";
		String[] ARRCOLS =com.cbrc.smis.util.ExcelUtil.getArrcols();
		HSSFWorkbook sourceWb = null;
		HSSFSheet sheet = null;
		Number cellNumb = null;
		FileInputStream inStream = new FileInputStream(path);
		POIFSFileSystem srcPOIFile = new POIFSFileSystem(inStream);
		sourceWb = new HSSFWorkbook(srcPOIFile);
		HSSFDataFormat cellFormat = sourceWb.createDataFormat();
		if (sourceWb.getNumberOfSheets() > 0)
		{
			sheet = sourceWb.getSheetAt(0);
		}
		inStream.close();

		HSSFRow row = null;
		HSSFCell cell = null;
		/*****************读取润乾模板begin********************/
		String reportFile = Config.RAQ_TEMPLATE_PATH + "templateFiles"
		+ File.separator + "Raq" + File.separator + templateId
		+ "_" +  versionId  + ".raq";
		INormalCell iExcelCell = null;
		ReportDefine rd = (ReportDefine) ReportUtils.read(reportFile);
		/*****************读取润乾模板end********************/
		String stakeholders[] = StakeholdersUtil.getStakeholders(Integer.parseInt(repId), templateId, versionId);
		for (Iterator iter = sheet.rowIterator(); iter.hasNext();)
		{
			row = (HSSFRow) iter.next();
  			for (short i = row.getFirstCellNum(), n = row.getLastCellNum(); i < n; i++)
			{
				cell = (HSSFCell) row.getCell(i);
				if (cell == null)
					continue;
				/* 检查是否是非公式单元格 */
//				HSSFCellStyle cs=cell.getCellStyle();
//				cs.setDataFormat(HSSFDataFormat.getBuiltinFormat("0.00"));
				if (cell.getCellType() != HSSFCell.CELL_TYPE_FORMULA)
				{
					if (cell.getCellNum() >= ARRCOLS.length)
						continue;
					String cellName = ARRCOLS[cell.getCellNum()]
							+ (row.getRowNum() + 1);

					if(cell.getCellType()==HSSFCell.CELL_TYPE_STRING){
						cell.setEncoding(HSSFCell.ENCODING_UTF_16);
						cell.setCellType(HSSFCell.CELL_TYPE_STRING);
						String tmp = "";
			            
						
						if(cell.getStringCellValue().indexOf(writer)>-1){							
							if(reportFlg!=null && reportFlg.equals("1")){
								ReportIn reportIn =AFPbocReportDelegate.getSimReportIn(Integer.parseInt(repId));	
								if(null != reportIn)
								{
									if(null == reportIn.getWriter())
									{
										tmp = "填表人：" + stakeholders[0];	
									}
									else
									{										 
										tmp = reportIn.getWriter();
										tmp = tmp.replaceAll("填表人", "").replaceAll("：", "").replace(":", "");	
										if("".equals(tmp))
											tmp = "填表人：" + stakeholders[0];
										else
											tmp = "填表人：" + tmp;
									}
									cell.setCellValue(tmp);
								}								
							}else {
								AfReport af = StrutsExcelData.getAfReport(repId);
								if(af!=null && af.getWriter()!=null && 
										!af.getWriter().equals("")){
									cell.setCellValue("");
									cell.setCellValue(af.getWriter());
								}
							}
						}else if(cell.getStringCellValue().indexOf(checker)>-1){
							cell.setCellValue("");
							if(reportFlg!=null && reportFlg.equals("1")){
								ReportIn reportIn =AFPbocReportDelegate.getSimReportIn(Integer.parseInt(repId));
								if(null != reportIn)
								{
									if(null == reportIn.getChecker())
									{
										tmp = "复核人：" + stakeholders[1];	
									}
									else
									{
										tmp = reportIn.getChecker();
										tmp = tmp.replaceAll("复核人", "").replaceAll("：", "").replace(":", "");	
										if("".equals(tmp))
											tmp = "复核人：" +  stakeholders[1];	
										else
											tmp = "复核人：" + tmp;
									}
									cell.setCellValue(tmp);
								}
							}else {
								AfReport af = StrutsExcelData.getAfReport(repId);
								if(af!=null && af.getChecker()!=null && 
										!af.getChecker().equals("")){
									cell.setCellValue("");
									cell.setCellValue(af.getChecker());
								}
							}
						}else if(cell.getStringCellValue().indexOf(principal)>-1){
							if(reportFlg!=null && reportFlg.equals("1")){
								ReportIn reportIn =AFPbocReportDelegate.getSimReportIn(Integer.parseInt(repId));
								if(null != reportIn)
								{
									if(null == reportIn.getPrincipal())
									{
										tmp = "负责人：" + stakeholders[2];	
									}
									else
									{
										tmp = reportIn.getPrincipal();
										tmp = tmp.replaceAll("负责人", "").replaceAll("：", "").replace(":", "");	
										if("".equals(tmp))
											tmp = "负责人：" + stakeholders[2];
										else
											tmp = "负责人：" + tmp;
									}
									cell.setCellValue(tmp);
								}
							}else {
								AfReport af = StrutsExcelData.getAfReport(repId);
								if(af!=null && af.getPrincipal()!=null && 
										!af.getPrincipal().equals("")){
									cell.setCellValue("");
									cell.setCellValue(af.getPrincipal());
								}
							}
						}else if(cell.getStringCellValue().indexOf(reptDate)>-1){
							if(reportFlg!=null && reportFlg.equals("1")){
								ReportIn reportIn =AFPbocReportDelegate.getSimReportIn(Integer.parseInt(repId));
								if(reportIn!=null && reportIn.getYear()!=null 
										&& !reportIn.getYear().equals("")){
									cell.setCellValue("");
									cell.setCellValue(reptDate+"："+reportIn.getYear()+"年"+reportIn.getTerm()+"月");
								}
							}else {
								AfReport af = StrutsExcelData.getAfReport(repId);
								if(af!=null && af.getYear()!=null && 
										!af.getYear().equals("")){
									cell.setCellValue("");
									cell.setCellValue(reptDate+"："+af.getYear()+"年"+af.getTerm()+"月");
								}
							}
						}
						else if(cell.getStringCellValue().indexOf(orgName)>-1){//长沙银行导出报表时，增加填报机构
							if(reportFlg!=null && reportFlg.equals("1")){
								ReportIn reportIn =AFPbocReportDelegate.getSimReportIn(Integer.parseInt(repId));
								if(reportIn!=null && reportIn.getOrgId()!=null 
										){
									AfOrg afOrg=AFOrgDelegate.getOrgInfo(reportIn.getOrgId());
									cell.setCellValue("");
									cell.setCellValue(orgName+"："+afOrg.getOrgName());
								}
							}else {
								AfReport af = StrutsExcelData.getAfReport(repId);
								if(af!=null &&
										!af.getOrgId().equals("")){
									AfOrg afOrg=AFOrgDelegate.getOrgInfo(af.getOrgId());
									cell.setCellValue("");
									cell.setCellValue(orgName+"："+afOrg.getOrgName());
								}
							}
						}
					}
					
					if(cellName.equals("T46"))
						System.out.println();
					iExcelCell = null;
					if(cellMap.get(cellName)!=null){
						iExcelCell = rd.getCell(row.getRowNum()+1, (short)(cell.getCellNum()+1));
					}
					String cellFormatStr = "";
					if(iExcelCell!=null && iExcelCell.getFormat()!=null){
						cellFormatStr = iExcelCell.getFormat();
					}
					
					if(cellMap.get(cellName)!=null && cellFormatStr.endsWith("%")){
						String va = (String) cellMap.get(cellName);
						BigDecimal num = new BigDecimal(va);
						num  = num.movePointRight(2);
						String vaperc = String.valueOf(num);
						String vapercTrimZero = com.fitech.gznx.util.ExcelUtil.subZeroAndDot(vaperc);
						String cellFormatNew = vapercTrimZero.replaceAll("[0-9]", "0") + "%";
						String[] cellFormatNewSplit = cellFormatNew.split("\\.");
						HSSFCellStyle cellStyle = cell.getCellStyle();
						HSSFCell cellNew = row.createCell(cell.getCellNum());
						cellNew.setCellStyle(cellStyle);
						cellNew.setCellValue(Double.parseDouble(va));
						if(cellFormatNewSplit.length==1){
							cellNew.getCellStyle().setDataFormat(cellFormat.getFormat("#0%"));
						}else{
							String formatStr = "#0." + cellFormatNewSplit[1];
							cellNew.getCellStyle().setDataFormat(cellFormat.getFormat(formatStr));
						}
					}					
					/*如果该单元格在Map中有值则把值写入该单元格*/
					String format ="";
					String cellValue = (String) cellMap.get(cellName);
					if (cellMap.get(cellName) != null)
					{
						if(cellValue.indexOf("@@")>-1){
							StringBuffer relValue =new StringBuffer();
							String v = cellValue.split("@@")[0];
							format = cellValue.split("@@")[1];
							
							if(new Integer(format)>v.length()){
								for (int j = 0; j < new Integer(format)-v.length(); j++) {
									relValue .append("0");
								}
							}
							relValue .append(v);
							//System.out.println(relValue);
							cellValue = relValue.toString();
							cell.setEncoding(HSSFCell.ENCODING_UTF_16);
							cell.setCellType(HSSFCell.CELL_TYPE_STRING);
							cell.setCellValue(cellValue.toString());
						}
						if(format.equals("")){
//							try
//							{
//								/*数值型单元格*/
//								Double d = Double.valueOf(cellValue.toString());
//								cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
//								cell.setCellValue(d);
//							}
//							catch (Exception ex)
//							{
//								/*字符型单元格*/
//								cell.setEncoding(HSSFCell.ENCODING_UTF_16);
//								cell.setCellType(HSSFCell.CELL_TYPE_STRING);
//								cell.setCellValue(cellValue.toString());
//							}
							if(cell.getCellType() == HSSFCell.CELL_TYPE_FORMULA){
//								
//								//Excel中的公式区域不设定数值，后续步骤中让POI自动根据公式重新计算
							}
							//if(hssfCell.getCellType() != HSSFCell.CELL_TYPE_FORMULA){
							else if(//hssfCell.getCellType() == HSSFCell.CELL_TYPE_FORMULA || 
									cell.getCellType() == HSSFCell.CELL_TYPE_NUMERIC){
								try{
									Double d = Double.valueOf(cellValue);
									cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
									cell.setCellValue(d.doubleValue());
								}catch(Exception ex){
									cell.setEncoding(HSSFCell.ENCODING_UTF_16);
									//hssfCell.setCellType(HSSFCell.CELL_TYPE_BLANK);
									cell.setCellValue(Utils.isPercentCell(percentCells,cellName)==true?
											Utils.round(cellValue,2):
												(cellValue==null?"":Utils.formatVal(cellValue)));
								}
							}else{				 
								try {
									cellNumb = parseNumber(cellValue);
									if(cellNumb == null){
										cell.setEncoding(HSSFCell.ENCODING_UTF_16);
										cell.setCellValue(cellValue);
									}else{
										if(cell.getCellType() !=HSSFCell.CELL_TYPE_STRING )
											cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
										if(Utils.isPercentCell(percentCells, cellName) == true)
											cell.setCellValue(Utils.round(cellValue, 2));
										else
											cell.setCellValue(cellNumb.doubleValue());
									}
								} catch (Exception ex) {
									ex.printStackTrace();
								}
						}
					}
				}
			}
			}
		}
		HSSFRow rowDataRange = sheet.getRow(1);
        HSSFCell rowDataRangeCell = rowDataRange.getCell((short) 0);
        // System.out.println(rowDataRangeCell.getCellFormula());
        if (null != cellMap.get("dateRg"))
        {
            rowDataRangeCell.setEncoding(HSSFCell.ENCODING_UTF_16);
            rowDataRangeCell.setCellType(HSSFCell.CELL_TYPE_STRING);
            rowDataRangeCell.setCellValue("报送口径：" + (String) cellMap.get("dateRg"));
        }
		return sourceWb;
	}
	
}
