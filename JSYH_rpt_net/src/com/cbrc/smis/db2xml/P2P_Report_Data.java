package com.cbrc.smis.db2xml;

/**
 * ��Ե㱨��Ԫ������
 * 
 * @author Ҧ��
 * 
 */
public class P2P_Report_Data {

    /**
     * ��Ԫ���к�
     */
    private String cellRow = null;

    /**
     * ��Ԫ���к�
     */
    private String cellCol = null;

    /**
     * ��Ԫ��ֵ
     */
    private String value = null;

    /**
     * ��Ԫ������
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
