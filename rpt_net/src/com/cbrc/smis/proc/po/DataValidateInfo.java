package com.cbrc.smis.proc.po;

import java.io.Serializable;
/**
 * ��������У�����ʵ����
 * 
 * @author rds
 * @serialData 2005-12-20
 */
public class DataValidateInfo implements Serializable{
	/**
	 * ʵ�ʱ���ID
	 */
	private Integer repInId;
	/**
	 * У�����ID
	 */
	private Integer validateTypeId;
	/**
	 * ��ϵ���ʽID
	 */
	private Integer cellFormuId;
	/**
	 * ���
	 */
	private String seqNo;
	/**
	 * ����
	 */
	private String colName;
	/**
	 * У�鲻ͨ����ԭ��
	 */
	private String cause;
	/**
	 * У����
	 */
	private Integer result;
	/**
	 * С���㾫ȷ��λ��
	 */
	private Integer pointNumber;
	
	
	private String sourceValue;
	private String targetValue;
	
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
	/**
	 * @return Returns the pointNumber.
	 */
	public Integer getPointNumber() {
		return pointNumber;
	}
	/**
	 * @param pointNumber The pointNumber to set.
	 */
	public void setPointNumber(Integer pointNumber) {
		this.pointNumber = pointNumber;
	}
	/**
	 * ���캯��
	 */
	public DataValidateInfo(){}
	/**
	 * ȫ����
	 */
	public DataValidateInfo(Integer repInId,Integer validateTypeId,Integer cellFormuId,String seqNo,String colName,String cause,Integer result,Integer pNumber){
		this.repInId=repInId;
		this.validateTypeId=validateTypeId;
		this.cellFormuId=cellFormuId;
		this.seqNo=seqNo;
		this.colName=colName;
		this.cause=cause;
		this.result=result;
		this.pointNumber=pNumber;
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
	public Integer getResult() {
		return result;
	}
	public void setResult(Integer result) {
		this.result = result;
	}
	public String getCause() {
		return cause;
	}
	public void setCause(String cause) {
		this.cause = cause;
	}
	public String getSeqNo() {
		return seqNo;
	}
	public void setSeqNo(String seqNo) {
		this.seqNo = seqNo;
	}
	public Integer getValidateTypeId() {
		return validateTypeId;
	}
	public void setValidateTypeId(Integer validateTypeId) {
		this.validateTypeId = validateTypeId;
	}
	public void setValidateTypeId(int validateTypeId){
		this.validateTypeId=new Integer(validateTypeId);
	}
}
