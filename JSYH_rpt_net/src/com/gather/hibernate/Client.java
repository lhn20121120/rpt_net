package com.gather.hibernate;

import java.io.Serializable;
import java.util.Date;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/** @author Hibernate CodeGenerator */
public class Client implements Serializable {

    /** identifier field */
    private String id;

    /** nullable persistent field */
    private String clientVersion;

    /** nullable persistent field */
    private Integer clientSize;

    /** nullable persistent field */
    private String note;

    /** nullable persistent field */
    private String clientEntity;

    /** nullable persistent field */
    private Date downTime;

    /** full constructor */
    public Client(String id, String clientVersion, Integer clientSize, String note, String clientEntity, Date downTime) {
        this.id = id;
        this.clientVersion = clientVersion;
        this.clientSize = clientSize;
        this.note = note;
        this.clientEntity = clientEntity;
        this.downTime = downTime;
    }

    /** default constructor */
    public Client() {
    }

    /** minimal constructor */
    public Client(String id) {
        this.id = id;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getClientVersion() {
        return this.clientVersion;
    }

    public void setClientVersion(String clientVersion) {
        this.clientVersion = clientVersion;
    }

    public Integer getClientSize() {
        return this.clientSize;
    }

    public void setClientSize(Integer clientSize) {
        this.clientSize = clientSize;
    }

    public String getNote() {
        return this.note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getClientEntity() {
        return this.clientEntity;
    }

    public void setClientEntity(String clientEntity) {
        this.clientEntity = clientEntity;
    }

    public Date getDownTime() {
        return this.downTime;
    }

    public void setDownTime(Date downTime) {
        this.downTime = downTime;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("id", getId())
            .toString();
    }

    public boolean equals(Object other) {
        if ( !(other instanceof Client) ) return false;
        Client castOther = (Client) other;
        return new EqualsBuilder()
            .append(this.getId(), castOther.getId())
            .isEquals();
    }

    public int hashCode() {
        return new HashCodeBuilder()
            .append(getId())
            .toHashCode();
    }

}
