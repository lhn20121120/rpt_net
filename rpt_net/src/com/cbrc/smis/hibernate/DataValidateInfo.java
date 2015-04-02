package com.cbrc.smis.hibernate;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/** @author Hibernate CodeGenerator */
public class DataValidateInfo implements Serializable {

    /** identifier field */
    private com.cbrc.smis.hibernate.DataValidateInfoPK comp_id;

    /** nullable persistent field */
    private Integer result;

	private String sourceValue;
	private String targetValue;
    
    /** nullable persistent field */
    private com.cbrc.smis.hibernate.ValidateType validateType;

    /** nullable persistent field */
    private com.cbrc.smis.hibernate.ReportIn reportIn;

    /** nullable persistent field */
    private com.cbrc.smis.hibernate.MCellFormu MCellFormu;

    /** full constructor */
    public DataValidateInfo(com.cbrc.smis.hibernate.DataValidateInfoPK comp_id, Integer result, com.cbrc.smis.hibernate.ValidateType validateType, com.cbrc.smis.hibernate.ReportIn reportIn, com.cbrc.smis.hibernate.MCellFormu MCellFormu) {
        this.comp_id = comp_id;
        this.result = result;
        this.validateType = validateType;
        this.reportIn = reportIn;
        this.MCellFormu = MCellFormu;
    }

    /** default constructor */
    public DataValidateInfo() {
    }

    /** minimal constructor */
    public DataValidateInfo(com.cbrc.smis.hibernate.DataValidateInfoPK comp_id) {
        this.comp_id = comp_id;
    }

    public com.cbrc.smis.hibernate.DataValidateInfoPK getComp_id() {
        return this.comp_id;
    }

    public void setComp_id(com.cbrc.smis.hibernate.DataValidateInfoPK comp_id) {
        this.comp_id = comp_id;
    }

    public Integer getResult() {
        return this.result;
    }

    public void setResult(Integer result) {
        this.result = result;
    }

    public com.cbrc.smis.hibernate.ValidateType getValidateType() {
        return this.validateType;
    }

    public void setValidateType(com.cbrc.smis.hibernate.ValidateType validateType) {
        this.validateType = validateType;
    }

    public com.cbrc.smis.hibernate.ReportIn getReportIn() {
        return this.reportIn;
    }

    public void setReportIn(com.cbrc.smis.hibernate.ReportIn reportIn) {
        this.reportIn = reportIn;
    }

    public com.cbrc.smis.hibernate.MCellFormu getMCellFormu() {
        return this.MCellFormu;
    }

    public void setMCellFormu(com.cbrc.smis.hibernate.MCellFormu MCellFormu) {
        this.MCellFormu = MCellFormu;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("comp_id", getComp_id())
            .toString();
    }

    public boolean equals(Object other) {
        if ( !(other instanceof DataValidateInfo) ) return false;
        DataValidateInfo castOther = (DataValidateInfo) other;
        return new EqualsBuilder()
            .append(this.getComp_id(), castOther.getComp_id())
            .isEquals();
    }

    public int hashCode() {
        return new HashCodeBuilder()
            .append(getComp_id())
            .toHashCode();
    }

	public String getSourceValue() {
		return sourceValue;
	}

	public void setSourceValue(String sourceValue) {
		this.sourceValue = sourceValue;
	}

	public String getTargetValue() {
		return targetValue;
	}

	public void setTargetValue(String targetValue) {
		this.targetValue = targetValue;
	}

}
