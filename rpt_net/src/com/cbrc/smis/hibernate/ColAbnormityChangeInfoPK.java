package com.cbrc.smis.hibernate;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/** @author Hibernate CodeGenerator */
public class ColAbnormityChangeInfoPK implements Serializable {

    /**
     * 实际子表ID 
     */
    private Integer repInId;

	/**
	 * 列名
	 */
	private String colName;
		
    /**
     * 行号
     */
    private Integer rowNo;

    /** full constructor */
    public ColAbnormityChangeInfoPK(Integer repInId,String colName,Integer rowNo) {
        this.colName = colName;
        this.repInId = repInId;
        this.rowNo = rowNo;
    }

    /** default constructor */
    public ColAbnormityChangeInfoPK() {
    }

    public Integer getRowNo() {
        return this.rowNo;
    }

		public void setColName(String colName){
				this.colName=colName;	
		}
		
		public String getColName(){
				return this.colName;
		}
		
    public void setRowNo(Integer rowNo) {
        this.rowNo = rowNo;
    }

    public Integer getRepInId() {
        return this.repInId;
    }

    public void setRepInId(Integer repInId) {
        this.repInId = repInId;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("colName", getColName())
            .append("repInId", getRepInId())
            .append("rowNo",getRowNo())
            .toString();
    }

    public boolean equals(Object other) {
        if ( !(other instanceof ColAbnormityChangeInfoPK) ) return false;
        ColAbnormityChangeInfoPK castOther = (ColAbnormityChangeInfoPK) other;
        return new EqualsBuilder()
            .append(this.getColName(), castOther.getColName())
            .append(this.getRepInId(), castOther.getRepInId())
            .append(this.getRowNo(),castOther.getRowNo())
            .isEquals();
    }

    public int hashCode() {
        return new HashCodeBuilder()
            .append(getColName())
            .append(getRepInId())
            .append(getRowNo())
            .toHashCode();
    }

}
