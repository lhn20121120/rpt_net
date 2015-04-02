package com.fitech.net.hibernate;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 *������
 *���ڣ�2007-12-12
 *���ߣ��ܷ���
 */
public class ACompareLogKey implements Serializable{
	private java.lang.Integer repInId;
	private java.lang.Integer acType;
	public ACompareLogKey(){
		
	}
	public ACompareLogKey(Integer repInId,Integer acType){
		this.repInId=repInId;
		this.acType=acType;
	}
	
	/**
	 * ���� acType
	 */
	public java.lang.Integer getAcType() {
		return acType;
	}
	/**
	 * ������acType 
	 * ���� acType
	 */
	public void setAcType(java.lang.Integer acType) {
		this.acType = acType;
	}
	/**
	 * ���� repInId
	 */
	public java.lang.Integer getRepInId() {
		return repInId;
	}
	/**
	 * ������repInId 
	 * ���� repInId
	 */
	public void setRepInId(java.lang.Integer repInId) {
		this.repInId = repInId;
	}
	 public boolean equals(Object obj)
	    {
	       if(!(obj instanceof ACompareLogKey)) return false;
	       ACompareLogKey other = (ACompareLogKey) obj;
	       return new EqualsBuilder()
	               .append(this.getRepInId(),other.getRepInId())
	               .append(this.getAcType(),other.getAcType())
	               .isEquals();
	    }

	    public int hashCode()
	    {
	        return new HashCodeBuilder()
	                .append(this.repInId)
	                .append(this.acType).toHashCode();
	    }

    
}

