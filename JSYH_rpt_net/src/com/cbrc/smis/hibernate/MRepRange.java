package com.cbrc.smis.hibernate;

import java.io.Serializable;
import java.util.Set;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/** @author Hibernate CodeGenerator */
public class MRepRange implements Serializable {

    /** identifier field */
    private com.cbrc.smis.hibernate.MRepRangePK comp_id;

    /**
     * 机构类别ID
     */
    private String orgClsId;
    
    /** nullable persistent field */
    private com.cbrc.smis.hibernate.MChildReport MChildReport = new com.cbrc.smis.hibernate.MChildReport();
    
    /** persistent field */
    private Set reportIns;

    /** full constructor */
    public MRepRange(com.cbrc.smis.hibernate.MRepRangePK comp_id, com.cbrc.smis.hibernate.MChildReport MChildReport, Set reportIns,String orgClsId) {
        this.comp_id = comp_id;
        this.MChildReport = MChildReport;
        this.reportIns = reportIns;
        this.orgClsId=orgClsId;
    }

    /** default constructor */
    public MRepRange() {
    }

    /** minimal constructor */
    public MRepRange(com.cbrc.smis.hibernate.MRepRangePK comp_id, Set reportIns) {
        this.comp_id = comp_id;
        this.reportIns = reportIns;
    }

    public com.cbrc.smis.hibernate.MRepRangePK getComp_id() {
        return this.comp_id;
    }

    public void setComp_id(com.cbrc.smis.hibernate.MRepRangePK comp_id) {
        this.comp_id = comp_id;
    }

    public com.cbrc.smis.hibernate.MChildReport getMChildReport() {
        return this.MChildReport;
    }

    public void setMChildReport(com.cbrc.smis.hibernate.MChildReport MChildReport) {
        this.MChildReport = MChildReport;
    }

    public Set getReportIns() {
        return this.reportIns;
    }

    public void setReportIns(Set reportIns) {
        this.reportIns = reportIns;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("comp_id", getComp_id())
            .toString();
    }

    public boolean equals(Object other) {
        if ( !(other instanceof MRepRange) ) return false;
        MRepRange castOther = (MRepRange) other;
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

}
