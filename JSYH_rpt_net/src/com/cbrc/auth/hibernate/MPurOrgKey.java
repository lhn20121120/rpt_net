/*
 * WARNING: DO NOT EDIT THIS FILE. This is a generated file that is synchronized
 * by MyEclipse Hibernate tool integration.
 *
 * Created Wed Dec 20 10:15:13 CST 2006 by MyEclipse Hibernate Tool.
 */
package com.cbrc.auth.hibernate;

import java.io.Serializable;

import com.fitech.gznx.po.AfOrg;
import com.fitech.net.hibernate.OrgNet;

/**
 * A class representing a composite primary key id for the M_PUR_ORG
 * table.  This object should only be instantiated for use with instances 
 * of the MPurOrg class.
 * WARNING: DO NOT EDIT THIS FILE. This is a generated file that is synchronized 
 * by MyEclipse Hibernate tool integration.
 */
public class MPurOrgKey implements Serializable{
    /** The cached hash code value for this instance.  Settting to 0 triggers re-calculation. */
    private volatile int hashValue = 0;

    /** The value of the USER_GRP_ID component of this composite id. */
    private MUserGrp MUserGrp;

    /** The value of the CHILD_REP_ID component of this composite id. */
    private java.lang.String childRepId;

    /** The value of the ORG_ID component of this composite id. */
    //private OrgNet org;
    private AfOrg org;
    
    /** The value of the POW_TYPE component of this composite id. */
    private java.lang.Integer powType;

    /**
     * Simple constructor of MPurOrgKey instances.
     */
    public MPurOrgKey()
    {
    }

    /**
	 * @return Returns the mUserGrp.
	 */
	public MUserGrp getMUserGrp() {
		return MUserGrp;
	}

	/**
	 * @param userGrp The mUserGrp to set.
	 */
	public void setMUserGrp(MUserGrp userGrp) {
		MUserGrp = userGrp;
	}

	/**
     * Returns the value of the childRepId property.
     * @return java.lang.String
     */
    public java.lang.String getChildRepId()
    {
        return childRepId;
    }

    /**
     * Sets the value of the childRepId property.
     * @param childRepId
     */
    public void setChildRepId(java.lang.String childRepId)
    {
        hashValue = 0;
        this.childRepId = childRepId;
    }

    /**
     * Returns the value of the org property.
     * @return Org
     */
    public AfOrg getOrg()
    {
        return org;
    }

    /**
     * Sets the value of the org property.
     * @param org
     */
    public void setOrg(AfOrg org)
    {
        hashValue = 0;
        this.org = org;
    }

    /**
     * Returns the value of the powType property.
     * @return java.lang.Integer
     */
    public java.lang.Integer getPowType()
    {
        return powType;
    }

    /**
     * Sets the value of the powType property.
     * @param powType
     */
    public void setPowType(java.lang.Integer powType)
    {
        hashValue = 0;
        this.powType = powType;
    }

    /**
     * Implementation of the equals comparison on the basis of equality of the id components.
     * @param rhs
     * @return boolean
     */
    public boolean equals(Object rhs)
    {
        if (rhs == null)
            return false;
        if (! (rhs instanceof MPurOrgKey))
            return false;
        MPurOrgKey that = (MPurOrgKey) rhs;
        if (this.getMUserGrp() == null || that.getMUserGrp() == null)
        {
            return false;
        }
        if (! this.getMUserGrp().equals(that.getMUserGrp()))
        {
            return false;
        }
        if (this.getChildRepId() == null || that.getChildRepId() == null)
        {
            return false;
        }
        if (! this.getChildRepId().equals(that.getChildRepId()))
        {
            return false;
        }
        if (this.getOrg() == null || that.getOrg() == null)
        {
            return false;
        }
        if (! this.getOrg().equals(that.getOrg()))
        {
            return false;
        }
        if (this.getPowType() == null || that.getPowType() == null)
        {
            return false;
        }
        if (! this.getPowType().equals(that.getPowType()))
        {
            return false;
        }
        return true;
    }

    /**
     * Implementation of the hashCode method conforming to the Bloch pattern with
     * the exception of array properties (these are very unlikely primary key types).
     * @return int
     */
    public int hashCode()
    {
        if (this.hashValue == 0)
        {
            int result = 17;
            int mUserGrpValue = this.getMUserGrp() == null ? 0 : this.getMUserGrp().hashCode();
            result = result * 37 + mUserGrpValue;
            int childRepIdValue = this.getChildRepId() == null ? 0 : this.getChildRepId().hashCode();
            result = result * 37 + childRepIdValue;
            int orgValue = this.getOrg() == null ? 0 : this.getOrg().hashCode();
            result = result * 37 + orgValue;
            int powTypeValue = this.getPowType() == null ? 0 : this.getPowType().hashCode();
            result = result * 37 + powTypeValue;
            this.hashValue = result;
        }
        return this.hashValue;
    }
}
