package com.cbrc.smis.hibernate;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * 清单式报表列对照表实体的主键类
 * 
 * @author IBMUSER
 * @serialData 2005-12-12
 */
public class ListingColsPK implements Serializable{
	/**
	 * 子报表ID
	 */
	private String childRepId;
	/**
	 * 版本号
	 */
	private String versionId;
	/**
	 * PDF报本模板中的列名
	 */
	private String pdfColName;
	
	/**
	 * 构造函数
	 */
	public ListingColsPK(){}
	/**
	 * 构造函数
	 */
	public ListingColsPK(String childRepId,String versionId,String pdfColName){
		this.childRepId=childRepId;
		this.versionId=versionId;
		this.pdfColName=pdfColName;
	}
	
	public String getChildRepId() {
		return childRepId;
	}
	public void setChildRepId(String childRepId) {
		this.childRepId = childRepId;
	}
	
	public String getPdfColName() {
		return pdfColName;
	}
	public void setPdfColName(String pdfColName) {
		this.pdfColName = pdfColName;
	}
	
	public String getVersionId() {
		return versionId;
	}
	public void setVersionId(String versionId) {
		this.versionId = versionId;
	}
	
	public boolean equals(Object obj){
		if(!(obj instanceof ListingColsPK)) return false;
		ListingColsPK other=(ListingColsPK)obj;
		return new EqualsBuilder()
			.append(this.childRepId,other.getChildRepId())
			.append(this.versionId,other.getVersionId())
			.append(this.pdfColName,other.getPdfColName())
			.isEquals();
	}
	
	public int hashCode() {
		return new HashCodeBuilder()
			.append(this.childRepId)
			.append(this.versionId)
			.append(this.pdfColName)
			.toHashCode();
	}
	
	public String toString(){
		return new ToStringBuilder(this)
			.append(this.childRepId)
			.append(this.versionId)
			.append(this.pdfColName)
			.toString();
	}
}
