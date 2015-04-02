package com.fitech.net.orgcls.hibernate;


import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

	/** @author Hibernate CodeGenerator */
	public class MOrgClNet implements Serializable {

	    /** identifier field */
	    private String orgClsId;

	    /** nullable persistent field */
	    private String orgClsNm;

	    /** persistent field */
	    private String orgClsType;

	    /** full constructor */
	    public MOrgClNet(String orgClsId, String orgClsNm, String orgClsType) {
	        this.orgClsId = orgClsId;
	        this.orgClsNm = orgClsNm;
	        this.orgClsType = orgClsType;
	    }

	    /** default constructor */
	    public MOrgClNet() {
	    }

	    /** minimal constructor */
	    public MOrgClNet(String orgClsId, String orgClsType) {
	        this.orgClsId = orgClsId;
	        this.orgClsType = orgClsType;
	    }

	    public String getOrgClsId() {
	        return this.orgClsId;
	    }

	    public void setOrgClsId(String orgClsId) {
	        this.orgClsId = orgClsId;
	    }

	    public String getOrgClsNm() {
	        return this.orgClsNm;
	    }

	    public void setOrgClsNm(String orgClsNm) {
	        this.orgClsNm = orgClsNm;
	    }

	    public String getOrgClsType() {
	        return this.orgClsType;
	    }

	    public void setOrgClsType(String orgClsType) {
	        this.orgClsType = orgClsType;
	    }

	    public String toString() {
	        return new ToStringBuilder(this)
	            .append("orgClsId", getOrgClsId())
	            .toString();
	    }

	    public boolean equals(Object other) {
	        if ( !(other instanceof MOrgClNet) ) return false;
	        MOrgClNet castOther = (MOrgClNet) other;
	        return new EqualsBuilder()
	            .append(this.getOrgClsId(), castOther.getOrgClsId())
	            .isEquals();
	    }

	    public int hashCode() {
	        return new HashCodeBuilder()
	            .append(getOrgClsId())
	            .toHashCode();
	    }

	}

