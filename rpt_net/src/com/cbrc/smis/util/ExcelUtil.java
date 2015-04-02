package com.cbrc.smis.util;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.Region;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.jxls.Cell;
import org.jxls.CellProcessor;
import org.jxls.XLSTransformer;

import com.cbrc.smis.common.Config;
import com.cbrc.smis.hibernate.MCell;

/**
 * Excel����������
 * 
 * @author Yao
 * 
 */
public class ExcelUtil
{
	private static final String[] ARRCOLS =
	{ "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T",
			"U", "V", "W", "X", "Y", "Z", "AA", "AB", "AC", "AD", "AE", "AF", "AG", "AH", "AI", "AJ", "AK",
			"AL", "AM", "AN", "AO", "AP", "AQ", "AR", "AS", "AT", "AU", "AV", "AW", "AX", "AY", "AZ" };

	public static String[] getArrcols() {
		return ARRCOLS;
	}

	/**
	 * дExcel
	 * 
	 * @param templateFilePath
	 *            excelģ��·��
	 * @param cellMap
	 *            ��Ԫ��Map ��Ԫ������--��Ԫ��ֵ
	 * @param saveAsFilePath
	 *            ���Ϊ���ļ�·��
	 * @throws Exception
	 */
	public static boolean writeExcel(String templateFilePath, Map cellMap, String saveAsFilePath) throws Exception
	{
		if (templateFilePath == null || templateFilePath.equals("") || cellMap == null
				|| saveAsFilePath == null || saveAsFilePath.equals(""))
			return false;
		HSSFWorkbook sourceWb = null;
		HSSFSheet sheet = null;
		FileInputStream inStream = new FileInputStream(templateFilePath);
		POIFSFileSystem srcPOIFile = new POIFSFileSystem(inStream);
		sourceWb = new HSSFWorkbook(srcPOIFile);
		if (sourceWb.getNumberOfSheets() > 0)
		{
			sheet = sourceWb.getSheetAt(0);
		}
		inStream.close();

		HSSFRow row = null;
		HSSFCell cell = null;

		for (Iterator iter = sheet.rowIterator(); iter.hasNext();)
		{
			row = (HSSFRow) iter.next();
			for (short i = row.getFirstCellNum(), n = row.getLastCellNum(); i < n; i++)
			{
				cell = (HSSFCell) row.getCell(i);
				if (cell == null)
					continue;
				/* ����Ƿ��Ƿǹ�ʽ��Ԫ�� */
				if (cell.getCellType() != HSSFCell.CELL_TYPE_FORMULA)
				{
					if (cell.getCellNum() >= ARRCOLS.length)
						continue;
					String cellName = ARRCOLS[cell.getCellNum()] + (row.getRowNum() + 1);
					/* ����õ�Ԫ����Map����ֵ���ֵд��õ�Ԫ�� */
					if (cellMap.get(cellName) != null)
					{
						String cellValue = (String) cellMap.get(cellName);
						try
						{
							/* ��ֵ�͵�Ԫ�� */
							Double d = Double.valueOf(cellValue);
							cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
							cell.setCellValue(d.doubleValue());
						}
						catch (Exception ex)
						{
							/* �ַ��͵�Ԫ�� */
							cell.setEncoding(HSSFCell.ENCODING_UTF_16);
							cell.setCellType(HSSFCell.CELL_TYPE_STRING);
							cell.setCellValue(cellValue);
						}
					}
				}
			}
		}
		FileOutputStream stream = new FileOutputStream(saveAsFilePath);
		sourceWb.write(stream);
		stream.close();
		return true;
	}

	/**
	 * �����ƶ���ɫ����Ӧ�ĵ�Ԫ���б�	
	 * @param excelFilePath
	 * @param parseColor
	 * @return Map ��ɫ����Ԫ��List
	 *
	 */
	public static Map parseExcelColorCell(String excelFilePath, Integer[] parseColor) throws Exception
	{
		Map colorCellMap = new HashMap();
		for(int i=0;i<parseColor.length;i++)
		{
			List cellList = parseExcel(excelFilePath,parseColor[i]);
			if(cellList!=null && cellList.size()>0)
			{
				colorCellMap.put(parseColor[i], cellList);
			}
		}
		return colorCellMap;
	}
	
