package com.cbrc.smis.hibernate;

import java.io.Serializable;
import java.sql.Blob;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/** @author Hibernate CodeGenerator */
public class ReportData implements Serializable {

    /** identifier field */
    private com.cbrc.smis.hibernate.ReportDataPK comp_id;

    /** nullable persistent field */
    private Blob pdf;

    /** nullable persistent field */
    private Blob note;

    /** nullable persistent field */
    private Blob xml;

    /** nullable persistent field */
    private com.cbrc.smis.hibernate.MChildReport MChildReport;

    /** full constructor */
    public ReportData(com.cbrc.smis.hibernate.ReportDataPK comp_id, Blob pdf, Blob note, Blob xml, com.cbrc.smis.hibernate.MChildReport MChildReport) {
        this.comp_id = comp_id;
        this.pdf = pdf;
        this.note = note;
        this.xml = xml;
        this.MChildReport = MChildReport;
    }

    /** default constructor */
    public ReportData() {
    }

    /** minimal constructor */
    public ReportData(com.cbrc.smis.hibernate.ReportDataPK comp_id) {
        this.comp_id = comp_id;
    }

    public com.cbrc.smis.hibernate.ReportDataPK getComp_id() {
        return this.comp_id;
    }

    public void setComp_id(com.cbrc.smis.hibernate.ReportDataPK comp_id) {
        this.comp_id = comp_id;
    }

    public Blob getPdf() {
        return this.pdf;
    }

    public void setPdf(Blob pdf) {
        this.pdf = pdf;
    }

    public Blob getNote() {
        return this.note;
    }

    public void setNote(Blob note) {
        this.note = note;
    }

    public Blob getXml() {
        return this.xml;
    }

    public void setXml(Blob xml) {
        this.xml = xml;
    }

    public com.cbrc.smis.hibernate.MChildReport getMChildReport() {
        return this.MChildReport;
    }

    public void setMChildReport(com.cbrc.smis.hibernate.MChildReport MChildReport) {
        this.MChildReport = MChildReport;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("comp_id", getComp_id())
            .toString();
    }

    public boolean equals(Object other) {
        if ( !(other instanceof ReportData) ) return false;
        ReportData castOther = (ReportData) other;
        return new EqualsBuilder()
            .append(this.getComp_id(), castOther.getComp_id())
            .isEquals();
    }

    public int hashCode() {
        return new HashCodeBuilder()
            .append(getComp_id())
            .toHashCode();
    }

}
