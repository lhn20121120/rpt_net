package com.cbrc.smis.action;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.RequestUtils;

import com.cbrc.smis.common.Config;
import com.cbrc.smis.form.ReportInForm;
import com.cbrc.smis.util.FitechException;
import com.cbrc.smis.util.ReportExcelHandler;

/**
 * 自动填零
 * 
 * @2008-01-03
 * @author jcm
 *
 */
public class AutoFillZeroReportAction extends Action{
	private FitechException log = new FitechException(AutoFillZeroReportAction.class);

	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException{
		
		boolean result = false;
		ReportInForm reportInForm = (ReportInForm) form;
		RequestUtils.populate(reportInForm, request);        		
		String fileName = request.getParameter("fileName");
		String excelPath = request.getParameter("excelPath");
		
		
		InputStream inStream = null;
		OutputStream outStream = null;
		HSSFWorkbook workBook = null;
		HSSFSheet sheet = null;
		HSSFRow hssfRow = null;
		HSSFCell hssfCell = null;
		int offset = 0;
		
		try {
			if(excelPath != null && fileName != null){
				StringBuffer excelRealPath = new StringBuffer(Config.WEBROOTPATH);
				String[] pathTemp = excelPath.split("/");
				for(int i=0;i<pathTemp.length;i++){
					excelRealPath.append(pathTemp[i]).append(File.separator);
				}
				excelRealPath.append(fileName);
				
				ReportExcelHandler excelHandler = new ReportExcelHandler(reportInForm.getRepInId(),null);
				List cellList = excelHandler.getCellList();  //取得报表所有单元格
				
				if(cellList != null && cellList.size() > 0){
					//获取法人报表的偏移量
					offset = excelHandler.getOffset();
					
					inStream = new FileInputStream(excelRealPath.toString());
					workBook = new HSSFWorkbook(new POIFSFileSystem(inStream));					
					if(workBook.getNumberOfSheets() > 0)
						sheet = workBook.getSheetAt(0);
					
					if(sheet != null){
						for(int index=0;index<cellList.size();index++){
							com.cbrc.smis.entity.Cell cellEntity = (com.cbrc.smis.entity.Cell) cellList.get(index);
							if(cellEntity.getCellName() != null){
								int rowIndex = cellEntity.getRowId() - 1 + offset;
								short colIndex = (short)excelHandler.convertColStringToNum(cellEntity.getColId());
								hssfRow = sheet.getRow(rowIndex);
								if(hssfRow == null) continue;
								hssfCell = hssfRow.getCell(colIndex);
								if(hssfCell == null) continue;
								
								/**
								 * 只对空白单元格设置自动填零
								 * 单元格中有公式或是已经有数据值
								 * 不作改动
								 */
								if(hssfCell.getCellType() == HSSFCell.CELL_TYPE_BLANK)
//										|| hssfCell.getCellType() == HSSFCell.CELL_TYPE_NUMERIC)
									hssfCell.setCellValue(0);									
							}
						}
					}
					inStream.close();
					
					/**
					 * 将填零的Excel保存到输出流中
					 */
					outStream = new FileOutputStream(excelRealPath.toString());
					workBook.write(outStream);
					outStream.flush();
					outStream.close();
					
					result = true;
				}
			}								
		} catch (FileNotFoundException e) {
			result = false;
			log.printStackTrace(e);
		} catch (IOException e) {
			result = false;
			log.printStackTrace(e);
		} catch (Exception e) {
			result = false;
			log.printStackTrace(e);
		}

		PrintWriter out = response.getWriter();		
		response.setContentType("text/plain");
		response.setHeader("Cache-control", "no-cache");
		if (result == false)
			out.println("false");
		else
			out.println("true");
		out.close();
		return null;
	}
}
