/*
 * Created on 2006-5-16
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.fitech.net.common;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.Region;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

import com.cbrc.smis.form.MExcelChildReportForm;

/**
 * @author Administrator
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class ExcelParser {
	public static int sheetIndex = 0;
	/**
	 * 复制源Excel文件里面的Sheet到目标Excel文件
	 * @param srcExcelUrl 源Excel文件
	 * @param objExcelUrl 目标Excel文件
	 * 
	 */
	public void copySheet(String srcExcelUrl, String objExcelUrl){
		
		FileInputStream inStream = null;
		FileInputStream inStream1 = null;
		HSSFWorkbook sourceWb = null;
		HSSFWorkbook targetWb = null;
		FileOutputStream outStream = null;
		
		try {
			inStream = new FileInputStream(srcExcelUrl);
			POIFSFileSystem srcFile = new POIFSFileSystem(inStream);
			sourceWb = new HSSFWorkbook(srcFile);			
			//targetWb = new HSSFWorkbook();
			inStream.close();
			inStream1 = new FileInputStream(objExcelUrl);
			POIFSFileSystem targetFile = new POIFSFileSystem(inStream1);			
			targetWb = new HSSFWorkbook(targetFile);
			inStream1.close();
			/*
			 * 强制性规定一个Excel只能有一个sheet
			 */
			
			HSSFSheet sourceSheet = sourceWb.getSheetAt(0);
			HSSFSheet targetSheet = targetWb.createSheet("TargetSheet" + (sheetIndex++));
			
			copyRows(targetWb, sourceWb, targetSheet, sourceSheet);
			outStream = new FileOutputStream(objExcelUrl);
			targetWb.write(outStream);
			outStream.flush();
			outStream.close();
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch(IOException e){
			e.printStackTrace();
		}
	}
	

	/**
	 * 行拷贝
	 * @param targetSheet 目标Excel工作表
	 * @param sourceSheet 待拷贝的Excel工作表
	 */
	private	void copyRows(HSSFWorkbook targetWb, 
						  HSSFWorkbook sourceWb,
						  HSSFSheet targetSheet, 
						  HSSFSheet sourceSheet){		
		HSSFRow sourceRow = null;
	    HSSFRow targetRow = null;
	    HSSFCell sourceCell = null;
	    HSSFCell targetCell = null;	    
	    Region region = null;
	    int cType;
	    int targetRowFrom;
	    int targetRowTo;	    

	    /*
	     * 如果sheet为null直接返回
	     */
	    if (targetSheet == null || sourceSheet == null) {
	    	return;
	    }
	    
	    /*
	     * 拷贝合并的单元格
	     */
	    for(int i=0,n=sourceSheet.getNumMergedRegions(); i<n; i++){
	    	region = sourceSheet.getMergedRegionAt(i);
	    	targetRowFrom = region.getRowFrom();
	    	
	    	targetSheet.addMergedRegion(region);
	    }
	    
	    
	    //拷贝行并填充数据
	    Iterator rowIter = sourceSheet.rowIterator();

		short rowIndex = 0;
		while(rowIter.hasNext()){			
			sourceRow = (HSSFRow)rowIter.next();
			if (sourceRow == null) {
				continue;
			}
			targetRow = targetSheet.createRow(rowIndex++);
			targetRow.setHeight(sourceRow.getHeight());			
						
			for(short i=sourceRow.getFirstCellNum(),n=sourceRow.getLastCellNum(); i<n; i++){
				sourceCell = (HSSFCell)sourceRow.getCell(i);
				if (sourceCell == null) {
					continue;
				}
				/*
				 * 建立单元格配置各项参数
				 */
				targetCell = targetRow.createCell(i);
				targetSheet.setColumnWidth((i), sourceSheet.getColumnWidth(i));
				targetCell.setEncoding(HSSFCell.ENCODING_UTF_16);
				HSSFCellStyle cellStyle = targetWb.createCellStyle();				
			    copyStyle(cellStyle, sourceCell.getCellStyle());			    
			    targetCell.setCellStyle(cellStyle);
			    cellStyle = null;
			    
			    /*
			     * 判断该font是否已经存在，如果相同就不在创建新的样式，否则新建
			     */
			    HSSFFont sourceFont = sourceWb.getFontAt(sourceCell.getCellStyle().getFontIndex());
			    HSSFFont targetFont = null;
			    short fontIndex = getExistFontIndex(targetWb, sourceFont);
			    if (fontIndex != -1) {
			    	targetFont = targetWb.getFontAt(fontIndex);
			    }else{
			    	targetFont = targetWb.createFont();
			    	copyFont(targetFont, sourceFont);			    	
			    }
			    targetCell.getCellStyle().setFont(targetFont);
			    sourceFont = null;
			    targetFont = null;
			    
			    /*
			     * 根据不同数据类型设置值
			     */
			    cType = sourceCell.getCellType();			    
			    			    
			    switch (cType){
			       case HSSFCell.CELL_TYPE_BOOLEAN: 
			       					targetCell.setCellType(cType);
			       					targetCell.setCellValue(sourceCell.getBooleanCellValue());			       					
			       					break;
			       case HSSFCell.CELL_TYPE_ERROR:
			       					targetCell.setCellType(cType);
			       					targetCell.setCellErrorValue(sourceCell.getErrorCellValue());
			        				break;            
			       case HSSFCell.CELL_TYPE_FORMULA:
			       					/*
			       					 * 读出公式储存格计算後的值,
			       					 * 此举迫不得已，因为poi对公式支持欠佳。
			       					 * 对apache的崇拜因此降低不少(wunaigang批注,呵呵)			       					 
			       					 */
			       					targetCell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
			       					targetCell.setCellValue(sourceCell.getNumericCellValue());
			        				break;
			       case HSSFCell.CELL_TYPE_NUMERIC:
			       					targetCell.setCellType(cType);
			       					targetCell.setCellValue(sourceCell.getNumericCellValue());
			        				break;
			       case HSSFCell.CELL_TYPE_STRING:	
			       					targetCell.setCellType(cType);
									targetCell.setCellValue(String.valueOf(sourceCell.getStringCellValue()));			       
			        				break;	
			    }
			}
			
		}	    
	    
	}
	
	/**
	 * 如果公式里面的函数不带参数，比如now()或today()，
	 * 那么通过getCellFormula()取出来的值就是now(ATTR(semiVolatile))和today(ATTR(semiVolatile))，
	 * 这样的值写入Excel是会出错的，parseFormula这个函数的功能很简单，就是把ATTR(semiVolatile)删掉
	 * @param pPOIFormula 公式字符串
	 * @return
	 */
	private String parseFormula(String pPOIFormula)  {
		final String cstReplaceString = "ATTR(semiVolatile)"; //$NON-NLS-1$
		StringBuffer result = new StringBuffer();
		int index = pPOIFormula.indexOf(cstReplaceString);
        if (index >= 0) {
        	result.append(pPOIFormula.substring(0, index));
        	result.append(pPOIFormula.substring(index + cstReplaceString.length()));
        }else{
        	result.append(pPOIFormula);
        }
    
        return result.toString();
    }
 
	/**
	 * 复制样式
	 * @param targetStyle  目标单元格样式
	 * @param sourceStyle  源单元格样式
	 */
	private void copyStyle( HSSFCellStyle targetStyle, HSSFCellStyle sourceStyle){
		
			targetStyle.setAlignment(sourceStyle.getAlignment());
			targetStyle.setBorderBottom(sourceStyle.getBorderBottom());
			targetStyle.setBorderLeft(sourceStyle.getBorderLeft());
			targetStyle.setBorderRight(sourceStyle.getBorderRight());
			targetStyle.setBorderTop(sourceStyle.getBorderTop());
			targetStyle.setBottomBorderColor(sourceStyle.getBottomBorderColor());
			targetStyle.setDataFormat(sourceStyle.getDataFormat());
			targetStyle.setFillBackgroundColor(sourceStyle.getFillBackgroundColor());
			targetStyle.setFillForegroundColor(sourceStyle.getFillForegroundColor());
			targetStyle.setFillPattern(sourceStyle.getFillPattern());
			targetStyle.setHidden(sourceStyle.getHidden());
			targetStyle.setIndention(sourceStyle.getIndention());
			targetStyle.setLeftBorderColor(sourceStyle.getLeftBorderColor());
			targetStyle.setRightBorderColor(sourceStyle.getRightBorderColor());
			targetStyle.setRotation(sourceStyle.getRotation());
			targetStyle.setTopBorderColor(sourceStyle.getTopBorderColor());			
			targetStyle.setVerticalAlignment(sourceStyle.getVerticalAlignment());
			targetStyle.setWrapText(sourceStyle.getWrapText());
			
	}
		
	/**
	 * 复制字体
	 * @param targetFont
	 * @param sourceFont
	 */
	private void copyFont(HSSFFont targetFont, HSSFFont sourceFont){
		
		targetFont.setBoldweight(sourceFont.getBoldweight());
		targetFont.setColor(sourceFont.getColor());
		targetFont.setFontHeight(sourceFont.getFontHeight());
		targetFont.setFontHeightInPoints(sourceFont.getFontHeightInPoints());
		targetFont.setFontName(sourceFont.getFontName());
		targetFont.setItalic(sourceFont.getItalic());
		targetFont.setStrikeout(sourceFont.getStrikeout());
		targetFont.setTypeOffset(sourceFont.getTypeOffset());
		targetFont.setUnderline(sourceFont.getUnderline());
		
	}
	
	/**
	 * 判断字体对象是否一致
	 * @param targetFont 　　　   目标字体
	 * @param sourceFont　源字体
	 * @return
	 */
	private boolean fontEquals(HSSFFont targetFont, HSSFFont sourceFont){
		boolean resultFlag = false;
		
		if (targetFont == sourceFont){
			resultFlag = true;
		}
		
		if (sourceFont.getBoldweight() == targetFont.getBoldweight() &&
				sourceFont.getColor() == targetFont.getColor() && 
				sourceFont.getFontHeight() == targetFont.getFontHeight() && 
				sourceFont.getFontHeightInPoints() == targetFont.getFontHeightInPoints() && 
				sourceFont.getFontName() == targetFont.getFontName() && 
				sourceFont.getItalic() == targetFont.getItalic() && 
				sourceFont.getStrikeout() == targetFont.getStrikeout() && 
				sourceFont.getTypeOffset() == targetFont.getTypeOffset() && 
				sourceFont.getUnderline() == targetFont.getUnderline()) {
			resultFlag = true;
		}
		
		return resultFlag;
	}
	
	/**
	 * 判断该字体对象在工作簿中是否已经存在,如果不存在返回-1
	 * @param wb
	 * @param sourceFont
	 * @return
	 */
	private short getExistFontIndex(HSSFWorkbook wb, HSSFFont sourceFont){
		boolean resultFlag = false;
		short index = -1;
		if (wb == null || sourceFont == null) {
			return -1;
		}
		
		HSSFFont tmpFont = null;
		for(short i=0,n=wb.getNumberOfFonts(); i<n; i++){
			tmpFont = wb.getFontAt(i);
			if (fontEquals(sourceFont, tmpFont)) {
				index = i;
				break;
			}
		}
		
		return index;
	}
	
	/**获取符合条件的HSSFSheet*/
	public static HSSFSheet getNeedSheet(MExcelChildReportForm mExcelChildReportForm,InputStream inStream,int i)
	{
		HSSFWorkbook wb=null;
		HSSFSheet sheet=null;
		/**获取HSSFWorkbook对象*/
		try {
			wb = new HSSFWorkbook(inStream);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		/**获取HSSFSheet对象*/
		if(wb!=null){
			sheet=wb.getSheetAt(0);
		}
		return sheet;
	}
	
	public static void main(String args[]){
		ExcelParser test = new ExcelParser();
		test.copySheet("d:\\test\\G12.xls", "d:\\test\\aa.xls");
		// System.out.println("this ok");
	}

}
