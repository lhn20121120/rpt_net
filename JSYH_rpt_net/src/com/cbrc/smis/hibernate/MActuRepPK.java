package com.cbrc.smis.hibernate;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/** @author Hibernate CodeGenerator */
public class MActuRepPK implements Serializable {

    /** identifier field */
    private Integer dataRangeId;

    /** identifier field */
    private String childRepId;

    /** identifier field */
    private String versionId;

    /** identifier field */
    private Integer repFreqId;

    private Integer OATId;
    
    /** full constructor */
    public MActuRepPK(Integer dataRangeId, String childRepId, String versionId, Integer repFreqId,Integer OATId) {
        this.dataRangeId = dataRangeId;
        this.childRepId = childRepId;
        this.versionId = versionId;
        this.repFreqId = repFreqId;
        this.OATId=OATId;
    }

    /** default constructor */
    public MActuRepPK() {
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

    public String toString() {
        return new ToStringBuilder(this)
            .append("dataRangeId", getDataRangeId())
            .append("childRepId", getChildRepId())
            .append("versionId", getVersionId())
            .append("repFreqId", getRepFreqId())
            .append("OATId",getOATId())
            .toString();
    }

    public boolean equals(Object other) {
        if ( !(other instanceof MActuRepPK) ) return false;
        MActuRepPK castOther = (MActuRepPK) other;
        return new EqualsBuilder()
            .append(this.getDataRangeId(), castOther.getDataRangeId())
            .append(this.getChildRepId(), castOther.getChildRepId())
            .append(this.getVersionId(), castOther.getVersionId())
            .append(this.getRepFreqId(), castOther.getRepFreqId())
            .append(this.getOATId(),castOther.getOATId())
            .isEquals();
    }

    public int hashCode() {
        return new HashCodeBuilder()
            .append(getDataRangeId())
            .append(getChildRepId())
            .append(getVersionId())
            .append(getRepFreqId())
            .append(getOATId())
            .toHashCode();
    }

	public Integer getOATId() {
		return this.OATId;
	}

	public void setOATId(Integer id) {
		this.OATId = id;
	}

}
