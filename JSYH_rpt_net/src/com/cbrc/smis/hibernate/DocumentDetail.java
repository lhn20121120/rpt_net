package com.cbrc.smis.hibernate;

import java.io.Serializable;
import java.sql.Blob;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/** @author Hibernate CodeGenerator */
public class DocumentDetail implements Serializable {

    /** identifier field */
    private Integer docDetailId;

    /** nullable persistent field */
    private Blob docContent;

    /** persistent field */
    private com.cbrc.smis.hibernate.Document document;

    /** full constructor */
    public DocumentDetail(Integer docDetailId, Blob docContent, com.cbrc.smis.hibernate.Document document) {
        this.docDetailId = docDetailId;
        this.docContent = docContent;
        this.document = document;
    }

    /** default constructor */
    public DocumentDetail() {
    }

    /** minimal constructor */
    public DocumentDetail(Integer docDetailId, com.cbrc.smis.hibernate.Document document) {
        this.docDetailId = docDetailId;
        this.document = document;
    }

    public Integer getDocDetailId() {
        return this.docDetailId;
    }

    public void setDocDetailId(Integer docDetailId) {
        this.docDetailId = docDetailId;
    }

    public Blob getDocContent() {
        return this.docContent;
    }

    public void setDocContent(Blob docContent) {
        this.docContent = docContent;
    }

    public com.cbrc.smis.hibernate.Document getDocument() {
        return this.document;
    }

    public void setDocument(com.cbrc.smis.hibernate.Document document) {
        this.document = document;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("docDetailId", getDocDetailId())
            .toString();
    }

    public boolean equals(Object other) {
        if ( !(other instanceof DocumentDetail) ) return false;
        DocumentDetail castOther = (DocumentDetail) other;
        return new EqualsBuilder()
            .append(this.getDocDetailId(), castOther.getDocDetailId())
            .isEquals();
    }

    public int hashCode() {
        return new HashCodeBuilder()
            .append(getDocDetailId())
            .toHashCode();
    }

}
