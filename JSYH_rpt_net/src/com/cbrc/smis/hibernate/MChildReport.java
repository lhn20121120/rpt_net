package com.cbrc.smis.hibernate;

import java.io.Serializable;
import java.util.Set;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/** @author Hibernate CodeGenerator */
public class MChildReport implements Serializable {

	private static final long serialVersionUID = 1L;
	/** identifier field */
    private com.cbrc.smis.hibernate.MChildReportPK comp_id =new MChildReportPK();

    /** nullable persistent field */
    private String reportName;

    /** nullable persistent field */
    private String startDate;

    /** nullable persistent field */
    private String endDate;

    /** nullable persistent field */
    private String formatTempId;
    /**
     * 模板发布标志
     */
    private Integer isPublic;
    /** 
     * 报送范围设定标志
     */
    private Integer repRangeFlag;
    /**
     * 报送频度设定标志 
     */
    private Integer actuRepFlag;    
    /**
     * 代表的类型：点对点式或清单式
     */
    private Integer reportStyle;
    /**
     * 模板的类型:pdf或是excel
     * */
    private String templateType;
    /**
     * 法人/分支模板
     */
    private String frOrFzType;
    
    /** nullable persistent field */
    private com.cbrc.smis.hibernate.ReportData reportData;

    /** persistent field */
    private com.cbrc.smis.hibernate.MCurUnit MCurUnit;

    /** persistent field */
    private com.cbrc.smis.hibernate.MMainRep MMainRep;

    /** persistent field */
    private Set MActuReps;

    /** persistent field */
    private Set listingTables;

    /** persistent field */
    private Set MRepRanges;

    /** persistent field */
    private Set colToFormus;

    /** persistent field */
    private Set abnormityChanges;

    /** persistent field */
    private Set reportIns;

    /** persistent field */
    private Set colAbnormityChanges;

    /** persistent field */
    private Set MCells;

    /** full constructor */
    public MChildReport(com.cbrc.smis.hibernate.MChildReportPK comp_id, String reportName,String templateType, String startDate, String endDate, String formatTempId, Integer isPublic, Integer repRangeFlag, Integer actuRepFlag, com.cbrc.smis.hibernate.ReportData reportData, com.cbrc.smis.hibernate.MCurUnit MCurUnit, com.cbrc.smis.hibernate.MMainRep MMainRep, Set MActuReps, Set listingTables, Set MRepRanges, Set colToFormus, Set abnormityChanges, Set reportIns, Set colAbnormityChanges, Set MCells,Integer reportStyle,String frOrFzType_p) {
        this.comp_id = comp_id;
        this.reportName = reportName;
        this.templateType=templateType;
        this.startDate = startDate;
        this.endDate = endDate;
        this.formatTempId = formatTempId;
        this.isPublic = isPublic;
        this.repRangeFlag = repRangeFlag;
        this.actuRepFlag = actuRepFlag;
        this.reportData = reportData;
        this.MCurUnit = MCurUnit;
        this.MMainRep = MMainRep;
        this.MActuReps = MActuReps;
        this.listingTables = listingTables;
        this.MRepRanges = MRepRanges;
        this.colToFormus = colToFormus;
        this.abnormityChanges = abnormityChanges;
        this.reportIns = reportIns;
        this.colAbnormityChanges = colAbnormityChanges;
        this.MCells = MCells;
        this.reportStyle=reportStyle;
        this.frOrFzType = frOrFzType_p;
    }

    /** default constructor */
    public MChildReport() {
    }

    /** minimal constructor */
    public MChildReport(com.cbrc.smis.hibernate.MChildReportPK comp_id, com.cbrc.smis.hibernate.MCurUnit MCurUnit, com.cbrc.smis.hibernate.MMainRep MMainRep, Set MActuReps, Set listingTables, Set MRepRanges, Set colToFormus, Set abnormityChanges, Set reportIns, Set colAbnormityChanges, Set MCells) {
        this.comp_id = comp_id;
        this.MCurUnit = MCurUnit;
        this.MMainRep = MMainRep;
        this.MActuReps = MActuReps;
        this.listingTables = listingTables;
        this.MRepRanges = MRepRanges;
        this.colToFormus = colToFormus;
        this.abnormityChanges = abnormityChanges;
        this.reportIns = reportIns;
        this.colAbnormityChanges = colAbnormityChanges;
        this.MCells = MCells;
    }

