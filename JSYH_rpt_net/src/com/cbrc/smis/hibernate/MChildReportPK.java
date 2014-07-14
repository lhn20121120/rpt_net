package com.cbrc.smis.hibernate;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/** @author Hibernate CodeGenerator */
public class MChildReportPK implements Serializable {

    /** identifier field */
    private String childRepId;

    /** identifier field */
    private String versionId;

    /** full constructor */
    public MChildReportPK(String childRepId, String versionId) {
        this.childRepId = childRepId;
        this.versionId = versionId;
    }

    /** default constructor */
    public MChildReportPK() {
    }


   

    public String getChildRepId() {
        return this.childRepId;
    }



  
    /**
     * @param childRepId The childRepId to set.
     */
   

    public void setChildRepId(String childRepId) {

        this.childRepId = childRepId;
    }
    /**
     * @return Returns the versionId.
     */
    public String getVersionId() {
        return versionId;
    }
    /**
     * @param versionId The versionId to set.
     */
    public void setVersionId(String versionId) {
        this.versionId = versionId;
    }
    public String toString() {
        return new ToStringBuilder(this)
            .append("childRepId", getChildRepId())
            .append("versionId", getVersionId())
            .toString();
    }

    public boolean equals(Object other) {
        if ( !(other instanceof MChildReportPK) ) return false;
        MChildReportPK castOther = (MChildReportPK) other;
        return new EqualsBuilder()
            .append(this.getChildRepId(), castOther.getChildRepId())
            .append(this.getVersionId(), castOther.getVersionId())
            .isEquals();
    }

    public int hashCode() {
        return new HashCodeBuilder()
            .append(getChildRepId())
            .append(getVersionId())
            .toHashCode();
    }

}
