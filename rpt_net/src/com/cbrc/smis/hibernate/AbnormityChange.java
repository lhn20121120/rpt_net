package com.cbrc.smis.hibernate;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/** @author Hibernate CodeGenerator */
public class AbnormityChange implements Serializable {

    /** identifier field */
    private com.cbrc.smis.hibernate.AbnormityChangePK comp_id;

    /** nullable persistent field */
    private Float prevRiseStandard;

    /** nullable persistent field */
    private Float prevFallStandard;

    /** nullable persistent field */
    private Float sameRiseStandard;

    /** nullable persistent field */
    private Float sameFallStandard;
    private String orgId;
    /**机构类别ID**/
    private String orgClsId;
    
    /** nullable persistent field */
    private com.cbrc.smis.hibernate.MCell MCell;

    /** nullable persistent field */
    private com.cbrc.smis.hibernate.MChildReport MChildReport;

    /** full constructor */
    public AbnormityChange(com.cbrc.smis.hibernate.AbnormityChangePK comp_id, Float prevRiseStandard, Float prevFallStandard, Float sameRiseStandard, Float sameFallStandard, com.cbrc.smis.hibernate.MCell MCell, com.cbrc.smis.hibernate.MChildReport MChildReport,String orgClsId) {
        this.comp_id = comp_id;
        this.prevRiseStandard = prevRiseStandard;
        this.prevFallStandard = prevFallStandard;
        this.sameRiseStandard = sameRiseStandard;
        this.sameFallStandard = sameFallStandard;
        this.MCell = MCell;
        this.MChildReport = MChildReport;
        this.orgClsId=orgClsId;
    }
    
    public AbnormityChange(Float prevRiseStandard, Float prevFallStandard, Float sameRiseStandard, Float sameFallStandard) {
        this.prevRiseStandard = prevRiseStandard;
        this.prevFallStandard = prevFallStandard;
        this.sameRiseStandard = sameRiseStandard;
        this.sameFallStandard = sameFallStandard;
    }
    
    /** default constructor */
    public AbnormityChange() {
    }

    /** minimal constructor */
    public AbnormityChange(com.cbrc.smis.hibernate.AbnormityChangePK comp_id) {
        this.comp_id = comp_id;
    }

    public com.cbrc.smis.hibernate.AbnormityChangePK getComp_id() {
        return this.comp_id;
    }

    public void setComp_id(com.cbrc.smis.hibernate.AbnormityChangePK comp_id) {
        this.comp_id = comp_id;
    }

    public Float getPrevRiseStandard() {
        return this.prevRiseStandard;
    }

    public void setPrevRiseStandard(Float prevRiseStandard) {
        this.prevRiseStandard = prevRiseStandard;
    }

    public Float getPrevFallStandard() {
        return this.prevFallStandard;
    }

    public void setPrevFallStandard(Float prevFallStandard) {
        this.prevFallStandard = prevFallStandard;
    }

    public Float getSameRiseStandard() {
        return this.sameRiseStandard;
    }

    public void setSameRiseStandard(Float sameRiseStandard) {
        this.sameRiseStandard = sameRiseStandard;
    }

    public Float getSameFallStandard() {
        return this.sameFallStandard;
    }

    public void setSameFallStandard(Float sameFallStandard) {
        this.sameFallStandard = sameFallStandard;
    }

    public com.cbrc.smis.hibernate.MCell getMCell() {
        return this.MCell;
    }

    public void setMCell(com.cbrc.smis.hibernate.MCell MCell) {
        this.MCell = MCell;
    }

    public com.cbrc.smis.hibernate.MChildReport getMChildReport() {
        return this.MChildReport;
    }

    public void setMChildReport(com.cbrc.smis.hibernate.MChildReport MChildReport) {
        this.MChildReport = MChildReport;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("comp_id", getComp_id())
            .toString();
    }

    public boolean equals(Object other) {
        if ( !(other instanceof AbnormityChange) ) return false;
        AbnormityChange castOther = (AbnormityChange) other;
        return new EqualsBuilder()
            .append(this.getComp_id(), castOther.getComp_id())
            .isEquals();
    }

    public int hashCode() {
        return new HashCodeBuilder()
            .append(getComp_id())
            .toHashCode();
    }

	public String getOrgClsId() {
		return orgClsId;
	}

	public void setOrgClsId(String orgClsId) {
		this.orgClsId = orgClsId;
	}

	public String getOrgId()
	{
		return orgId;
	}

	public void setOrgId(String orgId)
	{
		this.orgId = orgId;
	}

}
