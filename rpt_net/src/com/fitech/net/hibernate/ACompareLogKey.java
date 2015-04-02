package com.fitech.net.hibernate;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 *描述：
 *日期：2007-12-12
 *作者：曹发根
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
	 * 返回 acType
	 */
	public java.lang.Integer getAcType() {
		return acType;
	}
	/**
	 * 参数：acType 
	 * 设置 acType
	 */
	public void setAcType(java.lang.Integer acType) {
		this.acType = acType;
	}
	/**
	 * 返回 repInId
	 */
	public java.lang.Integer getRepInId() {
		return repInId;
	}
	/**
	 * 参数：repInId 
	 * 设置 repInId
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

