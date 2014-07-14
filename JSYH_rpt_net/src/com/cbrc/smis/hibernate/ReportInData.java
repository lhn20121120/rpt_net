package com.cbrc.smis.hibernate;

import java.io.Serializable;
import java.sql.Blob;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

public class ReportInData implements Serializable{
	/**
	 * 实际报表的ID
	 */
	private Integer repInId=null;
	/**
	 * 报表的XML文件内容
	 */
	private Blob xml=null;
	/**
	 * XML的大小
	 */
	private Integer xmlSize=null;
	
	public ReportInData(){}
	
	public ReportInData(Integer repInId){
		this.repInId=repInId;
	}
	
	public ReportInData(Integer repInId,Blob xml,Integer xmlSize){
		this.repInId=repInId;
		this.xml=xml;
		this.xmlSize=xmlSize;
	}
	
	public Integer getRepInId() {
		return repInId;
	}
	public void setRepInId(Integer repInId) {
		this.repInId = repInId;
	}
	public Blob getXml() {
		return xml;
	}
	public void setXml(Blob xml) {
		this.xml = xml;
	}
	public Integer getXmlSize() {
		return xmlSize;
	}
	public void setXmlSize(Integer xmlSize) {
		this.xmlSize = xmlSize;
	}
	
	public boolean equals(Object object){
		if(!(object instanceof ReportInData)) return false;
		ReportInData other=(ReportInData)object;
		return new EqualsBuilder()
			.append(getRepInId(),other.getRepInId())
			.append(getXml(),other.getXml())
			.append(getXmlSize(),other.getXmlSize())
			.isEquals();
	}
	
	public int hashCode(){
		return new HashCodeBuilder()
			.append(this.repInId)
			.append(this.xml)
			.append(this.xmlSize)
			.toHashCode();
	}
	
	public String toString(){
		return new ToStringBuilder(this)
			.append(this.getRepInId())
			.append(this.getXml())
			.append(this.getXmlSize())
			.toString();
	}
}
