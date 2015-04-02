package com.cbrc.smis.hibernate;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/** @author Hibernate CodeGenerator */
public class DataValidateInfoPK implements Serializable {

    /** identifier field */
    private Integer repInId;

    /** identifier field */
    private Integer validateTypeId;

    /** identifier field */
    private Integer cellFormuId;
    
    private Integer seqNo;
    
    /** full constructor */
    public DataValidateInfoPK(Integer repInId, Integer validateTypeId, Integer cellFormuId,Integer seqNo) {
        this.repInId = repInId;
        this.validateTypeId = validateTypeId;
        this.cellFormuId = cellFormuId;
        this.seqNo=seqNo;
    }

    /** default constructor */
    public DataValidateInfoPK() {
    }

    public Integer getRepInId() {
        return this.repInId;
    }

    public void setRepInId(Integer repInId) {
        this.repInId = repInId;
    }

    public Integer getValidateTypeId() {
        return this.validateTypeId;
    }

    public void setValidateTypeId(Integer validateTypeId) {
        this.validateTypeId = validateTypeId;
    }

    public Integer getCellFormuId() {
        return this.cellFormuId;
    }

    public void setCellFormuId(Integer cellFormuId) {
        this.cellFormuId = cellFormuId;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("repInId", getRepInId())
            .append("validateTypeId", getValidateTypeId())
            .append("cellFormuId", getCellFormuId())
            .toString();
    }

    public boolean equals(Object other) {
        if ( !(other instanceof DataValidateInfoPK) ) return false;
        DataValidateInfoPK castOther = (DataValidateInfoPK) other;
        return new EqualsBuilder()
            .append(this.getRepInId(), castOther.getRepInId())
            .append(this.getValidateTypeId(), castOther.getValidateTypeId())
            .append(this.getCellFormuId(), castOther.getCellFormuId())
            .isEquals();
    }

    public int hashCode() {
        return new HashCodeBuilder()
            .append(getRepInId())
            .append(getValidateTypeId())
            .append(getCellFormuId())
            .toHashCode();
    }

	public Integer getSeqNo() {
		return seqNo;
	}

	public void setSeqNo(Integer seqNo) {
		this.seqNo = seqNo;
	}

}
