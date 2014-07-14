package com.cbrc.smis.hibernate;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * �嵥ʽ�����ж��ձ�ʵ����
 * 
 * @author rds
 * @serialData 2005-12-12 17:49
 */
public class ListingCols implements Serializable {
	/**
	 * ����
	 */
	private ListingColsPK comp_id;
	/**
	 * ���ݱ��е�����
	 */
	private String dbColName;
	
	/**
	 * ���캯��
	 */
	public ListingCols(){}
	/**
	 * ���캯��
	 */
	public ListingCols(ListingColsPK pk){
		this.comp_id=pk;
	}
	/**
	 * ���캯��
	 */
	public ListingCols(ListingColsPK pk,String dbColName){
		this.comp_id=pk;
		this.dbColName=dbColName;
	}
	
	public ListingColsPK getComp_id() {
		return comp_id;
	}
	public void setComp_id(ListingColsPK comp_id) {
		this.comp_id = comp_id;
	}
	public String getDbColName() {
		return dbColName;
	}
	public void setDbColName(String dbColName) {
		this.dbColName = dbColName;
	}
	
	public boolean equals(Object obj){
		if(!(obj instanceof ListingCols)) return false;
		ListingCols other=(ListingCols)obj;
		return new EqualsBuilder()
			.append(this.comp_id,other.getComp_id())
			.append(this.dbColName,other.getDbColName())
			.isEquals();
	}
	
	public int hashCode() {
		return new HashCodeBuilder()
			.append(this.comp_id)
			.append(this.dbColName)
			.toHashCode();
	}
	
	public String toString(){
		return new ToStringBuilder(this)
			.append(this.comp_id)
			.append(this.dbColName)
			.toString();
	}
}