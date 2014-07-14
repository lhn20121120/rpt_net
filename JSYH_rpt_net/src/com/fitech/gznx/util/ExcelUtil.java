package com.fitech.gznx.util;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.jxls.XLSTransformer;

import com.cbrc.smis.adapter.StrutsReportInDelegate;
import com.cbrc.smis.common.Config;
import com.cbrc.smis.excel.StyleCellProcessor;
import com.cbrc.smis.form.ReportInForm;
import com.fitech.gznx.common.FileUtil;

public class ExcelUtil {
	
	public static String Key_TemplateInfo = "Key_TemplateInfo";
	
	public static void writeDataExcel(Map map,HttpServletResponse response) throws Exception{
		String fileName=writeExcel(map);
		if(fileName!=null){
			downLoadExcel(fileName, response);
			
			new File(fileName).delete();
		}
	}
	
	/**
	 * 将从数据库中查询的数据放入map中，并写入excel
	 * @param cellMap
	 * @return
	 * @throws Exception
	 */
	public static String writeExcel(Map cellMap) throws Exception
	{
		String path=(String)cellMap.get("path");
		String temp=Config.TEMP_DIR;
		/*创建目录*/
		String newpath=path.substring(path.lastIndexOf(Config.FILESEPARATOR)+1,path.length());
		File fl=new File(path);
		if(!fl.exists()){throw new Exception(path+"无此excel模版!");}
		File file = new File(new File(temp),newpath);
		if(!file.getParentFile().exists()){file.getParentFile().mkdirs();}
		FileUtil.copyFile(fl, file);
		String[] ARRCOLS =
		{ "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T",
				"U", "V", "W", "X", "Y", "Z","AA","AB","AC","AD","AE","AF","AG","AH","AI","AJ","AK","AL","AM","AN","AO","AP","AQ","AR","AS","AT","AU","AV","AW","AX","AY","AZ"};
		
		HSSFWorkbook sourceWb = null;
		HSSFSheet sheet = null;
		FileInputStream inStream = new FileInputStream(path);
		POIFSFileSystem srcPOIFile = new POIFSFileSystem(inStream);
		sourceWb = new HSSFWorkbook(srcPOIFile);
		if (sourceWb.getNumberOfSheets() > 0)
		{
			sheet = sourceWb.getSheetAt(0);
		}
		inStream.close();

		HSSFRow row = null;
		HSSFCell cell = null;
		String tempateInfo = (String)cellMap.get(Key_TemplateInfo);//得到报表编号
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
					/*如果该单元格在Map中有值则把值写入该单元格*/
					if (cellMap.get(cellName) != null)
					{
						String cellValue = (String) cellMap.get(cellName);
						try
						{
							//2013-11-08 LYF:报表最大十家的13系列和14系列，需要特殊处理--防止出现科学计算法的问题，同时需要处理以0开头的字符串(如000855956导出后不应该为855956)
							if(((tempateInfo.startsWith("G13")||tempateInfo.startsWith("GF13"))&&cell.getCellNum()==2)//13系列的C列
								||((tempateInfo.startsWith("G14")||tempateInfo.startsWith("GF14"))&&cell.getCellNum()==3)){//14系统的D列
								cell.setCellValue(cellValue);//直接使用数据库中的值，避免数值转化出现的异常情况
							}else{/*数值型单元格*/
								Double d = Double.valueOf(cellValue);
								cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
								cell.setCellValue(d);
							}
						}
						catch (Exception ex)
						{
							/*字符型单元格*/
							cell.setEncoding(HSSFCell.ENCODING_UTF_16);
							cell.setCellType(HSSFCell.CELL_TYPE_STRING);
							cell.setCellValue(cellValue);
						}
					}
				}
			}
		}
		FileOutputStream stream = new FileOutputStream(file);
		
		sourceWb.write(stream);
		stream.flush();
		stream.close();
		
		//打开导出的Excel，重新计算所有的公式区域并保存
		POI2Util.excelFormulaEval(file);
		
		//设定Excel中时间期数的占位符为具体的时间期数
		setHeadAndFooter(file,cellMap);

		return file.getPath();
	}

	/**
	 * 设定Excel中时间期数的占位符为具体的时间期数
	 * @param exclFilePath
	 * @param date
	 * @throws Exception
	 */
	public static void setHeadAndFooter(File exclFilePath, Map cellMap) throws Exception {
		String repInId=(String)cellMap.get("repInId");
		String reportFlg=(String)cellMap.get("reportFlg");
		
		if(repInId==null||repInId.length()==0||reportFlg==null||reportFlg.length()==0){
//				||!reportFlg.equals("1")){//目前设定为只影响1104报表
			return;
		}
		
		FileInputStream fis=null;		//文件输入流
		BufferedInputStream bis=null;	//缓存输入流
		FileOutputStream fos= null;		//文件输出流
		try {
			fis=new FileInputStream(exclFilePath);           
			bis=new BufferedInputStream(fis);
			
			Map beans=new HashMap();
			ReportInForm reportInForm=null;
			if(reportFlg.equals("1")){//银监报表
				reportInForm=StrutsReportInDelegate.getReportIn(new Integer(repInId));
			}else{//非银监报表
				reportInForm=com.fitech.gznx.service.AFReportDelegate.getReportIn(new Integer(repInId));
			}
			//设定机构名称，便于导出的时候显示
			if(reportInForm.getOrgId()!=null && !reportInForm.getOrgId().trim().equals("")){
				com.fitech.gznx.po.AfOrg org = com.fitech.gznx.service.AFOrgDelegate.getOrgInfo(reportInForm.getOrgId());
				if(org!=null && reportInForm!=null){
					reportInForm.setOrgName(org.getOrgName());
				}
			}
			beans.put("report",reportInForm);
			//数据格式化:目前将"报表日期： ${report.year} 年${report.term} 月"替换为"报表日期：2013 年6 月"
			XLSTransformer transformer = new XLSTransformer();
			transformer.registerCellProcessor(new StyleCellProcessor());
			HSSFWorkbook sourceWb=transformer.transformXLS(bis, beans);
					
			fos = new FileOutputStream(exclFilePath);
			sourceWb.write(fos);
		} catch (RuntimeException e) {
			e.printStackTrace();
		}finally{
			if(fis!=null) fis.close();
			if(bis!=null) bis.close();
			if(fos!=null) fos.close();
		}
	}
	/**
	 * 下载excel
	 * @param fileName
	 * @param response
	 */
	public static void downLoadExcel(String fileName,HttpServletResponse response) {
		try {
			String newpath=fileName.substring(fileName.lastIndexOf(Config.FILESEPARATOR)+1,fileName.lastIndexOf(".xls"));
			String path = java.net.URLDecoder.decode(fileName, "utf-8");
			File file = new File(path);
			if (!file.exists()) {
				response.setContentType("text/html;charset=utf-8");
				System.out.println("指定的文件"+file.getPath()+"不存在!");
				return;
			}
			response.setContentType("application/vnd.ms-excel");
			response.setHeader("Content-Disposition", "attachment; filename=" + newpath+".xls");
			FileInputStream fis = new FileInputStream(file);
			BufferedInputStream bis = new BufferedInputStream(fis);
			OutputStream out = response.getOutputStream();
			long j=0;
			byte[] b=new byte[1024];
			while(j<file.length()){
				int k=bis.read(b, 0, 1024);
				j+=k;
				out.write(b, 0, k);
			}
			out.flush();
			out.close();
			bis.close();
			fis.close();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
