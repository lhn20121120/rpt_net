package com.cbrc.auth.hibernate;

import java.io.Serializable;
import java.util.Set;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.fitech.net.hibernate.OrgNet;

/** @author Hibernate CodeGenerator */
public class MUserGrp implements Serializable {

    /** identifier field */
    private Long userGrpId;

    /** nullable persistent field */
    private String userGrpNm;
    
    private OrgNet setOrg;

    /** nullable persistent field */
    private com.cbrc.auth.hibernate.MUserToGrp MUserToGrp;

    /** persistent field */
    private Set MPurBanklevels;

    /** full constructor */
    public MUserGrp(Long userGrpId, String userGrpNm,OrgNet setOrg, com.cbrc.auth.hibernate.MUserToGrp MUserToGrp, Set MPurBanklevels) {
        this.userGrpId = userGrpId;
        this.userGrpNm = userGrpNm;
        this.MUserToGrp = MUserToGrp;
        this.MPurBanklevels = MPurBanklevels;
        this.setOrg = setOrg;
    }

    /** default constructor */
    public MUserGrp() {
    }

	public OrgNet getSetOrg() {
		return setOrg;
	}

	public void setSetOrg(OrgNet setOrg) {
		this.setOrg = setOrg;
	}

	/** minimal constructor */
    public MUserGrp(Long userGrpId, Set MPurBanklevels) {
        this.userGrpId = userGrpId;
        this.MPurBanklevels = MPurBanklevels;
    }

    public Long getUserGrpId() {
        return this.userGrpId;
    }

    public void setUserGrpId(Long userGrpId) {
        this.userGrpId = userGrpId;
    }

    public String getUserGrpNm() {
        return this.userGrpNm;
    }

    public void setUserGrpNm(String userGrpNm) {
        this.userGrpNm = userGrpNm;
    }

    public com.cbrc.auth.hibernate.MUserToGrp getMUserToGrp() {
        return this.MUserToGrp;
    }

    public void setMUserToGrp(com.cbrc.auth.hibernate.MUserToGrp MUserToGrp) {
        this.MUserToGrp = MUserToGrp;
    }    

    public Set getMPurBanklevels() {
		return MPurBanklevels;
	}

	public void setMPurBanklevels(Set purBanklevels) {
		MPurBanklevels = purBanklevels;
	}

	public String toString() {
        return new ToStringBuilder(this)
            .append("userGrpId", getUserGrpId())
            .toString();
    }

    public boolean equals(Object other) {
        if ( !(other instanceof MUserGrp) ) return false;
        MUserGrp castOther = (MUserGrp) other;
        return new EqualsBuilder()
            .append(this.getUserGrpId(), castOther.getUserGrpId())
            .isEquals();
    }

    public int hashCode() {
        return new HashCodeBuilder()
            .append(getUserGrpId())
            .toHashCode();
    }

}
