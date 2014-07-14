package com.gather.hibernate;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/** @author Hibernate CodeGenerator */
public class DataValidateInfo implements Serializable {

    /** identifier field */
    private com.gather.hibernate.DataValidateInfoPK comp_id;
    

    /** identifier field */
    private Integer validateTypeId;

    /** identifier field */
    private Integer cellFormId;

    /** nullable persistent field */
    private com.gather.hibernate.ValidateType validateType;

    /** nullable persistent field */
    private com.gather.hibernate.Report report;
   
    /** nullable persistent field */
    private com.gather.hibernate.MCellForm MCellForm;
    /** full constructor */
    public DataValidateInfo(com.gather.hibernate.DataValidateInfoPK comp_id, Integer validateTypeId, Integer cellFormId, com.gather.hibernate.ValidateType validateType, com.gather.hibernate.Report report, com.gather.hibernate.MCellForm MCellForm) {
        this.comp_id = comp_id;
        this.validateTypeId = validateTypeId;
        this.cellFormId = cellFormId;
        this.validateType = validateType;
        this.report = report;
        this.MCellForm = MCellForm;
    }

    /** default constructor */
    public DataValidateInfo() {
    }

    /** minimal constructor */
    public DataValidateInfo(com.gather.hibernate.DataValidateInfoPK comp_id) {
        this.comp_id = comp_id;
    }

    public com.gather.hibernate.DataValidateInfoPK getComp_id() {
        return this.comp_id;
    }

    public void setComp_id(com.gather.hibernate.DataValidateInfoPK comp_id) {
        this.comp_id = comp_id;
    }

    public com.gather.hibernate.ValidateType getValidateType() {
        return this.validateType;
    }

    public void setValidateType(com.gather.hibernate.ValidateType validateType) {
        this.validateType = validateType;
    }

    public com.gather.hibernate.Report getReport() {
        return this.report;
    }

    public void setReport(com.gather.hibernate.Report report) {
        this.report = report;
    }

    public com.gather.hibernate.MCellForm getMCellForm() {
        return this.MCellForm;
    }

    public void setMCellForm(com.gather.hibernate.MCellForm MCellForm) {
        this.MCellForm = MCellForm;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("comp_id", getComp_id())
            .append("validateTypeId", getValidateTypeId())
            .append("cellFormId", getCellFormId())
            .toString();
    }

    public boolean equals(Object other) {
        if ( !(other instanceof DataValidateInfo) ) return false;
        DataValidateInfo castOther = (DataValidateInfo) other;
        return new EqualsBuilder()
            .append(this.getComp_id(), castOther.getComp_id())
            .append(this.getValidateTypeId(), castOther.getValidateTypeId())
            .append(this.getCellFormId(), castOther.getCellFormId())
            .isEquals();
    }

    public int hashCode() {
        return new HashCodeBuilder()
            .append(getComp_id())
            .append(getValidateTypeId())
            .append(getCellFormId())
            .toHashCode();
    }

	/**
	 * @return Returns the cellFormId.
	 */
	public Integer getCellFormId() {
		return cellFormId;
	}

	/**
	 * @param cellFormId The cellFormId to set.
	 */
	public void setCellFormId(Integer cellFormId) {
		this.cellFormId = cellFormId;
	}

	/**
	 * @return Returns the validateTypeId.
	 */
	public Integer getValidateTypeId() {
		return validateTypeId;
	}

	/**
	 * @param validateTypeId The validateTypeId to set.
	 */
	public void setValidateTypeId(Integer validateTypeId) {
		this.validateTypeId = validateTypeId;
	}

}
