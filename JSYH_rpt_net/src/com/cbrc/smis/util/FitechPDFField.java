package com.cbrc.smis.util;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
/**
 * ���PDF�ļ�������Ϣ��
 * 
 * @author rds
 * @date 2005-12-3
 */
class FitechPDFField implements Serializable{
	private final String OMIT_CHARACTER="[0]";
	
	/**
	 * �����������:<br>
	 * 	�ı�����	1
	 */
	public static final Integer CELL_DATA_TYPE_CHAR=new Integer(1);
	/**
	 * �����������:<br>
	 * 	������	2
	 */
	public static final Integer CELL_DATA_TYPE_INTEGER=new Integer(2); 
	/**
	 * �����������:<br>
	 * 	������	3
	 */
	public static final Integer CElL_DATA_TYPE_DATE=new Integer(3);
	/**
	 * �ı���
	 */
	public static final String TYPE_TX="TX";
	/**
	 * ��ť��
	 */
	public static final String TYPE_BTN="BTN";
	
	/**
	 * �������<br>
	 * ����������:<br>
	 * 	<code>Tx</code>	��ʾ�ı���<br>
	 * 	<code>Btn</code> ��ʾ��ť��<br>
	 */
	private String fieldType;
	
	/**
	 * ����PDF�ļ��ж��������
	 */
	private String fieldName;
	
	/**
	 * ����PDF�ļ��ж����ֵ
	 */
	private String fieldValue;
	
	/**
	 * �����������
	 */
	private Integer dataType;
	
	/**
	 * �Ƿ��ǿ�д��
	 */
	private boolean readOnly;
	
	/**
	 * ���Ƿ�ɼ�����
	 */
	private boolean visibility;
	
	/**
	 * ���캯��
	 */
	public FitechPDFField(){}
	
	/**
	 * ���캯��
	 */
	public FitechPDFField(String fieldType,String fieldName,String fieldValue,boolean _readOnly){
		this.fieldType=fieldType;
		this.fieldName=fieldName;
		this.fieldValue=fieldValue;
		this.readOnly=_readOnly;
	}
	
	/**
	 * ��ȡ�������
	 * 
	 * @return String
	 */
	public String getFieldName() {
		return format(this.fieldName);
	}

	/**
	 * �����������
	 * 
	 * @param fieldName String
	 * @return void
	 */
	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	/**
	 * ��ȡ�������
	 * 
	 * @return String
	 */
	public String getFieldType() {
		return fieldType;
	}

	/**
	 * �����������
	 * 
	 * @param fieldType String
	 * @return void
	 */
	public void setFieldType(String fieldType) {
		this.fieldType = fieldType;
	}

	/**
	 * ��ȡ�������ֵ
	 * 
	 * @return String
	 */
	public String getFieldValue() {
		return fieldValue;
	}

	/**
	 * �����������ֵ
	 * 
	 * @param fieldValue String
	 * @return void
	 */
	public void setFieldValue(String fieldValue) {
		this.fieldValue = fieldValue;
	}

	/**
	 * ��ȡ���Ƿ��д
	 * 
	 * @return boolean
	 */
	public boolean isReadOnly() {
		return readOnly;
	}

	/**
	 * �������Ƿ��д
	 * 
	 * @param readOnly boolean 
	 * @return void 
	 */
	public void setReadOnly(boolean readOnly) {
		this.readOnly = readOnly;
	}
	
	/**
	 * ���������������
	 * 
	 * @param dataType Integer
	 * @return void
	 */
	public void setDataType(Integer dataType){
		this.dataType=dataType;
	}
	
	/**
	 * ��ȡ�����������
	 * 
	 * @return Integer
	 */
	public Integer getDataType(){
		return this.dataType;
	}
	
	/**
	 * �������Ƿ�ɼ�
	 * 
	 * @param visibility boolean
	 * @return void
	 */
	public void setVisibility(boolean visibility){
		this.visibility=visibility;
	}
	
	/**
	 * �ж����Ƿ�ɼ�
	 * 
	 * @return boolean
	 */
	public boolean isVisibility(){
		return this.visibility;
	}
	
	/**
	 * ��ʽ���������<br>
	 * ����PDF�ļ������õ���������ƣ���:P1[0],������PDF�ļ���ʵ�ʵ�������:P1
	 * 
	 * @param partialName String �����õ����������
	 * @return String 
	 */
	private String format(String partialName){
		if(partialName==null) return null;
		
		return partialName.indexOf(this.OMIT_CHARACTER)>=0?
				partialName.substring(0,partialName.indexOf(this.OMIT_CHARACTER)):"";
	}
	
	/**
	 * equals����
	 * 
	 * @param object Object ��һ��Ƚ϶���
	 * @return boolean ��������ȣ�����true�����򣬷���false
	 */
	public boolean equals(Object object){
		if(!(object instanceof FitechPDFField)) return false;
		FitechPDFField other=(FitechPDFField)object;
		return new EqualsBuilder()
			.append(this.getFieldName(),other.getFieldName())
			.append(this.getFieldType(),other.getFieldType())
			.append(this.getFieldValue(),other.getFieldValue())
			.isEquals();
	}

	/**
	 * hashCode���� 
	 * 
	 * @return int
	 */
	public int hashCode(){
		return new HashCodeBuilder()
			.append(this.fieldName)
			.append(this.fieldType)
			.append(this.fieldValue)
			.hashCode();
	}
	
	/**
	 * toString����
	 * 
	 * @return String
	 */
	public String toString(){
		return new ToStringBuilder(this)
			.append("fildName",this.fieldName)
			.append("fieldType",this.fieldType)
			.append("fieldValue",this.fieldValue)
			.toString();
	}
}