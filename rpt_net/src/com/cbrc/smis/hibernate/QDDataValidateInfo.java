package com.cbrc.smis.hibernate;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * 清单式报表的表内关系数据校验情况表主键实体类
 * 
 * @author rds
 * @serialData 2005-12-18 10:38
 */
public class QDDataValidateInfo implements Serializable{
	/**
	 * 主键
	 */
	private QDDataValidateInfoPK comp_id;
	
	/**
	 * 实际数据报表ID
	 */
	private Integer repInId;
	/**
	 * 校验公式类别ID
	 */
	private Integer validateTypeID;
	/**
	 * 关系表达式ID
	 */
	private Integer cellFormuId;
	/**
	 * 校验结果
	 */
	private Integer result;
	/**
	 * 校验未通过原因
	 */
	private String cause;
	
	private String sourceValue;
	private String targetValue;
	
	private com.cbrc.smis.hibernate.MCellFormu MCellFormu;
	
	private com.cbrc.smis.hibernate.ValidateType validateType;
	
	 private com.cbrc.smis.hibernate.ReportIn reportIn;
 

    public QDDataValidateInfo(){}
	
	public QDDataValidateInfo(QDDataValidateInfoPK comp_id){
		this.comp_id=comp_id;
	}
	
	public QDDataValidateInfo(QDDataValidateInfoPK comp_id,Integer result,com.cbrc.smis.hibernate.MCellFormu MCellFormu,com.cbrc.smis.hibernate.ValidateType validateType,com.cbrc.smis.hibernate.ReportIn reportIn,String cause){
		this.comp_id=comp_id;
		this.result=result;
		this.MCellFormu = MCellFormu;
		this.validateType = validateType;
	    this.reportIn = reportIn;
	    this.cause=cause;
	}
	
	public boolean equals(Object obj){
		if(!(obj instanceof QDDataValidateInfo)) return false;
		QDDataValidateInfo other=(QDDataValidateInfo)obj;
		return new EqualsBuilder()
			.append(this.comp_id,other.getComp_id())
			.isEquals();
	}
	
	public int hashCode() {
		return new HashCodeBuilder()
			.append(this.comp_id)
			.toHashCode();
	}
	
	public String toString(){
		return new ToStringBuilder(this)
			.append(this.comp_id)
			.toString();
	}

	public QDDataValidateInfoPK getComp_id() {
		return comp_id;
	}

	public void setComp_id(QDDataValidateInfoPK comp_id) {
		this.comp_id = comp_id;
	}

	public Integer getResult() {
		return result;
	}

	public void setResult(Integer result) {
		this.result = result;
	}

	public com.cbrc.smis.hibernate.MCellFormu getMCellFormu() {
		return MCellFormu;
	}

	public void setMCellFormu(com.cbrc.smis.hibernate.MCellFormu cellFormu) {
		MCellFormu = cellFormu;
	}

	public com.cbrc.smis.hibernate.ReportIn getReportIn() {
		return reportIn;
	}

	public void setReportIn(com.cbrc.smis.hibernate.ReportIn reportIn) {
		this.reportIn = reportIn;
	}

	public com.cbrc.smis.hibernate.ValidateType getValidateType() {
		return validateType;
	}

	public void setValidateType(com.cbrc.smis.hibernate.ValidateType validateType) {
		this.validateType = validateType;
	}

	public String getCause() {
		return cause;
	}

	public void setCause(String cause) {
		this.cause = cause;
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

	public Integer getCellFormuId() {
		return cellFormuId;
	}

	public void setCellFormuId(Integer cellFormuId) {
		this.cellFormuId = cellFormuId;
	}

	public String getSourceValue() {
		return sourceValue;
	}

	public void setSourceValue(String sourceValue) {
		this.sourceValue = sourceValue;
	}

	public String getTargetValue() {
		return targetValue;
	}

	public void setTargetValue(String targetValue) {
		this.targetValue = targetValue;
	}
	
	
}
