package com.cbrc.smis.hibernate;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * �嵥ʽ����ı��ڹ�ϵ����У�������ʵ����
 * 
 * @author rds
 * @serialData 2005-12-18
 */
public class QDDataValidateInfoPK implements Serializable{
	/**
	 * ʵ�����ݱ���ID
	 */
	private Integer repInId;
	/**
	 * У�鹫ʽ���ID
	 */
	private Integer validateTypeID;
	/**
	 * ��ϵ���ʽID
	 */
	private Integer cellFormuId;
	/**
	 * ����
	 */
	private String colName;
			
	public QDDataValidateInfoPK(){}
	
	public QDDataValidateInfoPK(Integer repInId,Integer validateTypeID,Integer cellFormuId,String colName){
		this.repInId=repInId;
		this.validateTypeID=validateTypeID;
		this.cellFormuId=cellFormuId;
		this.colName=colName;
	}
	
	public boolean equals(Object obj){
		if(!(obj instanceof QDDataValidateInfoPK)) return false;
		QDDataValidateInfoPK other=(QDDataValidateInfoPK)obj;
		return new EqualsBuilder()
			.append(this.repInId,other.getRepInId())
			.append(this.validateTypeID,other.getValidateTypeID())
			.append(this.cellFormuId,other.getCellFormuId())
			.append(this.colName,other.getColName())
			.isEquals();
	}
	
	public int hashCode() {
		return new HashCodeBuilder()
			.append(this.repInId)
			.append(this.validateTypeID)
			.append(this.cellFormuId)
			.append(this.colName)
			.toHashCode();
	}
	
	public String toString(){
		return new ToStringBuilder(this)
			.append(this.repInId)
			.append(this.validateTypeID)
			.append(this.cellFormuId)
			.append(this.colName)
			.toString();
	}

	public Integer getCellFormuId() {
		return cellFormuId;
	}

	public void setCellFormuId(Integer cellFormuId) {
		this.cellFormuId = cellFormuId;
	}

	public String getColName() {
		return colName;
	}

	public void setColName(String colName) {
		this.colName = colName;
	}

	public Integer getRepInId() {
		return repInId;
	}

	public void setRepInId(Integer repInId) {
		this.repInId = repInId;
	}
	
	public Integer getValidateTypeID() {
		return validateTypeID;
	}

	public void setValidateTypeID(Integer validateTypeID) {
		this.validateTypeID = validateTypeID;
	}
}
