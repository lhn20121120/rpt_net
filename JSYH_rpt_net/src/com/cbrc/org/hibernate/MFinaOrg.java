package com.cbrc.org.hibernate;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/** @author Hibernate CodeGenerator */
public class MFinaOrg implements Serializable {

    /** identifier field */
    private String finaOrgCode;

    /** nullable persistent field */
    private String finaOrgNm;

    /** nullable persistent field */
    private String licence;

    /** nullable persistent field */
    private String address;

    /** persistent field */
    private String orgId;

    /** nullable persistent field */
    private String corpAddress;

    /** nullable persistent field */
    private String openDate;

    /** nullable persistent field */
    private String bankStyle;

    /** nullable persistent field */
    private String corpName;
    
    private String regionId;

    public MFinaOrg(String finaOrgCode, String finaOrgNm, String licence, String address, String orgId, String corpAddress, String openDate, String bankStyle, String corpName, String regionId) {
		super();
		// TODO 自动生成构造函数存根
		this.finaOrgCode = finaOrgCode;
		this.finaOrgNm = finaOrgNm;
		this.licence = licence;
		this.address = address;
		this.orgId = orgId;
		this.corpAddress = corpAddress;
		this.openDate = openDate;
		this.bankStyle = bankStyle;
		this.corpName = corpName;
		this.regionId = regionId;
	}

	/** full constructor */
    public MFinaOrg(String finaOrgCode, String finaOrgNm, String licence, String address, String orgId, String corpAddress, String openDate, String bankStyle, String corpName) {
        this.finaOrgCode = finaOrgCode;
        this.finaOrgNm = finaOrgNm;
        this.licence = licence;
        this.address = address;
        this.orgId = orgId;
        this.corpAddress = corpAddress;
        this.openDate = openDate;
        this.bankStyle = bankStyle;
        this.corpName = corpName;
    }

    /** default constructor */
    public MFinaOrg() {
    }

    /** minimal constructor */
    public MFinaOrg(String finaOrgCode, String orgId) {
        this.finaOrgCode = finaOrgCode;
        this.orgId = orgId;
    }

    public String getFinaOrgCode() {
        return this.finaOrgCode;
    }

    public void setFinaOrgCode(String finaOrgCode) {
        this.finaOrgCode = finaOrgCode;
    }

    public String getFinaOrgNm() {
        return this.finaOrgNm;
    }

    public void setFinaOrgNm(String finaOrgNm) {
        this.finaOrgNm = finaOrgNm;
    }

    public String getLicence() {
        return this.licence;
    }

    public void setLicence(String licence) {
        this.licence = licence;
    }

    public String getAddress() {
        return this.address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getOrgId() {
        return this.orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public String getCorpAddress() {
        return this.corpAddress;
    }

    public void setCorpAddress(String corpAddress) {
        this.corpAddress = corpAddress;
    }

    public String getOpenDate() {
        return this.openDate;
    }

    public void setOpenDate(String openDate) {
        this.openDate = openDate;
    }

    public String getBankStyle() {
        return this.bankStyle;
    }

    public void setBankStyle(String bankStyle) {
        this.bankStyle = bankStyle;
    }

    public String getCorpName() {
        return this.corpName;
    }

    public void setCorpName(String corpName) {
        this.corpName = corpName;
    }
    
    

    public String getRegionId() {
		return regionId;
	}

	public void setRegionId(String regionId) {
		this.regionId = regionId;
	}

	public String toString() {
        return new ToStringBuilder(this)
            .append("finaOrgCode", getFinaOrgCode())
            .toString();
    }

    public boolean equals(Object other) {
        if ( !(other instanceof MFinaOrg) ) return false;
        MFinaOrg castOther = (MFinaOrg) other;
        return new EqualsBuilder()
            .append(this.getFinaOrgCode(), castOther.getFinaOrgCode())
            .isEquals();
    }

    public int hashCode() {
        return new HashCodeBuilder()
            .append(getFinaOrgCode())
            .toHashCode();
    }

}
