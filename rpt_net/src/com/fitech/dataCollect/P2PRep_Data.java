package com.fitech.dataCollect;
/**
 * 
 * 合并表数据
 * @author Yao
 * 
 * */
public class P2PRep_Data {
	
	/**行号*/
	private Integer rowId;
	/**列号*/
	private String colId;
	/**值*/
	private String value;
	/**单元格数据类型*/
	private String dataType;
	
	public String getDataType() {
		return dataType;
	}
	public void setDataType(String dataType) {
		this.dataType = dataType;
	}
	
	public String getColId() {
		return colId;
	}
	public void setColId(String colId) {
		this.colId = colId;
	}
	public Integer getRowId() {
		return rowId;
	}
	public void setRowId(Integer rowId) {
		this.rowId = rowId;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	
	

}
