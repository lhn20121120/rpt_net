package com.cbrc.smis.util;

import com.fitech.net.common.StringTool;

public class DataArea {
	
	private String startRow;
	private String startCol;
	private String endRow;
	private String endCol;
	
	public DataArea(){}

	public String getEndCol() {
		return endCol;
	}

	public void setEndCol(String endCol) {
		this.endCol = endCol;
	}

	public String getEndRow() {
		return endRow;
	}

	public void setEndRow(String endRow) {
		this.endRow = endRow;
	}

	public String getStartCol() {
		return startCol;
	}

	public void setStartCol(String startCol) {
		this.startCol = startCol;
	}

	public String getStartRow() {
		return startRow;
	}

	public void setStartRow(String startRow) {
		this.startRow = startRow;
	}

	/**��֤���������Ч��*/
	public static boolean isValid(DataArea dataArea)
	{
		boolean flag=false;
		/**��鿪ʼ�кͽ������Ƿ�������*/
		if(StringTool.strIsNum(dataArea.getStartRow()) && StringTool.strIsNum(dataArea.getEndRow())){
			/**��鿪ʼ�кͽ������Ƿ�����ĸ*/
			if(StringTool.strIsLetter(dataArea.getStartCol()) && StringTool.strIsLetter(dataArea.getEndCol())){
				/**���������Ƿ����ڿ�ʼ��*/
				if(Integer.parseInt(dataArea.getEndRow())>=Integer.parseInt(dataArea.getStartRow())){
					/**���������Ƿ����ڿ�ʼ��*/
					if(compareCol(dataArea.getStartCol(),dataArea.getEndCol())){
						flag=true;
					}
				}
			}
		}
		return flag;
	}
	
	/**�Ƚ��еĴ�С*/
	public static boolean compareCol(String startCol,String endCol)
	{
		boolean flag=false;
		
		if(startCol.length()==1 && endCol.length()==1){
			if(Character.getNumericValue(endCol.charAt(0))>=Character.getNumericValue(startCol.charAt(0))){
				return true;
			}
		}
		
		if(startCol.length()==1 && endCol.length()>1){
			return true;
		}
		
		if(startCol.length()>1 && endCol.length()==1){
			return false;
		}
		
		return flag;
	}
	
}
