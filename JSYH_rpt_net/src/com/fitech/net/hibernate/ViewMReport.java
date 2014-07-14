package com.fitech.net.hibernate;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.cbrc.smis.hibernate.DocumentDetail;

/**
 *������
 *���ڣ�2008-1-9
 *���ߣ��ܷ���
 */
public class ViewMReport implements Serializable{
	

    private ViewMReportPK comp_id;
	private String dataRgTypeName;
    private String curName;
    private Integer repFreqId;
    private String repFreqName;
    private String reportName;
    private Integer delayTime;
    private Integer normalTime;
    private String startDate;
    private String endDate;
    private String frOrFzType;
    private Integer repTypeId;
    private Integer reportStyle;
    
    /**
	 * ���� endDate
	 */
	public String getEndDate() {
		return endDate;
	}
	/**
	 * ������endDate 
	 * ���� endDate
	 */
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	/**
	 * ���� startDate
	 */
	public String getStartDate() {
		return startDate;
	}
	/**
	 * ������startDate 
	 * ���� startDate
	 */
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public ViewMReport(){
		
	}
	/**
	 * ���� comp_id
	 */
	public ViewMReportPK getComp_id() {
		return comp_id;
	}
	/**
	 * ������comp_id 
	 * ���� comp_id
	 */
	public void setComp_id(ViewMReportPK comp_id) {
		this.comp_id = comp_id;
	}
	/**
	 * ���� curName
	 */
	public String getCurName() {
		return curName;
	}
	/**
	 * ������curName 
	 * ���� curName
	 */
	public void setCurName(String curName) {
		this.curName = curName;
	}
	/**
	 * ���� dataRgTypeName
	 */
	public String getDataRgTypeName() {
		return dataRgTypeName;
	}
	/**
	 * ������dataRgTypeName 
	 * ���� dataRgTypeName
	 */
	public void setDataRgTypeName(String dataRgTypeName) {
		this.dataRgTypeName = dataRgTypeName;
	}
	/**
	 * ���� delayTime
	 */
	public Integer getDelayTime() {
		return delayTime;
	}
	/**
	 * ������delayTime 
	 * ���� delayTime
	 */
	public void setDelayTime(Integer delayTime) {
		this.delayTime = delayTime;
	}
	/**
	 * ���� normalTime
	 */
	public Integer getNormalTime() {
		return normalTime;
	}
	/**
	 * ������normalTime 
	 * ���� normalTime
	 */
	public void setNormalTime(Integer normalTime) {
		this.normalTime = normalTime;
	}
	/**
	 * ���� repFreqId
	 */
	public Integer getRepFreqId() {
		return repFreqId;
	}
	/**
	 * ������repFreqId 
	 * ���� repFreqId
	 */
	public void setRepFreqId(Integer repFreqId) {
		this.repFreqId = repFreqId;
	}
	/**
	 * ���� repFreqName
	 */
	public String getRepFreqName() {
		return repFreqName;
	}
	/**
	 * ������repFreqName 
	 * ���� repFreqName
	 */
	public void setRepFreqName(String repFreqName) {
		this.repFreqName = repFreqName;
	}
	/**
	 * ���� reportName
	 */
	public String getReportName() {
		return reportName;
	}
	/**
	 * ������reportName 
	 * ���� reportName
	 */
	public void setReportName(String reportName) {
		this.reportName = reportName;
	}
    
	 public String getFrOrFzType() {
		return frOrFzType;
	}
	public void setFrOrFzType(String frOrFzType) {
		this.frOrFzType = frOrFzType;
	}
	public String toString() {
		 return new ToStringBuilder(this)
         .append("comp_id", getComp_id())
         .toString();
	    }

	    public boolean equals(Object other) {
	        if ( !(other instanceof ViewMReport) ) return false;
	        ViewMReport castOther = (ViewMReport) other;
	        return new EqualsBuilder()
            .append(this.getComp_id(), castOther.getComp_id())
            .isEquals();
	    }

	    public int hashCode() {
	    	 return new HashCodeBuilder()
	            .append(getComp_id())
	            .toHashCode();
	    }
		public Integer getRepTypeId() {
			return repTypeId;
		}
		public void setRepTypeId(Integer repTypeId) {
			this.repTypeId = repTypeId;
		}
		public Integer getReportStyle() {
			return reportStyle;
		}
		public void setReportStyle(Integer reportStyle) {
			this.reportStyle = reportStyle;
		}

}

