package com.cbrc.smis.hibernate;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

public class OrgActuType implements Serializable{
	/**
	 * ID
	 */
	private Integer OATId;
	/**
	 * 频度类别名称
	 */
	private String OATName;
	/**
	 * 备注
	 */
	private String OATMemo;
	
	public OrgActuType(){}
	
	public OrgActuType(Integer OATId){
		this.OATId=OATId;
	}
	
	public OrgActuType(Integer OATId,String OATName,String OATMemo){
		this.OATId=OATId;
		this.OATName=OATName;
		this.OATMemo=OATMemo;
	}
	
	public Integer getOATId() {
		return OATId;
	}
	public void setOATId(Integer id) {
		OATId = id;
	}
	public String getOATMemo() {
		return OATMemo;
	}
	public void setOATMemo(String memo) {
		OATMemo = memo;
	}
	public String getOATName() {
		return OATName;
	}
	public void setOATName(String name) {
		OATName = name;
	}
	
	public String toString() {
        return new ToStringBuilder(this)
            .append("OATID", this.OATId)
            .toString();
    }

    public boolean equals(Object other) {
        if ( !(other instanceof OrgActuType) ) return false;
        OrgActuType castOther = (OrgActuType) other;
        return new EqualsBuilder()
            .append(this.getOATId(), castOther.getOATId())
            .isEquals();
    }

    public int hashCode() {
        return new HashCodeBuilder()
            .append(getOATId())
            .toHashCode();
    }
}
