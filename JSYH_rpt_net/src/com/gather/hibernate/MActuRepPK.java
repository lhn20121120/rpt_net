package com.gather.hibernate;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/** @author Hibernate CodeGenerator */
public class MActuRepPK implements Serializable {

    /** identifier field */
    private String versionId;

    /** identifier field */
    private Integer repFreqId;

    /** identifier field */
    private Integer dataRangeId;

    /** identifier field */
    private String childRepId;

    private Integer orgType;
    
    /** full constructor */
    public MActuRepPK(String versionId, Integer repFreqId, Integer dataRangeId, String childRepId,Integer orgType) {
        this.versionId = versionId;
        this.repFreqId = repFreqId;
        this.dataRangeId = dataRangeId;
        this.childRepId = childRepId;
        this.orgType=orgType;
    }

    /** default constructor */
    public MActuRepPK() {
    }

    public String getVersionId() {
        return this.versionId;
    }

    public void setVersionId(String versionId) {
        this.versionId = versionId;
    }

    public Integer getRepFreqId() {
        return this.repFreqId;
    }

    public void setRepFreqId(Integer repFreqId) {
        this.repFreqId = repFreqId;
    }

    public Integer getDataRangeId() {
        return this.dataRangeId;
    }

    public void setDataRangeId(Integer dataRangeId) {
        this.dataRangeId = dataRangeId;
    }

    public String getChildRepId() {
        return this.childRepId;
    }

    public void setChildRepId(String childRepId) {
        this.childRepId = childRepId;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("versionId", getVersionId())
            .append("repFreqId", getRepFreqId())
            .append("dataRangeId", getDataRangeId())
            .append("childRepId", getChildRepId())
            .toString();
    }

    public boolean equals(Object other) {
        if ( !(other instanceof MActuRepPK) ) return false;
        MActuRepPK castOther = (MActuRepPK) other;
        return new EqualsBuilder()
            .append(this.getVersionId(), castOther.getVersionId())
            .append(this.getRepFreqId(), castOther.getRepFreqId())
            .append(this.getDataRangeId(), castOther.getDataRangeId())
            .append(this.getChildRepId(), castOther.getChildRepId())
            .isEquals();
    }

    public int hashCode() {
        return new HashCodeBuilder()
            .append(getVersionId())
            .append(getRepFreqId())
            .append(getDataRangeId())
            .append(getChildRepId())
            .toHashCode();
    }

	public Integer getOrgType() {
		return orgType;
	}

	public void setOrgType(Integer orgType) {
		this.orgType = orgType;
	}

}