	/**
	 * ������Excel�е��������Ԫ��
	 * 
	 * @param excelFilePath
	 * @return
	 */
	public static List parseExcel(String excelFilePath, Integer parseColor) throws Exception
	{
		if (excelFilePath == null || excelFilePath.equals(""))
			return null;
		List cellList = new ArrayList();

		HSSFWorkbook sourceWb = null;
		HSSFSheet sheet = null;
		FileInputStream inStream = new FileInputStream(excelFilePath);
		POIFSFileSystem srcPOIFile = new POIFSFileSystem(inStream);
		sourceWb = new HSSFWorkbook(srcPOIFile);
		if (sourceWb.getNumberOfSheets() > 0)
		{
			sheet = sourceWb.getSheetAt(0);
		}

		HSSFRow row = null;
		HSSFCell cell = null;

		for (Iterator iter = sheet.rowIterator(); iter.hasNext();)
		{
			row = (HSSFRow) iter.next();
			for (short i = row.getFirstCellNum(), n = row.getLastCellNum(); i < n; i++)
			{
				cell = (HSSFCell) row.getCell(i);
				if (cell == null)
					continue;
				HSSFCellStyle style = cell.getCellStyle();
				if (style.getFillForegroundColor() == parseColor.intValue())
				{
					if (cell.getCellNum() >= ARRCOLS.length)
						continue;
					String cellName = ARRCOLS[cell.getCellNum()] + (row.getRowNum() + 1);
					cellList.add(cellName);
				}
			}
		}
		inStream.close();
		//�������еĺϲ���Ԫ��ɾ������ÿ���ϲ���Ԫ��ֻ���ϲ��ĵ�һ����Ԫ�����Ϣ
		removeMergedCell(sheet,cellList);
		return cellList;
	}
	
	/**
	 * �������еĺϲ���Ԫ��ɾ������ÿ���ϲ���Ԫ��ֻ���ϲ��ĵ�һ����Ԫ�����Ϣ	
	 * @param sheet
	 * @param cellList
	 *
	 */
	public static void removeMergedCell(HSSFSheet sheet,List cellList)
	{
		//����ÿ���ϲ���Ԫ��
		for (int i = 0; i < sheet.getNumMergedRegions(); i++)
		{
			Region region = (Region) sheet.getMergedRegionAt(i);

			// �õ��ϲ���Ԫ��ķ�Χ��
			int rowFrom = region.getRowFrom();
			int rowTo = region.getRowTo();
			int colFrom = region.getColumnFrom();
			int colTo = region.getColumnTo();
			
			//�ӵ�Ԫ�񼯺����Ƴ��õ�Ԫ�����ϲ��ĸ�����Ԫ��
			for (int row = rowFrom; row <= rowTo; row++)
				for (int col = colFrom; col <= colTo; col++)
				{
					if (row == rowFrom && col == colFrom)
						continue;
					String cellName = ARRCOLS[col] + (row + 1);
					cellList.remove(cellName);
				}
		}
		
	}
	


	/**
	 * 
	 * ����˵��:�÷����������EXCEL��Ԫ���еı���ɫ
	 * 
	 * @author chenbing
	 * @date 2007-4-12
	 * @param excelFile
	 * @return
	 */
	public static boolean cleanExcelCellBg(File excelFile)
	{

		boolean result = false;

		InputStream input = null;

		OutputStream output = null;

		try
		{

			input = new FileInputStream(excelFile);
			POIFSFileSystem fileSystem = new POIFSFileSystem(input);
			HSSFWorkbook wb = new HSSFWorkbook(fileSystem);
			HSSFSheet sheet = wb.getSheetAt(0);

			for (int i = sheet.getFirstRowNum(); i <= sheet.getLastRowNum(); i++)
			{

				HSSFRow row = (HSSFRow) sheet.getRow(i);
				if (row != null)
				{
					for (int j = row.getFirstCellNum(); j < row.getLastCellNum(); j++)
					{
						HSSFCell cell = row.getCell((short) j);
						if (cell != null)
						{
							HSSFCellStyle cellStyle = cell.getCellStyle();
							if (cellStyle.getFillForegroundColor() == Config.NUMBER_CELL_BGCOLOR.intValue()
									|| cellStyle.getFillForegroundColor() == Config.STRING_CELL_BGCOLOR.intValue())
							{
								cellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
								cellStyle.setFillForegroundColor((short) 65);
								cell.setCellStyle(cellStyle);
							}
						}
					}
				}
			}
			output = new FileOutputStream(excelFile);
			output.flush();
			wb.write(output);
			result = true;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			result = false;
		}
		finally
		{

			try
			{
				input.close();
				output.close();
			}
			catch (Exception e)
			{
				e.printStackTrace();
				result = false;
			}
		}
		return result;
	}

	/**
	 * �������������A3��ֳ�A,3
	 */
	public static String[] splitRowAndCol(String cellName)
	{
		String[] rowAndCol = new String[2];
		if (cellName == null || cellName.equals(""))
		{
			return null;
		}
		else
		{
			String[] temp = cellName.split("\\d");
			rowAndCol[0] = temp[0];
			rowAndCol[1] = cellName.substring(rowAndCol[0].length(), cellName.length());
			return rowAndCol;
		}
	}

}

class StyleCellProcessor implements CellProcessor
{
	public StyleCellProcessor()
	{
	}

	/**
	 * ��Ԫ����
	 * 
	 * @author gj
	 * @param cell
	 *            Cell
	 * @param namedCells
	 *            Map
	 * @return void
	 */
	public void processCell(Cell cell, Map namedCells)
	{
		if (cell == null)
			return;

		try
		{
			HSSFCell hssfCell = cell.getHssfCell();
			if (hssfCell == null)
				return;

			hssfCell.setEncoding(HSSFCell.ENCODING_UTF_16);
			// hssfCell.setCellType(HSSFCell.CELL_TYPE_STRING);
			cell.setHssfCell(hssfCell);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

}