    public com.cbrc.smis.hibernate.MChildReportPK getComp_id() {
        return this.comp_id;
    }

    public void setComp_id(com.cbrc.smis.hibernate.MChildReportPK comp_id) {
        this.comp_id = comp_id;
    }

    public String getReportName() {
        return this.reportName;
    }

    public void setReportName(String reportName) {
        this.reportName = reportName;
    }

    public String getStartDate() {
        return this.startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return this.endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getFormatTempId() {
        return this.formatTempId;
    }

    public void setFormatTempId(String formatTempId) {
        this.formatTempId = formatTempId;
    }

    public Integer getIsPublic() {
        return this.isPublic;
    }

    public void setIsPublic(Integer isPublic) {
        this.isPublic = isPublic;
    }

    public Integer getRepRangeFlag() {
        return this.repRangeFlag;
    }

    public void setRepRangeFlag(Integer repRangeFlag) {
        this.repRangeFlag = repRangeFlag;
    }

    public Integer getActuRepFlag() {
        return this.actuRepFlag;
    }

    public void setActuRepFlag(Integer actuRepFlag) {
        this.actuRepFlag = actuRepFlag;
    }

    public com.cbrc.smis.hibernate.ReportData getReportData() {
        return this.reportData;
    }

    public void setReportData(com.cbrc.smis.hibernate.ReportData reportData) {
        this.reportData = reportData;
    }

    public com.cbrc.smis.hibernate.MCurUnit getMCurUnit() {
        return this.MCurUnit;
    }

    public void setMCurUnit(com.cbrc.smis.hibernate.MCurUnit MCurUnit) {
        this.MCurUnit = MCurUnit;
    }

    public com.cbrc.smis.hibernate.MMainRep getMMainRep() {
        return this.MMainRep;
    }

    public void setMMainRep(com.cbrc.smis.hibernate.MMainRep MMainRep) {
        this.MMainRep = MMainRep;
    }

    public Set getMActuReps() {
        return this.MActuReps;
    }

    public void setMActuReps(Set MActuReps) {
        this.MActuReps = MActuReps;
    }

    public Set getListingTables() {
        return this.listingTables;
    }

    public void setListingTables(Set listingTables) {
        this.listingTables = listingTables;
    }

    public Set getMRepRanges() {
        return this.MRepRanges;
    }

    public void setMRepRanges(Set MRepRanges) {
        this.MRepRanges = MRepRanges;
    }

    public Set getColToFormus() {
        return this.colToFormus;
    }

    public void setColToFormus(Set colToFormus) {
        this.colToFormus = colToFormus;
    }

    public Set getAbnormityChanges() {
        return this.abnormityChanges;
    }

    public void setAbnormityChanges(Set abnormityChanges) {
        this.abnormityChanges = abnormityChanges;
    }

    public Set getReportIns() {
        return this.reportIns;
    }

    public void setReportIns(Set reportIns) {
        this.reportIns = reportIns;
    }

    public Set getColAbnormityChanges() {
        return this.colAbnormityChanges;
    }

    public void setColAbnormityChanges(Set colAbnormityChanges) {
        this.colAbnormityChanges = colAbnormityChanges;
    }

    public Set getMCells() {
        return this.MCells;
    }

    public void setMCells(Set MCells) {
        this.MCells = MCells;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("comp_id", getComp_id())
            .toString();
    }

    public boolean equals(Object other) {
        if ( !(other instanceof MChildReport) ) return false;
        MChildReport castOther = (MChildReport) other;
        return new EqualsBuilder()
            .append(this.getComp_id(), castOther.getComp_id())
            .isEquals();
    }

    public int hashCode() {
        return new HashCodeBuilder()
            .append(getComp_id())
            .toHashCode();
    }

	public Integer getReportStyle() {
		return reportStyle;
	}

	public void setReportStyle(Integer reportStyle) {
		this.reportStyle = reportStyle;
	}

	public String getTemplateType() {
		return templateType;
	}

	public void setTemplateType(String templateType) {
		this.templateType = templateType;
	}

	public String getFrOrFzType() {
		return frOrFzType;
	}

	public void setFrOrFzType(String frOrFzType) {
		this.frOrFzType = frOrFzType;
	}
}
