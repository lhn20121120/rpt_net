package com.cbrc.smis.hibernate;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/** @author Hibernate CodeGenerator */
public class MCellToFormu implements Serializable {

	/** ID **/
	private Integer cellToFormuId;
	/**
	 * 单元格公式ID
	 */
	private Integer cellFormuId;
	/**
     * 子报表ID
     */
    private String childRepId;
   /**
    * 报表的版本号
    */ 
    private String versionId;
    /**
     * 单元格公式描述表
     */
    private com.cbrc.smis.hibernate.MCellFormu MCellFormu;
     /** full constructor */

    public MCellToFormu(Integer cellToFormuId,Integer cellFormuId,String childRepId,String versionId) {
        this.cellToFormuId=cellToFormuId;
        this.childRepId=childRepId;
        this.cellFormuId=cellFormuId;
        this.versionId=versionId;
    }

    /** default constructor */
    public MCellToFormu() {
    }

    /** minimal constructor */
    public MCellToFormu(Integer cellToFormuId) {
        this.cellToFormuId = cellToFormuId;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("comp_id", getCellToFormuId())
            .toString();
    }

    public boolean equals(Object other) {
        if ( !(other instanceof MCellToFormu) ) return false;
        MCellToFormu castOther = (MCellToFormu) other;
        return new EqualsBuilder()
            .append(this.getCellToFormuId(), castOther.getCellToFormuId())
            .isEquals();
    }

    public int hashCode() {
        return new HashCodeBuilder()
            .append(getCellToFormuId())
            .toHashCode();
    }

	public Integer getCellToFormuId() {
		return cellToFormuId;
	}

	public void setCellToFormuId(Integer cellToFormuId) {
		this.cellToFormuId = cellToFormuId;
	}

	public String getChildRepId() {
		return childRepId;
	}

	public void setChildRepId(String childRepId) {
		this.childRepId = childRepId;
	}

	public String getVersionId() {
		return versionId;
	}

	public void setVersionId(String versionId) {
		this.versionId = versionId;
	}

	public Integer getCellFormuId() {
		return cellFormuId;
	}

	public void setCellFormuId(Integer cellFormuId) {
		this.cellFormuId = cellFormuId;
	}

    public com.cbrc.smis.hibernate.MCellFormu getMCellFormu() {
        return MCellFormu;
    }

    public void setMCellFormu(com.cbrc.smis.hibernate.MCellFormu cellFormu) {
        MCellFormu = cellFormu;
    }

}
