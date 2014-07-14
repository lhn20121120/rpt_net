package com.cbrc.smis.hibernate;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/** @author Hibernate CodeGenerator */
public class Document implements Serializable {

    /** identifier field */
    private Integer docId;

    /** nullable persistent field */
    private String docType;

    /** nullable persistent field */
    private Integer docSize;

    /** nullable persistent field */
    private Date docDate;

    /** nullable persistent field */
    private String docMemo;

    /** nullable persistent field */
    private String writer;

    /** nullable persistent field */
    private String checker;

    /** nullable persistent field */
    private String principal;

    /** persistent field */
    private String orgId;

    /** persistent field */
    private Set documentDetails;

    /** full constructor */
    public Document(Integer docId, String docType, Integer docSize, Date docDate, String docMemo, String writer, String checker, String principal,String orgId, Set documentDetails) {
        this.docId = docId;
        this.docType = docType;
        this.docSize = docSize;
        this.docDate = docDate;
        this.docMemo = docMemo;
        this.writer = writer;
        this.checker = checker;
        this.principal = principal;
        this.orgId = orgId;
        this.documentDetails = documentDetails;
    }

    /** default constructor */
    public Document() {
    }

    /** minimal constructor */
    public Document(Integer docId, String orgId, Set documentDetails) {
        this.docId = docId;
        this.orgId = orgId;
        this.documentDetails = documentDetails;
    }

    public Integer getDocId() {
        return this.docId;
    }

    public void setDocId(Integer docId) {
        this.docId = docId;
    }

    public String getDocType() {
        return this.docType;
    }

    public void setDocType(String docType) {
        this.docType = docType;
    }

    public Integer getDocSize() {
        return this.docSize;
    }

    public void setDocSize(Integer docSize) {
        this.docSize = docSize;
    }

    public Date getDocDate() {
        return this.docDate;
    }

    public void setDocDate(Date docDate) {
        this.docDate = docDate;
    }

    public String getDocMemo() {
        return this.docMemo;
    }

    public void setDocMemo(String docMemo) {
        this.docMemo = docMemo;
    }

    public String getWriter() {
        return this.writer;
    }

    public void setWriter(String writer) {
        this.writer = writer;
    }

    public String getChecker() {
        return this.checker;
    }

    public void setChecker(String checker) {
        this.checker = checker;
    }

    public String getPrincipal() {
        return this.principal;
    }

    public void setPrincipal(String principal) {
        this.principal = principal;
    }

    public String getOrgId() {
        return this.orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public Set getDocumentDetails() {
        return this.documentDetails;
    }

    public void setDocumentDetails(Set documentDetails) {
        this.documentDetails = documentDetails;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("docId", getDocId())
            .toString();
    }

    public boolean equals(Object other) {
        if ( !(other instanceof Document) ) return false;
        Document castOther = (Document) other;
        return new EqualsBuilder()
            .append(this.getDocId(), castOther.getDocId())
            .isEquals();
    }

    public int hashCode() {
        return new HashCodeBuilder()
            .append(getDocId())
            .toHashCode();
    }

}
