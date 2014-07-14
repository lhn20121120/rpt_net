package com.fitech.net.hibernate;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.cbrc.smis.hibernate.DocumentDetail;

/**
 *描述：
 *日期：2008-1-9
 *作者：曹发根
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
	 * 返回 endDate
	 */
	public String getEndDate() {
		return endDate;
	}
	/**
	 * 参数：endDate 
	 * 设置 endDate
	 */
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	/**
	 * 返回 startDate
	 */
	public String getStartDate() {
		return startDate;
	}
	/**
	 * 参数：startDate 
	 * 设置 startDate
	 */
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public ViewMReport(){
		
	}
	/**
	 * 返回 comp_id
	 */
	public ViewMReportPK getComp_id() {
		return comp_id;
	}
	/**
	 * 参数：comp_id 
	 * 设置 comp_id
	 */
	public void setComp_id(ViewMReportPK comp_id) {
		this.comp_id = comp_id;
	}
	/**
	 * 返回 curName
	 */
	public String getCurName() {
		return curName;
	}
	/**
	 * 参数：curName 
	 * 设置 curName
	 */
	public void setCurName(String curName) {
		this.curName = curName;
	}
	/**
	 * 返回 dataRgTypeName
	 */
	public String getDataRgTypeName() {
		return dataRgTypeName;
	}
	/**
	 * 参数：dataRgTypeName 
	 * 设置 dataRgTypeName
	 */
	public void setDataRgTypeName(String dataRgTypeName) {
		this.dataRgTypeName = dataRgTypeName;
	}
	/**
	 * 返回 delayTime
	 */
	public Integer getDelayTime() {
		return delayTime;
	}
	/**
	 * 参数：delayTime 
	 * 设置 delayTime
	 */
	public void setDelayTime(Integer delayTime) {
		this.delayTime = delayTime;
	}
	/**
	 * 返回 normalTime
	 */
	public Integer getNormalTime() {
		return normalTime;
	}
	/**
	 * 参数：normalTime 
	 * 设置 normalTime
	 */
	public void setNormalTime(Integer normalTime) {
		this.normalTime = normalTime;
	}
	/**
	 * 返回 repFreqId
	 */
	public Integer getRepFreqId() {
		return repFreqId;
	}
	/**
	 * 参数：repFreqId 
	 * 设置 repFreqId
	 */
	public void setRepFreqId(Integer repFreqId) {
		this.repFreqId = repFreqId;
	}
	/**
	 * 返回 repFreqName
	 */
	public String getRepFreqName() {
		return repFreqName;
	}
	/**
	 * 参数：repFreqName 
	 * 设置 repFreqName
	 */
	public void setRepFreqName(String repFreqName) {
		this.repFreqName = repFreqName;
	}
	/**
	 * 返回 reportName
	 */
	public String getReportName() {
		return reportName;
	}
	/**
	 * 参数：reportName 
	 * 设置 reportName
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

