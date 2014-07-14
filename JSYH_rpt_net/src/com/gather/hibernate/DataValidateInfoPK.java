package com.gather.hibernate;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/** @author Hibernate CodeGenerator */
public class DataValidateInfoPK implements Serializable {

    /** identifier field */
    private Integer repOutId;
    private Integer sequenceId;


    /**
	 * @return Returns the sequenceId.
	 */
	public Integer getSequenceId() {
		return sequenceId;
	}

	/**
	 * @param sequenceId The sequenceId to set.
	 */
	public void setSequenceId(Integer sequenceId) {
		this.sequenceId = sequenceId;
	}

	/** full constructor */
    public DataValidateInfoPK(Integer repOutId) {
        this.repOutId = repOutId;

    }

    /** default constructor */
    public DataValidateInfoPK() {
    }

    public Integer getRepOutId() {
        return this.repOutId;
    }

    public void setRepOutId(Integer repOutId) {
        this.repOutId = repOutId;
    }

   
    public String toString() {
        return new ToStringBuilder(this)
            .append("repOutId", getRepOutId())
            .append("sequenceId",getSequenceId())
            .toString();
    }

    public boolean equals(Object other) {
        if ( !(other instanceof DataValidateInfoPK) ) return false;
        DataValidateInfoPK castOther = (DataValidateInfoPK) other;
        return new EqualsBuilder()
            .append(this.getRepOutId(), castOther.getRepOutId())
            .append(this.getSequenceId(),castOther.getSequenceId())
            .isEquals();
    }

    public int hashCode() {
        return new HashCodeBuilder()
            .append(getRepOutId())
            .append(getSequenceId())
            .toHashCode();
    }

}
