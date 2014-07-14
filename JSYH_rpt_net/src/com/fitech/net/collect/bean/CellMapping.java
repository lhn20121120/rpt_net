package com.fitech.net.collect.bean;

import java.io.Serializable;

/**
*
* <p>Title: CellMapping  bean</p>
*
* <p>Description: 单元格映射类bean</p>
*
* <p>Copyright: Copyright (c) 2007</p>
*
* <p>Company: </p>
*
* @author   龚明
* @date     2009-09-13
* @version 1.0
*/
public class CellMapping implements Serializable {

    
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**单元格映射Id*/
    private Long cellMapId = null;
    
    /**报表映射*/
    private ReportMapping reportMapping = null;
    
    /**源单元格名称*/
    private String srcCellName = null;
    
    /**目标单元格名称*/
    private String tarCellName = null;
    
    /**源单元格行Id*/
    private Integer srcRowId = null;
    
    /**源单元格列Id*/
    private Integer srcColId = null;
    
    /**目标单元格行Id*/
    private Integer tarRowId = null;
    
    /**目标单元格列Id*/
    private Integer tarColId = null;
    
    /**构造函数*/
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
