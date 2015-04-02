package com.cbrc.smis.hibernate;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/** @author Hibernate CodeGenerator */
public class MCellToFormuPK implements Serializable {

    /** identifier field */
    private Integer cellFormuId;
    /**
     * 子报表ID
     */
    private String childRepId;
   /**
    * 报表的版本号
    */ 
    private String versionId;
    
    /** full constructor */
    public MCellToFormuPK(Integer cellFormuId, String childRepId,String versionId) {
        this.cellFormuId = cellFormuId;
        this.childRepId=childRepId;
        this.versionId=versionId;
    }

    /** default constructor */
    public MCellToFormuPK() {
    }

    public Integer getCellFormuId() {
        return this.cellFormuId;
    }

    public void setCellFormuId(Integer cellFormuId) {
        this.cellFormuId = cellFormuId;
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
	
    public String toString() {
        return new ToStringBuilder(this)
            .append("cellFormuId", getCellFormuId())
            .append("childRepId", getChildRepId())
            .append("versionId",getVersionId())
            .toString();
    }

    public boolean equals(Object other) {
        if ( !(other instanceof MCellToFormuPK) ) return false;
        MCellToFormuPK castOther = (MCellToFormuPK) other;
        return new EqualsBuilder()
            .append(this.getCellFormuId(), castOther.getCellFormuId())
            .append(this.getChildRepId(), castOther.getVersionId())
            .append(this.getVersionId(),castOther.getVersionId())
            .isEquals();
    }

    public int hashCode() {
        return new HashCodeBuilder()
            .append(getCellFormuId())
            .append(getChildRepId())
            .append(getVersionId())
            .toHashCode();
    }
}
