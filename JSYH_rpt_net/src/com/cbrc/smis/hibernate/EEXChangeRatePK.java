package com.cbrc.smis.hibernate;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

public class EEXChangeRatePK  implements Serializable{
	 private String vcid;
     private Integer vdid;
     public EEXChangeRatePK(String vcid, Integer vdid) {
         this.vcid = vcid;
         this.vdid = vdid;
     }
     public String getVcid() {
		return vcid;
	}
	public void setVcid(String vcid) {
		this.vcid = vcid;
	}
	public Integer getVdid() {
		return vdid;
	}
	public void setVdid(Integer vdid) {
		this.vdid = vdid;
	}
	public EEXChangeRatePK() {
     }
	 public String toString() {
	        return new ToStringBuilder(this)
	            .append("vcid", getVcid())
	            .append("vdid", getVdid())
	            .toString();
	    }

	    public boolean equals(Object other) {
	        if ( !(other instanceof EEXChangeRatePK) ) return false;
	        EEXChangeRatePK castOther = (EEXChangeRatePK) other;
	        return new EqualsBuilder()
	            .append(this.getVcid(), castOther.getVcid())
	            .append(this.getVdid(), castOther.getVdid())
	            .isEquals();
	    }

	    public int hashCode() {
	        return new HashCodeBuilder()
	            .append(getVcid())
	            .append(getVcid())
	            .toHashCode();
	    }

}
