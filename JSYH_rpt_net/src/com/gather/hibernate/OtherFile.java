package com.gather.hibernate;

import java.io.Serializable;
import java.util.Date;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/** @author Hibernate CodeGenerator */
public class OtherFile implements Serializable {

    /** identifier field */
    private Integer otherFileId;

    /** nullable persistent field */
    private String orgId;

    /** nullable persistent field */
    private Date upReportDate;

    /** nullable persistent field */
    private String fileName;

    /** nullable persistent field */
    private String operator;

    /** full constructor */
    public OtherFile(Integer otherFileId, String orgId, Date upReportDate, String fileName, String operator) {
        this.otherFileId = otherFileId;
        this.orgId = orgId;
        this.upReportDate = upReportDate;
        this.fileName = fileName;
        this.operator = operator;
    }

    /** default constructor */
    public OtherFile() {
    }

    /** minimal constructor */
    public OtherFile(Integer otherFileId) {
        this.otherFileId = otherFileId;
    }

    public Integer getOtherFileId() {
        return this.otherFileId;
    }

    public void setOtherFileId(Integer otherFileId) {
        this.otherFileId = otherFileId;
    }

    public String getOrgId() {
        return this.orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public Date getUpReportDate() {
        return this.upReportDate;
    }

    public void setUpReportDate(Date upReportDate) {
        this.upReportDate = upReportDate;
    }

    public String getFileName() {
        return this.fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getOperator() {
        return this.operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("otherFileId", getOtherFileId())
            .toString();
    }

    public boolean equals(Object other) {
        if ( !(other instanceof OtherFile) ) return false;
        OtherFile castOther = (OtherFile) other;
        return new EqualsBuilder()
            .append(this.getOtherFileId(), castOther.getOtherFileId())
            .isEquals();
    }

    public int hashCode() {
        return new HashCodeBuilder()
            .append(getOtherFileId())
            .toHashCode();
    }

}
