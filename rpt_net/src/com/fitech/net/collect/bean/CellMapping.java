package com.fitech.net.collect.bean;

import java.io.Serializable;

/**
*
* <p>Title: CellMapping  bean</p>
*
* <p>Description: ��Ԫ��ӳ����bean</p>
*
* <p>Copyright: Copyright (c) 2007</p>
*
* <p>Company: </p>
*
* @author   ����
* @date     2009-09-13
* @version 1.0
*/
public class CellMapping implements Serializable {

    
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**��Ԫ��ӳ��Id*/
    private Long cellMapId = null;
    
    /**����ӳ��*/
    private ReportMapping reportMapping = null;
    
    /**Դ��Ԫ������*/
    private String srcCellName = null;
    
    /**Ŀ�굥Ԫ������*/
    private String tarCellName = null;
    
    /**Դ��Ԫ����Id*/
    private Integer srcRowId = null;
    
    /**Դ��Ԫ����Id*/
    private Integer srcColId = null;
    
    /**Ŀ�굥Ԫ����Id*/
    private Integer tarRowId = null;
    
    /**Ŀ�굥Ԫ����Id*/
    private Integer tarColId = null;
    
    /**���캯��*/
    public CellMapping() {
       
    }

    public Long getCellMapId() {
        return cellMapId;
    }

    public void setCellMapId(Long cellMapId) {
        this.cellMapId = cellMapId;
    }

    public ReportMapping getReportMapping() {
        return reportMapping;
    }

    public void setReportMapping(ReportMapping reportMapping) {
        this.reportMapping = reportMapping;
    }

    public String getSrcCellName() {
        return srcCellName;
    }

    public void setSrcCellName(String srcCellName) {
        this.srcCellName = srcCellName;
    }

   
    public String getTarCellName() {
        return tarCellName;
    }

    public void setTarCellName(String tarCellName) {
        this.tarCellName = tarCellName;
    }

    public Integer getSrcColId() {
        return srcColId;
    }

    public void setSrcColId(Integer srcColId) {
        this.srcColId = srcColId;
    }

    public Integer getSrcRowId() {
        return srcRowId;
    }

    public void setSrcRowId(Integer srcRowId) {
        this.srcRowId = srcRowId;
    }

    public Integer getTarColId() {
        return tarColId;
    }

    public void setTarColId(Integer tarColId) {
        this.tarColId = tarColId;
    }

    public Integer getTarRowId() {
        return tarRowId;
    }

    public void setTarRowId(Integer tarRowId) {
        this.tarRowId = tarRowId;
    }
  
}
