package com.cbrc.smis.entity;

/**
 * 单元格实体对象
 * @author Yao
 *
 */
public class Cell
{
	private int repInId;

	private int templateId;

	private String cellName;

	private String cellValue;

	private int cellId;

	private int dataType;

	private String colId;

	private int rowId;
	

	public int getCellId()
	{
		return cellId;
	}

	public void setCellId(int cellId)
	{
		this.cellId = cellId;
	}

	public String getCellName()
	{
		return cellName;
	}

	public void setCellName(String cellName)
	{
		this.cellName = cellName;
	}

	public String getCellValue()
	{
		return cellValue;
	}

	public void setCellValue(String cellValue)
	{
		this.cellValue = cellValue;
	}

	public String getColId()
	{
		return colId;
	}

	public void setColId(String colId)
	{
		this.colId = colId;
	}

	public int getDataType()
	{
		return dataType;
	}

	public void setDataType(int dataType)
	{
		this.dataType = dataType;
	}



	public int getRepInId()
	{
		return repInId;
	}

	public void setRepInId(int repInId)
	{
		this.repInId = repInId;
	}

	public int getRowId()
	{
		return rowId;
	}

	public void setRowId(int rowId)
	{
		this.rowId = rowId;
	}

	public int getTemplateId()
	{
		return templateId;
	}

	public void setTemplateId(int templateId)
	{
		this.templateId = templateId;
	}


}
