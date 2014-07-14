package com.fitech.gznx.po;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;


public class AfViewReportPK  implements Serializable{
	private String templateId;
	private String versionId;
	private String orgId;
	private Integer repFreqId;
	private Integer curId;
	
	public AfViewReportPK(){
		
	}
	
	/**
	 * ���� curId
	 */
	public Integer getCurId() {
		return curId;
	}
	/**
	 * ������curId 
	 * ���� curId
	 */
	public void setCurId(Integer curId) {
		this.curId = curId;
	}
	
	/**
	 * ���� orgId
	 */
	public String getOrgId() {
		return orgId;
	}
	/**
	 * ������orgId 
	 * ���� orgId
	 */
	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}
	/**
	 * ���� versionId
	 */
	public String getVersionId() {
		return versionId;
	}
	/**
	 * ������versionId 
	 * ���� versionId
	 */
	public void setVersionId(String versionId) {
		this.versionId = versionId;
	}
	public String toString() {
	    return new ToStringBuilder(this)
	        .append("templateId", getTemplateId()).append("versionId",getVersionId())
	        .append("orgId",getOrgId()).append("repFreqId",getRepFreqId()).append("curId",getCurId())
	        .toString();
	}
	
	public boolean equals(Object other) {
	    if ( !(other instanceof AfViewReportPK) ) return false;
	    AfViewReportPK castOther = (AfViewReportPK) other;
	    return new EqualsBuilder()
	        .append(this.toString(), castOther.toString())
	        .isEquals();
	}
	
	public int hashCode() {
	    return new HashCodeBuilder()
	    .append(getTemplateId()).append(getVersionId())
	 .append(getOrgId()).append(getRepFreqId()).append(getCurId())
	 .toHashCode();
	}
	
	public String getTemplateId() {
		return templateId;
	}
	
	public void setTemplateId(String templateId) {
		this.templateId = templateId;
	}
	
	public Integer getRepFreqId() {
		return repFreqId;
	}
	
	public void setRepFreqId(Integer repFreqId) {
		this.repFreqId = repFreqId;
	}

}
