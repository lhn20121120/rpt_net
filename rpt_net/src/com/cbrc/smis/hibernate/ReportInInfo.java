package com.cbrc.smis.hibernate;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/** @author Hibernate CodeGenerator */
public class ReportInInfo implements Serializable {

    /** identifier field */
    private com.cbrc.smis.hibernate.ReportInInfoPK comp_id;

    /** nullable persistent field */
    private String reportValue;

    /** nullable persistent field */
    private Float thanPrevRise;

    /** nullable persistent field */
    private Float thanSameRise;

    /** nullable persistent field */
    private Float thanSameFall;

    /** nullable persistent field */
    private Float thanPrevFall;

    /** nullable persistent field */
    private com.cbrc.smis.hibernate.ReportIn reportIn;

    /** nullable persistent field */
    private com.cbrc.smis.hibernate.MCell MCell;

    /** full constructor */
    public ReportInInfo(com.cbrc.smis.hibernate.ReportInInfoPK comp_id, String reportValue, Float thanPrevRise, Float thanSameRise, Float thanSameFall, Float thanPrevFall, com.cbrc.smis.hibernate.ReportIn reportIn, com.cbrc.smis.hibernate.MCell MCell) {
        this.comp_id = comp_id;
        this.reportValue = reportValue;
        this.thanPrevRise = thanPrevRise;
        this.thanSameRise = thanSameRise;
        this.thanSameFall = thanSameFall;
        this.thanPrevFall = thanPrevFall;
        this.reportIn = reportIn;
        this.MCell = MCell;
        
    }

    /** default constructor */
    public ReportInInfo() {
    }

    /** minimal constructor */
    public ReportInInfo(com.cbrc.smis.hibernate.ReportInInfoPK comp_id) {
        this.comp_id = comp_id;
    }

    public com.cbrc.smis.hibernate.ReportInInfoPK getComp_id() {
        return this.comp_id;
    }

    public void setComp_id(com.cbrc.smis.hibernate.ReportInInfoPK comp_id) {
        this.comp_id = comp_id;
    }

    public String getReportValue() {
    	//是否科学计数法
 /*   	boolean isnum=false;
    	
    	if(reportValue!=null){
    		
        		String fw="-+1234567890eE.,";
        		
        		for(int i=0;i<reportValue.trim().length();i++){
        			if(fw.indexOf(reportValue.trim().charAt(i))==-1){
        				return this.reportValue;
        			}			
        		}
        		isnum=true;
        		
    	}
    	
    	if(reportValue.indexOf('E')>-1||reportValue.indexOf('e')>-1){
    		try{
    			return new java.text.DecimalFormat("##0.00").format(Double.parseDouble(reportValue)).toString();
    		}catch(Exception e){
    			e.printStackTrace();
    			return this.reportValue;
    		}
    	}else{
    		return reportValue.replaceAll(",", "");
    	}*/
        return this.reportValue;
    }

    public void setReportValue(String reportValue) {
        this.reportValue = reportValue;
    }

    public Float getThanPrevRise() {
        return this.thanPrevRise;
    }

    public void setThanPrevRise(Float thanPrevRise) {
        this.thanPrevRise = thanPrevRise;
    }

    public Float getThanSameRise() {
        return this.thanSameRise;
    }

    public void setThanSameRise(Float thanSameRise) {
        this.thanSameRise = thanSameRise;
    }

    public Float getThanSameFall() {
        return this.thanSameFall;
    }

    public void setThanSameFall(Float thanSameFall) {
        this.thanSameFall = thanSameFall;
    }

    public Float getThanPrevFall() {
        return this.thanPrevFall;
    }

    public void setThanPrevFall(Float thanPrevFall) {
        this.thanPrevFall = thanPrevFall;
    }

    public com.cbrc.smis.hibernate.ReportIn getReportIn() {
        return this.reportIn;
    }

    public void setReportIn(com.cbrc.smis.hibernate.ReportIn reportIn) {
        this.reportIn = reportIn;
    }

    public com.cbrc.smis.hibernate.MCell getMCell() {
        return this.MCell;
    }

    public void setMCell(com.cbrc.smis.hibernate.MCell MCell) {
        this.MCell = MCell;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("comp_id", getComp_id())
            .toString();
    }

    public boolean equals(Object other) {
        if ( !(other instanceof ReportInInfo) ) return false;
        ReportInInfo castOther = (ReportInInfo) other;
        return new EqualsBuilder()
            .append(this.getComp_id(), castOther.getComp_id())
            .isEquals();
    }

    public int hashCode() {
        return new HashCodeBuilder()
            .append(getComp_id())
            .toHashCode();
    }

}
