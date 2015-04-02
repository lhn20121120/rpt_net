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
	 * ����ԴExcel�ļ������Sheet��Ŀ��Excel�ļ�
	 * @param srcExcelUrl ԴExcel�ļ�
	 * @param objExcelUrl Ŀ��Excel�ļ�
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
			 * ǿ���Թ涨һ��Excelֻ����һ��sheet
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
	 * �п���
	 * @param targetSheet Ŀ��Excel������
	 * @param sourceSheet ��������Excel������
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
	     * ���sheetΪnullֱ�ӷ���
	     */
	    if (targetSheet == null || sourceSheet == null) {
	    	return;
	    }
	    
	    /*
	     * �����ϲ��ĵ�Ԫ��
	     */
	    for(int i=0,n=sourceSheet.getNumMergedRegions(); i<n; i++){
	    	region = sourceSheet.getMergedRegionAt(i);
	    	targetRowFrom = region.getRowFrom();
	    	
	    	targetSheet.addMergedRegion(region);
	    }
	    
	    
	    //�����в��������
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
				 * ������Ԫ�����ø������
				 */
				targetCell = targetRow.createCell(i);
				targetSheet.setColumnWidth((i), sourceSheet.getColumnWidth(i));
				targetCell.setEncoding(HSSFCell.ENCODING_UTF_16);
				HSSFCellStyle cellStyle = targetWb.createCellStyle();				
			    copyStyle(cellStyle, sourceCell.getCellStyle());			    
			    targetCell.setCellStyle(cellStyle);
			    cellStyle = null;
			    
			    /*
			     * �жϸ�font�Ƿ��Ѿ����ڣ������ͬ�Ͳ��ڴ����µ���ʽ�������½�
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
			     * ���ݲ�ͬ������������ֵ
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
			       					 * ������ʽ�����������ֵ,
			       					 * �˾��Ȳ����ѣ���Ϊpoi�Թ�ʽ֧��Ƿ�ѡ�
			       					 * ��apache�ĳ����˽��Ͳ���(wunaigang��ע,�Ǻ�)			       					 
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
	 * �����ʽ����ĺ�����������������now()��today()��
	 * ��ôͨ��getCellFormula()ȡ������ֵ����now(ATTR(semiVolatile))��today(ATTR(semiVolatile))��
	 * ������ֵд��Excel�ǻ����ģ�parseFormula��������Ĺ��ܼܺ򵥣����ǰ�ATTR(semiVolatile)ɾ��
	 * @param pPOIFormula ��ʽ�ַ���
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
	 * ������ʽ
	 * @param targetStyle  Ŀ�굥Ԫ����ʽ
	 * @param sourceStyle  Դ��Ԫ����ʽ
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
	 * ��������
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
	 * �ж���������Ƿ�һ��
	 * @param targetFont ������   Ŀ������
	 * @param sourceFont��Դ����
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
	 * �жϸ���������ڹ��������Ƿ��Ѿ�����,��������ڷ���-1
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
	
	/**��ȡ����������HSSFSheet*/
	public static HSSFSheet getNeedSheet(MExcelChildReportForm mExcelChildReportForm,InputStream inStream,int i)
	{
		HSSFWorkbook wb=null;
		HSSFSheet sheet=null;
		/**��ȡHSSFWorkbook����*/
		try {
			wb = new HSSFWorkbook(inStream);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		/**��ȡHSSFSheet����*/
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
