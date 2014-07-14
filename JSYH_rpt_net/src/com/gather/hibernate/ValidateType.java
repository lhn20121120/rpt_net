package com.gather.hibernate;

import java.io.Serializable;
import java.util.Set;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/** @author Hibernate CodeGenerator */
public class ValidateType implements Serializable {

    /** identifier field */
    private Integer validateTypeId;

    /** nullable persistent field */
    private String validateTypeName;

    /** persistent field */
    private Set dataValidateInfos;

    /** full constructor */
    public ValidateType(Integer validateTypeId, String validateTypeName, Set dataValidateInfos) {
        this.validateTypeId = validateTypeId;
        this.validateTypeName = validateTypeName;
        this.dataValidateInfos = dataValidateInfos;
    }

    /** default constructor */
    public ValidateType() {
    }

    /** minimal constructor */
    public ValidateType(Integer validateTypeId, Set dataValidateInfos) {
        this.validateTypeId = validateTypeId;
        this.dataValidateInfos = dataValidateInfos;
    }

    public Integer getValidateTypeId() {
        return this.validateTypeId;
    }

    public void setValidateTypeId(Integer validateTypeId) {
        this.validateTypeId = validateTypeId;
    }

    public String getValidateTypeName() {
        return this.validateTypeName;
    }

    public void setValidateTypeName(String validateTypeName) {
        this.validateTypeName = validateTypeName;
    }

    public Set getDataValidateInfos() {
        return this.dataValidateInfos;
    }

    public void setDataValidateInfos(Set dataValidateInfos) {
        this.dataValidateInfos = dataValidateInfos;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("validateTypeId", getValidateTypeId())
            .toString();
    }

    public boolean equals(Object other) {
        if ( !(other instanceof ValidateType) ) return false;
        ValidateType castOther = (ValidateType) other;
        return new EqualsBuilder()
            .append(this.getValidateTypeId(), castOther.getValidateTypeId())
            .isEquals();
    }

    public int hashCode() {
        return new HashCodeBuilder()
            .append(getValidateTypeId())
            .toHashCode();
    }

}
