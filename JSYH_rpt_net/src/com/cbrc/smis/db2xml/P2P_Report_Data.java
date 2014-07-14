package com.cbrc.smis.db2xml;

/**
 * 点对点报表单元格数据
 * 
 * @author 姚捷
 * 
 */
public class P2P_Report_Data {

    /**
     * 单元格行号
     */
    private String cellRow = null;

    /**
     * 单元格列号
     */
    private String cellCol = null;

    /**
     * 单元格值
     */
    private String value = null;

    /**
     * 单元格名称
     */
    private String cellName = null;

    public P2P_Report_Data(String cellRow, String cellCol, String value,String cellName) 
    {
        this.cellRow = cellRow;
        this.cellCol = cellCol;
        this.value = value;
        this.cellName = cellName;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getCellCol() {
        return cellCol;
    }

    public void setCellCol(String cellCol) {
        this.cellCol = cellCol;
    }

    public String getCellRow() {
        return cellRow;
    }

    public void setCellRow(String cellRow) {
        this.cellRow = cellRow;
    }

    public String getCellName() {
        return cellName;
    }

    public void setCellName(String cellName) {
        this.cellName = cellName;
    }

}
