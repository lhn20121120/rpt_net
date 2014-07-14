package com.cbrc.smis.hibernate;

import java.io.Serializable;
import java.util.Set;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/** @author Hibernate CodeGenerator */
public class MCell implements Serializable {

    /** identifier field */
    private Integer cellId;

    /** nullable persistent field */
    private String cellName;

    /** nullable persistent field */
    private Integer dataType;

    /** nullable persistent field */
    private Integer rowId;

    /** nullable persistent field */
    private String colId;

    
    /**单元格汇总类型2006-09-17龚明  添加*/
    private Integer collectType;
    
    /** persistent field */
    private com.cbrc.smis.hibernate.MChildReport MChildReport =new MChildReport();

    /** persistent field */
    private Set reportInInfos;

    /** persistent field */
    private Set MCellToFormus;

    /** persistent field */
    private Set abnormityChanges;

    /** full constructor */
    public MCell(Integer cellId, String cellName, Integer dataType, Integer rowId, String colId, com.cbrc.smis.hibernate.MChildReport MChildReport, Set reportInInfos, Set MCellToFormus, Set abnormityChanges) {
        this.cellId = cellId;
        this.cellName = cellName;
        this.dataType = dataType;
        this.rowId = rowId;
        this.colId = colId;
        this.MChildReport = MChildReport;
        this.reportInInfos = reportInInfos;
        this.MCellToFormus = MCellToFormus;
        this.abnormityChanges = abnormityChanges;
    }

    /** default constructor */
    public MCell() {
    }

    /** minimal constructor */
    public MCell(Integer cellId, com.cbrc.smis.hibernate.MChildReport MChildReport, Set reportInInfos, Set MCellToFormus, Set abnormityChanges) {
        this.cellId = cellId;
        this.MChildReport = MChildReport;
        this.reportInInfos = reportInInfos;
        this.MCellToFormus = MCellToFormus;
        this.abnormityChanges = abnormityChanges;
    }

    public Integer getCellId() {
        return this.cellId;
    }

    public void setCellId(Integer cellId) {
        this.cellId = cellId;
    }

    public String getCellName() {
        return this.cellName;
    }

    public void setCellName(String cellName) {
        this.cellName = cellName;
    }

    public Integer getDataType() {
        return this.dataType;
    }

    public void setDataType(Integer dataType) {
        this.dataType = dataType;
    }

    public Integer getRowId() {
        return this.rowId;
    }

    public void setRowId(Integer rowId) {
        this.rowId = rowId;
    }

    public String getColId() {
        return this.colId;
    }

    public void setColId(String colId) {
        this.colId = colId;
    }

    public com.cbrc.smis.hibernate.MChildReport getMChildReport() {
        return this.MChildReport;
    }

    public void setMChildReport(com.cbrc.smis.hibernate.MChildReport MChildReport) {
        this.MChildReport = MChildReport;
    }

    public Set getReportInInfos() {
        return this.reportInInfos;
    }

    public void setReportInInfos(Set reportInInfos) {
        this.reportInInfos = reportInInfos;
    }

    public Set getMCellToFormus() {
        return this.MCellToFormus;
    }

    public void setMCellToFormus(Set MCellToFormus) {
        this.MCellToFormus = MCellToFormus;
    }

    public Set getAbnormityChanges() {
        return this.abnormityChanges;
    }

    public void setAbnormityChanges(Set abnormityChanges) {
        this.abnormityChanges = abnormityChanges;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("cellId", getCellId())
            .toString();
    }

    public boolean equals(Object other) {
        if ( !(other instanceof MCell) ) return false;
        MCell castOther = (MCell) other;
        return new EqualsBuilder()
            .append(this.getCellId(), castOther.getCellId())
            .isEquals();
    }

    public int hashCode() {
        return new HashCodeBuilder()
            .append(getCellId())
            .toHashCode();
    }

    public Integer getCollectType() {
        return collectType;
    }

    public void setCollectType(Integer collectType) {
        this.collectType = collectType;
    }

}
