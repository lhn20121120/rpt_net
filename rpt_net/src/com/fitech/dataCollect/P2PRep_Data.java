package com.fitech.dataCollect;
/**
 * 
 * �ϲ�������
 * @author Yao
 * 
 * */
public class P2PRep_Data {
	
	/**�к�*/
	private Integer rowId;
	/**�к�*/
	private String colId;
	/**ֵ*/
	private String value;
	/**��Ԫ����������*/
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
