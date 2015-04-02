package com.fitech.gznx.excel;

import java.io.File;
import java.io.FileInputStream;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.cbrc.smis.util.FitechException;
import com.fitech.gznx.common.StringUtil;
import com.cbrc.smis.util.FitechMessages;

public class QDReportExcelHandler {
	private static FitechException log = new FitechException(QDReportExcelHandler.class);

	/* 报表主标题 */
	public String title = "";
	/* 报表附注名称 */
	public String subTitle = "";
	
	public FitechMessages messages=null;

	public QDReportExcelHandler() {
	}

	public QDReportExcelHandler(String excelFilePath,FitechMessages messager) throws Exception{
		try {
			messages = messager;
			getDataFromExcels(excelFilePath);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	/**
	 * 取得清单式Excel报表的主标题和附注名称
	 * 
	 */
	public void getDataFromExcels(String excelFilePath) throws Exception {
		FileInputStream inStream = null;
		try {
			File excelFile = new File(excelFilePath);
			if (!excelFile.exists()){
				return;
			}
			
			inStream = new FileInputStream(excelFile);
			HSSFWorkbook workbook = new HSSFWorkbook(inStream);
			if (workbook == null)
				return;

			HSSFSheet sheet = workbook.getSheetAt(0);
			HSSFRow rowTitle = sheet.getRow(0);

			if (rowTitle != null && rowTitle.cellIterator().hasNext() && null != rowTitle.getCell((short) 0).getStringCellValue()) {
				title = rowTitle.getCell((short) 0).getStringCellValue().replaceAll(" ", "");
				if (title.indexOf("GF04利润表") > -1) {
					rowTitle = sheet.getRow(2);
					if (rowTitle != null && rowTitle.cellIterator().hasNext() && null != rowTitle.getCell((short) 0).getStringCellValue()) {
						if (rowTitle.getCell((short) 0).getStringCellValue().indexOf("附注项目") > -1) {
							title = "GF04利润表附注";
						}
					}
				}
				if (title == null || title.equals("")) {
					//rowTitle = sheet.getRow(1);
					title = rowTitle.getCell((short) 1).getStringCellValue().replaceAll(" ", "");
					if(title == null || title.equals("")){
						rowTitle = sheet.getRow(1);
						title = rowTitle.getCell((short) 0).getStringCellValue().replaceAll(" ", "");
					}
					
				}
			} else {
				rowTitle = sheet.getRow(1);
				if (rowTitle != null && rowTitle.cellIterator().hasNext() && null != rowTitle.getCell((short) 1).getStringCellValue()) {
					title = rowTitle.getCell((short) 1).getStringCellValue().replaceAll(" ", "");
				}
			}

			int subTitleCol = 0;
			int subTitleRow = 2;
			HSSFRow row1 = sheet.getRow(subTitleRow);
			if (null != row1.getCell((short) subTitleCol) && row1.getCell((short) subTitleCol).getCellType() == HSSFCell.CELL_TYPE_STRING) {
				subTitle = row1.getCell((short) subTitleCol).getStringCellValue().trim();
			}

		} catch (Exception e) {
			messages.add("读取报表文件出现错误!");	
			throw e ;
		} finally {
			if (inStream != null)
				inStream.close();
		}
	}

	public FitechMessages getMessages() {
		return messages;
	}

	public void setMessages(FitechMessages messages) {
		this.messages = messages;
	}

}
