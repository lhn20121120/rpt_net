package com.gather.hibernate;

import java.io.Serializable;
import java.util.Set;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/** @author Hibernate CodeGenerator */
public class MCellForm implements Serializable {

    /** identifier field */
    private Integer cellFormId;

    /** nullable persistent field */
    private String cellForm;

    /** nullable persistent field */
    private Integer formType;

    /** persistent field */
    private Set dataValidateInfos;

    /** full constructor */
    public MCellForm(Integer cellFormId, String cellForm, Integer formType, Set dataValidateInfos) {
        this.cellFormId = cellFormId;
        this.cellForm = cellForm;
        this.formType = formType;
        this.dataValidateInfos = dataValidateInfos;
    }

    /** default constructor */
    public MCellForm() {
    }

    /** minimal constructor */
    public MCellForm(Integer cellFormId, Set dataValidateInfos) {
        this.cellFormId = cellFormId;
        this.dataValidateInfos = dataValidateInfos;
    }

    public Integer getCellFormId() {
        return this.cellFormId;
    }

    public void setCellFormId(Integer cellFormId) {
        this.cellFormId = cellFormId;
    }

    public String getCellForm() {
        return this.cellForm;
    }

    public void setCellForm(String cellForm) {
        this.cellForm = cellForm;
    }

    public Integer getFormType() {
        return this.formType;
    }

    public void setFormType(Integer formType) {
        this.formType = formType;
    }

    public Set getDataValidateInfos() {
        return this.dataValidateInfos;
    }

    public void setDataValidateInfos(Set dataValidateInfos) {
        this.dataValidateInfos = dataValidateInfos;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("cellFormId", getCellFormId())
            .toString();
    }

    public boolean equals(Object other) {
        if ( !(other instanceof MCellForm) ) return false;
        MCellForm castOther = (MCellForm) other;
        return new EqualsBuilder()
            .append(this.getCellFormId(), castOther.getCellFormId())
            .isEquals();
    }

    public int hashCode() {
        return new HashCodeBuilder()
            .append(getCellFormId())
            .toHashCode();
    }

}
