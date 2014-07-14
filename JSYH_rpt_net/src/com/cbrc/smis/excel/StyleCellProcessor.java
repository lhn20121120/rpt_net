
package com.cbrc.smis.excel;

import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.jxls.Cell;
import org.jxls.CellProcessor;

public class StyleCellProcessor implements CellProcessor{
	public StyleCellProcessor(){}
	/**
	 * 单元格处理
	 * 
	 * @param cell Cell
	 * @param namedCells Map
	 * @return void
	 */
	public void processCell(Cell cell,Map namedCells){
		if(cell==null) return;
		
		try{
			HSSFCell hssfCell=cell.getHssfCell();
			if(hssfCell==null) return;
			
			hssfCell.setEncoding(HSSFCell.ENCODING_UTF_16);
			//hssfCell.setCellType(HSSFCell.CELL_TYPE_STRING);
			cell.setHssfCell(hssfCell);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}

