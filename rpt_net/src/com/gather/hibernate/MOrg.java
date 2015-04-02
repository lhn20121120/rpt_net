package com.gather.hibernate;

import java.io.Serializable;
import java.util.Set;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/** @author Hibernate CodeGenerator */
public class MOrg implements Serializable {

    /** identifier field */
    private String orgId;

    /** nullable persistent field */
    private String orgName;

    /** nullable persistent field */
    private Integer orgType;

    /** nullable persistent field */
    private String isCorp;

    /** nullable persistent field */
    private String isInUsing;

    /** persistent field */
    private Set MRepRanges;

    /** persistent field */
    private Set reports;

    /** persistent field */
    private Set repRangeDownLogs;

    /** persistent field */
    private Set mappingRelations;

    /** full constructor */
    public MOrg(String orgId, String orgName, Integer orgType, String isCorp, String isInUsing, Set MRepRanges, Set reports, Set repRangeDownLogs, Set mappingRelations) {
        this.orgId = orgId;
        this.orgName = orgName;
        this.orgType = orgType;
        this.isCorp = isCorp;
        this.isInUsing = isInUsing;
        this.MRepRanges = MRepRanges;
        this.reports = reports;
        this.repRangeDownLogs = repRangeDownLogs;
        this.mappingRelations = mappingRelations;
    }

    /** default constructor */
    public MOrg() {
    }

    /** minimal constructor */
    public MOrg(String orgId, Set MRepRanges, Set reports, Set repRangeDownLogs, Set mappingRelations) {
        this.orgId = orgId;
        this.MRepRanges = MRepRanges;
        this.reports = reports;
        this.repRangeDownLogs = repRangeDownLogs;
        this.mappingRelations = mappingRelations;
    }

    public String getOrgId() {
        return this.orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public String getOrgName() {
        return this.orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public Integer getOrgType() {
        return this.orgType;
    }

    public void setOrgType(Integer orgType) {
        this.orgType = orgType;
    }

    public String getIsCorp() {
        return this.isCorp;
    }

    public void setIsCorp(String isCorp) {
        this.isCorp = isCorp;
    }

    public String getIsInUsing() {
        return this.isInUsing;
    }

    public void setIsInUsing(String isInUsing) {
        this.isInUsing = isInUsing;
    }

    public Set getMRepRanges() {
        return this.MRepRanges;
    }

    public void setMRepRanges(Set MRepRanges) {
        this.MRepRanges = MRepRanges;
    }

    public Set getReports() {
        return this.reports;
    }

    public void setReports(Set reports) {
        this.reports = reports;
    }

    public Set getRepRangeDownLogs() {
        return this.repRangeDownLogs;
    }

    public void setRepRangeDownLogs(Set repRangeDownLogs) {
        this.repRangeDownLogs = repRangeDownLogs;
    }

    public Set getMappingRelations() {
        return this.mappingRelations;
    }

    public void setMappingRelations(Set mappingRelations) {
        this.mappingRelations = mappingRelations;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("orgId", getOrgId())
            .toString();
    }

    public boolean equals(Object other) {
        if ( !(other instanceof MOrg) ) return false;
        MOrg castOther = (MOrg) other;
        return new EqualsBuilder()
            .append(this.getOrgId(), castOther.getOrgId())
            .isEquals();
    }

    public int hashCode() {
        return new HashCodeBuilder()
            .append(getOrgId())
            .toHashCode();
    }

}
