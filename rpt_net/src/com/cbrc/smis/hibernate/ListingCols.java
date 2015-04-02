package com.cbrc.smis.hibernate;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * 清单式报表列对照表实体类
 * 
 * @author rds
 * @serialData 2005-12-12 17:49
 */
public class ListingCols implements Serializable {
	/**
	 * 主键
	 */
	private ListingColsPK comp_id;
	/**
	 * 数据表中的列名
	 */
	private String dbColName;
	
	/**
	 * 构造函数
	 */
	public ListingCols(){}
	/**
	 * 构造函数
	 */
	public ListingCols(ListingColsPK pk){
		this.comp_id=pk;
	}
	/**
	 * 构造函数
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